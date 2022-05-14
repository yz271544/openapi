package com.teradata.bigdata.openapi.rewrite;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by hdfs on 2016/6/6.
 */
public class GBKTextOutputFormat<K, V> extends FileOutputFormat<K, V> {
    private static String SEPERATOR = "mapreduce.output.textoutputformat.separator";
    @SuppressWarnings("unchecked")
    public GBKTextOutputFormat() {
    }
    @SuppressWarnings("unchecked")
    public RecordWriter<K, V> getRecordWriter(TaskAttemptContext job) throws IOException, InterruptedException {
        Configuration conf = job.getConfiguration();
        boolean isCompressed = getCompressOutput(job);
        String keyValueSeparator = conf.get(SEPERATOR, "\t");
        CompressionCodec codec = null;
        String extension = "";
        if(isCompressed) {
            Class file = getOutputCompressorClass(job, GzipCodec.class);
            codec = (CompressionCodec) ReflectionUtils.newInstance(file, conf);
            extension = codec.getDefaultExtension();
        }

        Path file1 = this.getDefaultWorkFile(job, extension);
        FileSystem fs = file1.getFileSystem(conf);
        FSDataOutputStream fileOut;
        if(!isCompressed) {
            fileOut = fs.create(file1, false);
            return new GBKTextOutputFormat.LineRecordWriter(fileOut, keyValueSeparator);
        } else {
            fileOut = fs.create(file1, false);
            return new GBKTextOutputFormat.LineRecordWriter(new DataOutputStream(codec.createOutputStream(fileOut)), keyValueSeparator);
        }
    }
    @SuppressWarnings("unchecked")
    protected static class LineRecordWriter<K, V> extends RecordWriter<K, V> {
        private static final String gbk = "gbk";
        private static final byte[] newline;
        protected DataOutputStream out;
        private final byte[] keyValueSeparator;

        public LineRecordWriter(DataOutputStream out, String keyValueSeparator) {
            this.out = out;

            try {
                this.keyValueSeparator = keyValueSeparator.getBytes(gbk);
            } catch (UnsupportedEncodingException var4) {
                throw new IllegalArgumentException("can\'t find gbk encoding");
            }
        }
        @SuppressWarnings("unchecked")
        public LineRecordWriter(DataOutputStream out) {
            this(out, "\t");
        }
        @SuppressWarnings("unchecked")
        private void writeObject(Object o) throws IOException {
            if(o instanceof Text) {
               /* Text to = (Text)o;
                this.out.write(to.getBytes(), 0, to.getLength());
            } else {*/
                this.out.write(o.toString().getBytes(gbk));
            }

        }
        @SuppressWarnings("unchecked")
        public synchronized void write(K key, V value) throws IOException {
            boolean nullKey = key == null || key instanceof NullWritable;
            boolean nullValue = value == null || value instanceof NullWritable;
            if(!nullKey || !nullValue) {
                if(!nullKey) {
                    this.writeObject(key);
                }

                if(!nullKey && !nullValue) {
                    this.out.write(this.keyValueSeparator);
                }

                if(!nullValue) {
                    this.writeObject(value);
                }

                this.out.write(newline);
            }
        }
        @SuppressWarnings("unchecked")
        public synchronized void close(TaskAttemptContext context) throws IOException {
            this.out.close();
        }

        static {
            try {
                newline = "\n".getBytes("gbk");
            } catch (UnsupportedEncodingException var1) {
                throw new IllegalArgumentException("can\'t find GBK encoding");
            }
        }
    }
}
