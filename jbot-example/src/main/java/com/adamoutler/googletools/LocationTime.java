/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.googletools;

import com.adamoutler.slacktools.datatypes.ChannelResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import example.jbot.slack.SlackBot;
import java.util.HashMap;
import java.util.Map;
import me.ramswaroop.jbot.core.slack.models.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketSession;

/**
 * https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=Germany&inputtype=textquery&fields=geometry,id,name,place_id,plus_code&key=AIzaSyCB8Xw_NbB7xOh9TMSYv_OIV_lziSJf18g
 *
 * @author adamo
 */
public class LocationTime {


    public static DetailsResult getLocation(String location, String apiKey) {
        String locationRequest = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json"
                + "?key=" + apiKey
                + "&inputtype=textquery&input=" + location;
        JsonElement jes = new JsonParser().parse(new RestTemplate().getForEntity(locationRequest, String.class).getBody());
        String placeID = jes.getAsJsonObject().get("candidates").getAsJsonArray().get(0).getAsJsonObject().get("place_id").toString();
        String detailsRequest = "https://maps.googleapis.com/maps/api/place/details/json"
                + "?placeid=" + placeID.replace("\"", "").replace("\\\"", "")
                + "&fields=name,address_components,utc_offset"
                + "&inputtype=textquery"
                + "&key=" + apiKey;
        String dsr = new RestTemplate().getForEntity(detailsRequest, String.class).getBody();

        DetailsResult dr = new RestTemplate().getForEntity(detailsRequest, DetailsResult.class).getBody();

        return dr;

    }

    static String check = "time is it in ";

    public static boolean getDetails(String apiKey, Event event, WebSocketSession session, SlackBot slackBot) {
        if (!isTimeExtractionCommand(event.getText())) {
           return false;
        }
        int index = event.getText().indexOf(check);
        String area = event.getText().substring(index + check.length());

        try {
            DetailsResult dr = getLocation(area, apiKey);

            slackBot.reply(session, event, "It is " + dr.getCurrentTime() + " in " + dr.getLongName() + "(" + dr.getShortName() + ").");

        } catch (Exception ex) {
            slackBot.reply(session, event, "I don't know that place.");

        }

        return true;

    }

    public static boolean isTimeExtractionCommand(String value) {
        return value.toLowerCase().contains(check);
    }

   
}
