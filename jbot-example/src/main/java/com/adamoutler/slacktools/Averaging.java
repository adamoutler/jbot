/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.slacktools;

import java.util.ArrayList;

/**
 *
 * @author adamo
 */
public class Averaging extends ArrayList<Float>{
    

    
    public float getAverage(){
        float cumulative=0;
        for (Float f:this){
            cumulative=cumulative+f;
        }
        return cumulative/this.size();
    }
    
}
