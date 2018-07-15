/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.time;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 *
 * @author adamo
 */
public class CityKingsTime {

    final static int CKSTOPUTC = 3;   //UTC hour 3 = 0300 UK, 2400 EST, 2100 PAC, 1200 BEJ
    final static int CKSTARTUTC = 7;  //UTC hour 7 = 0800 UK, 0400 EST, 2300 PAC, 1800 BEJ

//    public static void main(String[] args) {
//        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
//        //int uHour = now.getHour();
//        for (int uHour = 0; uHour < 24; uHour++) {
//            int minutesLeft = 59 - getMinutes(now);
//            int secondsLeft = 59 - getSeconds(now);
//            System.out.println(calculateCKTimes(uHour, minutesLeft, secondsLeft));
//        }
//        System.out.println(getCityKingsTimeMessage());
//    }

    public static String getCityKingsTimeMessage() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        int uHour = getHour(now);
        int minutesLeft = 59 - getMinutes(now);
        int secondsLeft = 59 - getSeconds(now);
        return calculateCKTimes(uHour, minutesLeft, secondsLeft);
    }

    static int getHour(ZonedDateTime now) {
        return now.getHour();
    }

    static int getSeconds(ZonedDateTime now) {
        return now.getSecond();
    }

    static int getMinutes(ZonedDateTime now) {
        return now.getMinute();
    }

    static String calculateCKTimes(int uHour, int minutesLeft, int secondsLeft) {

        //calculate
        int stopHours = (CKSTOPUTC) - uHour;
        int startHours = (CKSTARTUTC) - uHour;
        if (stopHours < 0) {
            stopHours = 24 + stopHours;
        }
        String minutes;
        String seconds;

        //format 0 prefixes
        if (minutesLeft < 10) {
            minutes = "0" + Integer.toString(minutesLeft);
        } else {
            minutes = Integer.toString(minutesLeft);
        }
        if (secondsLeft < 10) {
            seconds = Integer.toString(secondsLeft);
        } else {
            seconds = Integer.toString(secondsLeft);
        }

        //display
        if (uHour >= CKSTOPUTC && uHour <= CKSTARTUTC) {
            return "DO NOT START CK! Wait for " + startHours + " hours, " + minutes + " minutes, " + seconds + " seconds.";
        } else {
            return "You're good! Join City Kings within " + stopHours + " hours, " + minutes + " minutes, " + seconds + " seconds.";
        }
    }
}
