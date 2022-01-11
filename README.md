# springkafka

Projeto de estudo de Kafka com Spring

## Anotações
```
# inicia container kafka
docker-compose up -d

# acessa o terminal do kafka
docker-compose run --rm kafka bash

# lista os tópicos kafka
kafka-topics --zookeeper zookeeper:2181 --list

# cria um producer na linha de comando
kafka-console-producer --broker-list kafka:29092 --topic <TOPIC_NAME>

# cria um consumer na linha de comando
kafka-console-consumer --bootstrap-server kafka:29092 --topic <TOPIC_NAME>
```
