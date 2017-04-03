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
package org.topicquests.backside.kafka;

import java.util.Map;

import org.nex.config.ConfigPullParser;
import org.topicquests.backside.kafka.apps.chat.SimpleChatApp;
import org.topicquests.util.LoggingPlatform;
import org.topicquests.util.Tracer;

/**
 * @author jackpark
 *
 */
public class KafkaBacksideEnvironment {
	private LoggingPlatform log = LoggingPlatform.getInstance("logger.properties");
	private Map<String,Object>properties;
	private SimpleChatApp chatApp;

	/**
	 * 
	 */
	public KafkaBacksideEnvironment() {
		ConfigPullParser p = new ConfigPullParser("config-props.xml");
		properties = p.getProperties();
		chatApp = new SimpleChatApp(this);
		//TODO
		
		System.out.println("Booted");
	}

	
	public SimpleChatApp getChatApp() {
		return chatApp;
	}
	//////////////////////
	// Utilities
	//////////////////////
	
	public void logDebug(String msg) {
		log.logDebug(msg);
	}

	public void logError(String msg, Exception e) {
		log.logError(msg, e);
	}
	
	public Tracer getTracer(String agentName) {
		return new Tracer(agentName, log);
	}

	
	
	public void shutDown() {
		chatApp.close();
		//TODO
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new KafkaBacksideEnvironment();
	}

}
