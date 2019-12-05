package com.byron.media.server;

import com.byron.media.server.data.MediaDescriptionData;

import java.util.HashMap;
import java.util.Map;

public class MediaCenter {

    private Map<String, MediaGroup> groupMap = new HashMap<>();

    /**
     * 注册发送端
     */
    public MediaDescriptionData registerReceiver(String groupName, MediaSession session){
        MediaGroup group = groupMap.get(groupName);
        if(group != null){
            session.setMediaGroup(group);
            group.addToReceiver(session);
            return group.getMediaDescription();
        }
        return null;
    }

    /**
     * 注册发送端
     */
    public boolean registerSender(String groupName, MediaSession session, MediaDescriptionData mediaDescription){
        MediaGroup group = groupMap.get(groupName);
        if(group != null){
           return false;
        }
        group = new MediaGroup(groupName, session, mediaDescription);
        session.setMediaGroup(group);
        groupMap.put(session.getGroupName(), group);
        return true;
    }

    private static MediaCenter instance = new MediaCenter();
    public static MediaCenter getInstance() {
        return instance;
    }


}
