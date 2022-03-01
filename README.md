# API Gateway

## Build and Run

### Build
Go to your home directory and run the below command
```
gradlew build
```

### Run as Docker Container

**_Note_**
* Please make sure the service discovery IP address is configured correctly in API-Gateway application.properties file. So please run the Service Discovery app first and get the Docker IP by using the below command
```
docker network inspect bridge
```
```
"Containers": {
            "ef95e3d21074523ea2630b9cabb41d85725cc3c7b0c5abfcc870ada5ae860d61": {
                "Name": "service-discovery",
                "EndpointID": "e809ac69a80a7739ccee0afe6028a08d0b10c47c5560b29dc611657481374bef",
                "MacAddress": "02:42:ac:11:00:02",
                "IPv4Address": "172.17.0.2/16",
                "IPv6Address": ""
            }
        }
```
Please get the IPv4Address and set it in the below configs

* eureka.client.serviceUrl.defaultZone=http://172.17.0.2:8761/eureka/
* eureka.instance.hostname=172.17.0.3

Go to the application home folder and run the below commands to create the docker image and run the container
```
docker build -t api/gateway .
```
Check the image is build successfully
```
docker images

api/gateway    latest         eff3e9573427   18 hours ago   151MB
```
Run the image
```
docker run --rm -d -v /home/users/logs:/logs -p 8080:8080 --name api-gateway api/gateway
```
Check the container started
```
docker ps

cc16d82c240d   api/gateway         "java -Djava.securit…"   4 seconds ago    Up 3 seconds    0.0.0.0:8080->8080/tcp   api-gateway
```
Note: Please use your desired location for log files in the above command: -v ${PATH_TO_LOCAL_FOLDER}:/logs


## API

### Create Account

```
Request
-------

POST http://localhost:8080/savings/api/v1/account/create

BODY:
{
    "firstName": "kayal",
    "lastName": "magil",
    "address": "1-2-3, TN, India",
    "accountType": "savings",
    "initialDeposit": 500.00
}

Response
--------
{
    "message": "success",
    "status": "OK",
    "result": {
        "accountNo": 49101151007,
        "accountType": "savings",
        "balance": 500.00
    },
    "timestamp": "2022-03-01T03:21:22.001+00:00"
}

```

### Get Balance
```
Request
-------

GET http://localhost:8080/savings/b/balance?accountNo=49101151005

Response
--------
{
    "message": "success",
    "status": "OK",
    "result": {
        "accountNo": 49101151005,
        "accountType": "Savings",
        "balance": 90000
    },
    "timestamp": "2022-03-01T03:16:11.956+00:00"
}
```

### Increase Balance
```
Request
-------

POST http://localhost:8080/savings/b/balance

BODY:
{
    "accountNo": 49101151005,
    "amount": 500.00,
    "accountType": "Savings",
    "description": "Transfered From account XYZ",
    "transactionType":"DEPOSIT"
}

Response
--------
{
    "message": "success",
    "status": "OK",
    "result": {
        "accountNo": 49101151005,
        "currentBalance": 90000.00,
        "accountType": "Savings",
        "transactionType": "DEPOSIT"
    },
    "timestamp": "2022-03-01T03:20:14.470+00:00"
}
```

### Decrease Balance
```
Request
-------

POST http://localhost:8080/savings/b/balance

BODY:
{
    "accountNo": 49101151005,
    "amount": 500.00,
    "accountType": "Savings",
    "description": "Transfered To account XYZ",
    "transactionType":"WITHDRAW"
}

Response
--------
{
    "message": "success",
    "status": "OK",
    "result": {
        "accountNo": 49101151005,
        "currentBalance": 89500.00,
        "accountType": "Savings",
        "transactionType": "WITHDRAW"
    },
    "timestamp": "2022-03-01T03:19:48.107+00:00"
}
```

## API Gateway Security
* By default, all the requests are secured with username and password.
* The credentials are configured in application.properties file.


# Improvements
### Security
* The API Gateway security should be moved to oAuth based implementation instead of username and password stored in application.

### Monitor
* To monitor the uptime of the services, we can implement monitoring and visualization tools like Prometheus and Grafana
  * Prometheus - used to collect the metrics from the API 
  * Grafana - used to visualize the collected metrics

### Test Timeouts
* To test the timeouts of the application, CircuitBreaker is implemented in the API Gateway service.
* Stop either of the savings service A or B and hit the request to the stopped service and expect the Gateway timeout error in the response.
* But there is fallback url configured, so if the response is not received within 5s, the timeout will happen and fallback url will be called to show some useful message to the user.

### Scale API Gateway
* Since there is only one instance of API Gateway, it leads to Single Point Of Failure.
* So we need to scale the API Gateway to multiple nodes. To do so, we need to introduce the Load Balancer between Client and API Gateway, to redirect the incoming requests to the API Gateway using LB algorithms like Round Robin, Consistent hashing etc.
* LB can work like a Master Slave, if one goes down the other one can identify and serve as master.