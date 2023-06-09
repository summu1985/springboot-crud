package com.redhat.demo.springbootcrud.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Users {
    ArrayList < User > records = null;

    public Users() {
    }

    public ArrayList<User> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<User> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return "Users [records=" + records + "]";
    }    
}
