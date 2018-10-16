package bcntec.learning.bigdata.kafka.twitter.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class KafkaTwitterProducer {
    public static void main(String[] args) throws Exception {
        LinkedBlockingQueue<Status> queue = new LinkedBlockingQueue<>(1000);


        Properties properties = new Properties();
        properties.load(KafkaTwitterProducer.class.getResourceAsStream("/bigdata/twitter/producer/default.properties"));

        CommandLine cmd = commandLine(args);

        if (cmd.hasOption("c")) {
            properties.load(new FileInputStream(cmd.getOptionValue("c")));
        }

        String consumerKey = properties.getProperty("twitter.consumer-key");
        String consumerSecret = properties.getProperty("twitter.consumer-secret");
        String accessToken = properties.getProperty("twitter.access.token");
        String accessTokenSecret = properties.getProperty("twitter.access.token-secret");
        String topicName = properties.getProperty("kafka.topic.name");

        String[] keyWords = cmd.getArgs();

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);

        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        StatusListener listener = new StatusListener() {

            @Override
            public void onStatus(Status status) {
                queue.offer(status);

                if (log.isDebugEnabled()) {
                    log.debug("@" + status.getUser().getScreenName() + " - " + status.getText());
                    for (URLEntity urle : status.getURLEntities()) {
                        log.debug(urle.getDisplayURL());
                    }
                    for (HashtagEntity hashtage : status.getHashtagEntities()) {
                        log.debug(hashtage.getText());
                    }
                }


            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                log.info("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                log.info("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                log.info("Got scrub_geo event userId:" + userId + "upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                log.warn("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                log.error(ex.getMessage());

            }
        };
        twitterStream.addListener(listener);

        FilterQuery query = new FilterQuery().track(keyWords);
        twitterStream.filter(query);

        Thread.sleep(5000);

        //Add Kafka producer config settings
        Properties props = new Properties();
        props.put("bootstrap.servers", properties.getProperty("kafka.bootstrap.servers"));
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);

        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());

        Producer<String, String> producer = new KafkaProducer<>(props);
        int i = 0;
        int j = 0;

        while (i < 10) {
            Status ret = queue.poll();

            if (ret == null) {
                Thread.sleep(100);
                i++;
            } else {
                for (HashtagEntity hashtage : ret.getHashtagEntities()) {
                    System.out.println("Hashtag: " + hashtage.getText());
                    producer.send(new ProducerRecord<>(topicName, Integer.toString(j++), hashtage.getText()));
                }
            }
        }
        producer.close();
        Thread.sleep(5000);
        twitterStream.shutdown();
    }


    public static CommandLine commandLine(String[] args) {
        Options options = new Options();

        Option input = new Option("c", "configuration", true, "configuration file path");
        options.addOption(input);

        Option output = new Option("o", "output", true, "output file");
        options.addOption(output);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            return parser.parse(options, args, true);
        } catch (ParseException e) {
            formatter.printHelp("kafka-twitter", options);
            System.exit(1);
            return null;
        }
    }


}