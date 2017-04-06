通用kafka队列，可通过队列实现异步操作

注意:  1. 使用kryo序列化时,需要在producer中配置io缓存大小:kryobuffersize,默认4096,请根据业务数据大小配置,如果业务数据超出缓存大小,序列化不会报错,但是反序列化会失败,导致丢数据.捕获了反序列化异常打error log,返回值为null,否则队列不能继续消费。
      2. 使用kryo序列化时，不能支持bean的扩展，推荐将bean转为Map传递给kafka。
      3.kafka的consumer为线程不安全的对象,需要使用者注意线程安全问题


1. 使用方法：
结合spring：
producer：

  (1)producer的参数配置通过com.andy.cugb.kafka.config.KafkaProducerConfig类来设置，最少只需配置bootservers即可

    <bean id="kafkaProducerConfig" class="com.andy.cugb.kafka.config.KafkaProducerConfig">
        <property name="bootservers" value="127.0.0.1:9092"/>
    </bean>

  (2)com.andy.cugb.kafka.KafkaProducerClient实现发送逻辑，具体可用的方法可以参考其中的多个produce重载方法。
    <bean id="kafkaProducerUsercenter" class="com.andy.cugb.kafka.KafkaProducerClient">
        <property name="kafkaProducerConfig" ref="kafkaProducerConfig"/>
    </bean>


consumer：

  （1）consumer的参数配置通过com.andy.cugb.kafka.config.KafkaConsumerConfig类来设置，最少需要配置bootservers和groupid。
    <bean id="kafkaConsumerConfig" class="com.andy.cugb.kafka.config.KafkaConsumerConfig">
        <property name="bootservers" value="127.0.0.1:9092"/>
        <property name="groupid" value="test"/>
    </bean>

  （2）需要自己实现com.andy.cugb.kafka.KafkaConsumerClient这个抽象类，通过processRecord方法实现业务逻辑，alarm
  方法实现报警逻辑。

  （3）子类需要传递两个构造参数，一个是KafkaConsumerConfig，另一个是一个监听topic的List。
    <bean id="kafkaConsumer" class="com.andy.cugb.kafka.KafkaConsumerTestSpring"
          destroy-method="closeConsumer">
        <constructor-arg index="0" ref="kafkaConsumerConfig"/>
        <constructor-arg index="1" value="#{T(java.util.Arrays).asList('test')}"/>
    </bean>

  （4）子类推荐使用spring的destroy-method的方式调用closeConsumer方法，可以在tomcat重启及时收回连接,否则server端需要等到超时才能发现连接失效。

  （5）KafkaConsumerClient类中有两种消费方式，consumeAuto方法可以自动提交offset，但是可能自动提交offset
  时业务尚未处理完。consumeManual方法是
      处理完业务逻辑手动提交offset，如果需要实现最终一致性，推荐使用consumeManual。

  （6）通过kafka发送的对象，推荐实现toString方法，以便业务处理出错可以打出详细业务内容, 方便处理。

  （7）推荐在子类构造函数中调用super的consumeAuto或consumeManual，如此实例化时便可启动consumer。

KafkaConsumerClient子类举例：
public class KafkaConsumerSpringTest extends KafkaConsumerClient<KafkaTestPojo> {

    public KafkaConsumerSpringTest(KafkaConsumerConfig kafkaConsumerConfig,
            List<String> subscribeTopics) {
        super(kafkaConsumerConfig, subscribeTopics);
        super.consumeManual();
    }

    @Override
    protected void processRecord(ConsumerRecord<String, KafkaTestPojo> consumerRecords) {
        System.out.printf("offset = %d, value = %s\n", consumerRecords.offset(),
                consumerRecords.value());
    }

    @Override
    protected void alarm() {

        System.out.println("this is alarm");

    }

}

参数说明：
producer：参考http://kafka.apache.org/documentation.html#producerconfigs
    private String bootservers; （连接kafka地址，只用来发现服务，发现后会自动拉取全部配置，此处也可以配置多个防止单台机器无法访问发现服务失败。）
    private String acks = "all";（发送消息验证方式，all为保证集群所有机器收到确认消息，1为只等一台机器确认，0为不等任何机器确认，最终一致性推荐用all）
    private Integer retries = 3;（重试次数，发送失败后的重试次数）
    private Integer batchsize = 16384;
    private Integer lingerms = 1;
    private String compressiontype = "lz4";(压缩方式,默认开启lz4)
    private Integer buffermemory = 33554432;
    private String keyserializer = "org.apache.kafka.common.serialization.StringSerializer";(key的序列化方式，可以在配置文件中设置，默认使用string)
    private String valueserializer = "com.netease.ncuc.kafka.serializer.KryoKafkaSerialize";（value的序列化方式，可以在配置文件中设置，默认使用kryo）
    private String partitionerclass;
    private Integer kryobuffersize = 4096;(非kafka配置,在使用kryo时,kryo序列化io有缓存,默认4096,业务需要根据数据大小设置缓存大小!!)


consumer：参考http://kafka.apache.org/documentation.html#consumerconfigs
    private String bootservers;
    private String groupid;
    private String enableautocommit = "true";(是否自动提交,使用手动提交时此项需要配置为false)
    private String autocommitintervalms = "1000";(是否自动提交时间间隔)
    private String sessiontimeoutms = "30000";(链接超时时间,心跳超时后,会认为consumer down了,触发rebalance)
    private String keydeserializer = "org.apache.kafka.common.serialization.StringDeserializer";（key的反序列化方式，可以在配置文件中配置，默认使用string）
    private String valuedeserializer = "com.netease.ncuc.kafka.serializer.KryoKafkaDeserializer";（value的反序列化方式，可以再配置文件中配置，默认使用kryo）
    private String keytype;（使用kryo序列化key时，需要反序列化时指明key的class type，不用kryo时可以忽略）
    private String valuetype;（使用kryo序列化value是，需要反序列化时指明value的class type，不用kryo时可以忽略）
    private String maxpartitionfetch = "1048576";(consumer一次取得数据的大小,默认1m)


junit测试体验方式：
1. 运行com.andy.cugb.kafka.KafkaConsumerTestSpring.test()方法, consumer启动。
2. 运行com.andy.cugb.kafka.KafkaProducerTestSpring.testProducer()或testProducerWithKey()方法,同时观察消费者可以看到消费信息。


DispatcherConsumerClient与DispatcherProducerClient用法:
1. 对于一些阶段性上下线的活动,如果创建专有队列下线后队列不会自动删除,会导致无用队列数量增多.可以使用公共队列.
2. 这两个类提供了公共队列的分发功能.

用法:
(1)DispatcherWrapper
可以看做使用分发器的DTO,有两个属性:
    private String productKey;
    private T sendValue;
productKey是唯一业务id,sendValue是要发送的值,推荐使用Map或者Json

(2)BusinessWrapper
无需再继承原有的com.andy.cugb.kafka.KafkaConsumerClient这个抽象类,只需要实现BusinessWrapper接口即可,
接口需要实现的方法与原来com.andy.cugb.kafka.KafkaConsumerClient中的抽象方法一致.

(3)producer:
基本与之前相同,只需要替换成com.andy.cugb.kafka.dispatcher.DispatcherProducerClient即可,一个工程
可以只有一个com.andy.cugb.kafka.dispatcher.DispatcherProducerClient.
    <bean id="kafkaProducerClient"
          class="com.andy.cugb.kafka.dispatcher.DispatcherProducerClient">
        <property name="kafkaProducerConfig" ref="kafkaProducerConfig"/>
    </bean>
在调用produce时传递的值需要包装成DispatcherWrapper.
    public Future<RecordMetadata> produce(String topic, DispatcherWrapper<T> obj)

(4)consumer:
consumer统一使用com.andy.cugb.kafka.dispatcher.DispatcherConsumerClient,与原来的com.andy.cugb.kafka.KafkaConsumerClient
相比,多了第三个构造参数,需要传递一个Map,来对应DispatcherWrapper中的productKey与处理这个product的BusinessWrapper
的对应关系.

    <bean id="businessWrapperTest"
          class="com.andy.cugb.kafka.dispatcher.BusinessWrapperTest"/>
    <bean id="businessWrapperDispatchTest"
          class="com.andy.cugb.kafka.dispatcher.DispatcherWrapperTest"/>

    <bean id="kafkaConsumerClient"
          class="com.andy.cugb.kafka.dispatcher.DispatcherConsumerClient"
          destroy-method="closeConsumer">
        <constructor-arg index="0" ref="kafkaConsumerConfig"/>
        <!--<constructor-arg index="1" value="#{T(java.util.Arrays).asList('test')}"/>-->
        <constructor-arg index="1">
            <list>
                <value>dispatch-test</value>
            </list>
        </constructor-arg>
        <constructor-arg index="2">
            <map>
                <entry key="productKey1" value-ref="businessWrapperTest"/>
                <entry key="productKey2" value-ref="businessWrapperDispatchTest"/>
            </map>
        </constructor-arg>
    </bean>