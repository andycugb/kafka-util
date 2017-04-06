package com.andy.cugb.kafka.dispatcher;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.andy.cugb.kafka.dispatcher.wrapper.BusinessWrapper;
import com.andy.cugb.kafka.dispatcher.wrapper.DispatcherWrapper;

/**
 * Created by jbcheng on 4/6/17.
 */
public class DispatcherWrapperTest implements BusinessWrapper<KafkaDispPojo>{

    public void alarm() {
        System.out.println("wrapper alarm");
    }

    public void businessAlarm(ConsumerRecord<String, DispatcherWrapper<KafkaDispPojo>> record) {
        System.out.println("business wrapper alarm");
    }

    public void processRecord(ConsumerRecord<String, DispatcherWrapper<KafkaDispPojo>> consumerRecord) {
        System.out.println("DispatcherWrapperTest,key:" + consumerRecord.key());
        System.out.println("DispatcherWrapperTest,producetKey:" + consumerRecord.value().getProductKey());
        System.out.println("DispatcherWrapperTest,producetValue:" + consumerRecord.value().getSendValue());
    }
}