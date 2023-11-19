package br.com.estudo.springkafka;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.subject.TopicRecordNameStrategy;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Properties;


public class KafkaAvroCustomProducer {
    private static final String TOPIC = "mykafkatopic";

    public static void main(String[] args) throws IOException {
        //Kafka producer properties
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", KafkaAvroSerializer.class.getName());
        properties.setProperty("schema.registry.url", "http://127.0.0.1:8081");

        //Set value for new property
        properties.setProperty("value.subject.name.strategy", TopicRecordNameStrategy.class.getName());

        //Create user avro message
        GenericRecord userAvroPayload = createUserAvroPayload();

        //Create movie avro message
        GenericRecord movieAvroPayload = createMovieAvroPayload();

        //Create kafka producer and set properties
        Producer<String, GenericRecord> producer = new KafkaProducer<>(properties);

        //Create 2 kafka messages
        ProducerRecord<String, GenericRecord> userAvroRecord = new ProducerRecord<>(TOPIC, userAvroPayload);
        ProducerRecord<String, GenericRecord> movieAvroRecord = new ProducerRecord<>(TOPIC, movieAvroPayload);

        //Send both messages to kafka
        producer.send(movieAvroRecord);
        producer.send(userAvroRecord);

        producer.flush();
        producer.close();
    }

    private static GenericRecord createMovieAvroPayload() throws IOException {

        //Create schema from .avsc file
        Schema mainSchema = new Schema.Parser().parse(new ClassPathResource("avro/movie-v1.avsc").getInputStream());

        //Create avro message with defined schema
        GenericRecord avroMessage = new GenericData.Record(mainSchema);

        //Populate avro message
        avroMessage.put("movie_name", "Casablanca");
        avroMessage.put("genre", "Drama/Romance");

        return avroMessage;
    }

    private static GenericRecord createUserAvroPayload() throws IOException {

        //Create schema from .avsc file
        Schema mainSchema = new Schema.Parser().parse(new ClassPathResource("avro/user-v1.avsc").getInputStream());

        //Create avro message with defined schema
        GenericRecord avroMessage = new GenericData.Record(mainSchema);

        //Populate avro message
        avroMessage.put("first_name", "Karen");
        avroMessage.put("last_name", "Grygoryan");

        return avroMessage;
    }
}
