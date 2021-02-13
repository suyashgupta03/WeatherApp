# Android Weather App

An Android app that displays the weather forecast of a user location. The app will gather the users location and hit an API (ex: community open weather) to display the weather of next 7 days.
Also, user will have the ability to see a more detailed view of the weather for each of those days.

###Functionality:
The app contains the following functionality:

- The 7 day forecast is displayed using a View Pager
- The app uses an room database and displays the data while the phone is offline
- The transition to the detailed view uses a custom animation
- Proper unit tests for all usecases and utility functions.

### Details
- The program flow starts from the MainActivity with a single fragment.
- The datasources package has api data-source which calls the server and handles the response accordingly and local data-source for database and shared preferences.
- Coroutines are for asynchronous calling.
- Service side weather model(ApiWeatherForecast) is different from client side weather model(WeatherForecast) and same is applied for local database.
- The logic of app is in interactor package. It has usecases package which has the weather forecast service call logic
- Android arch components are used for the app which viewmodel support.
- Koin is used for dependency injection.
- I used navigation framework for transacting fragments.
- The unit tests and UI tests are written for the app.


