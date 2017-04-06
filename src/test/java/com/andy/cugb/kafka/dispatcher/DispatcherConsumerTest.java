package com.andy.cugb.kafka.dispatcher;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by jbcheng on 4/6/17.
 */
public class DispatcherConsumerTest {
    @BeforeClass
    public static void setUp() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-dispatch-consumer.xml");
    }

    @Test
    public void testConsumer() {
        while (true) {

        }
    }
}
