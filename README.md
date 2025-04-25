# floo-server
Floo - A cloud native food delivery application built using micro service architecture.


## How to setup a secret in kubernetes

>kubectl create secret generic mongo-secret \
--from-literal=MONGODB_URI="mongodb+srv://<username>:<password>@<cluster-address>/<database>?retryWrites=true&w=majority"

If the above command does not work, try with all in one line:

>kubectl create secret generic mongo-secret --from-literal=MONGODB_URI="mongodb+srv://<username>:<password>@<cluster-address>/<database>?retryWrites=true&w=majority"

update the deployment file with the secret name and key.

```yaml
env:
  - name: MONGODB_URI
    valueFrom:
      secretKeyRef:
        name: mongo-secret
        key: MONGODB_URI
```


## Updated deployment file with env variables
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: order-service-deploy
spec:
  replicas: 1
  selector:
    matchLabels:
      app: order-service
  template:
    metadata:
      labels:
        app: order-service
    spec:
      containers:
        - name: order-service
          image: order-service:le
          imagePullPolicy: IfNotPresent
          ports:
            - containerPort: 8082
          env:
            - name: MONGODB_URI
              valueFrom:
                secretKeyRef:
                  name: mongo-secret
                  key: MONGODB_URI
```
