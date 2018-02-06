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
package devtests;

import org.topicquests.backside.kafka.KafkaBacksideEnvironment;
import org.topicquests.backside.kafka.apps.chat.SimpleChatApp;

/**
 * @author jackpark
 * A test of SimpleChatApp which just sends a message on the ChatApp channel
 * and the app receives that message back and prints it out.
 * This is the simplest way to sort out Consumers and Producers
 */
public class FirstTest {
	private KafkaBacksideEnvironment environment;

	/**
	 * 
	 */
	public FirstTest() {
		environment = new KafkaBacksideEnvironment();
		//SimpleChatApp app = environment.getChatApp();
		//app.sendMessage("Testing");
		//This will have the app printout a message it receives
		//We cannot shut down because the app is threaded and it
		// will be shut down before anything can happen
		//environment.shutDown();
	}

	public static void main(String[] args) {
		new FirstTest();
	}
}
