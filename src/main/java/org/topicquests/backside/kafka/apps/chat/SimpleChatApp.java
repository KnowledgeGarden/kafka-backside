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
package org.topicquests.backside.kafka.apps.chat;

import java.util.Iterator;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.topicquests.backside.kafka.KafkaBacksideEnvironment;
import org.topicquests.backside.kafka.apps.AbstractKafkaApp;
import org.topicquests.backside.kafka.consumer.MessageConsumer;
import org.topicquests.backside.kafka.consumer.api.IMessageConsumerListener;
import org.topicquests.backside.kafka.producer.MessageProducer;

/**
 * @author jackpark
 * Just a really trivial app to sort out how to make Consumers and Producers
 */
public class SimpleChatApp extends AbstractKafkaApp 
		implements IMessageConsumerListener {
	private MessageProducer producer;
	private MessageConsumer consumer;
	private String name;
	
	/**
	 * @param env
	 */
	public SimpleChatApp(KafkaBacksideEnvironment env) {
		super(env);
		name = "ChatRoom"; //TODO make into a config value
		producer = new MessageProducer(environment, name, false);
		consumer = new MessageConsumer(environment, name, this);
		producer.start();
		consumer.start();
	}

	public void sendMessage(String msg) {
		environment.logDebug("ChatAppSendMessage "+msg);
		producer.sendMessage(msg);
	}

	@Override
	public void close() {
		producer.close();
		consumer.close();
	}


	@Override
	public void acceptRecords(ConsumerRecords<String, String> records) {
		System.out.println("ChatAppGetting "+records.count());
		Iterator<ConsumerRecord<String, String>>itr = records.records(name).iterator();
		ConsumerRecord<String, String>cr;
		String val;
		while (itr.hasNext()) {
			cr = itr.next();
			val = cr.value();
			//for DEBUG
			System.out.println("V "+val);
			//TODO CREATE an Interface to do something with these records
			// either as a collection, or individually
		}
		
	}

}
