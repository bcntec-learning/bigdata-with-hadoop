package bcntec.learning.bigdata.hadoop.mapred.partitioner1;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author fphilip@bcntec.es
 */
public class WordCounterMap extends Mapper<LongWritable, Text, Text, LongWritable> {

    private static List<String> discardWords = new ArrayList<>();
    static {
        discardWords.add("");
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String line = value.toString().toUpperCase().replaceAll("[^a-zA-Z 0-9]+","").replaceAll("\\s+", " ");

        final List<String> words = extractWordsFromLine(line);

        for (String word : words) {
            context.write(new Text(word), new LongWritable(1));
        }

    }

    protected List<String>  extractWordsFromLine(String line) {

        if (line != null && !"".equals(line)) {
            return getWords(line);
        }
        return Collections.emptyList();
    }

    private List<String>  getWords(String line) {

        final String[] words = line.split(" ");

        List<String>  finalWords = new ArrayList<>(words.length);
        for (String word : words) {
            if (!"".equals(word) && !discardWords.contains(word)) {
                finalWords.add(word);
            }
        }
        return finalWords;
    }
}