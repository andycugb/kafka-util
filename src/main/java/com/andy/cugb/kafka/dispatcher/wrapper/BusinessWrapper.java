package com.andy.cugb.kafka.dispatcher.wrapper;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * Created by jbcheng on 4/5/17.kafka分发业务队列包装类
 */
public interface BusinessWrapper<T> {

    void alarm();

    void businessAlarm(ConsumerRecord<String, DispatcherWrapper<T>> record);

    void processRecord(ConsumerRecord<String, DispatcherWrapper<T>> consumerRecord);
}
