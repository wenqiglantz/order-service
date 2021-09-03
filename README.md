# Order Service

![customer-order.jpg](customer-order.jpg)

###  Order Service, consumer of Customer Service where customer events are published:
* 1. CustomerWasCreated
* 2. CustomerWasUpdated
* 3. CustomerWasDeleted


* **A. Prerequisites**
  * 1. Windows/Linux based operating system
  * 2. Java 11
  * 3. Dapr CLI (follow instructions on https://docs.dapr.io/getting-started/)
  * 4. Docker (follow instructions on https://docs.docker.com/get-docker/.)
  * 5. Database has been created or assuming using an in-memory database for testing purpose.


* **B. Getting Started & Run the Application**
  * 1. Clone the git repo in to your local directory from github repository

  * 2. Open the command prompt and navigate to the main project directory.


* ** Run Application on Local Docker Containers**
  
  * 1. Build the application:
    * `mvn clean install`

  * 2. Run the application with Dapr as sidecar WITH docker container:
    * `docker-compose up --build`

  * 3. Tear down the app after running the app (warning: this will delete the contents of your app's database):
    * `docker-compose down`
  

* **C. Project Code Modules**
  * `config` - Spring Configuration classes for all of the modules.
  * `dapr-components` - dapr components files for pubsub.
  * `data` - Contains domain data shared with other applications such as rest-api, events etc.
  * `persistence` - Responsible for hosting the Entities and Repositories for the database.
  * `rest-controller` - REST Endpoints for the application.
  * `service` - Contains business logic for the application.


* **D. Application Launch**
  * web: http://localhost:9200/swagger-ui.html


### PubSub

For testing purpose, we are using EMQ X MQTT public broker [https://www.emqx.io/mqtt/public-mqtt5-broker](https://www.emqx.io/mqtt/public-mqtt5-broker)."# order-service" 
