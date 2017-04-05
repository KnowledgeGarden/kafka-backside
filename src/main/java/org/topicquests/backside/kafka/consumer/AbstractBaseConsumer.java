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

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerConnector;
import kafka.consumer.KafkaStream;
import kafka.utils.ShutdownableThread;

/**
 * @author jackpark
 *
 */
public abstract class AbstractBaseConsumer extends ShutdownableThread implements IClosable {
	private KafkaBacksideEnvironment environment;
	//private final ConsumerConnector xconsumer;
    private final KafkaConsumer<String, String> consumer;
    private boolean isRunning = true;
	/**
	 * 
	 */
	public AbstractBaseConsumer(KafkaBacksideEnvironment e, String groupId, String topic) {
		super(topic, false);
		environment = e;
		String gid = groupId;
		if (groupId == null)
			gid = topic;
		//xconsumer = kafka.consumer.Consumer.create(createConsumerConfig(topic));
		Properties props = new Properties();
		String url = "localhost";
		String port = "9092";
		if (environment != null && environment.getStringProperty("KAFKA_SERVER_URL") != null) {
			url = environment.getStringProperty("KAFKA_SERVER_URL");
			port = environment.getStringProperty("KAFKA_SERVER_PORT");
		}
		props.put("bootstrap.servers", url+":"+port);
		props.put("group.id", gid); //TODO not sure about that
		//testing auto.offset.reset -- does nothing to solve groupconsumer
		//props.put("auto.offset.reset", "latest");//latest, earliest, none
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
	private static ConsumerConfig createConsumerConfig(String topic) {
		Properties props = new Properties();
		props.put("zookeeper.connect", "localhost:2181");
		props.put("group.id", topic+Long.toString(System.currentTimeMillis()));
		props.put("zookeeper.session.timeout.ms", "400");
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "1000");

		return new ConsumerConfig(props);

}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.kafka.apps.api.IClosable#close()
	 */
	@Override
	public void close() {
		System.out.println("AbstractBaseConsumer.closing");
		isRunning = false;
		synchronized(consumer) {
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
		KafkaStream x;
		ConsumerRecords<String, String> records = null;
		while (isRunning) {
			synchronized(consumer) {
	          records = consumer.poll(1000);
			}
	         if (records != null && records.count() > 0) {
	 			System.out.println("AbstractBaseConsumer "+records);
	 			if (environment != null)
	 				environment.logDebug("ConsumerGet "+records);
	        	handleRecords(records);
	         }
		}
	}

}
