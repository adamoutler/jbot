/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.time;


import me.ramswaroop.jbot.core.slack.models.Event;

/**
 *
 * @author adamo
 */
public class SlackTools {
    public static String getSlackUsername(Event event) {
        String s=event.getUserId();
        return s;

    }
}
