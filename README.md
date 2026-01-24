# 🐳 Dockerization of Book Store Service

This update introduces containerization to the project using **Docker** and **Docker Compose**. This allows anyone to
run the entire application, including the database, with a single command without needing to install MySQL or Java
locally.

## 🛠 Features Added

* **Dockerfile**: Configuration for building the Spring Boot application image.
* **Docker Compose**: Orchestration for the app and a MySQL database.
* **Environment Configuration**: Secure management of credentials using `.env` files.

---

## 🚀 How to Run with Docker

### 1. Prerequisites

Ensure you have **Docker** and **Docker Compose** installed on your machine.

### 2. Environment Setup

The project uses environment variables for database configuration.

1. Locate the `.env.sample` file in the root directory.
2. Create a copy named `.env`:

```bash
cp .env.sample .env

```

3. Open `.env` and fill in your local credentials (they are ignored by Git for security).

### 3. Launching the Application

Run the following command in the terminal:

```bash
docker-compose up --build

```

This will:

* Build the Spring Boot `.jar` file.
* Create a Docker image for the application.
* Start a **MySQL** container.
* Start the **Book Store** application container and link it to the DB.

---

## ⚙️ Configuration Details

### Environment Variables

To avoid naming conflicts with standard MySQL images, we use custom prefixes:
| Variable | Description |
| :--- | :--- |
| `MYSQLDB_USER` | Database username |
| `MYSQLDB_ROOT_PASSWORD` | Root password for MySQL |
| `MYSQLDB_DATABASE` | Name of the schema to create |
| `MYSQLDB_LOCAL_PORT` | Port exposed on your host machine (default: 3306) |
| `SPRING_LOCAL_PORT` | Port for the Spring Boot app (default: 8080) |

### Database Access

When the containers are running, you can connect to the database via:

* **Host:** `localhost` (if accessing from your machine)
* **Host:** `mysqldb` (if accessing from another container)
* **Port:** Value specified in `MYSQLDB_LOCAL_PORT`

---

## 🔒 Security Note

The `.env` file contains sensitive information and is **excluded from version control** via `.gitignore`. Always use
`.env.sample` or `.env.template` to share the required structure with other contributors.


