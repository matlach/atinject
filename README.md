this article is in progress

# atinject
##realtime, scalable, cdi enhanced, 3-tier architecture

traditional architecture, apache + app server + database.
each component must scale independently.
how to scale ? a box per component which often leads to inefficient distribution of resources

while this approach might still work for stateless application this is definitely not going for realtime / stateful application. 

the atinject project feature an integrated pipeline which can deliver static content, run business logic and store data in a distributed fashion.
just scale when more cpu, ram or storage is needed.
affinity is the key in distributed architecture ; it require smart server but more importantly smart client

features (backend) :
* cdi extensible framework, observers and interceptors
* http static content delivery server
* stateless http client / server
* stateful http websocket client / server
* fast, in-memory, distributed transaction
* distributed event
* map reduce operation
* distributed / replicated cache
* storage abstraction
* entity versioning

features (frontend) :
* javascript dto prototype generation
* polymorphic dto support
* javascript web socket service prototype generation

atinject is built on 4 key components :
* [weld](http://seamframework.org/Weld) provides the cdi backbone
* [netty](https://netty.io) provides the http and websockets transport between client and server
* [infinispan](http://www.jboss.org/infinispan) provides the distributed executor and map reduce framework, distributed and replicated cache and finally data storage abstraction
* [jackson](https://github.com/FasterXML/jackson-core) provides the serialization framework used between client and server and the support for data versioning

##contributors
=======
video game industry grown up, worked 4 years as lead r&d backend for [frimastudio](http://www.frimastudio.com)
along with a small intercourse at [funcom](http://www.funcom.com) as java developper.
since now a year i have been working at [myca health inc.](http://www.myca.com) as a software engineer.
feel free to contact me, [matlach](http://ca.linkedin.com/in/lachancemathieu/)

##installation

atinject is built with [maven](http://maven.apache.org) and require jdk7 to build and run. the easiest way to checkout, build and start using atinject is to follow the following steps:

1. download [eclipse juno](http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/junosr1)
2. install [maven integration for eclipse (m2e)](http://marketplace.eclipse.org/content/maven-integration-eclipse) plugin from eclipse marketplace
3. install git scm connector from m2e marketplace
4. checkout maven project from scm

## licence

Copyright 2013 the atinject project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

## Asynchronous Service
![alt text](http://yuml.me/d8ac2fd9 "Asynchronous Service")

## Distributed Event Service 
![alt text](http://yuml.me/ad529290 "Distributed Event Service")

## Tiers
![alt text](http://yuml.me/870ee2f1 "Tiers")

## Authentication, Registration and User
![alt text](http://yuml.me/ecfcb3fd "Authentication, Registration and User")
