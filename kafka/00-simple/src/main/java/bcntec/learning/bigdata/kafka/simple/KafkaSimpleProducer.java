package bcntec.learning.bigdata.kafka.simple;

import bcntec.learning.bigdata.kafka.KafkaUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author fphilip@houseware.es
 */
public class KafkaSimpleProducer {


    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CommandLine cl = SimpleUtils.commandLine("kafka-producer", args);

        Properties properties = SimpleUtils.commandLineKafkaProperties(cl);
        String topicName = cl.getOptionValue("t");

        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer", StringSerializer.class.getName());

        Producer<String, String> producer = new KafkaProducer<>(properties);
        Scanner sc = new Scanner(System.in);
        int j=0;
        do {
            String l = sc.nextLine();
            Future<RecordMetadata> ret = producer.send(new ProducerRecord<>(topicName, Integer.toString(j++),l));
            System.out.println("sent.. "+ret.get().offset());
        }while (true);
    }
}
