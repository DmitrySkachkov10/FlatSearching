# **FlatSearching:**

A web application developed based on microservices architecture, providing convenient functionality for working with apartment data. Here is a brief description of its main components:

**[Apartment Data Collection Microservice:](https://github.com/DmitrySkachkov10/FlatSearching/tree/master/parser-service)**

This microservice automatically parses information about apartments from popular resources.
Operates in a multi-threaded environment for efficient data collection and updates.

**[Logging Microservice:](https://github.com/DmitrySkachkov10/FlatSearching/tree/master/audit-service)**

This service is responsible for logging actions in the application.
Administrators have access to this service for monitoring and analysis.
It can generate Excel files with data for administrators.

**[User Service Microservice:](https://github.com/DmitrySkachkov10/FlatSearching/tree/master/user-service)**

This is where user registration and management of their accounts take place.
Implements the mechanism of authentication and user role management.
Administrators have privileged access to all services.

**[Apartment Search Microservice:](https://github.com/DmitrySkachkov10/FlatSearching/tree/master/flat-service)**

Users can use this service to search for apartments with convenient filters.
Provides the ability to add apartments to bookmarks for convenient access.

**[Email Verification Code Sending Microservice:](https://github.com/DmitrySkachkov10/FlatSearching/tree/master/mailing-service)**

Responsible for sending verification codes to email addresses during user registration.

**[Docker Compose:](https://github.com/DmitrySkachkov10/FlatSearching/blob/master/docker-compose.yml)**

Used for deploying and managing the PostgreSQL database, PgAdmin and other services in a containerized environment.

*Currently, an Nginx proxy server is also being added to route requests
