#### Run Kafka Cluster

From the root directory
```
$ cd docker-compose
$ docker-compose -f common.yml -f kafka_cluster.yml up
```
Inspect running containers:
```
$ docker ps
```
To run services in docker
```
$ cd docker-compose
$ docker-compose -f common.yml -f kafka_cluster.yml -f services.yml up
```
Run below command to make it an executable file
```
$ chmod +x check-config-server-started.sh
```