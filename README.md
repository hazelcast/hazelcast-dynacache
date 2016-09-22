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

# Requirements
    
- Maven
- Download and extract [Liberty 16.0.0.3](https://public.dhe.ibm.com/ibmdl/export/pub/software/websphere/wasdev/downloads/wlp/16.0.0.3/wlp-javaee7-16.0.0.3.zip)

# Creating Development Environment

- Clone `bundle` branch of the modified version of Hazelcast library:
 
    ```
    git clone -b bundle https://github.com/emre-aydin/hazelcast.git
    ```

- Install the modified version of Hazelcast into local Maven repository. Execute the following in `hazelcast` 
project root directory:

    ```
    mvn clean install -DskipTests=true
    ```

- Clone this GitHub repository: 

    ```
    git clone https://github.com/hazelcast/hazelcast-dynacache.git
    ```

- Execute `mvn clean package` on `hazelcast-dynacache` root directory to create the `.esa` file

# Installing Hazelcast DynaCache to Liberty

Install the `.esa` file by executing the following command:

```
<liberty dir>/bin/featureManager install hazelcast-dynacache/hazelcast-dynacache/target/hazelcast-dynacache-0.1.esa
```    

# Enabling Hazelcast DynaCache

Enable Hazelcast DynaCache by adding the following snippet to your `server.xml` file:

 ```
  <featureManager>
    <feature>usr:hazelcast-dynacache</feature>
  </featureManager>
 ```
