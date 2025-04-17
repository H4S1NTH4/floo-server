1. build your Spring Boot app: ./mvnw clean package 
2. build your docker image: docker build -t restaurant-service .
3. List all Docker images: docker images
3. Run your docker container in the terminal: docker run -p 8080:8080 restaurant-service 
4. List running containers: docker ps
5. Test the running container: curl http://localhost:8080/api/restaurants
6. Then, install minikube if haven't: choco install minikube
7. check version: minikube version 
8. start minikube: minikube start
9. check status: minikube status 
10. install kubectl if haven't: choco install kubernetes-cli
11. check version: kubectl version --client
12. Create restaurant-service-deployment.yaml
13. Apply deployment: kubectl apply -f restaurant-service-deployment.yaml
14. Apply service: kubectl apply -f restaurant-service.yaml
15. Check all pods: kubectl get pods
16. Check all services: kubectl get services
17. Check all deployments: kubectl get deployments
18. To Access Your Restaurant Service; 
19. Get the minikube IP: minikube ip
20. Access the service on browser: http://<minikube-ip>:<node-port> :add endpoint
21. Get minikube to give url: minikube service restaurant-service --url : this url should give the ip address given by minikube ip and service.yaml nodeport. if its giving another url its the temporary proxy url tunnel created by minikube


1. build your Spring Boot app: ./mvnw clean package
3. List all Docker images: docker images
4. Then you have make these docker images available and accessible in kubernetes (powershell as admin): & minikube -p minikube docker-env | Invoke-Expression
5. build your docker image: docker build -t restaurant-service .
3. List all Docker images: docker images
4. 12. Create restaurant-service-deployment.yaml: kubectl run restaurant-service --image=restaurant-service --port=8080 --image-pull-policy=Never
5. and then the service.yaml
6. expose service: kubectl expose deployment restaurant-service --type=NodePort
7. Check all pods: kubectl get pods
16. Check all services: kubectl get services
17. Check all deployments: kubectl get deployments
18. To Access Your Restaurant Service;
19. Get the minikube IP: minikube ip
20. Access the service on browser: http://<minikube-ip>:<node-port> :add endpoint
21. 
3. Run your docker container in the terminal: docker run -p 8080:8080 restaurant-service
4. List running containers: docker ps
5. Test the running container: curl http://localhost:8080/api/restaurants
6. Then, install minikube if haven't: choco install minikube
7. check version: minikube version
8. start minikube: minikube start
9. check status: minikube status
10. install kubectl if haven't: choco install kubernetes-cli
11. check version: kubectl version --client

13. Apply deployment: kubectl apply -f restaurant-service-deployment.yaml
14. Apply service: kubectl apply -f restaurant-service.yaml
15. Check all pods: kubectl get pods
16. Check all services: kubectl get services
17. Check all deployments: kubectl get deployments
18. 
21. Get minikube to give url: minikube service restaurant-service --url : this url should give the ip address given by minikube ip and service.yaml nodeport. if its giving another url its the temporary proxy url tunnel created by minikube