java -jar atinject.jar org.atinject.Boostrap
-server
-XX:+AggressiveOpts
-Xms1g
-Xmx1g
-XX:NewRatio=1
-XX:+UseG1GC
-XX:+DisableExplicitGC
-Duser.timezone=GMT
-Dinfinispan.unsafe.allow_jdk8_chm=true