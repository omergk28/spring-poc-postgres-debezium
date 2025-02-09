# spring-poc-postgres-debezium
This is a proof of concept Change Data Capture using Debezium and PostgreSQL 

Following diagram shows a quick overview of the architecture:

```mermaid
graph TD
    A[OrderService] -->|JDBC| B[(PostgreSQL)]
    C[Debezium] -->|JDBC| B[(PostgreSQL)]
    B[(PostgreSQL)] -->|Change Events| C[Debezium]
    C[Debezium] -->|Change Events| D[(Kafka)]
    D[(Kafka)] -->|Change Events| E[OrderEventListener]
```

## Requirements

You need to have the following installed on your machine:

- Docker
- Java 21
- Gradle 
- flyway

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

4. Use [http/OrderApplication.http](http/OrderApplication.http) collection to create orders and observe incoming CDC events in Logs.

Alternatively, you can use the following command to create an order:

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

## Note

Kafka [cluster setup](https://github.com/apache/kafka/blob/trunk/docker/examples/docker-compose-files/cluster/isolated/plaintext/docker-compose.yml) is taken from official Kafka Docker image repository. See [Multi Node Cluster/Isolated](https://github.com/apache/kafka/tree/trunk/docker/examples#multi-node-cluster) section for more details.
