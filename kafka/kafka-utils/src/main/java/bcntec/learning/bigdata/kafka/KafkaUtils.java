package bcntec.learning.bigdata.kafka;

import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author fphilip@houseware.es
 */
public class KafkaUtils {


    public static CommandLine commandLine(String name, Option[] options, String[] args) {
        Options options2 = new Options();

        for (Option option : options) {
            options2.addOption(option);
        }

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            return parser.parse(options2, args, true);
        } catch (ParseException e) {
            formatter.printHelp(name, options2);
            System.exit(1);
            return null;
        }
    }

    public static Properties resolveProperties(String def, String option, CommandLine cmd) throws IOException {

        try(InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(def)){

            return resolveProperties(inputStream, option, cmd);

        }
    }

    public static Properties resolveProperties(InputStream inputStream, String option, CommandLine cmd) throws IOException {

        Properties properties = new Properties();
        properties.load(inputStream);

        if (cmd.hasOption(option)) {
            properties.load(new FileInputStream(cmd.getOptionValue(option)));
        }
        return properties;

    }

    public static Properties kafkaConfigurations(Properties properties) {

        Properties ret = new Properties();

        for (String o : properties.stringPropertyNames()) {
            if(o.startsWith("kafka.")){
                ret.setProperty(o.substring(6),properties.getProperty(o));
            }
        }


        return ret;
    }

}
