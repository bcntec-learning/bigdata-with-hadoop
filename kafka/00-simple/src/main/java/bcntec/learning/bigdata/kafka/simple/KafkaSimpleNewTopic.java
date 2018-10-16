package bcntec.learning.bigdata.kafka.simple;

import bcntec.learning.bigdata.kafka.KafkaUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.TopicExistsException;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;


/**
 * @author fphilip@houseware.es
 */
public class KafkaSimpleNewTopic {


    public static void main(String[] args) {

        CommandLine cl = SimpleUtils.commandLine("kafka-topic",args);

        Properties p = SimpleUtils.commandLineKafkaProperties(cl);
        String topicName = cl.getOptionValue("t");



        try (final AdminClient adminClient = KafkaAdminClient.create(p)) {
                try {

                    final NewTopic newTopic = new NewTopic(topicName, 1, (short)1);


                    final CreateTopicsResult createTopicsResult = adminClient.createTopics(Collections.singleton(newTopic));

                    createTopicsResult.values().get(topicName).get();
                } catch (InterruptedException | ExecutionException e) {
                    if (!(e.getCause() instanceof TopicExistsException)) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            }


    }

}
