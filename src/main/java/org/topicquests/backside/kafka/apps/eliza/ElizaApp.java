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
package org.topicquests.backside.kafka.apps.eliza;

import java.util.Iterator;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.topicquests.backside.kafka.KafkaBacksideEnvironment;
import org.topicquests.backside.kafka.apps.AbstractKafkaApp;
import org.topicquests.backside.kafka.consumer.AbstractBaseConsumer;
import org.topicquests.backside.kafka.producer.MessageProducer;

/**
 * @author jackpark
 *
 */
public class ElizaApp extends AbstractKafkaApp {
	private MessageProducer producer;
	private MyConsumer consumer;
	private ElizaResponder eliza;

	/**
	 * @param env
	 */
	public ElizaApp(KafkaBacksideEnvironment env) {
		super(env);
		eliza = new ElizaResponder();
		String topic = environment.getStringProperty("ElizaProducerTopic");
		producer = new MessageProducer(environment, topic, topic, false);
		topic = environment.getStringProperty("ElizaConsumerTopic");
		consumer = new MyConsumer(environment, topic);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.kafka.apps.AbstractKafkaApp#close()
	 */
	@Override
	public void close() {
		producer.close();
		consumer.close();
	}

	/**
	 * Listen for users talking to Eliza
	 * Respond by sending back Eliza's response
	 */
	class MyConsumer extends AbstractBaseConsumer {

		public MyConsumer(KafkaBacksideEnvironment e, String topic) {
			super(e, topic, topic);
		}
		
		public void handleRecords(ConsumerRecords<String, String> records) {
			Iterator<ConsumerRecord<String,String>>itr = records.iterator();
			ConsumerRecord<String,String>cr;
			String txt;
			while (itr.hasNext()) {
				cr = itr.next();
				txt = cr.value();
				System.out.println("ElizaApp got: "+txt);
				producer.sendMessage(eliza.elzTalk(txt), new Integer(0));
			}
		}
		
	}
}
