package bcntec.learning.bigdata.hadoop.twitter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class HadoopTwitterIngestor {
    public static void main(String[] args) throws Exception {
        LinkedBlockingQueue<Status> queue = new LinkedBlockingQueue<>(1000);


        CommandLine cmd = commandLine(args);
        String defProperties = "twitter-default.properties";
        Properties properties = resolveProperties(defProperties, "f", cmd);


        String consumerKey = properties.getProperty("twitter.consumer-key");
        String consumerSecret = properties.getProperty("twitter.consumer-secret");
        String accessToken = properties.getProperty("twitter.access.token");
        String accessTokenSecret = properties.getProperty("twitter.access.token-secret");

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
                if (log.isDebugEnabled()) {
                    log.debug("user:" + ret.getUser().getName() + "   text: " + ret.getText());
                }
                for (HashtagEntity hashtage : ret.getHashtagEntities()) {
                    if (log.isDebugEnabled()) {
                        log.debug("Hashtag: " + hashtage.getText());
                    }

                }
            }
            i = 0;
            k++;

        }


        Thread.sleep(5000);
        log.info("bye...");
        twitterStream.shutdown();
    }


    private static Properties resolveProperties(String def, String option, CommandLine cmd) throws IOException {

        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(def)) {

            return resolveProperties(inputStream, option, cmd);

        }
    }

    private static Properties resolveProperties(InputStream inputStream, String option, CommandLine cmd) throws IOException {

        Properties properties = new Properties();
        properties.load(inputStream);

        if (cmd.hasOption(option)) {
            properties.load(new FileInputStream(cmd.getOptionValue(option)));
        }
        return properties;

    }

    private static CommandLine commandLine(String[] args) {
        Options options = new Options();

        options.addOption(new Option("f", "file", true, "configuration file path"));

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            return parser.parse(options, args, true);
        } catch (ParseException e) {
            formatter.printHelp("hadoop-twitter", options);
            System.exit(1);
            return null;
        }
    }


}