package com.andy.cugb.kafka.dispatcher;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Enchanter on 16/8/31.
 */
public class KafkaDispPojo {
    private String user;
    private int price;
    private Date date;
    private UUID uuidddd;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public UUID getUuidddd() {
        return uuidddd;
    }

    public void setUuidddd(UUID uuidddd) {
        this.uuidddd = uuidddd;
    }

    @Override
    public String toString() {
        return "KafkaDispPojo{" + "user='" + user + '\'' + ", price=" + price + ", date=" + date
                + ", uuidddd=" + uuidddd + '}';
    }
}
