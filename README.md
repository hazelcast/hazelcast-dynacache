# Table of Contents

* [Hazelcast DynaCache Plugin](#hazelcast-dynacache-plugin)
* [Requirements](#requirements)
* [Creating Development Environment](#creating-development-environment)
* [Installing Hazelcast Dynacache Plugin to Liberty](#installing-hazelcast-dynacache-plugin-to-liberty)
* [Enabling Hazelcast Dynacache Plugin](#enabling-hazelcast-dynacache-plugin)
* [Advanced Configuration](#advanced-configuration)


# Hazelcast DynaCache Plugin
This repository contains Hazelcast Dynacache feature for Liberty Profile.
In Liberty WAS you can use dynamic cache engine ( which is default ) in order to cache your data.
But also with this feature, you can use Hazelcast as cache provider.


# Requirements
    
    
- Download and extract [Liberty 16.0.0.3](https://public.dhe.ibm.com/ibmdl/export/pub/software/websphere/wasdev/downloads/wlp/16.0.0.3/wlp-javaee7-16.0.0.3.zip)
- Download and install Eclipse
- Install [IBM WebSphere Application Server Liberty Developer Tools](https://marketplace.eclipse.org/content/ibm-websphere-application-server-liberty-developer-tools) to your Eclipse
    

# Creating Development Environment

- clone this github repo `git clone https://github.com/bilalyasar/hazelcast-liberty.git`
- Open Eclipse create new empty workspace
- In Eclipse File -> Open Projects from File System and select this repo directory.
- After that select `hazelcast-dynacache` and `hazelcast-dynacache-plugin` directories as to be imported.
- Now you need to change project facets. 
  - Right click hazelcast-dynacache-plugin -> Properties -> Project Facets -> Liberty Feature -> Apply
  - Right Click hazelcast-dynacache -> Properties -> Project Facets -> Java & Osgi Bundle  -> Apply
- You neeed to add following directories/jars to Target Platforms
    - hazelcast and hazelcast-client jar files
    - ${liberty_folder}/lib
    - ${liberty_folder}/dev/spi/ibm
    - ${liberty_folder}/dev/spi/spec
    
- In order to try the feature in Liberty, you need to [add Liberty Server to Eclipse.](https://developer.ibm.com/bluemix/2016/05/24/liberty-and-eclipse-installing-dev-environment-p9/)
- You can install feature by Right Click hazelcast-dynacache-plugin -> Install Feature
- You can update feature by Right Click hazelcast-dynacache-plugin -> Update Feature



# Installing Hazelcast Dynacache Plugin to Liberty

In the repository there is `hazelcast-dynacache-plugin_XXXX.esa` file. This is exported feature file.
You can install the hazelcast feature by using ${liberty_folder}/bin/installUtility

`installUtility install hazelcast-dynacache-plugin_XXXX.esa`


# Enabling Hazelcast Dynacache Plugin
 In your server.xml, enable hazelcast feature by adding following snippet.
 ```
      <featureManager>
 		<feature>hazelcast-dynacache-plugin</feature>
 	  </featureManager>
 ```

# Advanced Configurations

