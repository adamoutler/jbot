/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.catstools;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adamo
 */
public class BattleCalculator {
    static String calc2="Stadium#121,Stadium#211,Metro21,Bridge21,Factory8,Substation19";
    Building[] buildings= new Building[6];
    int numberOfPlayers=0;
    BattleCalculator(String s) throws Exception{
        String[] bString=s.split(",");
        if (bString.length<6){
            throw new Exception("Improperly formatted building string. eg: "+calc2);
        }
        for (int i=0;i<6;i++){
            boolean isNumbered=false;
            String name="";
            int size=0;
            if (bString[i].contains("#")){
                isNumbered=true;
                String[] nString=bString[i].split("#");
                name=nString[0]+" "+nString[1].toCharArray()[0];
                size=Integer.parseInt(nString[1].substring(1,nString[1].length()));
             
                
            } else {
                name=bString[i].split("[0-9]")[0];
                String numbers=bString[i].replaceAll("[a-zA-Z]","");
                size=Integer.parseInt(numbers);
            }
            buildings[i]=new Building(name,size,isNumbered);
        }
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (Building building:buildings){
            sb.append(building.toString());
        }
        return sb.toString();
    }
 
//    public static void main(String[] args){
//        try {
//            BattleCalculator bc=new BattleCalculator(calc2);
//            System.out.println(bc.toString());
//        } catch (Exception ex) {
//            Logger.getLogger(BattleCalculator.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
    
}
