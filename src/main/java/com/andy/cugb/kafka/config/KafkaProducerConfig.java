package com.andy.cugb.kafka.config;

import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

/**
 * Created by jbcheng on 4/1/17. kafka生产者相关配置
 */
public class KafkaProducerConfig {
    private volatile Properties properties;

    private String bootstrapServers;
    private String acks = "-1";// -1,0,1
    private int retries = 3;
    private int batchSize = 16384;
    private int lingerms = 1;
    private String compressionType = "lz4";// gzip,snappy
    private int bufferMemory = 33554432;
    private String keySerilizer = "org.apache.kafka.common.serialization.StringSerializer";
    private String valueSerializer = "com.andy.cugb.kafka.serializer.KryoKafkaSerializer";
    private String partitionerClass;
    private int kryoBufferSize = 4096;

    public Properties getProducerConfig() {
        if (null == properties) {
            synchronized (this) {
                if (null == properties) {
                    properties = new Properties();
                    properties.put("bootstrap.servers", getBootstrapServers());
                    properties.put("acks", getAcks());
                    properties.put("retries", getRetries());
                    properties.put("batch.size", getBatchSize());
                    properties.put("linger.ms", getLingerms());
                    properties.put("compression.type", getCompressionType());
                    properties.put("buffer.memory", getBufferMemory());
                    properties.put("key.serializer", getKeySerilizer());
                    properties.put("value.serializer", getValueSerializer());
                    if (StringUtils.isNotBlank(partitionerClass)) {
                        properties.put("partitioner.class", getPartitionerClass());
                    }
                    properties.put("kryo.buffer.size", getKryoBufferSize());
                }
            }
        }
        return properties;
    }

    public String getAcks() {
        return acks;
    }

    public void setAcks(String acks) {
        this.acks = acks;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public int getBufferMemory() {
        return bufferMemory;
    }

    public void setBufferMemory(int bufferMemory) {
        this.bufferMemory = bufferMemory;
    }

    public String getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(String compressionType) {
        this.compressionType = compressionType;
    }

    public String getKeySerilizer() {
        return keySerilizer;
    }

    public void setKeySerilizer(String keySerilizer) {
        this.keySerilizer = keySerilizer;
    }

    public int getKryoBufferSize() {
        return kryoBufferSize;
    }

    public void setKryoBufferSize(int kryoBufferSize) {
        this.kryoBufferSize = kryoBufferSize;
    }

    public int getLingerms() {
        return lingerms;
    }

    public void setLingerms(int lingerms) {
        this.lingerms = lingerms;
    }

    public String getPartitionerClass() {
        return partitionerClass;
    }

    public void setPartitionerClass(String partitionerClass) {
        this.partitionerClass = partitionerClass;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public String getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }
}
