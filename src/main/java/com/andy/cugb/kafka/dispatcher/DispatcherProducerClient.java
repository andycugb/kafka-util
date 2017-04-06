package com.andy.cugb.kafka.dispatcher;

import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.andy.cugb.kafka.KafkaProducerClient;
import com.andy.cugb.kafka.dispatcher.wrapper.DispatcherWrapper;

/**
 * Created by jbcheng on 4/5/17.公用队列到分发器的producer
 */
public class DispatcherProducerClient<T> extends KafkaProducerClient<DispatcherWrapper<T>> {

    /**
     * record方式生产.
     *
     * @param producerRecord record
     * @return 操作结果
     */
    @Override
    public Future<RecordMetadata> produce(
            ProducerRecord<String, DispatcherWrapper<T>> producerRecord) {
        return produce(producerRecord, null);
    }

    /**
     * record方式生产,带回调
     *
     * @param producerRecord record对象
     * @param callback 回调
     * @return 操作结果
     */
    @Override
    public Future<RecordMetadata> produce(
            ProducerRecord<String, DispatcherWrapper<T>> producerRecord, Callback callback) {
        return super.produce(producerRecord, callback);
    }

    /**
     * 不使用key的生产方式
     *
     * @param topic topic
     * @param value 值
     * @return 操作结果
     */
    @Override
    public Future<RecordMetadata> produce(String topic, DispatcherWrapper<T> value) {
        return produce(new ProducerRecord<String, DispatcherWrapper<T>>(topic, value));
    }
}
