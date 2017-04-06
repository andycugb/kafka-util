package com.andy.cugb.kafka.serializer;

import com.esotericsoftware.kryo.io.Output;

import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * Created by jbcheng on 4/1/17. kryo序列化
 */
public class KryoKafkaSerializer implements Serializer {
    private int kryoBufferSize;

    public static void main(String[] args) {
        System.out.println(KryoHelper.getKryo());
    }

    public byte[] serialize(String topic, Object data) {
        Output output = null;
        try {
            output = new Output(new ByteArrayOutputStream(), kryoBufferSize);
            KryoHelper.getKryo().writeObject(output, data);
            return output.toBytes();
        } finally {
            if (null != output) {
                output.close();
            }
        }
    }

    public void close() {

    }

    public void configure(Map map, boolean b) {
        kryoBufferSize = (Integer) map.get("kryo.buffer.size");
    }
}
