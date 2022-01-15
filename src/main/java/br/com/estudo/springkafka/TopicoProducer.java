package br.com.estudo.springkafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TopicoProducer {
    Logger logger = LoggerFactory.getLogger(TopicoProducer.class);

    @Value("${topic.name.producer}")
    private String topicName;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public TopicoProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviarMensagem(String mensagem) {
        kafkaTemplate.send(topicName, mensagem);
        logger.info("Mensagem {} enviada", mensagem);
    }
}
