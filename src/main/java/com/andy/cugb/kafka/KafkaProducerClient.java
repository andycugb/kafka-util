package com.andy.cugb.kafka;

import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andy.cugb.kafka.config.KafkaProducerConfig;

/**
 * Created by jbcheng on 4/5/17. kafka生产者使用类
 */
public class KafkaProducerClient<T> {
    private KafkaProducerConfig producerConfig;
    private volatile Producer<String, T> producer;
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Producer<String, T> getProducer() {
        if (null == producer) {
            synchronized (this) {
                if (null == producer) {
                    producer = new KafkaProducer<String, T>(producerConfig.getProducerConfig());
                }
            }
        }
        return producer;
    }

    /**
     * 不使用key的生产方式
     * 
     * @param topic topic
     * @param value 值
     * @return 操作结果
     */
    public Future<RecordMetadata> produce(String topic, T value) {
        return produce(new ProducerRecord<String, T>(topic, value));
    }

    /**
     * record方式生产.
     * 
     * @param producerRecord record
     * @return 操作结果
     */
    public Future<RecordMetadata> produce(ProducerRecord<String, T> producerRecord) {
        return produce(producerRecord, null);
    }

    /**
     * record方式生产,带回调
     * 
     * @param producerRecord record对象
     * @param callback 回调
     * @return 操作结果
     */
    public Future<RecordMetadata> produce(ProducerRecord<String, T> producerRecord,
            Callback callback) {
        return getProducer().send(producerRecord, callback);
    }

    public void setProducerConfig(KafkaProducerConfig producerConfig) {
        this.producerConfig = producerConfig;
    }
}
