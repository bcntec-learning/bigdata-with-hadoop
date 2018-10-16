package bcntec.learning.bigdata.kafka.simple;

import org.apache.commons.cli.CommandLine;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author fphilip@houseware.es
 */
public class KafkaSimpleConsumer {

    public static void main(String[] args) {

        CommandLine cl = SimpleUtils.commandLine("kafka-consumed", args);

        Properties properties = SimpleUtils.commandLineKafkaProperties(cl);
        String topicName = cl.getOptionValue("t");


        /* option 1
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("value.deserializer", StringDeserializer.class.getName());
        Consumer<String, String> consumer = new KafkaConsumer<>(properties);
        */
        //option2
        Consumer<String, String> consumer = new KafkaConsumer<>(properties,
                new StringDeserializer(),
                new StringDeserializer());
        consumer.subscribe(Arrays.asList(topicName));

        Scanner sc = new Scanner(System.in);
        int j = 0;
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(100000000));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(record);
                }
            }
        } catch (WakeupException e) {   // ignore for shutdown } finally {   consumer.close(); }
        }
    }
}
