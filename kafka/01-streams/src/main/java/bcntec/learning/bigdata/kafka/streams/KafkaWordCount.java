package bcntec.learning.bigdata.kafka.streams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Arrays;
import java.util.Properties;

/**
 * Demonstrates, using the high-level KStream DSL, how to implement the WordCount program
 * that computes a simple word occurrence histogram from an input text.
 *
 * In this example, the input stream reads from a topic named "test-line-input", where the values of messages
 * represent lines of text; and the histogram output is written to topic "test-wordcount-output" where each record
 * is an updated count of a single word.
 * kafka-console-consumer.sh --bootstrap-server <hostname:port> \
     --topic test-wordcount-output \
     --from-beginning \
     --formatter kafka.tools.DefaultMessageFormatter \
     --property print.key=true \
     --property print.value=true \
     --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
     --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer
 *
 */
public class KafkaWordCount {

    public static void main(final String[] args) throws Exception {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,System.getProperty("bootstrap.servers"));
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> textLines = builder.stream("test");
        KTable<String, Long> wordCounts = textLines
                .mapValues((ValueMapper<String, String>) String::toLowerCase)
                .flatMapValues(lowerCaseText -> Arrays.asList(lowerCaseText.toLowerCase().split("\\W+")))
                .selectKey((key,word) -> word)
                .groupByKey() //
                .count(Materialized.as("count"));

        wordCounts.toStream().to("test-count", Produced.with(Serdes.String(), Serdes.Long()));

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
    }
}