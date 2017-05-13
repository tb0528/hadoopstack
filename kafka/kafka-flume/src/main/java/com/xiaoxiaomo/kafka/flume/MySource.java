package com.xiaoxiaomo.kafka.flume;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDrivenSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.source.AbstractSource;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoxiaomo on 2015/12/27.
 */
public class MySource extends AbstractSource implements Configurable, EventDrivenSource {
    Logger logger=Logger.getLogger(MySource.class);
    private String file;
    public void configure(Context context) {
        file=context.getString("file",null);

        logger.info(context.getString("file"));
    }

    @Override
    public synchronized void start() {

        logger.info("-------------start----------------");
        // Initialize the connection to the external client

        if (null==file){
            logger.error("-----file not set!!!----");
        }
        List<Event> eventList=new ArrayList<Event>();
        try {
            BufferedReader fileReader=new BufferedReader(new FileReader(file));
            String msg=null;
            while ((msg=fileReader.readLine())!=null)
            {
                Event e = new SimpleEvent();
                e.setBody(msg.getBytes());
                logger.info(msg);
                eventList.add(e);
            }
            fileReader.close();
            getChannelProcessor().processEventBatch(eventList);

        } catch (IOException e) {
            e.printStackTrace();
        }
        super.start();
    }

    @Override
    public synchronized void stop() {
        logger.info("-------------stop----------------");
        // Disconnect from external client and do any additional cleanup
        super.stop();
    }
}
