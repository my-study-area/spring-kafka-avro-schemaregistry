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

    @Value("${topic.name.producer.avro}")
    private String topicNameAvro;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTemplate<String, Mensagem> kafkaTemplateAvro;

    public TopicoProducer(KafkaTemplate<String, String> kafkaTemplate,
                          KafkaTemplate<String, Mensagem> kafkaTemplateAvro) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplateAvro = kafkaTemplateAvro;
    }

    public void enviarMensagem(String mensagem) {
        kafkaTemplate.send(topicName, mensagem);
        logger.info("Mensagem {} enviada", mensagem);
    }

    public void enviarMensagemAvro(Mensagem mensagem) {
        kafkaTemplateAvro.send(topicNameAvro, mensagem.getRemetente().toString(), mensagem);
        logger.info("Mensagem {} enviada", mensagem);
    }

}
