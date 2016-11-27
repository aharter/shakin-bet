package de.andreas.harter.shakin.bet;

import java.time.Duration;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1")
public class RestService {

    /**
     * Helper class allowing automatic serialisation.
     */
    public final class Timestamp {
        private long timestamp;

        public Timestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public long getTimestamp() {
            return this.timestamp;
        }
    }

    /**
     * Helper class allowing automatic serialisation.
     */
    public final class Earthquake {
        private long timestamp;
        private double magnitude;

        public Earthquake(long timestamp, double magnitude) {
            this.timestamp = timestamp;
            this.magnitude = magnitude;
        }

        public long getTimestamp() {
            return this.timestamp;
        }

        public double getMagnitude() {
            return magnitude;
        }
    }

    private EarthquakeReader earthquakeReader;

    @Autowired
    public RestService(EarthquakeReader earthquakeReader) {
        this.earthquakeReader = earthquakeReader;
    }

    @RequestMapping(value = "/next-bet-timestamp", method = RequestMethod.GET)
    public Timestamp getNextBetTimestamp() {
        // 5 minutes is the update rate of http://earthquake.usgs.gov
        long timestamp = new Date().getTime() + Duration.ofMinutes(5).toMillis();
        return new Timestamp(timestamp);
    }

    @RequestMapping(value = "/latest-earthquake", method = RequestMethod.GET)
    public Earthquake getLatestEarthquake() {
        double magnitude = earthquakeReader.getLatestEarthquakeMagnitude();
        // Truncate to one digit
        magnitude = Math.floor(magnitude * 10) / 10;
        return new Earthquake(earthquakeReader.getLatestEarthquakeTimestamp(), magnitude);
    }
}
