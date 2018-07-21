/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.slacktools.datatypes;

/**
 *
 * @author adamo
 */
public class Purpose {
    private String value;
    private String creator;
    private long last_set;
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String n = "\n";
        sb.append("purpose:\nTopic:").append(value).append(n);
        sb.append("creator:").append(creator).append(n);
        sb.append("last set:").append(last_set).append(n);
        return sb.toString();
    }
    /**
     * @return the creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @param creator the creator to set
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * @return the last_set
     */
    public long getLast_set() {
        return last_set;
    }

    /**
     * @param last_set the last_set to set
     */
    public void setLast_set(long last_set) {
        this.last_set = last_set;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}
