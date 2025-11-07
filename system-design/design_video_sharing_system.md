> 💬 **Original Interview Prompt:**  
> Design a system that allows users to share, view, and comment on videos.  
> Functional Requirements:  
> • Users should be able to register.  
> • Users should be able to upload videos.  
> • Users should be able to search, view, comment, and like videos.


# 🎥 System Design: Video Sharing Platform (MVP)

This document outlines a simplified version of a video-sharing system — like a basic version of YouTube — suitable for system design interviews. This version focuses on the **Minimum Viable Product (MVP)** features: upload, search, view, comment, and like videos.

---

## 1. Clarify Requirements

### 1.1 Functional Requirements

- Users can register and log in
- Users can upload videos
- Users can search videos (by title/tags)
- Users can view (stream) videos
- Users can like/dislike videos
- Users can comment on videos

### 1.2 Non-Functional Requirements

- High availability
- Smooth video playback
- Scalable storage for video files
- Low latency for streaming popular videos

---

## 2. Use Cases & API Sketch

### 2.1 Core Use Cases

- Register/Login
- Upload video
- View video
- Search videos
- Like/Dislike a video
- Comment on a video

### 2.2 Sample REST APIs

```http
POST   /api/users/register
POST   /api/users/login

POST   /api/videos/upload
GET    /api/videos/{videoId}
GET    /api/videos/search?q=keyword

POST   /api/videos/{videoId}/like
POST   /api/videos/{videoId}/comment
GET    /api/videos/{videoId}/comments
```

---

## 3. High-Level Architecture

```
Client (Web/Mobile)
   |
API Gateway
   |
+-------------------------+
|  Core Services          |
+-------------------------+
| Auth Service            |
| User Service            |
| Video Upload Service    |
| Video Metadata Service  |
| Comment Service         |
| Like/Vote Service       |
+-------------------------+
   |
+-------------------------------+
|     Storage and Infra Layer   |
+-------------------------------+
| Relational DB (PostgreSQL)    |
| Redis Cache                   |
| S3 or Blob Storage (videos)   |
| CDN for Playback (optional)   |
+-------------------------------+
```

---

## 4. Database Schema

### 4.1 Users

```sql
users(
  id SERIAL PRIMARY KEY,
  username VARCHAR UNIQUE,
  email VARCHAR UNIQUE,
  password_hash VARCHAR,
  created_at TIMESTAMP
)
```

### 4.2 Videos

```sql
videos(
  id SERIAL PRIMARY KEY,
  user_id INT REFERENCES users(id),
  title TEXT,
  description TEXT,
  video_url TEXT,
  thumbnail_url TEXT,
  created_at TIMESTAMP
)
```

### 4.3 Comments

```sql
comments(
  id SERIAL PRIMARY KEY,
  video_id INT REFERENCES videos(id),
  user_id INT REFERENCES users(id),
  content TEXT,
  created_at TIMESTAMP
)
```

### 4.4 Likes

```sql
video_likes(
  user_id INT,
  video_id INT,
  value SMALLINT CHECK (value IN (1, -1)),
  PRIMARY KEY(user_id, video_id)
)
```

---

## 5. Video Upload & Playback Flow

1. Client uploads video via API
2. Backend stores video in S3/blob storage
3. Video metadata stored in DB
4. Playback URLs are returned (could be public S3 or via CDN)
5. Comments and likes are fetched from DB

---

## 6. Optimizations (Optional)

- **Redis** for hot video metadata, comments
- **CDN** (e.g., Cloudflare) for serving video content globally
- **Search indexing** with PostgreSQL full-text search or Elasticsearch
- Rate limiting for upload/comment APIs
- Pagination on comments and search

---

## 7. Summary

This system provides a minimal but complete foundation for a video-sharing platform:

- Simple relational schema (User, Video, Comment, Like)
- Upload and stream videos using S3 or similar storage
- RESTful APIs for core operations
- Easily extensible into a full-scale system (with CDN, recommendation, notification, etc.)

This design is suitable for small-scale or MVP-level deployment and can be gradually evolved into a large-scale platform like YouTube.
