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

import java.util.*;

import org.topicquests.backside.kafka.apps.chat.ChatApp;
import org.topicquests.backside.kafka.apps.chat.SimpleChatApp;
import org.topicquests.backside.kafka.apps.eliza.ElizaApp;
import org.topicquests.backside.kafka.connector.api.IPluginConnector;
import org.topicquests.support.config.ConfigPullParser;
import org.topicquests.support.util.LoggingPlatform;
import org.topicquests.support.util.Tracer;

/**
 * @author jackpark
 *
 */
public class KafkaBacksideEnvironment {
	private LoggingPlatform log = LoggingPlatform.getInstance("logger.properties");
	private Map<String, Object>properties;
	private Map<String, IPluginConnector> connectors;
	
	/**
	 * SimpleChatApp is for debugging
	 * @deprecated
	 */
	private SimpleChatApp chatApp;
	/**
	 * ChatApp is a real app
	 */
	private ChatApp	mainChatApp;
	private ElizaApp elizaApp;

	/**
	 * 
	 */
	public KafkaBacksideEnvironment() {
		ConfigPullParser p = new ConfigPullParser("kafka-props.xml");
		properties = p.getProperties();
		connectors = new HashMap<String, IPluginConnector>();
		//chatApp = new SimpleChatApp(this);
		mainChatApp = new ChatApp(this);
		elizaApp = new ElizaApp(this);
		bootPlugins();
		//TODO other stuff?
		System.out.println("Booted");
		//Register a shutdown hook so ^c will properly close things
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() { shutDown(); }
		});
	}

	public void registerPluginConnector(IPluginConnector c) {
		System.out.println("RegisteringPlugin "+c.getPluginName());
		connectors.put(c.getPluginName(), c);
	}
	/**
	 * Use the plugins list config property to boot plugins
	 */
	void bootPlugins() {
		List<List<String>>vals = (List<List<String>>)properties.get("PlugInConnectors");
		if (vals != null && vals.size() > 0) {
			String name, path;
			IPluginConnector pc;
			List<String>cntr;
			Iterator<List<String>>itr = vals.iterator();
			while (itr.hasNext()) {
				cntr = itr.next();
				name = cntr.get(0);
				path = cntr.get(1);
				try {
					pc = (IPluginConnector)Class.forName(path).newInstance();
					pc.init(this, name);
				} catch (Exception e) {
					logError(e.getMessage(), e);
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * @deprecated
	 * @return
	 */
	public SimpleChatApp getChatApp() {
		return chatApp;
	}
	//////////////////////
	// Utilities
	//////////////////////
	
	public String getStringProperty(String key) {
		return (String)properties.get(key);
	}
	
	public Map<String, Object> getProperties() {
		return properties;
	}
	
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
		System.out.println("Shutting Down");
	//	chatApp.close();
		mainChatApp.close();
		elizaApp.close();
		//TODO others?
		//plugins
		if (connectors.size() > 0) {
			Iterator<String>itr = connectors.keySet().iterator();
			while (itr.hasNext())
				connectors.get(itr.next()).close();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new KafkaBacksideEnvironment();
	}

}
