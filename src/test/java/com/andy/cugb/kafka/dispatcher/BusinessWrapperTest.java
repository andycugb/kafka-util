package com.andy.cugb.kafka.dispatcher;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.andy.cugb.kafka.KafkaTestPojo;
import com.andy.cugb.kafka.dispatcher.wrapper.BusinessWrapper;
import com.andy.cugb.kafka.dispatcher.wrapper.DispatcherWrapper;

/**
 * Created by jbcheng on 4/6/17.
 */
public class BusinessWrapperTest implements BusinessWrapper<KafkaTestPojo>{
    public void alarm() {
        System.out.println("wrapper alarm");
    }

    public void businessAlarm(ConsumerRecord<String, DispatcherWrapper<KafkaTestPojo>> record) {
        System.out.println("business wrapper alarm");
    }

    public void processRecord(ConsumerRecord<String, DispatcherWrapper<KafkaTestPojo>> consumerRecord) {
        System.out.println("businessWrapperTest,key:" + consumerRecord.key());
        System.out.println("businessWrapperTest,producetKey:" + consumerRecord.value().getProductKey());
        System.out.println("businessWrapperTest,producetValue:" + consumerRecord.value().getSendValue());
    }
}
