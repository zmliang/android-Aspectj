package com.general.code.model;

import java.io.Serializable;

/**
 * Author: zml
 * Date  : 2019/1/2 - 14:18
 **/
public class GeneralModel implements Serializable {
    String name;
    String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
