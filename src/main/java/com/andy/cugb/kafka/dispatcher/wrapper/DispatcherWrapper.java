package com.andy.cugb.kafka.dispatcher.wrapper;

/**
 * Created by jbcheng on 4/5/17.kafka分发数据包装类
 */
public class DispatcherWrapper<T> {
    private T sendValue;
    private String productKey;

    public DispatcherWrapper() {

    }

    public DispatcherWrapper(String productKey) {
        this.productKey = productKey;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public T getSendValue() {
        return sendValue;
    }

    public void setSendValue(T sendValue) {
        this.sendValue = sendValue;
    }
}
