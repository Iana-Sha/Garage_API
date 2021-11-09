#!/usr/bin/env bash

mkdir microservices
cd microservices

spring init \
--boot-version=2.3.2.RELEASE \
--build=gradle \
--java-version=1.8 \
--packaging=jar \
--name=car-service \
--package-name=com.shapovalova.microservices.core.car \
--groupId=com.shapovalova.microservices.core.car \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
car-service

spring init \
--boot-version=2.3.2.RELEASE \
--build=gradle \
--java-version=1.8 \
--packaging=jar \
--name=appointment-service \
--package-name=com.shapovalova.microservices.core.appointment \
--groupId=com.shapovalova.microservices.core.appointment \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
appointment-service

spring init \
--boot-version=2.3.2.RELEASE \
--build=gradle \
--java-version=1.8 \
--packaging=jar \
--name=user-service \
--package-name=com.shapovalova.microservices.core.user \
--groupId=com.shapovalova.microservices.core.user \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
user-service

spring init \
--boot-version=2.3.2.RELEASE \
--build=gradle \
--java-version=1.8 \
--packaging=jar \
--name=user-composite-service \
--package-name=com.shapovalova.microservices.composite.user \
--groupId=com.shapovalova.microservices.composite.user \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
user-composite-service

cd ..
