this article is in progress

# atinject
##realtime, scalable, cdi enhanced, 3-tier architecture

traditional architecture, web server + app server + database server.
each component must scale independently.
how to scale ? a box per component which often leads to inefficient distribution of resources

traditional architecture is suitable for stateless application where with limited interaction between connected clients.
this is definitely not the case for realtime application where interactions between connected clients is the goal.
atinject address this issue.

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

atinject backend is built on 4 key components :
* [weld](http://seamframework.org/Weld) provides the cdi backbone
* [netty](https://www.netty.io) provides the http and websockets transport between client and server
* [infinispan](http://www.jboss.org/infinispan) provides the distributed executor and map reduce framework, distributed and replicated cache and finally data storage abstraction
* [jackson](https://github.com/FasterXML/jackson-core) provides the serialization framework used between client and server and the support for data versioning

atinject frontend is build on 2 key components :
* [requirejs](http://www.requirejs.org)
* [createjs](http://www.createjs.com)

##contributors

### mathieu lachance
video game industry grown up, worked 4 years as lead r&d backend for [frimastudio](http://www.frimastudio.com)
along with a small intercourse at [funcom](http://www.funcom.com) as java developper.
since now a year i have been working at [myca health inc.](http://www.myca.com) as a software engineer.
feel free to contact me, [matlach](http://ca.linkedin.com/in/lachancemathieu/)

### pierre-alain chabot
TODO

##installation

atinject is built with [maven](http://maven.apache.org) and require jdk7 to build and run. the easiest way to checkout, build and start using atinject is to follow the following steps:

1. download [eclipse juno](http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/junosr1)
2. install [maven integration for eclipse (m2e)](http://marketplace.eclipse.org/content/maven-integration-eclipse) plugin from eclipse marketplace
3. install git scm connector from m2e marketplace
4. checkout maven project from scm

TODO, null analysis
@NonNull
@Nullable
@NonNullByDefault

TODO, running core 
* open IntegrationBootstrap run as... java application
* mvn ... run server.sh / bat

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

* CDI Provider : see [CDI Provider](#cdi-provider)
* Transaction : see [@Transactional, Transaction Manager and Transaction Services](#transactional-transaction-manager-and-transaction-services)

![Weld SE enhancements](http://yuml.me/ec44f248 "Weld SE enhancements")

### CDI Provider
```java
CDI.getBeanManager()
```

### Logger

inject an [slf4j](http://www.slf4j.org) logger interface based on the injection point bean class backed by the [logback](http://logback.qos.ch) implementation.
by default, logging level can be changed at runtime any time through it is mbean located under ```ch.qos.logback.classic```.

![Logger](http://yuml.me/36c406d7 "Logger")

usage:
```java
@Inject Logger logger;
```
note : logback can be replaced by any slf4j compatible implementation by changing pom.xml.

### Asynchronous Service
Wrap java ThreadPoolExecutor and ThreadFactory that will be used to perform all asynchronous operation.
![Asynchronous Service](http://yuml.me/d8ac2fd9 "Asynchronous Service")

### @Asynchronous and Asynchronous Service
Provides a way to execute asynchronously a method that either returns ```void``` or ```Future<?>```.
Asynchronous execution of the method is ensured by the AsynchronousService.
usage:
```java
@Asynchronous public void performAsynchronously(...){...}
@Asynchronous public Future<?> performAsynchronously(...){...}

@Asynchronous
public class PerformAllAsynchronously{...}
```
### Scheduled Service
Wrap a java ScheduledThreadPoolExecutor and ThreadFactory that will be used to perform all delayed operation.
![Scheduled Service](http://yuml.me/d61b82f1 "Scheduled Service")

### @Retry and Scheduled Service
Provides a way to re-execute a method which execution has failed using exponential backoff algorithm.
By default, a method that has failed will try to re-executor 3 times with a backoff of 100ms.
Execution and / or re-execution of the method is ensured by the ScheduledService.
Retry count and backoff are configurable.
http://en.wikipedia.org/wiki/Exponential_backoff

usage:
```java
@Retry public Object performWithRetry(...){...}

@Retry(count=..., timeout=...) public Object performWithCustomRetry(...){...}

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

#### Transaction algorithms

get:
```java
// 1. with key, get value from cache
// 2. return value
```

get and create:
```java
// 1. with key, get value from cache
// 2. if value is not null, return value
// 3. value is null, lock key
// 4. with key, get value from cache, again
// 5. if value is not null, return value
// 6. value is still null, create value
// 7. put newly created value in cache
// 8. return value
```

get and update:
```java
// 1. lock key
// 2. with key, get value from get and create algorithm
// 3. update value
// 4. put updated value in cache
// 5. optionally return updated value
```

get and remove:
```java
// 1. lock key
// 2. with key, get value from get algorithm
// 3. if value is not null, remove value from cache
// 4. optionally return removed value
```

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
Entities versioning in a relational database context is easy to manage.
When something has to evolve, change the entity, run a sql migration script, done.
Entities versioning in a no sql database is not that trivial but can be easily accomplish with some work.
Storing entity as json make is easy to evolve.

Serialization is plain simple:

1. write the current version number
2. serialize object to json.

Deserialization :

1. read version number
2. if version number equals the current one, deserialize json.
3. if not, for each version, read json, and update object as required by the new version.

see also [@SerializeWith][].

[@SerializeWith]: /

![Entity, Versionable Entity and Serialization](http://yuml.me/dc91ade3 "Entity, Versionable Entity and Serialization")
![Entity, Versionable Entity and Serialization](http://yuml.me/31dfb262 "Entity, Versionable Entity and Serialization")
![Entity, Versionable Entity and Serialization](http://yuml.me/cac95d6b "Entity, Versionable Entity and Serialization")


```java
@ApplicationScoped
public class VersionableUserExternalizer implements VersionableEntityExternalizer<UserEntity> {
    
    private static final int CURRENT_VERSION = 1;
    
    @Inject
    private VersionableEntityObjectMapper objectMapper;
    
    @Override
    public void writeObject(ObjectOutput output, UserEntity user) throws IOException {
        objectMapper.writeObject(output, user, CURRENT_VERSION);
    }

    @Override
    public UserEntity readObject(ObjectInput input) throws IOException, ClassNotFoundException {
        int version = objectMapper.readVersion(input);
        byte[] json = objectMapper.readJSONBytes(input);
        
        switch (version) {
            // case ANY_OLD_VERSION :
            // read json as tree
            // then reconstruct object as needed ex:
            // JsonNode node = JSon.readTree(json);
            // UserEntity user = new UserEntity();
            // user.setName(node.get("name"));
            case CURRENT_VERSION:
                // just leave everything to jackson
                return objectMapper.readValue(json, UserEntity.class);
        }
        
        throw new IOException("bad version '" + version + "'");
    }

}

```

### Tiers
WebSocketService is responsible for managing incoming WebSocketRequest and
outgoing WebSocketResponse and WebSocketNotification. All business logic should be handled to the Service.

Service is responsible of the business logic execution and coordinate the usage of the CacheStore.

CacheStore is responsible to providing all access to the ClusteredCache.

![Tiers](http://yuml.me/870ee2f1 "Tiers")

Factory is responsible to create any new entity or dto.

Adapter is responsible of the conversion of any entity to dto and vice-versa.

Each tiers possesses many predefined inheritable interceptors.

### Clustered Cache Manager
Provides a wrapper around infinispan [EmbeddedCacheManager][].
It also defines the infinispan [GlobalConfiguration][] which is used especially to configure the cache transport.
Cache transport defines :
* machineId: identify the server uniquely across the cluster.
* rackId: identify the server rack physically, ex: "blade-01".
* siteId: identify the server site physically, ex: "bunker-01".

[EmbeddedCacheManager]: http://docs.jboss.org/infinispan/5.2/apidocs/org/infinispan/manager/EmbeddedCacheManager.html
[GlobalConfiguration]: http://docs.jboss.org/infinispan/5.2/apidocs/org/infinispan/configuration/global/GlobalConfiguration.html

### Topology Services
Provide a wrapper around infinispan [TopologyAwareAddress][]
TopologyService is also responsible to map a given ```machineId``` to it is url.

[TopologyAwareAddress]: http://docs.jboss.org/infinispan/5.2/apidocs/org/infinispan/remoting/transport/TopologyAwareAddress.html

### Session Services
A [Session][] represents an individual connected via web socket (i.e. after web socket handshake has been completed).
[Session][] contains the ```channelId``` and ```machineId``` which identify physically where it is bound.
After a successful login, the [Session][] will be updated with the given ```userId```.
The [Session][] is designed to be replicated across all servers and not be persisted by any means i.e. in-memory only.

[Session]: /atinject-core/src/main/java/org/atinject/api/session/Session.java

### Rendezvous Services
> Rendezvous (French: rendez-vous), to visit, to meet, tryst, less exclusive than tête-à-tête.

> http://en.wikipedia.org/wiki/Rendezvous

Map a given ```rendezvousId``` to a specific TopologyAwareAddress.
A ```rendezvousId``` must be generated to be affine with a given ```machineId```.
A [RendezvousPoint][] represents is a group of [Session][].
A [RendezvousPoint][] is designed to be distributed but without any copy i.e. number of owner equals 1.
As it is [Session][] counterpart, [RendezvousPoint][] should not also be persisted by any means.

[RendezVousPoint]: /atinject-core/ 

### Notification Services
Send a [WebSocketNotification][] to a given [Session][] or all [Session][] included in a given [RendezvousPoint][].

[WebSocketNotification]: /atinject-core/src/main/java/org/atinject/core/websocket/dto/WebSocketNotification.java

### User Topology Services
Map a given ```userId``` to a given TopologyAwareAddress.

### Password Digester
When registering, client will provide it is password as clear text (over ssl).
For subsequent login, client will provide it is hashed password (over ssl or not).
[PasswordDigester][] provides a way to hash a password using a pre-defined algorithm making the application less vunerable to attack.
[SimplePasswordDigester][] use SHA-1 algorithm to hash password as it is defacto provided by the JVM.

note : to provide stronger encryption, [PasswordDigester][] implementation can be replaced by defining an @Alternative class.

[PasswordDigester]: /
[SimplePasswordDigester]: /

### User and User Credential Services
A [User][] represent an individual identified by it is [UserCredential][].
[UserCredential][] contains the ```username``` and ```password```.
A [User][] is flagged as a ```guest``` as long as it does not have registered.
After registration a [User][] is flagged as ```registered```.
![User and User Credential Services](http://yuml.me/8e5dcc0f "User and User Credential Services")

[User]: /atinject-core/src/main/java/org/atinject/api/user/entity/UserEntity.java
[UserCredential]: /atinject-core/src/main/java/org/atinject/api/usercredential/entity/UserCredentialEntity.java

### Authentication, Registration and User Services
Authentication is the process of identifing a [User][] with it is [UserCredential][] while the
Registration is the process of creating a [User][] and it is [UserCredential][].

Registration is performed in two phases :
* [User][] register as a ```guest``` which generate a [User][] with random ```username``` and ```password``` [UserCredential][].
* a [User][] flagged as a ```guest``` become flagged as ```registered``` when it provides it is own ```username``` and ```password``` [UserCredential][].

![Authentication, Registration and User](http://yuml.me/ecfcb3fd "Authentication, Registration and User")

### Authorization, User, Role and Permission Services
![Authorization, User, Role and Permission Services](http://yuml.me/14648e63 "Authorization, User, Role and Permission Services")

TODO based on apache shiro design

> Permissions are the most atomic level of a security policy and they are statements of functionality. Permissions represent what can be done in your application. A well formed permission describes a resource types and what actions are possible when you interact with those resources. Can you open a door? Can you read a file? Can you delete a customer record? Can you push a button?
> Common actions for data-related resources are create, read, update, and delete, commonly referred to as CRUD.
> It is important to understand that permissions do not have knowledge of who can perform the actions-- they are just statements of what actions can be performed.

> In the context of Authorization, Roles are effectively a collection of permissions used to simplify the management of permissions and users. So users can be assigned roles instead of being assigned permissions directly, which can get complicated with larger user bases and more complex applications. So, for example, a bank application might have an administrator role or a bank teller role.
> An explicit role has permissions explicitly assigned to it and therefore is an explicit collection of permissions. Permission checks in code are a reflection of an explicit role. You can view patient data because because you have the view patient data permission as part of your administrator role. You can create an account because you have the create account permission as part of your bank teller role. You can perform these actions, not because of some implicit role name based on a string but because the corresponding permission was explicitly assigned to your role.
> The big benefits of explicit roles are easier manageability and lower maintenance of your application. If you ever need to add, remove, or change a role, you can do so without touching your source code

> http://shiro.apache.org/java-authorization-guide.html


![Shiro design](http://yuml.me/d2037488 "Shiro design")

### @RequiresUser, @RequiresGuest, @RequiresRoles, RequiresPermissions and Authorization Service

### User Preference Service
Associate a given ```userId``` and ```preferenceId``` any [UserPreference][].
A [SimpleUserPreference][] provides the default implementation for [UserPreference][].
All [UserPreference][] are grouped by an [UserPreferences][].

[UserPreferences]: /
[UserPreference]: /
[SimpleUserPreference]: /

## Quality

1. install sonar http://www.sonarsource.org
2. run ```mvn clean install``` and followed by ```mvn sonar:sonar``` to perform analysis
3. finally see results at http://localhost:9000
