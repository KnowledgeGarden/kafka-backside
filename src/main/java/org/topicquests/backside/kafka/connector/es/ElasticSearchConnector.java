/**
 * 
 */
package org.topicquests.backside.kafka.connector.es;

import org.topicquests.backside.kafka.connector.AbstractBaseConnector;

/**
 * @author jackpark
 * <p>To run against ElasticSearch</p>
 * <p>Requires a protocol for the JSON messages being sent.
 * Those messages must be interpreted as needed</p>
 */
public class ElasticSearchConnector extends AbstractBaseConnector {

	///////////////////////
	//TODO
	// This is where we write code which subscribes to
	//  appropriate topics and interacts with ElasticSearch
	///////////////////////
	
	/* (non-Javadoc)
	 * @see org.topicquests.backside.kafka.connector.AbstractBaseConnector#close()
	 */
	@Override
	public void close() {
		System.out.println("Closing connector: "+name);
		// TODO Auto-generated method stub
	}

}
