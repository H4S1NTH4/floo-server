1. start minikube
2. set docker environment to minikube 
3. build docker image inside minikube cluster
4. apply K8 files
5. make sure deployments are running inside minikube
6. access the service; 
   7. directly (tunnel service url for the microservice)/ 
   8. check the service registry (from url of discovery-server)/ 
   9. through API gateway (URL of API gateway/service_name/endpoint)

## TODO
- [ ] Check if all the required endpoints exist with comments.
- [ ] Add proper response entities for all endpoints.
- [ ] Add proper error handling for all endpoints.
- [ ] Add proper logging for all endpoints.
- [ ] Add feign clients.
- [ ] Add endpoints to the postman collection.
- [ ] Add Authorization.
- [ ] Connect with FE.