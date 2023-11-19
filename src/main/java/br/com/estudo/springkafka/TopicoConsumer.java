package br.com.estudo.springkafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Objects;

@EnableKafka
@Component
public class TopicoConsumer {
    Logger logger = LoggerFactory.getLogger(TopicoConsumer.class);

    @KafkaListener(topics = "${topic.name.producer.avro}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(ConsumerRecord<String, Mensagem> in,
                       @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) Object object) {

        logger.info("Key: {}",String.valueOf(object));
        logger.info("Consumer ...");
        logger.info(String.valueOf(in.value()));
    }
}
