# Spring Boot Modular Caching Service with Redis

---

```markdown

## 📌 Use Case
**Spring Boot Modular Caching Service with Storage, Invalidation, and Refresh Operations**

## 📌 Prompt Title  
**Spring Boot Modular Caching Service with Storage, Invalidation, and Refresh Operations**

## 📋 High-Level Description

This project implements a modular, scalable caching service using **Spring Boot** and **Redis**, optimized for high-traffic systems like e-commerce platforms, fintech applications, and content delivery systems. It supports:
- Efficient data caching (minimizing database load)
- Cache invalidation (removing stale entries)
- Cache refresh (ensuring data consistency)
- REST API endpoints for external access
- Rate limiting, validation, and robust error handling
```

---

## 📁 Folder Structure

```plaintext

cache-service/
├── src/
│   ├── main/java/com/example/cache/
│   │   ├── SpringBootCacheApplication.java
│   │   ├── config/
│   │   │   ├── RateLimitConfig.java
│   │   │   └── RedisConfig.java
│   │   ├── controller/
│   │   │   └── CacheController.java
│   │   ├── dto/
│   │   │   └── CacheRequestDTO.java
│   │   ├── exception/
│   │   │   ├── CacheConnectionException.java
│   │   │   ├── CacheKeyNotFoundException.java
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── ratelimit/
│   │   │   └── RateLimitFilter.java
│   │   ├── service/
│   │   │   ├── CacheService.java
│   │   │   ├── CacheRefreshService.java
│   │   │   └── CacheInvalidationService.java
│   │   └── util/
│   │       └── LogUtil.java
│   └── test/java/com/example/cache/
│       ├── SpringBootCacheApplicationTests.java
│       ├── controller/CacheControllerTest.java
│       ├── dto/CacheRequestDTOValidationTest.java
│       ├── service/
│       │   ├── CacheServiceTest.java
│       │   ├── CacheRefreshServiceTest.java
│       │   └── CacheInvalidationServiceTest.java
│       └── util/LogUtilTest.java
```

---

## ⚙️ Features

- **REST APIs** for `GET`, `POST`, `PUT`, `DELETE`, and `CLEAR` cache operations
- **Redis-based** caching
- **Rate limiting** using filters
- **Custom exceptions & global handler**
- **TTL (Time-To-Live)** support
- **Input validation** (null/length)
- **Modular service design** (MVCS)
- **Extensive logging**
- **Unit + Integration testing with 90%+ coverage**

---

## 🔨 How to Run

### 1. Clone and Navigate

```bash
git clone <your-repo-url>
cd cache-service
```

### 2. Redis Setup (Locally)

```bash
brew install redis      # macOS
sudo apt install redis  # Ubuntu
redis-server            # start Redis
redis-cli ping          # test Redis (expect "PONG")
```

### 3. Configure `application.properties`

```properties
spring.cache.type=redis
spring.redis.host=localhost
spring.redis.port=6379
```

### 4. Build and Run the App

```bash
./gradlew clean build
./gradlew bootRun
```

---

## 🚀 API Endpoints

| Method | Endpoint           | Description                      |
|--------|--------------------|----------------------------------|
| GET    | `/cache/{key}`     | Fetch value from cache           |
| POST   | `/cache`           | Add entry to cache               |
| PUT    | `/cache`           | Refresh existing cache entry     |
| DELETE | `/cache/{key}`     | Delete a cache entry             |
| DELETE | `/cache/clear`     | Clear all entries                |

### Example Request

```bash
curl -X POST "http://localhost:8080/cache" \
-H "Content-Type: application/json" \
-d '{"key":"sampleKey", "value":"sampleValue"}'
```

---

## ✅ Unit & Integration Testing

Run tests:
```bash
./gradlew test
```

- Tested components:
    - Services (CRUD logic)
    - Controller (REST APIs + validations)
    - Exception handling
    - Utility methods
    - Rate limiting

---

## 📈 Performance Considerations

| Operation          | Time Complexity |
|-------------------|-----------------|
| Get / Set          | `O(1)`          |
| Invalidate (key)   | `O(1)`          |
| Clear all          | `O(n)`          |

Redis is inherently fast and supports thousands of operations per second.

---

## 📊 Code Coverage Reports

- ✅ 1st Iteration: [Coverage Screenshot](https://drive.google.com/file/d/1IzGuO5mnlXnDNSw1JV6TfV1CYvv7ZyRb/view?usp=drive_link)
- ✅ 2nd Iteration: [Coverage Screenshot](https://drive.google.com/file/d/1P4AxTTKwBcM12m9zRKK8MuYqmG7JZ8gy/view?usp=drive_link)

---

## 📦 Download Code

[👉 Click to Download the Project Source Code](https://drive.google.com/file/d/14AN3SJFyM61_6aBwRYmmNC4iQsjdeUC_/view?usp=drive_link)

---

## 🧠 Conclusion

This Spring Boot caching service provides:
- High-performance data retrieval
- Low database dependency
- Scalable modular design
- Reliable Redis integration
- Production-grade error handling & observability

> A great foundational microservice for caching in distributed applications.

---
```

Let me know if you'd like this saved as a downloadable `.md` file or want a version tailored for GitHub README style.