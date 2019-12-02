# sb_currency_converter
Backend of currency converter that exposes API for the FE nextjs app

# build and start server
This project has spring profile activated. It's mandatory for builds in all staages except PROD. In order to build for development environment,
```
.\grablew clean build -Dspring.profiles.active=dev (for PROD builds, we can skip the param)
```
Similarly for starting the server,
```
.\gradlew bootRun -Dspring.profiles.active=dev (for PROD builds, we can skip the param)
```

# Features
1) Exposes multiple APIs in order to fetch currency rates from https://exchangeratesapi.io/
2) Adds Server-Time response header in order to record the duration of the API call
3) Has CORS enabled for cross domain protection
4) Has CSRF enabled for session based protection
5) Enabled Spring Actuator for application metrics and health checkup
6) Custom exceptions using controller advice that wraps any application exception into a custom exception

# Pending
1) Implement CSP to prevent static code injections
2) Instrumentation into InfluxDB
3) Possibly a swagger-ui documentation

# Heroku
This application has been deployed on Heroku which can be accessed using the FE application [here](https://currency-converter-ui.herokuapp.com/)
