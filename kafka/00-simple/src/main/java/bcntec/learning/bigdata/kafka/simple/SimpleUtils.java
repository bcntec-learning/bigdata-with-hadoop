package bcntec.learning.bigdata.kafka.simple;

import bcntec.learning.bigdata.kafka.KafkaUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import java.util.Properties;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

/**
 * @author fphilip@houseware.es
 */
public class SimpleUtils {

    public static Properties commandLineKafkaProperties(CommandLine cl) {

        Properties properties = new Properties();

        if(cl.hasOption("z")){
            properties.setProperty(BOOTSTRAP_SERVERS_CONFIG, cl.getOptionValue("z"));
        }
        if(cl.hasOption("b")){
            properties.setProperty(BOOTSTRAP_SERVERS_CONFIG, cl.getOptionValue("b"));
        }
        if(cl.hasOption("g")){
            properties.setProperty(GROUP_ID_CONFIG, cl.getOptionValue("g"));
        }
        if(cl.hasOption("i")){
            properties.setProperty(CLIENT_ID_CONFIG, cl.getOptionValue("i"));
        }



        return properties;

    }


    public static CommandLine commandLine(String name ,String[] args) {
        return KafkaUtils.commandLine(name,
                new Option[]{
                    new Option("b", "broker-list", true, "broker list"),
                    new Option("t", "topic-name", true, "topic name"),
                    new Option("g", "consumer-group", true, "consumer group id"),
                    new Option("p", "partitions", true, "partitions"),
                    new Option("r", "replication-factor", true, "replication factor")},
                args);
    }

}
