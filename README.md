# eSymposium

## Installation instructions

### System Requirements
1. Download and install Java 8
2. Download and install Maven (at least 3.6.0 version)
2. Download and install Google Chrome (at least 81.0 version)
3. Download and install MongoDb on your machine (at least 4.2.5 version)
### Application Requirements
1. Clone eSymposiumDb project and follow the instructions to restore db (optional)
2. Download chrome driver and update webdriver.chrome.driver in application.properties file with your own path
3. Add your facebook account credentials on login.facebook.personal.account.email and login.facebook.personal.account.password in application.properties file
4. Create a developer account on twitter, register the app and add your keys on twitter4j.properties
5. Run application from project's root folder with : mvn spring-boot:run