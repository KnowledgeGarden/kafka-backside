/**
 * 
 */
package org.topicquests.backside.kafka;

/**
 * @author jackpark
 *
 */
public class BacksideStart {
	private KafkaBacksideEnvironment environment;
	/**
	 * 
	 */
	public BacksideStart() {
		environment = new KafkaBacksideEnvironment();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new BacksideStart();
	}

}
