# kafka-backside
Kafka Backside project -- simple, to develop intuitions about Kafka

## Introduction ##
This is a simple platform, aimed at a much richer project to use  Stream Processing in backside work in the TopicQuests platform.<br/><br/>
The system is *Enviornment-centric* and includes the standard TopicQuests support functions of logging and configuration management with an XML file --config is not used at this time.<br/><br/>
There is included a SimpleChatApp which is used to explore the nature of applications which *live in a Kafka stream*. The app is exercised with a /devtests/FirstTest which simply fires up the framework and sends a test message, which, when received, will be printed out in the console.
## Building and Running ##
This code was built in Eclipse. Dependencies are included.<br/><br/>
To run, first boot the Kafka ensemble. Our approach was to download the Kafka distro, and boot its example system. That gives you a running system. Next, from the IDE, run the FirstTest through the test framework.