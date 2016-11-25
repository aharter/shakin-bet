package de.andreas.harter.shakin.bet;

import java.util.Comparator;
import java.util.stream.Collectors;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Fetches earthquake data from http://earthquake.usgs.gov and aggregates it.
 * Data is update automatically updated on a specific interval. Data from
 * http://earthquake.usgs.gov is updated every 5 minutes.
 */
@Component
public class EarthquakeReader {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // URL used for initial data
    private static final String PAST_WEEK_URL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.geojson";
    // URL used for latest data
    private static final String PAST_DAY_URL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson";
    private static final int UPDATE_RATE = 1000 * 60;

    // Names to access feature properties
    private static final String TIME_PROPERTY_NAME = "time";
    private static final String MAGNITUDE_PROPERTY_NAME = "mag";

    private volatile long latestEarthquakeTimestamp;
    private volatile double latestEarthquakeMagnitude;

    public EarthquakeReader() {
        latestEarthquakeTimestamp = 0;
        latestEarthquakeMagnitude = 0.0;
        updateLatestEarthquake(PAST_WEEK_URL);
    }

    /**
     * Fetches latest earthquake data. Method is automatically scheduled at a
     * fixed rate of {@link UPDATE_RATE} ms.
     */
    @Scheduled(fixedRate = UPDATE_RATE)
    private void updateEarthquakes() {
        updateLatestEarthquake(PAST_DAY_URL);
    }

    /**
     * Fetches earthquake data from given URL and updates
     * {@link latestEarthquakeTimestamp} and {@link latestEarthquakeMagnitude}
     * with the latest entry.
     *
     * @param url
     *            URL to fetch earthquake data from
     */
    private void updateLatestEarthquake(String url) {
        FeatureCollection features;
        try {
            RestTemplate restTemplate = new RestTemplate();
            features = restTemplate.getForObject(url, FeatureCollection.class);
        } catch (RestClientException e) {
            log.error("Failed to update earthquake data", e);
            return;
        }

        if (!features.getFeatures().isEmpty()) {
            Feature feature = getLatestFeature(features);
            // Casting to Number allows us to get expected type by calling
            // the corresponding method.
            Number t = (Number) feature.getProperty(TIME_PROPERTY_NAME);
            Number m = (Number) feature.getProperty(MAGNITUDE_PROPERTY_NAME);
            long timestamp = t.longValue();
            double magnitude = m.doubleValue();

            if (timestamp > latestEarthquakeTimestamp) {
                this.latestEarthquakeTimestamp = timestamp;
            }
            if (magnitude > latestEarthquakeMagnitude) {
                this.latestEarthquakeMagnitude = magnitude;
            }
        }
    }

    private Feature getLatestFeature(FeatureCollection collection) {
        // Search list for latest timestamp
        long latestTimestamp = collection.getFeatures()
                                         .parallelStream()
                                         .map(n -> (long) (n.getProperty(TIME_PROPERTY_NAME)))
                                         .collect(Collectors.toList())
                                         .parallelStream()
                                         .max(Comparator.comparing(i -> i))
                                         .get();

        // Get feature to latestTimestamp
        Feature latestFeature = collection.getFeatures()
                                          .parallelStream()
                                          .filter(f -> (long) f.getProperty(TIME_PROPERTY_NAME) == latestTimestamp)
                                          .collect(Collectors.toList())
                                          .get(0);
        return latestFeature;
    }

    public long getLatestEarthquakeTimestamp() {
        return latestEarthquakeTimestamp;
    }

    public double getLatestEarthquakeMagnitude() {
        return latestEarthquakeMagnitude;
    }
}
