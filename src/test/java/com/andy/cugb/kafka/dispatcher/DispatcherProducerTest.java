package com.andy.cugb.kafka.dispatcher;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.andy.cugb.kafka.KafkaTestPojo;
import com.andy.cugb.kafka.dispatcher.wrapper.DispatcherWrapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

/**
 * Created by jbcheng on 4/6/17.
 */
public class DispatcherProducerTest {
    private static DispatcherProducerClient dpu;
    @BeforeClass
    public static void setUp() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-dispatch-producer.xml");
        dpu = (DispatcherProducerClient) context.getBean("kafkaDispatcherProducer");
    }

    @Test
    public void testKafkaProducer() throws InterruptedException{
        for (int i = 0; i < 2; i++) {
            KafkaTestPojo ktp = new KafkaTestPojo();
            ktp.setName("this is name");
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
            ktp.setBigInteger(new BigInteger(new byte[] {1, 2}));
            ktp.setBigDecimal(new BigDecimal(2323.12123));
            ktp.setaDouble(12341.12341d);

            DispatcherWrapper<KafkaTestPojo> dw = new DispatcherWrapper<KafkaTestPojo>("productKey1");
            dw.setSendValue(ktp);
            Thread.sleep(5000);
            System.out.println(dpu.produce("dispatch-test", dw));


            KafkaDispPojo kdp = new KafkaDispPojo();
            kdp.setDate(new Date());
            kdp.setPrice(12);
            kdp.setUser("asdf");
            kdp.setUuidddd(UUID.randomUUID());
            DispatcherWrapper<KafkaDispPojo> dw1 = new DispatcherWrapper<KafkaDispPojo>("productKey2");
            dw1.setSendValue(kdp);
            System.out.println(dpu.produce("dispatch-test", dw1));
            Thread.sleep(5000);
        }
    }
}
