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
public class UserPresence {
    private boolean ok;
    private String presence;
    private boolean online;
    private boolean auto_away;
    private boolean manual_away;
    private int connection_count;
    private int last_activity;

    /**
     * @return the ok
     */
    public boolean isOk() {
        return ok;
    }

    /**
     * @return the presence
     */
    public String getPresence() {
        return presence;
    }

    /**
     * @return the online
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * @return the auto_away
     */
    public boolean isAuto_away() {
        return auto_away;
    }

    /**
     * @return the manual_away
     */
    public boolean isManual_away() {
        return manual_away;
    }

    /**
     * @return the connection_count
     */
    public int getConnection_count() {
        return connection_count;
    }

    /**
     * @return the last_activity
     */
    public int getLast_activity() {
        return last_activity;
    }
    
}
