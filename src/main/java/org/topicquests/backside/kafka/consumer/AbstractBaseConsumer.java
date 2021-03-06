/*
 * Copyright 2017, TopicQuests
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package org.topicquests.backside.kafka.consumer;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.topicquests.backside.kafka.KafkaBacksideEnvironment;
import org.topicquests.backside.kafka.apps.api.IClosable;

import kafka.utils.ShutdownableThread;

/**
 * @author jackpark
 *
 */
public abstract class AbstractBaseConsumer extends ShutdownableThread implements IClosable {
	private KafkaBacksideEnvironment environment;
    private final KafkaConsumer<String, String> consumer;
    private boolean isRunning = true;
    
	/**
	 * 
	 */
	public AbstractBaseConsumer(KafkaBacksideEnvironment e, String groupId, String topic) {
 		super(topic, false);
        System.out.println("ABC "+topic);
		environment = e;
		String gid = groupId;
		if (groupId == null)
			gid = topic;
		Properties props = new Properties();
		String url = "localhost";
		String port = "9092";
		if (environment != null && environment.getStringProperty("KAFKA_SERVER_URL") != null) {
			url = environment.getStringProperty("KAFKA_SERVER_URL");
			port = environment.getStringProperty("KAFKA_SERVER_PORT");
		}
		props.put("bootstrap.servers", url+":"+port);
		props.put("group.id", gid); //TODO not sure about that
		props.put("key.deserializer", 
		         "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", 
		         "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
	    props.put("session.timeout.ms", "30000");
 	    //TODO there may be other key/values but these survived FirstText
		consumer = new KafkaConsumer<String, String>(props);
		//Kafka Consumer subscribes list of topics here.
	    consumer.subscribe(Arrays.asList(topic));
	    this.start();
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.kafka.apps.api.IClosable#close()
	 */
	@Override
	public void close() {
		isRunning = false;
		synchronized(consumer) {
            System.out.println("AbstractBaseConsumer.closing");
			consumer.close();
		}
	}
	
	/**
	 * Extension class must implement this to do whatever it wants
	 * with the <code>records</code>
	 * @param records
	 */
	public abstract void handleRecords(ConsumerRecords<String, String> records);
	
	/* (non-Javadoc)
	 * @see kafka.utils.ShutdownableThread#doWork()
	 */
	@Override
	public void doWork() {
		ConsumerRecords<String, String> records = null;
		while (isRunning) {
            //System.out.println("FFF "+records);
			synchronized(consumer) {
	          records = consumer.poll(10);
			}
	         if (records != null && records.count() > 0) {
	 			System.out.println("AbstractBaseConsumer "+records);
	 			if (environment != null)
	 				environment.logDebug("ConsumerGet "+records);
	        	handleRecords(records);
                 records = null;
	         }
		}
	}

}
