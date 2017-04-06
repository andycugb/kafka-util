package com.andy.cugb.kafka.dispatcher;

import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.andy.cugb.kafka.KafkaConsumerClient;
import com.andy.cugb.kafka.config.KafkaConsumerConfig;
import com.andy.cugb.kafka.dispatcher.wrapper.BusinessWrapper;
import com.andy.cugb.kafka.dispatcher.wrapper.DispatcherWrapper;

/**
 * Created by jbcheng on 4/5/17.公用队列到分发器的consumer
 */
public class DispatcherConsumerClient<T> extends KafkaConsumerClient<DispatcherWrapper<T>> {
    private Map<String, BusinessWrapper> businessWrapperMap;
    private ThreadLocal<BusinessWrapper> businessWrapperThreadLocal =
            new ThreadLocal<BusinessWrapper>();

    protected DispatcherConsumerClient(KafkaConsumerConfig consumerConfig,
            List<String> subscribeTopics, Map<String, BusinessWrapper> dispatchMap) {
        super(consumerConfig, subscribeTopics);
        businessWrapperMap = dispatchMap;
        super.consumeMannual();
    }

    @Override
    protected void alarm() {
        businessWrapperThreadLocal.get().alarm();
    }

    @Override
    protected void businessAlarm(ConsumerRecord<String, DispatcherWrapper<T>> record) {
        businessWrapperThreadLocal.get().businessAlarm(record);
    }

    @Override
    protected void processRecord(ConsumerRecord<String, DispatcherWrapper<T>> consumerRecord) {
        BusinessWrapper businessWrapper =
                businessWrapperMap.get(consumerRecord.value().getProductKey());
        businessWrapperThreadLocal.set(businessWrapper);
        businessWrapper.processRecord(consumerRecord);
    }
}
