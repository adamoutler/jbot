/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.slacktools;

/**
 * Provides a math object that can be made final.
 * @author adamo
 */
public class CounterObject {
    int counter=0;
    /**
     * gets the current count
     * @return counter value
     */
    public int get(){
        return counter;
    }
    /**
     * adds a count to the value
     * @param toAdd value to add
     * @return reference to this object.
     */
    public CounterObject add(int toAdd){
        counter=counter+toAdd;
        return this;
    }
}
