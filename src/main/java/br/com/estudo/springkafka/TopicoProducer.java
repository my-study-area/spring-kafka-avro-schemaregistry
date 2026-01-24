package br.com.estudo.springkafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TopicoProducer {
    Logger logger = LoggerFactory.getLogger(TopicoProducer.class);

    @Value("${topic.name.producer}")
    private String topicName;

    @Value("${topic.name.producer.avro}")
    private String topicNameAvro;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTemplate<Key, Mensagem> kafkaTemplateAvro;

    public TopicoProducer(KafkaTemplate<String, String> kafkaTemplate,
                          KafkaTemplate<Key, Mensagem> kafkaTemplateAvro) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplateAvro = kafkaTemplateAvro;
    }

    public void enviarMensagem(String mensagem) {
        String key = UUID.randomUUID().toString();
        kafkaTemplate.send(topicName, key, mensagem);
        logger.info("Mensagem {} enviada", mensagem);
    }

    public void enviarMensagemAvro(Mensagem mensagem) {
        Key key = Key.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setCorrelationId(UUID.randomUUID().toString())
                .build();
        ProducerRecord<Key, Mensagem> producerRecord = new ProducerRecord<>(topicNameAvro,key, mensagem);
        kafkaTemplateAvro.send(producerRecord);
        logger.info("Mensagem {} enviada", mensagem);
        logger.info("ProducerRecord enviado: {}", producerRecord);
    }

}
