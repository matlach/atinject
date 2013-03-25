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
* [netty](https://www.netty.io) provides the http and websockets transport between client and server
* [infinispan](http://www.jboss.org/infinispan) provides the distributed executor and map reduce framework, distributed and replicated cache and finally data storage abstraction
* [jackson](https://github.com/FasterXML/jackson-core) provides the serialization framework used between client and server and the support for data versioning

##contributors

### mathieu lachance
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

## Documentation

### Weld SE enhancements

* Transaction : see [@Transactional, Transaction Manager and Transaction Services](#transactional-transaction-manager-and-transaction-services)
* Security
* Validation

![Weld SE enhancements](http://yuml.me/3e673ac4 "Weld SE enhancements")

### Logger

inject an [slf4j](http://www.slf4j.org) logger interface based on the injection point bean class backed by the [logback](http://logback.qos.ch) implementation.

![Logger](http://yuml.me/36c406d7 "Logger")

usage:
```java
@Inject Logger logger;
```
note : logback can be replaced by any slf4j compatible implementation. 

### Asynchronous Service
![Asynchronous Service](http://yuml.me/d8ac2fd9 "Asynchronous Service")

### @Asynchronous and Asynchronous Service
TODO
usage:
```java
@Asynchronous public void performAsynchronously(...){...}
@Asynchronous public Future<?> performAsynchronously(...){...}

@Asynchronous
public class PerformAllAsynchronously{...}
```
### Scheduled Service
![Scheduled Service](http://yuml.me/d61b82f1 "Scheduled Service")

### @Retry and Scheduled Service
TODO
usage:
```java
@Retry public Object performWithRetry(...){...}

@Retry
public class PerformAllWithRetry{...}
```

### @Profile and Profiling Service
TODO

### Distributed Event and Distributed Event Service 
![Distributed Event Service](http://yuml.me/ad529290 "Distributed Event Service")

usage:
```java
@Inject @Distributed Event<MyEvent> myEvent;

myEvent.fire(new MyEvent());

public void onMyEvent(@Observes MyEvent myEvent){...}
```

### @Transactional, Transaction Manager and Transaction Services
![@Transactional, Transaction Manager and Transaction Services](http://yuml.me/ac71653f "@Transactional, Transaction Manager and Transaction Services")

usage:
```java
@Transactional
public class PerformAllWithinTransaction{...}

@Transactional public performWithinTransaction(...){...}
```
note : WebSocketService and Services are @Transactional

### @ValidateRequest, @ValidateMethod and Validation Service

running :
```xml
<beans>
    <class>org.atinject.validation.ValidateRequestInterceptor</class>
</beans>
```

junit :
```xml
<beans>
    <class>org.atinject.validation.ValidateMethodInterceptor</class>
</beans>
```

### DTO and [Polymorphic] Serialization
![DTO and Polymorphic Serialization](http://yuml.me/51685dbc "DTO and Polymorphic Serialization")
![DTO and Polymorphic Serialization](http://yuml.me/92190f13 "DTO and Polymorphic Serialization")

### Entity, Versionable Entity and Serialization
![Entity, Versionable Entity and Serialization](http://yuml.me/dc91ade3 "Entity, Versionable Entity and Serialization")
![Entity, Versionable Entity and Serialization](http://yuml.me/31dfb262 "Entity, Versionable Entity and Serialization")
![Entity, Versionable Entity and Serialization](http://yuml.me/cac95d6b "Entity, Versionable Entity and Serialization")

### Tiers
![Tiers](http://yuml.me/870ee2f1 "Tiers")

### User and User Credential Services
![User and User Credential Services](http://yuml.me/8e5dcc0f "User and User Credential Services")

### Authentication, Registration and User Services
![Authentication, Registration and User](http://yuml.me/ecfcb3fd "Authentication, Registration and User")

### Authorization, User, Role and Permission Services
![Authorization, User, Role and Permission Services](http://yuml.me/14648e63 "Authorization, User, Role and Permission Services")

### @RequiresUser, @RequiresGuest, @RequiresRoles, RequiresPermissions and Authorization Service

## Quality

1. install sonar http://www.sonarsource.org
2. run analysis
```
mvn clean install
```
```
mvn sonar:sonar
```
3. see results at http://localhost:9000
