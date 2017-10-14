import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class CsvPivot {
    final static String CSV_SEPARATOR = ",";
    final static char SEPARATOR = ':';

    public static class CsvMapper extends Mapper<Object, Text, IntWritable, Text> {

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] values = value.toString().split(CSV_SEPARATOR);

            for (int i = 0; i < values.length; i++) {
                // num colonne => "num ligne:value"
                context.write(new IntWritable(i), new Text(String.valueOf(key) + SEPARATOR + values[i]));
            }
        }
    }

    public static class PivotReducer extends Reducer<IntWritable, Text, IntWritable, Text> {
        Text result = new Text();

        @Override
        protected void reduce(IntWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            StringBuilder sb = new StringBuilder();

            // treemap = auto sort by key
            Map<String, String> valuesByLineNumber = new TreeMap<String, String>();
            for (Text textValue : values) {
                String value = textValue.toString();
                // put into our map: key = before the colon, value = after the colon
                int separatorIndex = value.indexOf(SEPARATOR);
                valuesByLineNumber.put(value.substring(0, separatorIndex), value.substring(separatorIndex + 1));
            }

            // build a valid csv line by iterating our treemap values
            Iterator<String> it = valuesByLineNumber.values().iterator();
            while (it.hasNext()) {
                sb.append(it.next());
                if (it.hasNext()) {
                    sb.append(",");
                }
            }

            // output line
            result.set(sb.toString());
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "csv pivot");
        job.setJarByClass(CsvPivot.class);
        job.setMapperClass(CsvMapper.class);
        job.setReducerClass(PivotReducer.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        boolean success = job.waitForCompletion(true);
        System.exit(success ? 0 : 1);
    }
}
