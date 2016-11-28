# Shakin' bet
What happens if you combine vast amount of data with shady business practices? A new betting game!


## About this project
Big Data, IoT and Industry 4.0 are the big trends of our time! Collecting every possible piece of information and data,
use it to forecast and control. Data becomes a product, more valuable than the application producing it. Facebook and
Google are only the most known of those data collectors. It is claimed, that the data is used "for the greater good"
and "not to cause harm". This project wants to visualize, how available data can be used in a seemingly innocent
context. At the same time, it shows the user the moral and ethical wrong doing of his action by using the application.

The project is inspired by dystopian movies and stories, where (often rich) people got bored and start betting on the
life of others without considering or fully knowing the consequences. Such movies are ["Gamer"](http://www.imdb.com/title/tt1034032/)
or ["The Running Man"](http://www.imdb.com/title/tt0093894/). It is intended to be easily accessible for the user, at
the same time creating an awareness for potential earthquake victims. Ultimately, the project is supposed to lead to a
discussion regarding ["Datensparsamkeit"](http://martinfowler.com/bliki/Datensparsamkeit.html).


## Development
### Requirements
```
JDK8 or higher
maven
nodejs/npm
```

### Setup
```sh
# Install node dependencies
npm install
```

### Run development server
```sh
# run rest service on port 8090 by selecting development profile
java -jar -Dspring.profiles.active=development target/shakin.bet-1.0-SNAPSHOT.jar
# run webpack development server
npm run build-dev
```

### Build release
```sh
# Maven command runs 'npm run build' to build javascript.
# Result is placed in src/main/resource/public, where it is automatically picked up.
mvn clean package
# Run it. Binds to port 80, might require admin privileges
java -jar target/shakin.bet-1.0-SNAPSHOT.jar
```

### TODOs
* Fixed timer
```
Timer starts always with 5 minutes according to update interval of the earthquake data.
This allows the betting system to be exploited, as the result can be anticipated.
However, the "flaw" will intentionally not be fixed, as it adds another dimension to the moral component.
```

* Missing tests
```
The project has no unit or integration tests.
```

* Move every view in own component
```
The UI consist of a single page, using an internal state to show/hide certain parts of the application.
The states should be refactored into independent components.
This not only improves readability of the code, it also allows to introduce real navigation.
```
