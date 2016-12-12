#!/usr/bin/env bash

if [ ${LIBERTY_HOME} ]
then
	echo "LIBERTY_HOME found at $LIBERTY_HOME"
else
	echo "Set LIBERTY_HOME environment variable."
	exit 1
fi

${LIBERTY_HOME}/bin/featureManager uninstall hazelcast-dynacache --noPrompts
${LIBERTY_HOME}/bin/featureManager install hazelcast-dynacache/target/hazelcast-dynacache-0.2.esa
