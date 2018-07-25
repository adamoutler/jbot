/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.slacktools;

import com.adamoutler.googletools.LocationTime;
import com.adamoutler.slacktools.datatypes.ChannelExt;
import com.adamoutler.slacktools.datatypes.ChannelResponse;
import com.adamoutler.slacktools.datatypes.UserExt;
import com.adamoutler.slacktools.datatypes.UserPresence;
import com.adamoutler.slacktools.datatypes.UserResponse;
import com.adamoutler.time.CityKingsTime;
import example.jbot.slack.SlackBot;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.TimeZone;
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

    class MemberTimeIndicators {

        String timezoneName;
        float timezoneOffsetInSeconds;
        boolean online;
        String presence;
        String realName;

        MemberTimeIndicators(String timezoneName, float timezoneOffsetInSeconds, String presence, String realName) {
            this.timezoneName = timezoneName;
            this.timezoneOffsetInSeconds = timezoneOffsetInSeconds;
            this.online = online;
            this.presence = presence;
            this.realName = realName;
        }

        public String getName() {
            return realName;
        }

        public boolean getOnline() {
            return online;
        }

        public String getPresence() {
            return presence;
        }

        public String getKey() {
            return timezoneName;
        }

        public float getValue() {
            return timezoneOffsetInSeconds;
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
                    || maybeGetUserTimezoneAverage(event, session)
                    || LocationTime.getDetails(slackApiEndpoints.getGoogleToken(), event, session, slackBot)
                    ||maybeDoAdam(event,session))
            {

            } else {
                slackBot.reply(session, event, "I don't understand.");
            }
        }

    }
    

    private boolean maybeGetUserTimezoneAverage(Event event, WebSocketSession session) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date now = new Date();
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

        long userTime = now.getTime();

        if (!event.getText().toLowerCase().contains("user status")&& !event.getText().toLowerCase().contains("times")) {
            return false;
        }

        ArrayList<ArrayList<MemberTimeIndicators>> timezones = new ArrayList<>();


        for (int i = 0; i < 25; i++) {
            timezones.add(new ArrayList<>());
        }

        this.getChannels(event).stream().filter((channel) -> (channel.getName().toLowerCase().equals("general"))).forEachOrdered((channel) -> {
            channel.getUsers(slackApiEndpoints, slackToken).forEach((user) -> {
                if (!user.isDeleted() && !user.isIs_bot()) {
                    String username = user.getProfile().getDisplay_name().isEmpty() ? user.getProfile().getRealName() : user.getProfile().getDisplay_name();
                    timezones.get(12 + (int) Math.floor(user.getTz_offset() / 60 / 60))
                            .add(new MemberTimeIndicators(user.getTz_label(), user.getTz_offset(), Integer.toString(user.getPresence().getLast_activity()), username));
                }
            });
        });

        StringBuilder sb = new StringBuilder();
        Averaging averaging = new Averaging();
        timezones.stream().filter((memberTimes) -> (memberTimes.size() > 0)).map((memberTimes) -> {
            String time = "";
            long timeAdjusted = (long) Math.abs(userTime + (int) memberTimes.get(0).getValue()*1000);
            Date adjustedDate = new Date(timeAdjusted);

            time = sdf.format(adjustedDate);

            int gmt = (int) memberTimes.get(0).timezoneOffsetInSeconds / 60 / 60;
            String gmtValue;
            if (gmt > 0) {
                gmtValue = "+" + Integer.toString(gmt);
            } else {
                gmtValue = Integer.toString(gmt);
            }
            StringBuilder memberMessage = new StringBuilder();
            for (MemberTimeIndicators ind : memberTimes) {
                memberMessage.append(" - ").append(ind.getName());
            }

            slackBot.reply(session, event, memberTimes.size() + " members in " + memberTimes.get(0).getKey() + " GMT" + gmtValue + " ("+time+")  " + memberMessage.toString());
            return memberTimes;
        }).filter((memberTimes) -> (memberTimes.size() > 2)).forEachOrdered((memberTimes) -> {
            memberTimes.forEach((kvp) -> {
                averaging.add(kvp.timezoneOffsetInSeconds);
            });
        });

        slackBot.reply(session, event, sb.toString() + "\nExcluding the 1's and 2's, the mean-average Badazzes member is located in GMT " + averaging.getAverage() / 60 / 60);
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
    public boolean maybeDoAdam(Event event, WebSocketSession session) {
        if (event.getText().toLowerCase().contains("who is adam")) {
            slackBot.reply(session, event, "Adam is my creator. He hacks phones for a living. He has a social media page at google.com/+AdamOutler. He tries to tell me dad jokes, but... ");
            slackBot.reply(session, event, "I don't understand");
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
        return getUser(event.getUserId());
    }

    public User getUser(String userid) {
        UserResponse userResponse = new RestTemplate().getForEntity(slackApiEndpoints.getUserInfoAPI() + "&user=" + userid, UserResponse.class, slackToken).getBody();
        userResponse.getUser().setPresence(getUserPresence(userid));

        return userResponse.getUser();
    }

    public UserPresence getUserPresence(String userId) {

        UserPresence userPresence = new RestTemplate().getForEntity(slackApiEndpoints.getUserInfoAPI() + "&user=" + userId, UserPresence.class, slackToken).getBody();
        return userPresence;

    }

    public boolean maybeDoSource(Event event, WebSocketSession session) {
        if (event.getText().toLowerCase().contains("source")) {
            slackBot.reply(session, event, this.getUser(event).getProfile().getRealName() + ", my source code is here https://github.com/adamoutler/jbot.");
            return true;
        }
        return false;
    }

}
