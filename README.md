***
> :warning: **REMARK:**
>
> Dear community members,
>
> Thanks for your interest in **hazelcast-dynacache**! This project has become a Hazelcast Community project.
>
> Hazelcast Inc. gives this project to the developers community in the hope you can benefit from it. It comes without any maintenance guarantee by the original developers but their goodwill (and time!). We encourage you to use this project however you see fit, including any type of contribution in the form of a pull request or an issue.
> 
> Feel free to visit our Slack Community for any help and feedback.
***

# Table of Contents

* [Hazelcast DynaCache](#hazelcast-dynacache)
* [Requirements](#requirements)
* [Creating Development Environment](#creating-development-environment)
* [Installing Hazelcast DynaCache on Liberty](#installing-hazelcast-dynacache-on-liberty)
* [Enabling Hazelcast DynaCache](#enabling-hazelcast-dynacache)


# Hazelcast DynaCache

This repository contains Hazelcast DynaCache feature for Liberty Profile.
In Liberty WAS, you can use dynamic cache engine (which is the default) in order to cache your data.
With this feature, you can use Hazelcast as cache provider.

The Liberty feature uses the IBM Distributed Map API and can run alongside Jakarta EE applications.

# Requirements
    
- Maven
- Java 17 or later
- A Liberty runtime with the Distributed Map and Jakarta Transactions features available
- A Hazelcast 5.7 cluster. The build uses Hazelcast 5.7.0 by default.

# Creating Development Environment

- Clone this GitHub repository: 

    ```
    git clone https://github.com/hazelcast/hazelcast-dynacache.git
    ```

- Run `mvn clean verify` from the repository root to run the tests and create
  `hazelcast-dynacache/target/hazelcast-dynacache-0.3.esa`.

# Installing Hazelcast DynaCache on Liberty

On WebSphere Liberty, run the following from the repository root.

```sh
<liberty dir>/bin/featureManager install hazelcast-dynacache/target/hazelcast-dynacache-0.3.esa
```

For a manual installation, extract the JARs from the ESA into `<liberty user dir>/extension/lib`.
Save `OSGI-INF/SUBSYSTEM.MF` from the ESA as
`<liberty user dir>/extension/lib/features/hazelcast-dynacache.mf`.
The Liberty user directory defaults to `<liberty dir>/usr`.

# Enabling Hazelcast DynaCache

Enable the feature in `server.xml` and select the provider for your distributed map.

 ```
  <featureManager>
    <feature>usr:hazelcast-dynacache</feature>
  </featureManager>
  <distributedMap id="my-cache" jndiName="services/cache/my-cache" cacheProviderName="hazelcast"/>
 ```
Set `-Dhazelcast.client.config=/path/to/hazelcast-client.xml` in the server's `jvm.options`
to configure the cluster name and member addresses.

# Legacy installation on Traditional WebSphere 9.0.0.1

These instructions apply to the earlier Hazelcast 3.11 build of this library.

[Apply the iFix](http://www.ibm.com/support/knowledgecenter/SS7K4U_8.5.5/com.ibm.websphere.installation.nd.doc/ae/tins_install_fixes_dist.html)
 under `twas-ifix` directory to your WebSphere server and put the following files under `WAS_HOME/lib/ext`:

* hazelcast-all-3.11.jar
* hazelcast-dynacache-bundle-0.3.jar

Use `com.hazelcast.ibm.dynacache.CacheProviderImpl` as your cache provider name in your `cacheinstances.properties` file.
