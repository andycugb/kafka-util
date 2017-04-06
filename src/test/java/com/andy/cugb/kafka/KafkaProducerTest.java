package com.andy.cugb.kafka;

import java.util.Properties;
import java.util.Scanner;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * Created by jbcheng on 4/5/17.
 */
public class KafkaProducerTest<T> {
    private KafkaProducer<String,T> producer;

    public KafkaProducerTest() {
        Properties props = System.getProperties();
        props.put("bootstrap.servers", "123.126.62.116:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("compression.type", "lz4");
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // props.put("key.serializer", "org.apache.kafka.common.serialization.BytesSerializer");
        // props.put("value.serializer", "org.apache.kafka.common.serialization.BytesSerializer");
//        props.put("value.serializer", KryoKafkaSerializer.class.getName());

        producer = new KafkaProducer<String, T>(props);
    }

    private void produce(T value) {
        producer.send(new ProducerRecord<String, T>("test",value));
    }
    public static void main(String[] args) {
        KafkaProducerTest producerTest = new KafkaProducerTest();
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String msg = scanner.nextLine();
            if (null != msg && !"".equals(msg)) {
                if ("exit".equals(msg)) {
                    break;
                }
                producerTest.produce(msg);
            }
        }
        producerTest.producer.close();
    }
}
