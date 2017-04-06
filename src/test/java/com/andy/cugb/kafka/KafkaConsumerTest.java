package com.andy.cugb.kafka;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;

/**
 * Created by jbcheng on 4/5/17.
 */
public class KafkaConsumerTest<T> {

    private KafkaConsumer<String,T> consumer;

    public KafkaConsumerTest () {
        Properties props = System.getProperties();
        props.put("bootstrap.servers", "123.126.62.116:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "false");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.type","java.lang.String");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // props.put("key.deserializer", "org.apache.kafka.common.serialization.BytesSerializer");
        // props.put("value.deserializer", "org.apache.kafka.common.serialization.BytesSerializer");
//        props.put("value.deserializer", KryoKafkaSerializer.class.getName());
        consumer = new KafkaConsumer<String, T>(props);
    }

    public void consume() {
        consumer.subscribe(Arrays.asList("test"));
        while (true) {
            try {
                ConsumerRecords<String,T> records = consumer.poll(Long.MAX_VALUE);
                Set<TopicPartition> partitions = records.partitions();
                for (TopicPartition partition  : partitions) {
                    List<ConsumerRecord<String,T>> partitionRecords = records.records(partition);
                    for (ConsumerRecord<String, T> record : partitionRecords) {
                        System.out.println("record:" + record.toString());
                        System.out.printf("offset = %d,value=%s\n", record.offset(), record.value());
                        System.out.println("partitions:" + consumer.partitionsFor(record.topic()));
                        System.out.println(consumer.position(new TopicPartition(record.topic(), record.partition())));
//                    consumer.commitSync();
                    }
                    long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                    consumer.commitSync(Collections.singletonMap(partition,new OffsetAndMetadata(lastOffset + 1)));
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void position() {
        consumer.subscribe(Arrays.asList("test"));
        while (true) {
            long lastOffset = 0l;
            try {
                ConsumerRecords<String,T> records = consumer.poll(Long.MAX_VALUE);
                Set<TopicPartition> partitions = records.partitions();
                for (TopicPartition partition  : partitions) {
                    List<ConsumerRecord<String,T>> partitionRecords = records.records(partition);
                    for (ConsumerRecord<String, T> record : partitionRecords) {
                        System.out.println("record:" + record.toString());
                        System.out.printf("offset = %d,value=%s\n", record.offset(), record.value());
                        System.out.println("partitions:" + consumer.partitionsFor(record.topic()));
                        System.out.println(consumer.position(new TopicPartition(record.topic(), record.partition())));
//                    consumer.commitSync();
                    }
                    lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                    // 1.同步提交
                    consumer.commitSync(Collections.singletonMap(partition,new OffsetAndMetadata(lastOffset + 1)));
                    // 2.异步提交+回调
                    consumer.commitAsync(new OffsetCommitCallback() {
                        public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {

                        }
                    });
                }
            } catch (Exception e) {
                System.out.println(e);
                // 异常时,需要消费失败的记录
                consumer.seek(new TopicPartition("test", 0),lastOffset - 5);
            }
        }
    }

    public static void main(String[] args) {
        KafkaConsumerTest consumerTest = new KafkaConsumerTest();
//        consumerTest.consume();
        consumerTest.position();
    }
}
