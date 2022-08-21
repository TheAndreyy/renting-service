# Renting service

> Backend application for managing various objects reservations

## Table of Contents

<!-- TOC -->

* [Features](#features)
* [Api description](#api-description)
* [Test data](#test-data)
* [Technologies used](#technologies-used)
* [Running application](#running-application)

<!-- TOC -->

## Features

Application can:

* create reservation
* modify reservation
* get all user reservation
* get all reservations for object
* make sure reservations don't overlap

## Api description

* Get all reservations for user

```shell
curl --location --request GET'{server_url}/v1/user/{user_id}/reservations'
```

* Get all reservations for object

```shell
curl --location --request GET '{server_url}/v1/object/{object_id}/reservations'
```

* Create reservation

```shell
curl --location --request POST '{server_url}/v1/object/{object_id}/reserve' \
--header 'Content-Type: application/json' \
--data-raw '{
  "userId": 2,
  "start": "2022-08-18T17:03:35.00Z",
  "end": "2022-08-20T17:03:35.00Z",
  "cost": 21.11
}'
```

* Modify reservation

```shell
curl --location --request PATCH '{server_url}/v1/object/{object_id}/reservation/{reservation_id}' \
--header 'Content-Type: application/json' \
--data-raw '{
  "start": "2022-08-18T17:03:35.00Z",
  "end": "2022-08-20T17:03:35.00Z",
  "cost": 21.11
}'
```

start, end, cost fields are optional

## Test data

During start of application there is being created User with id: 1, whose are Objects with id: 1 and 2.
There is also second User with id: 2, whose is Object with id: 3.

For details see method: `RentingServiceApplication.initializeDatabase`

## Technologies used

* Build tool - gradle
* Application - Spring Framework
* ORM - JPA/Hibernate
* Tests - Spock
* Database - HSQLDB

## Running application

`$ ./gradlew bootRun`
