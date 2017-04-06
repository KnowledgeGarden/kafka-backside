/**
 * 
 */
package org.topicquests.backside.kafka.connector;

import org.topicquests.backside.kafka.KafkaBacksideEnvironment;
import org.topicquests.backside.kafka.connector.api.IPluginConnector;

/**
 * @author jackpark
 * Extend this class to make a connector
 */
public abstract class AbstractBaseConnector implements IPluginConnector {
	protected KafkaBacksideEnvironment environment;
	protected String name;
	
	/* (non-Javadoc)
	 * @see org.topicquests.backside.kafka.connector.api.IPluginConnector#init(org.topicquests.backside.kafka.KafkaBacksideEnvironment)
	 */
	@Override
	public void init(KafkaBacksideEnvironment environment, String pluginName) {
		this.environment = environment;
		name = pluginName;
		environment.registerPluginConnector(this);
	}
	
	/* (non-Javadoc)
	 * @see org.topicquests.backside.kafka.connector.api.IPluginConnector#getPluginName()
	 */
	@Override
	public String getPluginName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.kafka.connector.api.IPluginConnector#close()
	 */
	@Override
	public abstract void close();

}
