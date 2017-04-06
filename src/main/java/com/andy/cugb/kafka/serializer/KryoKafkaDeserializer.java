package com.andy.cugb.kafka.serializer;

import com.esotericsoftware.kryo.io.Input;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by jbcheng on 4/1/17. kryo反序列化
 */
public class KryoKafkaDeserializer<T> implements Deserializer {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private Class clazz;

    public void close() {

    }

    public void configure(Map map, boolean isKey) {
        Object type = map.get("value.type");
        if (isKey) {
            type = map.get("key.type");
        }
        if (null != type && type instanceof String) {
            try {
                clazz = Class.forName((String) type);
            } catch (ClassNotFoundException e) {
                logger.error("data type config failed caused by:", e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public T deserialize(String s, byte[] bytes) {
        Input input = null;
        try {
            input = new Input(bytes);
            return (T) KryoHelper.getKryo().readObject(input, clazz);
        } catch (Exception e) {
            logger.error("kryo desrialize failed....for topic:{}", s, e);
        } finally {
            if (null != input) {
                input.close();
            }
        }
        return null;
    }
}
