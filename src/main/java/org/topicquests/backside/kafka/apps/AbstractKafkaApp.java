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
package org.topicquests.backside.kafka.apps;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;
import org.topicquests.backside.kafka.KafkaBacksideEnvironment;
import org.topicquests.backside.kafka.apps.api.IKafkaApp;

/**
 * @author jackpark
 *
 */
public abstract class AbstractKafkaApp implements IKafkaApp {
	protected KafkaBacksideEnvironment environment;

	/**
	 * 
	 */
	public AbstractKafkaApp(KafkaBacksideEnvironment env) {
		environment = env;
	}
	
	/* (non-Javadoc)
	 * @see org.topicquests.backside.kafka.apps.api.IKafkaApp#close()
	 */
	@Override
	public abstract void close();

}
