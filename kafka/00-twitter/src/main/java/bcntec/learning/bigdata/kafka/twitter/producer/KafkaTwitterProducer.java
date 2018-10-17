package bcntec.learning.bigdata.kafka.twitter.producer;

import bcntec.learning.bigdata.kafka.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class KafkaTwitterProducer {
    public static void main(String[] args) throws Exception {
        LinkedBlockingQueue<Status> queue = new LinkedBlockingQueue<>(1000);


        CommandLine cmd = commandLine(args);
        String defProperties = "bigdata/twitter/producer/default.properties";
        Properties properties = KafkaUtils.resolveProperties(defProperties, "f", cmd);


        String consumerKey = properties.getProperty("twitter.consumer-key");
        String consumerSecret = properties.getProperty("twitter.consumer-secret");
        String accessToken = properties.getProperty("twitter.access.token");
        String accessTokenSecret = properties.getProperty("twitter.access.token-secret");
        String topicName = properties.getProperty("tweets.topic.name");
        String tagsTopicName = properties.getProperty("hashtags.topic.name");

        String[] keyWords = cmd.getArgs();

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);
        TwitterStreamFactory twitterStreamFactory = new TwitterStreamFactory(cb.build());
        TwitterStream twitterStream = twitterStreamFactory.getInstance();
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
        Properties kafkaProperties = KafkaUtils.kafkaConfigurations(properties);

        kafkaProperties.put("key.serializer", StringSerializer.class.getName());
        kafkaProperties.put("value.serializer", StringSerializer.class.getName());

        Producer<String, String> producer = new KafkaProducer<>(kafkaProperties);
        int i = 0;
        int j = 0;
        int k = 0;

        while (i < 10) {


            Status ret = queue.poll();

            if (ret == null) {
                log.info("processed :" + k);
                log.info("sleeping...");
                Thread.sleep(10000);
                k = 0;
                i++;
            } else {
                String s = new twitter4j.JSONObject(ret).toString();
                producer.send(new ProducerRecord<>(topicName, Integer.toString(j++), s));
                if (tagsTopicName != null) {
                    for (HashtagEntity hashtage : ret.getHashtagEntities()) {
                        if (log.isDebugEnabled()) {
                            log.debug("Hashtag: " + hashtage.getText());
                        }
                        producer.send(new ProducerRecord<>(tagsTopicName, Integer.toString(j++), hashtage.getText()));
                    }
                }
                i = 0;
                k++;

            }
        }

        producer.close();
        Thread.sleep(5000);
        log.info("bye...");
        twitterStream.shutdown();
    }


    private static CommandLine commandLine(String[] args) {
        return KafkaUtils.commandLine("kafka-twitter",
                new Option[]{
                        new Option("f", "file", true, "configuration file path")},
                args);
    }


}