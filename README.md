# spring-poc-postgres-debezium
This is a proof of concept Change Data Capture using Debezium and PostgreSQL.

## Architecture

```mermaid
graph TD
    A[OrderService] -->|JDBC| B[(PostgreSQL)]
    C[Debezium] -->|JDBC| B[(PostgreSQL)]
    B[(PostgreSQL)] -->|Change Events| C[Debezium]
    C[Debezium] -->|Change Events| D[(Kafka)]
    D[(Kafka)] -->|Change Events| E[OrderEventListener]
```

## Components

```mermaid
graph TD
    A[controller-1] -->|Quorum| B[controller-2]
    B[controller-2] -->|Quorum| C[controller-3]
    C[controller-3] -->|Quorum| A[controller-1]
    D[kafka-1] -->|Broker| A[controller-1]
    D[kafka-1] -->|Broker| B[controller-2]
    D[kafka-1] -->|Broker| C[controller-3]
    E[kafka-2] -->|Broker| A[controller-1]
    E[kafka-2] -->|Broker| B[controller-2]
    E[kafka-2] -->|Broker| C[controller-3]
    F[kafka-3] -->|Broker| A[controller-1]
    F[kafka-3] -->|Broker| B[controller-2]
    F[kafka-3] -->|Broker| C[controller-3]
    G[postgres] -->|Database| H[debezium]
    H[debezium] -->|Connect| D[kafka-1]
    H[debezium] -->|Connect| E[kafka-2]
    H[debezium] -->|Connect| F[kafka-3]
```

## Requirements to Run 

You need to have the following installed on your machine:

- Docker
- Java 21
- Gradle 
- curl

## How to run

1. Start the services using docker-compose

```shell
docker-compose up
```

2. Start the Order Service

```shell
./gradlew bootRun
```

3. Configure Debezium Connector

Use [http/DebeziumConnector.http](http/DebeziumConnector.http) collection to create a Debezium connector.

Alternatively, you can use the following command to create a Debezium connector:

```shell
curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" localhost:8083/connectors/ -d '{
   "name": "order-connector",
   "config": {
     "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
     "tasks.max": "1",
     "database.hostname": "postgres",
     "database.port": "5432",
     "database.user": "postgres",
     "database.password": "postgres",
     "database.dbname": "orderdb",
     "database.server.name": "orderdb_server",
     "plugin.name": "pgoutput",
     "slot.name": "debezium_slot",
     "publication.name": "order_publication",
     "publication.autocreate.mode": "filtered",
     "schema.include.list": "orderdb",
     "table.include.list": "orderdb.order",
     "topic.prefix": "cdc"
   }
 }'
```

# Examples 

Use [http/OrderApplication.http](http/OrderApplication.http) collection to create/update/delete orders and observe incoming CDC events in Logs.

Alternatively, you can use the following curl commands: 

## Create an order

```shell
curl -X POST -H "Content-Type: application/json" -d '{
    "customerName": "Omer Kocaoglu",
    "customerEmail": "omersw@email.com",
    "customerAddress": {
        "street": "1234 Elm St",
        "city": "Springfield",
        "state": "IL",
        "zip": "62701"
    }
}' http://localhost:8081/order-api/orders
```

## Update an order
    
```shell
curl -X POST -H "Content-Type: application/json" -d '{
    "customerName": "Omer Kocaoglu",
    "customerEmail": "e@mail.com",
    "customerAddress": {
        "street": "1234 Elm St",
        "city": "Springfield",
        "state": "IL",
        "zip": "62701"
    },
    "status": "FULFILLED"
    }' http://localhost:8081/order-api/orders/6d2c4d98-5aa6-4340-80d8-d737cadea2c6
```

## Delete an order

```shell
curl -X DELETE http://localhost:8081/order-api/orders/6d2c4d98-5aa6-4340-80d8-d737cadea2c6
```

# Debezium Monitoring

## Get health check
```shell
curl -X GET http://localhost:8083/
```

## List all connectors
```shell
curl -X GET http://localhost:8083/connectors
```

## Get connector status
```shell
curl -X GET http://localhost:8083/connectors/order-connector/status
```

## Get connector config
```shell
curl -X GET http://localhost:8083/connectors/order-connector/config
```

## Get connector topics
```shell
curl -X GET http://localhost:8083/connectors/order-connector/topics
```

See [Debezium Monitoring](https://debezium.io/documentation/reference/stable/connectors/postgresql.html#postgresql-monitoring) for more details.

## Kafka 

Kafka [cluster setup](https://github.com/apache/kafka/blob/trunk/docker/examples/docker-compose-files/cluster/isolated/plaintext/docker-compose.yml) is taken from official Kafka Docker image repository. See [Multi Node Cluster/Isolated](https://github.com/apache/kafka/tree/trunk/docker/examples#multi-node-cluster) section for more details.
