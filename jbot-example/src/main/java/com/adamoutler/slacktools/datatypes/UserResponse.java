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
public class UserResponse {

    private boolean ok;
    private UserExt user;

    /**
     * @return the ok
     */
    public boolean isOk() {
        return ok;
    }

    /**
     * @return the user
     */
    public UserExt getUser() {
        return user;
    }
}
