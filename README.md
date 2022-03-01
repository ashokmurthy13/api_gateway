# API Gateway

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
