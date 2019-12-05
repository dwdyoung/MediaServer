package com.byron.media.server.data;

import com.byron.media.server.data.MediaData;

import java.util.ArrayList;
import java.util.List;

public class DataFactory {

    private List<MediaData> mediaData = new ArrayList<>();

    /**
     * 从缓存中创建媒体数据
     */
    public MediaData createData(int length) {
        MediaData item = null;
        for(MediaData data : mediaData){
            if(!data.isFree()){
                continue;
            }
            if(data.getData().length < length){
                continue;
            }
            item = data;
            break;
        }
        if(item == null){
            item = new MediaData((byte)0, (byte)0, 0l, new byte[length], length);
            mediaData.add(item);
        }
        item.setFree(false);
        return item;
    }

//    /**
//     * 从缓存中创建媒体数据
//     */
//    public MediaData createData(byte channel, byte type, long id, int length) {
//        MediaData media = createData(length);
//        media.setChannel(channel);
//        media.setType(type);
//        media.setID(id);
//        media.setLength(length);
//        return media;
//    }

    private static ThreadLocal<DataFactory> instance = new ThreadLocal<>();
    public static DataFactory getInstance(){
        return instance.get();
    }
}
