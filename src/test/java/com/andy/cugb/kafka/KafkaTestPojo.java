package com.andy.cugb.kafka;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Enchanter on 16/8/31.
 */
public class KafkaTestPojo {
    private String name;
    private int age;
    private Date date;
    private UUID uuid;
    private List<String> list;
    private Set<String> set;
    private List<String> ilist;
    private Set<Integer> iset;
    private BigInteger bigInteger;
    private BigDecimal bigDecimal;
    private double aDouble;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Set<String> getSet() {
        return set;
    }

    public void setSet(Set<String> set) {
        this.set = set;
    }

    public List<String> getIlist() {
        return ilist;
    }

    public void setIlist(List<String> ilist) {
        this.ilist = ilist;
    }

    public Set<Integer> getIset() {
        return iset;
    }

    public void setIset(Set<Integer> iset) {
        this.iset = iset;
    }

    public BigInteger getBigInteger() {
        return bigInteger;
    }

    public void setBigInteger(BigInteger bigInteger) {
        this.bigInteger = bigInteger;
    }

    public double getaDouble() {
        return aDouble;
    }

    public void setaDouble(double aDouble) {
        this.aDouble = aDouble;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    @Override
    public String toString() {
        return "KafkaTestPojo{" + "name='" + name + '\'' + ", age=" + age + ", date=" + date
                + ", uuid=" + uuid + ", list=" + list + ", set=" + set + ", ilist=" + ilist
                + ", iset=" + iset + ", bigInteger=" + bigInteger + ", bigDecimal=" + bigDecimal
                + ", aDouble=" + aDouble + '}';
    }
}
