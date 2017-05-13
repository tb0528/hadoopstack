package com.xiaoxiaomo.kafka.flume;

import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.apache.log4j.Logger;




public class MySink extends AbstractSink implements Configurable {
	  private String key;
	  private String value;
	  private Logger logger; 
	  @Override
	  public void configure(Context context) {
	    String key = context.getString("key", "defaultKey");
	    String value = context.getString("value", "defaultValue");
	    // Process the myProp value (e.g. validation)

	    // Store myProp for later retrieval by process() method
	    this.key = key;
	    this.value = value;
	  }

	  @Override
	  public void start() {
		  logger=Logger.getLogger(MySink.class);
	    // Initialize the connection to the external repository (e.g. HDFS) that
	    // this Sink will forward Events to ..
	  }

	  @Override
	  public void stop () {
	    // Disconnect from the external respository and do any
	    // additional cleanup (e.g. releasing resources or nulling-out
	    // field values) ..
		  logger=null;
	  }

	  @Override
	  public Status process() throws EventDeliveryException {
	    Status status = null;

	    // Start transaction
	    Channel ch = getChannel();
	    Transaction txn = ch.getTransaction();
	    txn.begin();
	    try {
	      // This try clause includes whatever Channel operations you want to do

	      Event event = ch.take();

	      logger.info(String.format("key:%s value:%s m %s", key,value,new String(event.getBody())));
	      // Send the Event to the external repository.
	      // storeSomeData(e);

	      txn.commit();
	      status = Status.READY;
	    } catch (Throwable t) {
	      txn.rollback();

	      // Log exception, handle individual exceptions as needed

	      status = Status.BACKOFF;

	      // re-throw all Errors
	      if (t instanceof Error) {
	        throw (Error)t;
	      }
	    } finally {
	      txn.close();
	    }
	    return status;
	  }
	}