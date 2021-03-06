package bcntec.learning.bigdata.hadoop.mapred.multiples;

import lombok.NoArgsConstructor;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author fphilip@bcntec.es
 */

@SuppressWarnings("unused")
public class WordCounterWithMultiples {


    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("Use: WordCounterWithMultiples <SOURCE_PATH> <TARGET_PATH>");
            System.exit(1);
        }

        System.out.println("Start word counter program with these parameters: "
                + Arrays.toString(args));

        String source = args[0];
        String target = args[1];

        boolean success = false;

        try {
            Configuration conf = new Configuration();
            Job job = Job.getInstance(conf, "My WordCounter Program");
            job.setJarByClass(WordCounterWithMultiples.class);

            FileInputFormat.addInputPath(job, new Path(source));
            FileOutputFormat.setOutputPath(job, new Path(target));

            job.setMapperClass(WordCounterMap.class);
            job.setReducerClass(WordCounterReduce.class);
            job.setCombinerClass(WordCounterReduce.class);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(LongWritable.class);


            MultipleOutputs.addNamedOutput(job, "STARTA", TextOutputFormat.class, Text.class, IntWritable.class);


            success = job.waitForCompletion(true);
        } catch (Exception e) {
            System.err.println("Error counting words.");
            e.printStackTrace(System.err);
        }


        System.exit(success ? 0 : 1);

    }

    @NoArgsConstructor
    public static class WordCounterMap extends Mapper<LongWritable, Text, Text, LongWritable> {


        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, LongWritable>.Context context) throws IOException, InterruptedException {

            String line = value.toString().toUpperCase().replaceAll("[^a-zA-Z 0-9]+", "").replaceAll("\\s+", " ");


            if (line != null && !"".equals(line)) {
                final String[] words = line.split(" ");

                List<String> finalWords = new ArrayList<>(words.length);
                for (String word : words) {
                    if (!"".equals(word)) {
                        finalWords.add(word);
                    }
                }

                for (String word : finalWords) {
                    context.write(new Text(word), new LongWritable(1));
                }
            }

        }


    }

    @NoArgsConstructor
    public static class WordCounterReduce extends Reducer<Text, LongWritable, Text, LongWritable> {
        MultipleOutputs<Text, LongWritable> mos;

        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Reducer<Text, LongWritable, Text, LongWritable>.Context context) throws IOException, InterruptedException {

            long wordOccurrences = 0;

            for (LongWritable value : values) {
                wordOccurrences++;
            }

            context.write(key, new LongWritable(wordOccurrences));

            if (key.toString().startsWith("A")) {
                mos.write("STARTA", key, new LongWritable(wordOccurrences));
            }

        }

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            mos = new MultipleOutputs<>(context);

        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            mos.close();
        }
    }
}