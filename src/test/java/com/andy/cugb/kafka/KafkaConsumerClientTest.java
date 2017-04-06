package com.andy.cugb.kafka;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.andy.cugb.kafka.config.KafkaConsumerConfig;

/**
 * Created by jbcheng on 4/5/17.
 */
public class KafkaConsumerClientTest extends KafkaConsumerClient<KafkaTestPojo> {

    public KafkaConsumerClientTest(KafkaConsumerConfig consumerConfig,
            List<String> subscribeTopics) {
        super(consumerConfig, subscribeTopics);
        super.consumeMannual();
    }

    @Override
    protected void alarm() {
        System.out.println("this is alarm");
    }

    @Override
    protected void businessAlarm(ConsumerRecord<String, KafkaTestPojo> record) {
        System.out.println("this is business alarm");
    }


    @Override
    protected void processRecord(ConsumerRecord<String, KafkaTestPojo> record) {
        System.out.printf("offset = %d, value = %s\n", record.offset(), record.value());
    }
}
