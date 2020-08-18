#!/bin/bash
mvn package -Dmaven.test.skip=true
mvn exec:java
