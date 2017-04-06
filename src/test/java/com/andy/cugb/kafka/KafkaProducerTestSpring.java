package com.andy.cugb.kafka;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.andy.cugb.kafka.config.KafkaProducerConfig;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

/**
 * Created by jbcheng on 4/5/17.
 */
public class KafkaProducerTestSpring {
    private static KafkaProducerClient producerUtil;
    private static KafkaProducerConfig producerConfig;

    @BeforeClass
    public static void beforeClass() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        producerUtil = (KafkaProducerClient) context.getBean("kafkaProducerClient");
        producerConfig = (KafkaProducerConfig) context.getBean("kafkaProducerConfig");
    }

    @Test
    public void testConfig() {
        System.out.println(producerConfig.getProducerConfig());
    }

    @Test
    public void testProducer() throws InterruptedException,ExecutionException{
        for (int i = 0;i <= 5; i++) {
            KafkaTestPojo ktp = new KafkaTestPojo();
            ktp.setName("this is name-name" + i);
            ktp.setAge(11);
            ktp.setDate(new Date());
            ktp.setUuid(UUID.randomUUID());
            ktp.setList(Arrays.asList("a", "b", "c"));
            Set<String> s = Sets.newHashSet();
            s.add("dddd");
            s.add("sss");
            s.add("ffff");
            ktp.setSet(s);
            ktp.setIlist(ImmutableList.<String>of("wwww", "tttt", "ppp"));
            ktp.setIset(ImmutableSet.<Integer>of(23, 3465, 2341));
            ktp.setBigInteger(new BigInteger(new byte[]{1, 2}));
            ktp.setBigDecimal(new BigDecimal(2323.12123));
            ktp.setaDouble(12341.12341d);
            Thread.sleep(5000);
            System.out.println(producerUtil.produce("test",ktp).get());
            Thread.sleep(5000);
        }
    }

    @Test
    public void testProducerWithKey() throws InterruptedException,ExecutionException{
        for (int i = 0;i <= 5; i++) {
            KafkaTestPojo ktp = new KafkaTestPojo();
            ktp.setName("this is name-name" + i);
            ktp.setAge(11);
            ktp.setDate(new Date());
            ktp.setUuid(UUID.randomUUID());
            ktp.setList(Arrays.asList("a", "b", "c"));
            Set<String> s = Sets.newHashSet();
            s.add("dddd");
            s.add("sss");
            s.add("ffff");
            ktp.setSet(s);
            ktp.setIlist(ImmutableList.<String>of("wwww", "tttt", "ppp"));
            ktp.setIset(ImmutableSet.<Integer>of(23, 3465, 2341));
            ktp.setBigInteger(new BigInteger(new byte[]{1, 2}));
            ktp.setBigDecimal(new BigDecimal(2323.12123));
            ktp.setaDouble(12341.12341d);
            Thread.sleep(5000);
            ProducerRecord pr = new ProducerRecord("test","thisisakey",ktp);
            System.out.println(producerUtil.produce(pr).get());
            Thread.sleep(5000);
        }
    }
}
