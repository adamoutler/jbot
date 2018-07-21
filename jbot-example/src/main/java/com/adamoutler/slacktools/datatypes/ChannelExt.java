/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adamoutler.slacktools.datatypes;

import java.util.ArrayList;
import java.util.List;
import me.ramswaroop.jbot.core.slack.SlackApiEndpoints;
import me.ramswaroop.jbot.core.slack.models.Channel;
import me.ramswaroop.jbot.core.slack.models.User;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author adamo
 */
public class ChannelExt extends Channel {

    private String id;
    private String name;
    private boolean is_channel;
    private long created;
    private boolean is_archived;
    private boolean is_general;
    private int unlinked;
    private String name_normalized;
    private boolean is_shared;
    private boolean is_org_shared;
    private boolean is_member;
    private boolean is_private;
    private boolean is_mpim;
    private List<String> members;
    private Topic topic;
    private Purpose purpose;
    private List<String> previous_names;
    private int num_members;
    private long timeLastFetchedUpdated=0;
    private ArrayList<UserExt> users=new ArrayList<>();

    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();
        String n="\n";
        sb.append("id:").append(id).append(n);
        sb.append("name:").append(name).append(n);
        sb.append("is channel:").append(is_channel).append(n);
        sb.append("is archived:").append(is_archived).append(n);
        sb.append("is general:").append(is_general).append(n);
        sb.append("unlinked:").append(unlinked).append(n);
        sb.append("name normalized:").append(name_normalized).append(n);
        sb.append("is shared:").append(is_shared).append(n);
        sb.append("is org_shared:").append(is_org_shared).append(n);
        sb.append("is member:").append(is_member).append(n);
        sb.append("is private:").append(is_private).append(n);
        sb.append("is mpim:").append(is_mpim).append(n);
        sb.append("members:").append(members.toString()).append(n);
        sb.append("topic:").append(topic).append(n);
        sb.append("purpose:").append(purpose).append(n);
        sb.append("previous names:").append(previous_names).append(n);
        sb.append("num members:").append(num_members).append(n);
     
        
       return sb.toString();
    }
    
    
    /**
     * @return the id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the is_channel
     */
    public boolean isIs_channel() {
        return is_channel;
    }

    /**
     * @param is_channel the is_channel to set
     */
    public void setIs_channel(boolean is_channel) {
        this.is_channel = is_channel;
    }

    /**
     * @return the created
     */
    @Override
    public long getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    @Override
    public void setCreated(long created) {
        this.created = created;
    }

    /**
     * @return the is_archived
     */
    public boolean isIs_archived() {
        return is_archived;
    }

    /**
     * @param is_archived the is_archived to set
     */
    public void setIs_archived(boolean is_archived) {
        this.is_archived = is_archived;
    }

    /**
     * @return the is_general
     */
    public boolean isIs_general() {
        return is_general;
    }

    /**
     * @param is_general the is_general to set
     */
    public void setIs_general(boolean is_general) {
        this.is_general = is_general;
    }

    /**
     * @return the unlinked
     */
    public int getUnlinked() {
        return unlinked;
    }

    /**
     * @param unlinked the unlinked to set
     */
    public void setUnlinked(int unlinked) {
        this.unlinked = unlinked;
    }

    /**
     * @return the name_normalized
     */
    public String getName_normalized() {
        return name_normalized;
    }

    /**
     * @param name_normalized the name_normalized to set
     */
    public void setName_normalized(String name_normalized) {
        this.name_normalized = name_normalized;
    }

    /**
     * @return the is_shared
     */
    public boolean isIs_shared() {
        return is_shared;
    }

    /**
     * @param is_shared the is_shared to set
     */
    public void setIs_shared(boolean is_shared) {
        this.is_shared = is_shared;
    }

    /**
     * @return the is_org_shared
     */
    public boolean isIs_org_shared() {
        return is_org_shared;
    }

    /**
     * @param is_org_shared the is_org_shared to set
     */
    public void setIs_org_shared(boolean is_org_shared) {
        this.is_org_shared = is_org_shared;
    }

    /**
     * @return the is_member
     */
    public boolean isIs_member() {
        return is_member;
    }

    /**
     * @param is_member the is_member to set
     */
    public void setIs_member(boolean is_member) {
        this.is_member = is_member;
    }

    /**
     * @return the is_private
     */
    public boolean isIs_private() {
        return is_private;
    }

    /**
     * @param is_private the is_private to set
     */
    public void setIs_private(boolean is_private) {
        this.is_private = is_private;
    }

    /**
     * @return the is_mpim
     */
    public boolean isIs_mpim() {
        return is_mpim;
    }

    /**
     * @param is_mpim the is_mpim to set
     */
    public void setIs_mpim(boolean is_mpim) {
        this.is_mpim = is_mpim;
    }

    /**
     * @return the members
     */
    public List<String> getMembers() {
        return members;
    }
    
    public ArrayList<UserExt> getUsers(SlackApiEndpoints slackApiEndpoints, String slackToken){
        users=new ArrayList<>();
        getMembers().forEach((userid) -> {
            UserExt user=new RestTemplate().getForEntity(slackApiEndpoints.getUserInfoAPI() + "&user=" + userid, UserResponse.class, slackToken).getBody().getUser();
            user.setPresence(new RestTemplate().getForEntity(slackApiEndpoints.getUserPresenceApi()+ "&user=" + userid, UserPresence.class, slackToken).getBody());
            users.add(user);
        });
        return users;
    }

    /**
     * @param members the members to set
     */
    public void setMembers(List members) {
        this.members = members;
    }

    /**
     * @return the topic
     */
    public Topic getTopic() {
        return topic;
    }

    /**
     * @param topic the topic to set
     */
    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    /**
     * @return the previous_names
     */
    public List getPrevious_names() {
        return previous_names;
    }

    /**
     * @param previous_names the previous_names to set
     */
    public void setPrevious_names(List previous_names) {
        this.previous_names = previous_names;
    }

    /**
     * @return the num_members
     */
    public int getNum_members() {
        return num_members;
    }

    /**
     * @param num_members the num_members to set
     */
    public void setNum_members(int num_members) {
        this.num_members = num_members;
    }

    /**
     * @return the purpose
     */
    public Purpose getPurpose() {
        return purpose;
    }

    /**
     * @param purpose the purpose to set
     */
    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    /**
     * @return the timeLastFetchedUpdated
     */
    public long getTimeLastFetchedUpdated() {
        return timeLastFetchedUpdated;
    }

    /**
     * @param timeLastFetchedUpdated the timeLastFetchedUpdated to set
     */
    public void setTimeLastFetchedUpdated(long timeLastFetchedUpdated) {
        this.timeLastFetchedUpdated = timeLastFetchedUpdated;
    }

    /**
     * @return the users
     */
    public ArrayList<UserExt> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(ArrayList<UserExt> users) {
        this.users = users;
    }

}
