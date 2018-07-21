/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.slacktools.datatypes;

import me.ramswaroop.jbot.core.slack.models.User;

/**
 *
 * @author adamo
 */
public class UserExt extends User {

    private String tz;
    private String tz_label;
    private int tz_offset;

    /**
     * @return the tz
     */
    public String getTz() {
        return tz;
    }

    /**
     * @return the tz_label
     */
    public String getTz_label() {
        return tz_label;
    }

    /**
     * @return the tz_offset
     */
    public int getTz_offset() {
        return tz_offset;
    }

}
