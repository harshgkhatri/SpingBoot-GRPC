# Spring Boot + gRPC Demo Application

- gRPC is a modern open source high performance Remote Procedure Call (RPC) framework that can run in any environment. It can efficiently connect services in and across data centers with pluggable support for load balancing, tracing, health checking and authentication. It is also applicable in last mile of distributed computing to connect devices, mobile applications and browsers to backend services.

- Problem Statement: Build the User application The project is build using gRPC and includes below modules: USER-PROTO API- SERVICE USER-SERVICE

- Here the API-SERVICE acts as a gRPC client and USER-SERVICE acts as a server for communications.

# USER-PROTO

- The User proto contains schema.proto and common.proto files for generating java stubs(code). The generated Java code contains an inner abstract class suffixed with ImplBase, such as ServiceNameImplBase. This class defines one Java method for each method in the service definition. It is up to the service implementer to extend this class and implement the functionality of these methods. Without being overridden, the methods return an error to the client saying the method is unimplemented. For generating java stubs we have to execute a maven command which is mvn clean install. After building the maven project the java stubs will be automatically geneted.

# API-SERVICE

- The API-SERVICE acts as an api-gateway for processing any request the user sends.This module runs on port 8080. This module includes UserDeatilController and UserDetailService files. The UserDetailController is used for mapping the requests to the resources and the UserDetailService Class is using the generated java stubs for processing the requests.This module starts using the main SprinBootApplication file. This module is a Client. We did the required configuraation for client in application.yml file as follows:

- grpc: client: grpc-DevPrb-service: address: static://localhost:9000 negotiationType: plaintext

# USER-SERVICE

- This module acts as a server, This project is build using REST API's and it contains model,repository and service for processing the requests.We have set the grpc server in the application.yml file as follows: grpc: server: port: 9000

- For testing the application we have used Bloomrpc tool.In the tool we have to upload the proto files and the tool will configure the request body. When we requst for getting a user the grpc framwework will call the getUser method which is defined in the proto file as rpc getUsers(User) returns (User){} rpc stands for remote procedure call.


# To run this application follow the given below steps  

1. Clone the project in your system using given cmd:
   git clone https://github.com/harshgkhatri/SpingBoot-GRPC.git

2. Optional Generating java stubs
	select user-proto module and run maven clean install command 
	After the maven build the java stubs will be generated in target folder
3. Go to API SERVICE module and start the application using ClientApplication.java file
4. Go to USER-SERVICE module and start the application using ServerApplication.java file.

5. Open BloomRPC tool and go to java-test-grpc/gRPCDemonstration/gRPCDemonstration/gRPCDemonstration/protos/src/main/proto and uplaod schema.proto file.
6. select the method you want to work with and send the request for eg,createUser
