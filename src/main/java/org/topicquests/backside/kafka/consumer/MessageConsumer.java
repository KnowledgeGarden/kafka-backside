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
import org.topicquests.backside.kafka.consumer.api.IMessageConsumerListener;

import kafka.utils.ShutdownableThread;

/**
 * @author jackpark
 * Modeled after a Kafka example
 * @see https://www.tutorialspoint.com/apache_kafka/apache_kafka_simple_producer_example.htm
 */
public class MessageConsumer extends ShutdownableThread implements IClosable {
	private KafkaBacksideEnvironment environment;
    private final KafkaConsumer<String, String> consumer;
    private IMessageConsumerListener listener;
    private boolean isRunning = true;

	/**
	 * @param e
	 * @param topoic
	 * @param l
	 */
	public MessageConsumer(KafkaBacksideEnvironment e, String topic, IMessageConsumerListener l) {
		super(topic, false);
		environment = e;
		listener = l;
		Properties props = new Properties();
		//TODO make a config value
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", topic); //TODO not sure about that
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
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.kafka.apps.api.IClosable#close()
	 */
	@Override
	public void close() {
		isRunning = false;
		consumer.close();
	}

	/* (non-Javadoc)
	 * @see kafka.utils.ShutdownableThread#doWork()
	 */
	@Override
	public void doWork() {
		while (isRunning) {
	         ConsumerRecords<String, String> records = consumer.poll(100);
	         if (records.count() > 0) {
	        	 environment.logDebug("ConsumerGet "+records);
	        	 listener.acceptRecords(records);
	         }
		}
	}

}
