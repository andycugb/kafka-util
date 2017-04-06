package com.andy.cugb.kafka.config;

import java.util.Properties;

/**
 * Created by jbcheng on 4/1/17. kafka消费者相关配置
 */
public class KafkaConsumerConfig {
    private volatile Properties properties;

    private String bootstrapServers;
    private String groupId;
    private String enableAutoCommit = "true";
    private String sessionTimeoutms = "30000";
    private String keyDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";
    private String valueDeserializer = "com.andy.cugb.kafka.serializer.KryoKafkaDeserializer";
    private String keyType = String.class.getName();
    private String valueType;
    private String maxPartitionFetch = "1048576";// 1MB

    public Properties getKafkaConsumerConfig() {
        if (null == properties) {
            synchronized (this) {
                if (null == properties) {
                    properties = new Properties();
                    properties.put("bootstrap.servers", getBootstrapServers());
                    properties.put("group.id", getGroupId());
                    properties.put("enable.auto.commit", getEnableAutoCommit());
                    properties.put("session.timeout.ms", getSessionTimeoutms());
                    properties.put("key.deserializer", getKeyDeserializer());
                    properties.put("value.deserializer", getValueDeserializer());
                    properties.put("key.type", getKeyType());
                    properties.put("value.type", getValueType());
                    properties.put("max.partition.fetch.bytes", getMaxPartitionFetch());
                }
            }
        }
        return properties;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getEnableAutoCommit() {
        return enableAutoCommit;
    }

    public void setEnableAutoCommit(String enableAutoCommit) {
        this.enableAutoCommit = enableAutoCommit;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getKeyDeserializer() {
        return keyDeserializer;
    }

    public void setKeyDeserializer(String keyDeserializer) {
        this.keyDeserializer = keyDeserializer;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getMaxPartitionFetch() {
        return maxPartitionFetch;
    }

    public void setMaxPartitionFetch(String maxPartitionFetch) {
        this.maxPartitionFetch = maxPartitionFetch;
    }

    public String getSessionTimeoutms() {
        return sessionTimeoutms;
    }

    public void setSessionTimeoutms(String sessionTimeoutms) {
        this.sessionTimeoutms = sessionTimeoutms;
    }

    public String getValueDeserializer() {
        return valueDeserializer;
    }

    public void setValueDeserializer(String valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }
}
