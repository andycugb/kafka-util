package com.andy.cugb.kafka;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andy.cugb.kafka.config.KafkaConsumerConfig;

/**
 * Created by jbcheng on
 * 4/5/17.kafka消费者抽象类.processRecord方法供子类具体业务逻辑使用,alarm方法在业务异常时会被调用,子类需要实现destroy-method策略,及时回收连接.
 * <p>
 * 手动提交offset方式,业务异常时,依然会提交offset,子类可在businessAlram中获取异常record信息,补救可能丢失的记录
 * </p>
 * 建议发送数据实现toString方法,方便异常信息排查
 */
public abstract class KafkaConsumerClient<T> {
    private final AtomicBoolean closed = new AtomicBoolean(false);
    protected KafkaConsumer<String, T> consumer;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ExecutorService service = Executors.newSingleThreadExecutor();

    protected KafkaConsumerClient(KafkaConsumerConfig consumerConfig, List<String> subscribeTopics) {
        consumer = new KafkaConsumer<String, T>(consumerConfig.getKafkaConsumerConfig());
        consumer.subscribe(subscribeTopics);
    }

    /**
     * 自动提交,无一致性要求业务可使用此种调用方式
     */
    protected final void consumeAuto() {
        logger.info("[" + getClass() + "]kafka auto consume start...");
        service.execute(new Runnable() {
            public void run() {
                while (!closed.get()) {
                    try {
                        long start = System.currentTimeMillis();
                        if (logger.isDebugEnabled()) {
                            logger.debug("consume auto begin,time:" + start);
                        }
                        ConsumerRecords<String, T> records =
                                consumer.poll(TimeUnit.SECONDS.toMillis(100));
                        for (ConsumerRecord<String, T> record : records) {
                            try {
                                processRecord(record);
                            } catch (Exception e) {
                                logger.error("cosume auto business error,record:" + record, e);
                                businessAlarm(record);
                            }
                        }
                        long end = System.currentTimeMillis();
                        if (logger.isDebugEnabled()) {
                            logger.debug("consume auto end,time:" + end + ",cost:"
                                    + (end - start));
                        }
                    } catch (Exception e) {
                        logger.error("consume auto error:", e);
                        alarm();
                    }
                }
                consumer.close();
            }
        });
    }

    /**
     * 自动提交,无一致性要求业务可使用此种调用方式
     */
    protected final void consumeMannual() {
        logger.info("[" + getClass() + "]kafka mannual consume start...");
        service.execute(new Runnable() {
            public void run() {
                while (!closed.get()) {
                    try {
                        long start = System.currentTimeMillis();
                        if (logger.isDebugEnabled()) {
                            logger.debug("consume mannual begin,time:" + start);
                        }
                        ConsumerRecords<String, T> records = consumer.poll(100);
                        for (ConsumerRecord<String, T> record : records) {
                            try {
                                processRecord(record);
                            } catch (Exception e) {
                                logger.error("cosume mannual business error,record:" + record, e);
                                businessAlarm(record);
                            }
                        }
                        consumer.commitAsync();
                        long end = System.currentTimeMillis();
                        if (logger.isDebugEnabled()) {
                            logger.debug("consume mannual end,time:" + end + ",cost:"
                                    + (end - start));
                        }
                    } catch (Exception e) {
                        logger.error("consume mannual error:", e);
                        alarm();
                    }
                }
                consumer.close();
            }
        });
    }

    protected abstract void alarm();

    protected abstract void processRecord(ConsumerRecord<String, T> record);

    protected abstract void businessAlarm(ConsumerRecord<String, T> record);

    protected void closeConsumer() {
        logger.info("kafka consumer closing...");
        try {
            closed.set(true);
            service.shutdown();
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("thread been interrupted when shutdown", e);
        }
        logger.info("kafka consumer closed...");
    }
}
