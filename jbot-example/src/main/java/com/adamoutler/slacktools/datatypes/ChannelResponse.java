/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.slacktools.datatypes;

import java.util.ArrayList;

/**
 *
 * @author adamo
 */
public class ChannelResponse {
    private boolean ok;
    private ArrayList<ChannelExt> channels;

    /**
     * @return the ok
     */
    public boolean isOk() {
        return ok;
    }

    /**
     * @return the channels
     */
    public ArrayList<ChannelExt> getChannels() {
        return channels;
    }

    /**
     * @param channels the channels to set
     */
    public void setChannels(ArrayList<ChannelExt> channels) {
        this.channels = channels;
    }
    
}
