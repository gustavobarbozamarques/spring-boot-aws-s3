# Spring Boot AWS S3

This project demonstrates a simple microservice that uses Spring Boot and AWS S3.

### Run project

``` docker build . -t spring-boot-aws-s3 ```

``` docker run -p 8080:8080 -e ACCESS_KEY=MYACCESSKEY -e SECRET_KEY=MYSECRETKEY -e REGION=us-east-1 -e BUCKET=MYBUCKETNAME spring-boot-aws-s3 ``` 

And access: ``` http://localhost:8080/ ``` 

### Swagger

Access: ``` http://localhost:8080/swagger-ui.html ```

![Alt text](docs/swagger.png?raw=true "Swagger")


### Example

AWS S3 Console:

![Alt text](docs/aws-s3-console.png?raw=true "AWS S3 Console")

Get List:

![Alt text](docs/list.png?raw=true "List")