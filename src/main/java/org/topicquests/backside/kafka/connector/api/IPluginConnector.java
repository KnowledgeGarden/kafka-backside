/**
 * 
 */
package org.topicquests.backside.kafka.connector.api;

import org.topicquests.backside.kafka.KafkaBacksideEnvironment;

/**
 * @author jackpark
 * <p>An intention is to be able to compile connectors, add their
 * classpaths to config.xml, then boot them at runtime.</p>
 * <p>An easy way to use this is to extend <code>AbstractBaseConnector</code></p>
 */
public interface IPluginConnector {

	/**
	 * Initialize this connector with the core platform environment
	 * @param environment
	 * @param pluginName
	 */
	void init(KafkaBacksideEnvironment environment, String pluginName);
	
	String getPluginName();
	
	/**
	 * <p>Useful when the plugin needs closing. A plugin
	 * registers itself with the environment for closing</p>
	 * <p>Note, it's required because plugins are closed, even
	 * though it may have nothing to do</p>
	 */
	void close();
}
