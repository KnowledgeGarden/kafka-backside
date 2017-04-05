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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.topicquests.backside.kafka.KafkaBacksideEnvironment;
import org.topicquests.backside.kafka.consumer.AbstractBaseConsumer;
import org.topicquests.backside.kafka.producer.MessageProducer;

/**
 * @author jackpark
 * A simple Frame for doing chats
 */
public class ElizaUI {
	private MessageProducer producer;
	private MyConsumer consumer;
	private JFrame frame;
	private JTextArea chatArea = new JTextArea();
	private JTextField chatField = new JTextField();
	private JButton sendButton = new JButton("Send");
	private String clientId;
	
	//TODO must initialize log4j
	
	/**
	 * 
	 */
	public ElizaUI() {
		buildGUI();
		clientId = "ElizaGUI"+Long.toString(System.currentTimeMillis()); //TODO should be different for each instance
		//NOTE: producer and consumer topics are opposite of server
		producer = new MessageProducer(null, "elizain", clientId, false);
		consumer = new MyConsumer(null, "elizaout");
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() { 
		    	producer.close();
		    	consumer.close(); }
		});
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ElizaUI();
	}

	void buildGUI() {
		JMenuBar jMenuBar1 = new JMenuBar();
		JMenu jMenuFile = new JMenu();
		JMenuItem jMenuFileExit = new JMenuItem();
		frame = new JFrame();
		frame.setSize(400, 400);
		frame.setTitle("ElizaApp");
		JPanel contentPane = (JPanel)frame.getContentPane();
		JPanel southPanel = new JPanel(new BorderLayout());
		contentPane.setLayout(new BorderLayout());
		JScrollPane consoleTab = new JScrollPane();
		contentPane.add(consoleTab, BorderLayout.CENTER);
		chatArea.setEditable(false);
	    chatArea.setText("");
	    chatArea.setLineWrap(true);
	    consoleTab.getViewport().add(chatArea);
	    southPanel.add(chatField, BorderLayout.CENTER);
	    southPanel.add(sendButton, BorderLayout.EAST);
	    sendButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	             String txt = chatField.getText();
	             System.out.println("GOT "+txt);
	             if (!txt.equals("")) {
	            	 producer.sendMessage(txt, new Integer(0));
	            	 chatField.setText("");
	             }
	          }          
	       });
	    contentPane.add(southPanel, BorderLayout.SOUTH);
		jMenuFileExit.setText("Exit");
	    jMenuFileExit.addActionListener(new MainFrame_jMenuFileExit_ActionAdapter(this));
	    jMenuFile.add(jMenuFileExit);
	    jMenuBar1.add(jMenuFile);
	    frame.setJMenuBar(jMenuBar1);
	    frame.setVisible(true);
	}
	
	void say(String text) {
		chatArea.append(text+"\n");
	}
	
	void jMenuFileExit_actionPerformed(ActionEvent actionEvent) {
		producer.close();
		consumer.close();
		System.exit(0);
	}
	
	class MyConsumer extends AbstractBaseConsumer {

		public MyConsumer(KafkaBacksideEnvironment e, String topic) {
			super(e, topic+Long.toHexString(System.currentTimeMillis()), topic);
		}
		
		public void handleRecords(ConsumerRecords<String, String> records) {
			Iterator<ConsumerRecord<String,String>>itr = records.iterator();
			ConsumerRecord<String,String>cr;
			String txt;
			while (itr.hasNext()) {
				cr = itr.next();
				txt = cr.value();
				System.out.println("ChatUI got "+txt);
				//NOTE: at this point, it's possible to inject filter chains
				// on the txt object
				say(txt);
			}
		}
		
	}
}

class MainFrame_jMenuFileExit_ActionAdapter
	implements ActionListener {
	ElizaUI adaptee;

	MainFrame_jMenuFileExit_ActionAdapter(ElizaUI adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jMenuFileExit_actionPerformed(actionEvent);
	}
}
