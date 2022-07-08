# spring-kafka-avro-schemaregistry

Projeto de estudo de Kafka com Spring, Avro e Shema Registry

## Anotações
```bash
# inicia container kafka
docker-compose up -d

# acessa o terminal do kafka
docker-compose run --rm kafka bash

# lista os tópicos kafka no container kafka
kafka-topics --zookeeper zookeeper:2181 --list

# cria um producer na linha de comando no container kafka
kafka-console-producer --broker-list kafka:29092 --topic <TOPIC_NAME>

# cria um producer na enviar key e value na mensagem
kafka-console-producer --broker-list kafka:29092 \
--topic <TOPIC_NAME> --property "parse.key=true" --property "key.separator=;"

# exemplo de mensagem enviada com key e value para producer acima
{key: 1};{"nome": "Maria", "idade": 23}

# cri consumer para exibir key e value
kafka-console-consumer --bootstrap-server kafka:29092 --topic <TOPIC_NAME> \
--from-beginning --property print.key=true --property print.value=true

# cria um consumer na linha de comando no container kafka
kafka-console-consumer --bootstrap-server kafka:29092 \
--topic <TOPIC_NAME> --from-beginning

# acessa o console do container schemaregistry
docker-compose exec schemaregistry bash

# consome mensagens avro no container schemaregistry
kafka-avro-console-consumer --bootstrap-server kafka:29092 \
--topic topic3 --property schema.registry.url=http://schemaregistry:8085 \
--from-beginning

# cria uma mensagem via requesição HTTP
curl  http://localhost:8080/api/v1/enviar/<MENSAGEM>

# cria a classe Mensagem de acordo com o avro
mvn clean install

#cria uma mensagem avro
curl  "http://localhost:8080/api/v1/enviar/"\
"?remetente=adriano&destinatario=maria&mensagem=mensagem1" -v

curl -v -G -d 'remetente=adriano' -d 'destinatario=maria' \
-d 'mensagem=mensagem1' "http://localhost:8080/api/v1/enviar/"
```

## Configuração de consumer via código
```java
@EnableKafka
@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrap;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, Mensagem> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                groupId);
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroDeserializer.class);
        props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://0.0.0.0:8085");
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");

        System.out.println(props.toString());

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Mensagem>
                                          kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Mensagem> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

}
```
