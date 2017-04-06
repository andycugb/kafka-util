package com.andy.cugb.kafka;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by jbcheng on 4/5/17.
 */
public class KafkaConsumerTestSpring {

    private static KafkaConsumerClient consumerUtil;

    @BeforeClass
    public static void beforeClass() {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext-consumer.xml");
        consumerUtil = (KafkaConsumerClient) context.getBean("kafkaConsumerClient");
    }

    @Test
    public void test() {
        while (true) {

        }
    }
}
