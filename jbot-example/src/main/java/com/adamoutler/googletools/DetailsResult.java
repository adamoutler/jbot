/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.googletools;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author adamo
 */
public class DetailsResult {

    private String status;
    private String[] html_attributions;
    private PlaceDetailResult result;

    public String getLongName() {
        return getResult().getAddressComponents()[0].getLong_name();
    }

    public int getUtcOffset() {
        
       return getResult().getUtc_offset();
    }
    
    public String getCurrentTime(){
        
        long utc= Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
        long offset=getUtcOffset()*60*1000;
        long time= utc+ offset;
        
        SimpleDateFormat sdf=new SimpleDateFormat("ddMMMYY HH:mm.ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date(time));
        
    }
    

    public String getShortName() {
        return getResult().getAddressComponents()[0].getShort_name();
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the html_attributions
     */
    public String[] getHtml_attributions() {
        return html_attributions;
    }

    /**
     * @param html_attributions the html_attributions to set
     */
    public void setHtml_attributions(String[] html_attributions) {
        this.html_attributions = html_attributions;
    }

    /**
     * @return the result
     */
    public PlaceDetailResult getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(PlaceDetailResult result) {
        this.result = result;
    }

}



