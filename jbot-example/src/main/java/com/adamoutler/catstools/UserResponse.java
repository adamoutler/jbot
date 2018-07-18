/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.catstools;

import me.ramswaroop.jbot.core.slack.models.User;

/**
 *
 * @author adamo
 */
public class UserResponse {

    private boolean ok;
    private User user;

    /**
     * @return the ok
     */
    public boolean isOk() {
        return ok;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }
}
