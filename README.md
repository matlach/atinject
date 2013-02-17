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

features :
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

atinject is built on 4 key components :
[weld](http://seamframework.org/Weld) provides the cdi backbone
[netty](https://netty.io) provides the http and websockets transport between client and server
[infinispan](http://www.jboss.org/infinispan) provides the distributed executor and map reduce framework, distributed and replicated cache and finally data storage abstraction
[jackson](https://github.com/FasterXML/jackson-core) provides the serialization framework used between client and server and the support for data versioning

##contributors
=======
video game industry grown up, worked 4 years as lead r&d backend for [frimastudio](http://www.frimastudio.com)
now working at [myca health inc.](http://www.myca.com) as a software engineer.
feel free to contact me, [matlach](http://ca.linkedin.com/in/lachancemathieu/)

##installation

### clone git

TODO

### eclipse

TODO juno

### maven

TODO m2eclipse plugin

TODO mvn clean install

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
