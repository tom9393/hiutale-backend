# Hiutale.app Event management system

This project is an Event Management System built using Java and Maven, created during Metropolia University of Applied Sciences Software Development Project 1 course. The program allows users to manage events, including creating, updating, and deleting events, as well as managing user attendance and favorites. This repo is the backend of our project consisting of Spring Boot framework to act as a rest API for our frontend client.

## Features

- User authentication and session management
- Event creation, update, and deletion
- User attendance tracking
- Favorite events management
- Image handling for events

## Technologies Used

- Java
- Maven
- Spring Boot
- Spring Boot security
- Json web token
- MariaDB

## Project Structure

- `src/main/java/com.hiutaleapp/controller`: Controllers for endpoints.
- `src/main/java/com.hiutaleapp/dto`: DTOs for communicating with frontend.
- `src/main/java/com.hiutaleapp/entity`: Database entities.
- `src/main/java/com.hiutaleapp/repository`: JPA repositories for communication with DB.
- `src/main/java/com.hiutaleapp/service`: Services between controllers and repositories, transforming DTO to entities and vice versa.
- `src/main/java/com.hiutaleapp/util`: Different utilities such as JWT auth, exceptions etc.

## Setup and Installation

1. **Clone the repository:**
   ```
   git clone https://github.com/tom9393/hiutale-backend.git
   cd hiutale-backend
   ```

2. **Create .env file in root and create your DB credentials:**
   ```
    MYSQL_USER=USERNAME
    MYSQL_PASSWORD=PASSWORD
    MYSQL_DATABASE=DATABASE
    MYSQL_ROOTPASS=ROOTPASS
   ```

3. **Run the application:**
  ```
    docker compose up -d --build
  ```
Application will be available on port 8080.

## Usage

- **Register:** Users can create accounts.
- **Login:** Users can log in using their credentials.
- **Event Management:** Users can create, update, and delete events.
- **Attendance:** Users can mark their attendance for events.
- **Favorites:** Users can add or remove events from their favorites.
- **Image Handling:** Users can upload and view images for events.
- **Notifications:** Users will get notifications for upcoming events.

## Frontend

Project frontend is in [separate repository](https://github.com/teemvat/hiutale-frontend) by [@teemvat](https://github.com/teemvat).


## Contributors
Our project team was:
- [@teemvat](https://github.com/teemvat)
- [@mirapery](https://github.com/mirapery)
- [@tom9393](https://github.com/tom9393)

  