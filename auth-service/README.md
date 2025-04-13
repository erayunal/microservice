# Auth Service

## Overview

The `auth-service` is responsible for user authentication and authorization, providing functionality such as registration, login, refresh token, and logout. 
## It also includes an auditing mechanism for logging user login/logout activities using Hibernate Envers.

This service handles the following tasks:
- User Registration
- User Login
- Refresh Token Generation
- User Logout
- Logout All Devices

## Features
- Secure user registration and login with JWT tokens.
- Refresh token functionality to maintain active sessions.
- Ability to log out from all devices.
## - Logging of user login/logout actions using Hibernate Envers for auditing.

## Prerequisites

- Java 17 or later
- Spring Boot 3.x
- Spring Data JPA

## - Hibernate Envers for auditing
- PostgreSQL or another relational database

