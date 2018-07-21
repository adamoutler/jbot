/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.time;

import com.adamoutler.slacktools.datatypes.UserExt;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adamo
 */
public class UserTimeCalculator {

    public double calculateUserTime(List<UserExt> users) {
        ArrayList<Integer> times = new ArrayList<>();
        for (UserExt user : users) {
            times.add(user.getTz_offset());
        }
        return calculateAverage(times);
    }
    
private double calculateAverage(List <Integer> marks) {
  Integer sum = 0;
  if(!marks.isEmpty()) {
    for (Integer mark : marks) {
        sum += mark;
    }
    return sum.doubleValue() / marks.size();
  }
  return sum;
}
}
