# **FlatSearching:**

A web application developed based on microservices architecture, providing convenient functionality for working with apartment data. Here is a brief description of its main components:

**[Apartment Data Collection Microservice:](https://github.com/DmitrySkachkov10/FlatSearching/tree/master/parser-service)**

This microservice automatically parses information about apartments from popular resources.
Operates in a multi-threaded environment for efficient data collection and updates.

**[Logging Microservice:](#)**

This service is responsible for logging actions in the application.
Administrators have access to this service for monitoring and analysis.
It can generate Excel files with data for administrators.

**[User Service Microservice:](#)**

This is where user registration and management of their accounts take place.
Implements the mechanism of authentication and user role management.
Administrators have privileged access to all services.

**[Apartment Search Microservice:](#)**

Users can use this service to search for apartments with convenient filters.
Provides the ability to add apartments to bookmarks for convenient access.

**[Email Verification Code Sending Microservice:](#)**

Responsible for sending verification codes to email addresses during user registration.

**[Docker Container with PostgreSQL, pgAdmin, and Other Microservices:](#)**

Used for deploying and managing the PostgreSQL database and other services in a containerized environment.

These components together provide a full-featured web application for working with apartment data, with a user-friendly interface, data protection, and efficient user role management.
