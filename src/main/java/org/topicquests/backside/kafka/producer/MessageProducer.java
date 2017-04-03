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
package org.topicquests.backside.kafka.producer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.topicquests.backside.kafka.KafkaBacksideEnvironment;
import org.topicquests.backside.kafka.apps.api.IClosable;

/**
 * @author jackpark
 * Modeled after a Kafka example
 * @see https://www.tutorialspoint.com/apache_kafka/apache_kafka_simple_producer_example.htm
 */
public class MessageProducer extends Thread implements IClosable {
	private KafkaBacksideEnvironment environment;
    private final KafkaProducer<String, String> producer;
    private boolean isRunning = true;
    private boolean isAsync;
    private final String topic;
    private List<String>messages;

	/**
	 * @param e
	 * @param topic
	 * @param isAsynchronous // ignored for now
	 */
	public MessageProducer(KafkaBacksideEnvironment e, String topic, boolean isAsynchronous) {
		super(topic);
		environment = e;
		this.topic = topic;
		isAsync = isAsynchronous;
		Properties props = new Properties();
		//TODO make bootstrap.servers a config value
		props.put("bootstrap.servers", "localhost:9092");
		props.put("key.serializer", 
		         "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", 
		         "org.apache.kafka.common.serialization.StringSerializer");
		//TODO there may be other necessary key/value pairs
		// but this survived FirstTest
		producer = new KafkaProducer<String, String>(props);
		messages = new ArrayList<String>();
	}

	public void sendMessage(String message) {
		synchronized(messages) {
			messages.add(message);
			messages.notify();
		}
	}
	public void run() {
		String msg = null;
		while (isRunning) {
			synchronized(messages) {
				if (messages.isEmpty()) {
					try {
						messages.wait();
					} catch (Exception e) {}
				} else {
					msg = messages.remove(0);
				}
			}
			if (msg != null) {
				_sendMessage(msg);
				msg = null;
			}
		}
	}
	
	void _sendMessage(String msg) {
		ProducerRecord<String, String> pr = 
				new ProducerRecord<String, String>(topic, topic, msg);
		//TODO deal with asynch FutureCallback
		environment.logDebug("ProducerSend "+pr);
		if (isAsync) {
			try {
				producer.send(pr).get();
			} catch (Exception e) {
				environment.logError(e.getMessage(), e);
				e.printStackTrace();
			}			
		} else {
			try {
				producer.send(pr).get();
			} catch (Exception e) {
				environment.logError(e.getMessage(), e);
				e.printStackTrace();				
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.kafka.apps.api.IClosable#close()
	 */
	@Override
	public void close() {
		synchronized(messages) {
			isRunning = false;
			producer.close();
		}
	}

}
