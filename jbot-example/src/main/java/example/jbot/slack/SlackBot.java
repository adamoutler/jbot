package example.jbot.slack;

import me.ramswaroop.jbot.core.common.Controller;
import me.ramswaroop.jbot.core.common.EventType;
import me.ramswaroop.jbot.core.common.JBot;
import me.ramswaroop.jbot.core.slack.Bot;
import me.ramswaroop.jbot.core.slack.models.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.WebSocketSession;

import java.util.regex.Matcher;
import com.adamoutler.time.CityKingsTime;
import com.adamoutler.slacktools.SlackTools;
import java.util.Random;
import java.util.logging.Level;
import me.ramswaroop.jbot.core.slack.SlackApiEndpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketMessage;

/**
 * A simple Slack Bot. You can create multiple bots by just extending
 * {@link Bot} class like this one. Though it is recommended to create only bot
 * per jbot instance.
 *
 * @author ramswaroop
 * @version 1.0.0, 05/06/2016
 */
@JBot
@Profile("slack")
public class SlackBot extends Bot {

    @Autowired
    SlackApiEndpoints slackApiEndpoints;
    private final String MYUSERID = "UBS24JP1U";
    final String FLOWCHARTURL = "https://badazzes.slack.com/files/U9S44RGNP/FBN4T7TAA/image.png";
    String[] timeCommands = new String[]{"we join", "City Kings Time"};
    private static final Logger LOGGER = LoggerFactory.getLogger(SlackBot.class);

    /**
     * Slack token from application.properties file. You can get your slack
     * token next <a href="https://my.slack.com/services/new/bot">creating a new
     * bot</a>.
     */
    @Value("${slackBotToken}")
    private String slackToken;

    @Override
    public String getSlackToken() {
        return slackToken;
    }

    @Override
    public Bot getSlackBot() {
        return this;
    }

    /**
     * Invoked when the bot receives a direct mention (@botname: message) or a
     * direct message. NOTE: These two event types are added by jbot to make
     * your task easier, Slack doesn't have any direct way to determine these
     * type of events.
     *
     * @param session
     * @param event
     */
    @Controller(events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE})
    public void onReceiveDM(WebSocketSession session, Event event) {
        if (isMyOwnMessage(event)) {
            return;
        }

        if (!event.getText().isEmpty()) {
            if ( maybeDoTimeCommand(event, session) || maybeDoFlowchart(event, session) || maybeDoSource(event, session) || maybeGetQuote(event, session)|| maybeDoAlwaysWatching(event, session)) {
                System.out.println("done");
            } else {
                WebSocketMessage msg;
                reply(session, event, "I don't understand.");
            }
        }
    }

    private boolean maybeGetQuote(Event event, WebSocketSession session) {

        if (event.getText().toLowerCase().contains("inspir")) {

            reply(session, event, getQuote());
            return true;
        }
        return false;
    }

    public static String getQuote() {
        String[] array = new String[]{"All warfare is based on deception.--Sun Tsu",
            "The supreme art of war is to subdue the enemy without fighting.--Sun Tsu",
            "Know thy self, know thy enemy. A thousand battles, a thousand victories.--Sun Tsu",
            "If you know the enemy and know yourself you need not fear the results of a hundred battles.--Sun Tsu",
            "Know your enemy and know yourself and you can fight a hundred battles without disaster.--Sun Tsu",
            "Strategy without tactics is the slowest route to victory. Tactics without strategy is the noise before defeat.--Sun Tsu",
            "Opportunities multiply as they are seized.--Sun Tsu",
            "Victorious warriors win first and then go to war, while defeated warriors go to war first and then seek to win.--Sun Tsu",
            "Hence to fight and conquer in all your battles is not supreme excellence; supreme excellence consists in breaking the enemy's resistance without fighting.--Sun Tsu",
            "Pretend inferiority and encourage his arrogance.--Sun Tsu"};
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    private boolean isMyOwnMessage(Event event) {
        String user = SlackTools.getSlackUsername(event);
        return user.equals(MYUSERID);
    }

    private boolean maybeDoTimeCommand(Event event, WebSocketSession session) {
        for (String t : timeCommands) {
            if (event.getText().toLowerCase().contains(t)) {
                reply(session, event, CityKingsTime.getCityKingsTime());
                return true;
            }
        }
        return false;
    }

    private boolean maybeDoFlowchart(Event event, WebSocketSession session) {

        if (event.getText().toLowerCase().contains("flowchart")) {
            reply(session, event, "Here's the current flowchart! " + FLOWCHARTURL);
            return true;

        }
        return false;
    }

    private boolean maybeDoSource(Event event, WebSocketSession session) {

        if (event.getText().toLowerCase().contains("source")) {
            reply(session, event, "My source code is here https://github.com/adamoutler/jbot.");
            return true;

        }
        return false;
    }

    /**
     * Invoked when bot receives an event of type message with text satisfying
     * the pattern {@code ([a-z ]{2})(\d+)([a-z ]{2})}. For example, messages
     * like "ab12xy" or "ab2bc" etc will invoke this method.
     *
     * @param session
     * @param event
     * @param matcher
     */
    @Controller(events = EventType.MESSAGE, pattern = "^([a-z ]{2})(\\d+)([a-z ]{2})$")
    public void onReceiveMessage(WebSocketSession session, Event event, Matcher matcher) {
//        reply(session, event, "First group: " + matcher.group(0) + "\n"
//                + "Second group: " + matcher.group(1) + "\n"
//                + "Third group: " + matcher.group(2) + "\n"
//                + "Fourth group: " + matcher.group(3));
    }

    /**
     * Invoked when an item is pinned in the channel.
     *
     * @param session
     * @param event
     */
    @Controller(events = EventType.PIN_ADDED)
    public void onPinAdded(WebSocketSession session, Event event) {
//        reply(session, event, "Thanks for the pin! You can find all pinned items under channel details.");
    }

    /**
     * Invoked when bot receives an event of type file shared. NOTE: You can't
     * reply to this event as slack doesn't send a channel id for this event
     * type. You can learn more about
     * <a href="https://api.slack.com/events/file_shared">file_shared</a>
     * event from Slack's Api documentation.
     *
     * @param session
     * @param event
     */
    @Controller(events = EventType.FILE_SHARED)
    public void onFileShared(WebSocketSession session, Event event) {
//        logger.info("File shared: {}", event);
    }

    /**
     * Conversation feature of JBot. This method is the starting point of the
     * conversation (as it calls {@link Bot#startConversation(Event, String)}
     * within it. You can chain methods which will be invoked one after the
     * other leading to a conversation. You can chain methods with
     * {@link Controller#next()} by specifying the method name to chain with.
     *
     * @param session
     * @param event
     */
    @Controller(pattern = "(setup meeting)", next = "confirmTiming")
    public void setupMeeting(WebSocketSession session, Event event) {
//        startConversation(event, "confirmTiming");   // start conversation
//        reply(session, event, "Cool! At what time (ex. 15:30) do you want me to set up the meeting?");
    }

    /**
     * This method will be invoked after
     * {@link SlackBot#setupMeeting(WebSocketSession, Event)}.
     *
     * @param session
     * @param event
     */
    @Controller(next = "askTimeForMeeting")
    public void confirmTiming(WebSocketSession session, Event event) {
//        reply(session, event, "Your meeting is set at " + event.getText()
//                + ". Would you like to repeat it tomorrow?");
//        nextConversation(event);    // jump to next question in conversation
    }

    /**
     * This method will be invoked after
     * {@link SlackBot#confirmTiming(WebSocketSession, Event)}.
     *
     * @param session
     * @param event
     */
    @Controller(next = "askWhetherToRepeat")
    public void askTimeForMeeting(WebSocketSession session, Event event) {
//        if (event.getText().contains("yes")) {
//            reply(session, event, "Okay. Would you like me to set a reminder for you?");
//            nextConversation(event);    // jump to next question in conversation  
//        } else {
//            reply(session, event, "No problem. You can always schedule one with 'setup meeting' command.");
//            stopConversation(event);    // stop conversation only if user says no
//        }
    }

    /**
     * This method will be invoked after
     * {@link SlackBot#askTimeForMeeting(WebSocketSession, Event)}.
     *
     * @param session
     * @param event
     */
    @Controller
    public void askWhetherToRepeat(WebSocketSession session, Event event) {
//        if (event.getText().contains("yes")) {
//            reply(session, event, "Great! I will remind you tomorrow before the meeting.");
//        } else {
//            reply(session, event, "Okay, don't forget to attend the meeting tomorrow :)");
//        }
//        stopConversation(event);    // stop conversation
    }

    private boolean maybeDoAlwaysWatching(Event event, WebSocketSession session) {

        if (event.getText().toLowerCase().contains("always watching")) {

            reply(session, event, "I may be pretty quiet, but I'm always here and see everything going on at all times.  I could react to any message, but I choose not to. I'm always watching everything.  always...");
            try {
                Thread.sleep(30000);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(SlackBot.class.getName()).log(Level.SEVERE, null, ex);
            }
            reply(session, event, "...literally always.");
            return true;
        }
        return false;
    }

}
