package com.byron.media.server;

import com.byron.media.server.MediaSession;
import com.byron.media.server.data.MediaData;
import com.byron.media.server.data.MediaDescriptionData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 媒体组
 */
public class MediaGroup {

    private String groupName;           // 媒体组

    private MediaDescriptionData mediaDescription;          // 媒体描述

    private Set<MediaSession> receiverSessions;         // 该组下所有的接收端

    private MediaSession senderSession;             // 发送端

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public MediaGroup(String groupName, MediaSession senderSession, MediaDescriptionData mediaDescription){
        receiverSessions = new HashSet<>();
        this.groupName = groupName;
        this.mediaDescription = mediaDescription;
        this.senderSession = senderSession;
    }


    /**
     * 发送到所有接收端
     * @param mediaData
     */
    public void sendToAll(MediaData mediaData){
        lock.readLock().lock();
        try {
            for(MediaSession session : receiverSessions){
                session.write(mediaData);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * 添加到接收端
     * @param session
     */
    public void addToReceiver(MediaSession session){
        lock.writeLock().lock();
        try {
            receiverSessions.add(session);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 移除监听
     * @param session
     */
    public void removeFromReceiver(MediaSession session){
        lock.writeLock().lock();
        try {
            receiverSessions.remove(session);
        } finally {
            lock.writeLock().unlock();
        }
    }


    /**
     * 获取在线设备
     * @return
     */
    public Set<MediaSession> getReceiverSessions(){
        lock.readLock().lock();
        try {
            return new HashSet<>(receiverSessions);
        } finally {
            lock.readLock().unlock();
        }
    }

    public MediaDescriptionData getMediaDescription() {
        return mediaDescription;
    }

    public void setMediaDescription(MediaDescriptionData mediaDescription) {
        this.mediaDescription = mediaDescription;
    }
}
