/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.slacktools;

import com.adamoutler.slacktools.datatypes.ChannelExt;
import com.adamoutler.slacktools.datatypes.ChannelResponse;
import com.adamoutler.slacktools.datatypes.UserExt;
import com.adamoutler.slacktools.datatypes.UserResponse;
import com.adamoutler.time.CityKingsTime;
import example.jbot.slack.SlackBot;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.ramswaroop.jbot.core.slack.Bot;
import me.ramswaroop.jbot.core.slack.SlackApiEndpoints;
import me.ramswaroop.jbot.core.slack.models.Event;
import me.ramswaroop.jbot.core.slack.models.User;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 *
 * @author adamo
 */
public class Commands {

    @Autowired
    SlackApiEndpoints slackApiEndpoints;
    /**
     * Slack token from application.properties file. You can get your slack
     * token next <a href="https://my.slack.com/services/new/bot">creating a new
     * bot</a>.
     */
    @Value("${slackBotToken}")

    SlackBot slackBot;
    private String slackToken;

    public Commands(SlackBot bot, SlackApiEndpoints slackApiEndpoints, String slackToken) {
        this.slackBot = bot;
        this.slackToken = slackToken;
        this.slackApiEndpoints = slackApiEndpoints;

    }



    class KeyValuePair {

        String key;
        float value;

        KeyValuePair(String k, float v) {
            this.key = k;
            this.value = v;
        }

        public String getKey() {
            return key;
        }

        public float getValue() {
            return value;
        }
    }

    public void commandDistributor(WebSocketSession session, Event event) {

        if (isMyOwnMessage(event)) {
            return;
        }

        if (!event.getText().isEmpty()) {
            if (maybeDoTimeCommand(event, session)
                    || maybeDoFlowchart(event, session)
                    || maybeDoSource(event, session)
                    || maybeGetQuote(event, session)
                    || maybeDoAlwaysWatching(event, session)
                    || maybeDoNoobCommand(event, session)
                    || maybeGetUserTimezoneAverage(event, session)) {

            } else {
                slackBot.reply(session, event, "I don't understand.");
            }
        }

    }

    private boolean maybeGetUserTimezoneAverage(Event event, WebSocketSession session) {
        if(!event.getText().toLowerCase().contains("calibrate")){
            return false;
        }
        
        
        ArrayList<ArrayList<KeyValuePair>> timezones = new ArrayList<>();

        slackBot.reply(session, event, "Give me a minute, communicating with Slack's Channel List, Channel Unfo, User List, and User Info APIs and calculating for #General");

        for (int i = 0; i < 25; i++) {
            timezones.add(new ArrayList<>());
        }

        this.getChannels(event).stream().filter((channel) -> (channel.getName().toLowerCase().equals("general"))).forEachOrdered((channel) -> {
            channel.getUsers(slackApiEndpoints, slackToken).forEach((user) -> {
                if (!user.isDeleted()) {
                    timezones.get(12 + (int) Math.floor(user.getTz_offset() / 60 / 60))
                            .add(new KeyValuePair(user.getTz_label(), user.getTz_offset()));
                }
            });
        });

        StringBuilder sb=new StringBuilder();
        Averaging averaging = new Averaging();
        for (ArrayList<KeyValuePair> memberTimes : timezones) {
            if (memberTimes.size() > 2) {
                slackBot.reply(session, event, "Counting mean " + memberTimes.size() + " members in " + memberTimes.get(0).getKey() + " GMT" + (int) memberTimes.get(0).value / 60 / 60);
                memberTimes.forEach((kvp) -> {
                    averaging.add(kvp.value);
                });
            } else if (memberTimes.size() > 0) {
                sb.append("Ignored ").append(memberTimes.size()).append(" member(s( in ").append( memberTimes.get(0).getKey()).append(" GMT").append( (int)memberTimes.get(0).value / 60 / 60).append(" while removing extremes. ");
            }
        }

        slackBot.reply(session, event, sb.toString()+"\nThe mean Badazzes member is located in GMT " + averaging.getAverage() / 60 / 60);
        return true;
    }

    public static String getQuote() {
        String[] array = new String[]{"All warfare is based on deception.--Sun Tsu", "The supreme art of war is to subdue the enemy without fighting.--Sun Tsu", "Know thy self, know thy enemy. A thousand battles, a thousand victories.--Sun Tsu", "If you know the enemy and know yourself you need not fear the results of a hundred battles.--Sun Tsu", "Know your enemy and know yourself and you can fight a hundred battles without disaster.--Sun Tsu", "Strategy without tactics is the slowest route to victory. Tactics without strategy is the noise before defeat.--Sun Tsu", "Opportunities multiply as they are seized.--Sun Tsu", "Victorious warriors win first and then go to war, while defeated warriors go to war first and then seek to win.--Sun Tsu", "Hence to fight and conquer in all your battles is not supreme excellence; supreme excellence consists in breaking the enemy's resistance without fighting.--Sun Tsu", "Pretend inferiority and encourage his arrogance.--Sun Tsu"};
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }
    private final String NOOBSCRIPT = "https://pastebin.adamoutler.com/HxXW";
    private final String QUICKFIGHTSCRIPT = "https://pastebin.adamoutler.com/C0B7";
    String[] timeCommands = new String[]{"we join", "city kings time", "i join"};
    final String FLOWCHARTURL = "https://badazzes.slack.com/files/U9S44RGNP/FBN4T7TAA/image.png";
    private final String MYUSERID = "UBS24JP1U";

    public ArrayList<ChannelExt> getChannels(Event event) {
        ChannelResponse userResponse = new RestTemplate().getForEntity(slackApiEndpoints.getChannelListApi(), ChannelResponse.class, slackToken).getBody();
        return userResponse.getChannels();
    }

    public boolean isMyOwnMessage(Event event) {
        String user = SlackTools.getSlackUsername(event);
        return user.equals(MYUSERID);
    }

    public boolean maybeDoFlowchart(Event event, WebSocketSession session) {
        if (event.getText().toLowerCase().contains("flowchart")) {
            slackBot.reply(session, event, "Here's the current flowchart " + this.getUser(event).getProfile().getRealName() + "!" + FLOWCHARTURL);
            return true;
        }
        return false;
    }

    public boolean maybeDoTimeCommand(Event event, WebSocketSession session) {
        for (String t : timeCommands) {
            if (event.getText().toLowerCase().contains(t)) {
                slackBot.reply(session, event, CityKingsTime.getCityKingsTime());
                return true;
            }
        }
        return false;
    }

    public boolean maybeDoNoobCommand(Event event, WebSocketSession session) {
        for (String t : timeCommands) {
            if (event.getText().toLowerCase().contains("n00b")) {
                slackBot.reply(session, event, "Welcome to the gang.  I'm badazzes.org bot.  I have some helpful tips for you to pick up your game.");
                slackBot.reply(session, event, "This script will help you with automatic Quick Fights, and therefore Championship and Gang Fights on Android: " + QUICKFIGHTSCRIPT);
                slackBot.reply(session, event, "This script is used to display the message you saw when you came in this gang " + NOOBSCRIPT);
                slackBot.reply(session, event, "This is the City Kings flowchart " + FLOWCHARTURL);
                slackBot.reply(session, event, "Make sure to ask me directly before you click that JOIN button for a new round of City Kings.  You can say, '@badazzes.org should I join now?'.");
                return true;
            }
        }
        return false;
    }

    public boolean maybeDoAlwaysWatching(Event event, WebSocketSession session) {
        if (event.getText().toLowerCase().contains("always watching")) {
            slackBot.reply(session, event, "I may be pretty quiet, but I'm always here and see everything going on at all times.  I could react to any message, but I choose not to. I'm always watching everything.  always...");
            try {
                Thread.sleep(30000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SlackBot.class.getName()).log(Level.SEVERE, null, ex);
            }
            slackBot.reply(session, event, "...literally always " + this.getUser(event).getProfile().getRealName() + ".");
            return true;
        }
        return false;
    }

    public boolean maybeGetQuote(Event event, WebSocketSession session) {
        if (event.getText().toLowerCase().contains("inspir")) {
            slackBot.reply(session, event, getQuote());
            return true;
        }
        return false;
    }

    public User getUser(Event event) {
        UserResponse userResponse = new RestTemplate().getForEntity(slackApiEndpoints.getUserInfoAPI() + "&user=" + event.getUserId(), UserResponse.class, slackToken).getBody();
        return userResponse.getUser();
    }

    public User getUser(String userid) {
        UserResponse userResponse = new RestTemplate().getForEntity(slackApiEndpoints.getUserInfoAPI() + "&user=" + userid, UserResponse.class, slackToken).getBody();
        return userResponse.getUser();
    }

    public boolean maybeDoSource(Event event, WebSocketSession session) {
        if (event.getText().toLowerCase().contains("source")) {
            slackBot.reply(session, event, this.getUser(event).getProfile().getRealName() + ", my source code is here https://github.com/adamoutler/jbot.");
            return true;
        }
        return false;
    }

}
