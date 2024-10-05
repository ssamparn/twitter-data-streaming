#### Run Kafka Cluster

- From the root directory
```bash
$ cd docker-compose
$ docker compose -f common.yml -f kafka_cluster.yml up
$ docker compose -f common.yml -f elastic_cluster.yml up
$ docker compose -f common.yml -f keycloak_authorization_server.yml up
```

- Launch Keycloak Authorizaton Server Admin Console


- Inspect running containers
```bash
$ docker ps
```

- To run services in docker
```bash
$ cd docker-compose
$ docker compose -f common.yml -f services.yml up
$ docker compose -f common.yml -f kafka_cluster.yml -f services.yml up
```

- Monitor Kafka cluster and topics
```bash
$ docker run -it --network=host confluentinc/cp-kafkacat kafkacat -L -b localhost:19092
```

- View the messages as received by kafka consumer
```bash
$ kafkacat -C -b localhost:19092 -t twitter-topic
```

Run below command to make it an executable file
```bash
$ chmod +x check-config-server-started.sh
```