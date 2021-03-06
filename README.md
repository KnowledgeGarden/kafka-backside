# kafka-backside
Kafka Backside project -- simple, to develop intuitions about Kafka

## Introduction ##
This is a simple platform, aimed at a much richer project to use  Stream Processing in backside work in the TopicQuests platform.<br/><br/>
The system is *Enviornment-centric* and includes the standard TopicQuests support functions of logging and configuration management with an XML file which is used for a few properties.<br/><br/>
There is included a SimpleChatApp which is used to explore the nature of applications which *live in a Kafka stream*. The app is now deprecated since live Chat and Eliza are available for testing.<br/>
There is now a *connector* API and base class.<br/>
Originally included but now removed to its own project was a shell ElasticSearch connector, not wired. That connector is an instance of an IPluginConnector object.  It is booted by using the config.xml file to list connectors by name. They are then booted at runtime and closed when the system shuts down.<br/>

## Building and Running ##
This code was built in Eclipse. Dependencies are included.<br/><br/>
To run, first boot the Kafka ensemble. Our approach was to download the Kafka distro, and boot its example system. That gives you a running system.<br/>
Use the shell script StartBackside.sh to boot the system against a running Kafka server.<br/>
Use Chat.sh to boot more than one chat windows to test the chat feature.<br/>
Use Eliza.sh to boot an Eliza window and have a conversation. At this time, the codebase does not yet support private Eliza conversations. That's in the works.