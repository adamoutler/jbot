/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.catstools;

/**
 *
 * @author adamo
 */
public class Building {

    String name;
    boolean isNumbered;
    int size;

    Building(String name, int size, boolean isNumbered) {
        this.name = name;
        this.isNumbered = isNumbered;
        this.size = size;
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        String n="\n";
        sb.append("name: ").append(name).append(n);
        sb.append("size: ").append(Integer.toString(size)).append(n);
        sb.append("isNumbered: ").append(isNumbered).append(n);
        return sb.toString();
        
    }

}
