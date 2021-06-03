---
**REMARK**

Dear community members,

Thanks for your interest in **hazelcast-dynacache**! As of June 2021, this project has become a Hazelcast Community project.

Hazelcast Inc. gives this project to the developers community in the hope you can benefit from it. It comes without any maintenance guarantee by the original developers but their goodwill (and time!). We encourage you to use this project however you see fit, including any type of contribution in the form of a pull request or an issue. 

Feel free to visit our Slack Community for any help and feedback.

---

# Table of Contents

* [Hazelcast DynaCache](#hazelcast-dynacache)
* [Requirements](#requirements)
* [Creating Development Environment](#creating-development-environment)
* [Installing Hazelcast DynaCache to Liberty](#installing-hazelcast-dynacache-to-liberty)
* [Enabling Hazelcast DynaCache](#enabling-hazelcast-dynacache)


# Hazelcast DynaCache

This repository contains Hazelcast DynaCache feature for Liberty Profile.
In Liberty WAS, you can use dynamic cache engine (which is the default) in order to cache your data.
With this feature, you can use Hazelcast as cache provider.

Tested with Liberty 16.0.0.3.

# Requirements
    
- Maven
- Download and extract [Liberty 16.0.0.3](https://public.dhe.ibm.com/ibmdl/export/pub/software/websphere/wasdev/downloads/wlp/16.0.0.3/wlp-javaee7-16.0.0.3.zip)

# Creating Development Environment

- Clone this GitHub repository: 

    ```
    git clone https://github.com/hazelcast/hazelcast-dynacache.git
    ```

- Execute `mvn clean package` on `hazelcast-dynacache` root directory to create the `.esa` file

# Installing Hazelcast DynaCache to WebSphere Liberty

Install the `.esa` file by executing the following command:

```
<liberty dir>/bin/featureManager install hazelcast-dynacache/hazelcast-dynacache/target/hazelcast-dynacache-0.3.esa
```    

Use `hazelcast` as your cache provider name in your `cacheinstances.properties` file.

# Enabling Hazelcast DynaCache on WebSphere Liberty

Enable Hazelcast DynaCache by adding the following snippet to your `server.xml` file:

 ```
  <featureManager>
    <feature>usr:hazelcast-dynacache</feature>
  </featureManager>
 ```
 
 

# Installing Hazelcast DynaCache to Traditional WebSphere 9.0.0.1

[Apply the iFix](http://www.ibm.com/support/knowledgecenter/SS7K4U_8.5.5/com.ibm.websphere.installation.nd.doc/ae/tins_install_fixes_dist.html)
 under `twas-ifix` directory to your WebSphere server and put the following files under `WAS_HOME/lib/ext`:

* hazelcast-all-3.11.jar
* hazelcast-dynacache-bundle-0.3.jar

Use `com.hazelcast.ibm.dynacache.CacheProviderImpl` as your cache provider name in your `cacheinstances.properties` file.
