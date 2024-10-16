### Twitter Events Streaming 

#### Run Kafka Cluster
- From the root directory
```bash
$ cd docker-compose
$ docker compose -f common.yml -f kafka_cluster.yml up
```

#### Run Elastic Cluster
- From the root directory
```bash
$ cd docker-compose
$ docker compose -f common.yml -f elastic_cluster.yml up
```

#### Run Postgresql Database
- From the root directory
```bash
$ cd docker-compose
$ docker compose -f common.yml -f postgresql.yml up
```

#### Run Redis Database
- From the root directory
```bash
$ cd docker-compose
$ docker compose -f common.yml -f redis_cluster.yml up
```

#### Run Prometheus & Grafana
- From the root directory
```bash
$ cd docker-compose
$ docker compose -f common.yml -f monitoring.yml up
```

| App / Features / Resources         | Host & Port                   | Username   | Password   |
|------------------------------------|-------------------------------|------------|------------|
| Postgresql                         | http://localhost:5432         | `postgres` | `password` |
| View Prometheus Registered Targets | http://localhost:9090/targets |            |            |
| Grafana Dashboard                  | http://localhost:3000         | `user`     | `password` |
| Prometheus Server Url              | http://prometheus:9090        |            |            |
| Kibana                             | http://localhost:5601         |            |            |


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

To Do:

1. Add rate limiting with redis
2. Add OAuth2.0 security
3. Docker compose support