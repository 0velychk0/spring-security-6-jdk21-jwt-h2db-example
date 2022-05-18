# spring-boot-jwt-h2db-minimal-example

Link to main example with explanation:
- https://www.javainuse.com/spring/boot-jwt

1) Send request to localhost:8080/test to check that endpoint is not available without authorization


2) To Generate a JSON Web Token - Create a POST request with url localhost:8080/authenticate with body
{
    "username":"admin",
    "password":"test"
}

Response JWT token :

{
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE2NTI0NjAyOTgsImlhdCI6MTY1MjQ0MjI5OH0.7K7FP2SNYthRmu_NNqAMdILcryrw510n7Eh4bpNvHzMYjPiNPqxkVrrfoWEhIzbQ9ITQWwR1Wy7HzRCgYmpsKw"
}


3) Use this Bearer token to check localhost:8080/test one more time.

Response : "Hello World"

