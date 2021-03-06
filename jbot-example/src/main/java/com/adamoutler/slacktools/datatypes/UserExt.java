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
    private UserPresence presence;
    private boolean is_bot;

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

    /**
     * @return the presence
     */
    public UserPresence getPresence() {
        return presence;
    }

    /**
     * @param presence the presence to set
     */
    public void setPresence(UserPresence presence) {
        this.presence = presence;
    }

    /**
     * @return the is_bot
     */
    public boolean isIs_bot() {
        return is_bot;
    }

}
