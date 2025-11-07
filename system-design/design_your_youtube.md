
# 📺 **System Design: YouTube Architecture**

## 1. Clarify Requirements

We are designing a YouTube-like video-sharing platform with billions of videos and users.

### 1.1 Functional Requirements

- Users can register and log in
- Users can upload videos
- Videos are processed (encoded/compressed)
- Users can stream videos (on-demand)
- Users can search for videos
- Users can like/dislike videos
- Users can comment on videos
- Users get notified of new uploads from subscribed channels
- System recommends videos to users

### 1.2 Non-Functional Requirements

- High availability (99.9%+ uptime)
- Low latency for video playback and page loads
- Scalable to billions of users and videos
- Durable storage (videos must not be lost)
- Global content delivery with minimal lag
- Cost-effective architecture

### 1.3 Out of Scope

- Live streaming
- Ad monetization
- Recommendation algorithm training
- Copyright management

## 2. Use Cases & API Sketch

### 2.1 Core Use Cases

- Upload video
- Stream video
- Search videos
- Like/dislike a video
- Comment on a video
- View homepage/recommended videos
- Subscribe to a channel
- Get notified of new uploads

### 2.2 Sample APIs

```http
POST   /api/videos/upload
GET    /api/videos/{videoId}/stream
GET    /api/videos/search?q=cat
POST   /api/videos/{videoId}/like
POST   /api/videos/{videoId}/comment
GET    /api/users/{userId}/subscriptions
POST   /api/subscribe/{channelId}
```

## 3. High-Level Architecture

```
Client (Web/Mobile)
     |
API Gateway
     |
+--------------------------+
|   Microservices Layer    |
+--------------------------+
| Auth Service             |
| User/Channel Service     |
| Video Upload Service     |
| Video Processing Service |
| Video Metadata Service   |
| Comment Service          |
| Notification Service     |
| Recommendation Service   |
| Search Service           |
+--------------------------+
     |
+--------------------------------------+
|          Data Storage Layer          |
+--------------------------------------+
| PostgreSQL (users, metadata, comments) |
| Redis (sessions, cache)              |
| S3/Blob Storage (video files)        |
| Elasticsearch (full-text search)     |
| Kafka/SQS (async video pipeline)     |
| CDN (Cloudflare/Akamai)              |
+--------------------------------------+
```

## 4. Database Design (Schema)

### 4.1 User Table

```sql
users(id, username, email, password_hash, created_at)
```

### 4.2 Video Metadata

```sql
videos(
    id, user_id, title, description, 
    upload_time, duration, status, 
    video_url, thumbnail_url, tags[], 
    view_count, like_count
)
```

### 4.3 Comments

```sql
comments(
    id, video_id, user_id, parent_id, 
    text, created_at, likes
)
```

### 4.4 Subscriptions

```sql
subscriptions(
    follower_id, channel_id, created_at
)
```

### 4.5 Likes

```sql
video_likes(user_id, video_id, value)
comment_likes(user_id, comment_id, value)
```

## 5. Video Processing & Storage

### 5.1 Upload Flow

1. Client uploads video file to API
2. Video file stored temporarily in blob storage (e.g., S3)
3. Kafka event sent to `video-processing-service`
4. Transcoding into multiple formats (480p, 720p, 1080p)
5. Thumbnails generated
6. CDN is updated with the final video files
7. Metadata is stored in DB
8. User notified that video is published

### 5.2 CDN Integration

- Final video segments are stored in S3 and served via CDN (e.g., Cloudflare, Akamai)
- Videos are chunked and delivered via HLS/DASH

## 6. Core Components

### 6.1 Video Upload Service

- Handles file upload and pre-validation
- Stores video file in object storage
- Sends message to Kafka queue

### 6.2 Video Processing Service

- Listens to Kafka events
- Transcodes videos into multiple resolutions
- Extracts thumbnail
- Pushes processed video to CDN
- Marks status as "ready" in metadata

### 6.3 Video Metadata Service

- Manages video information: title, description, tags
- Returns list of videos (for feed/search)

### 6.4 Search Service

- Indexes videos by title, tags, user, etc.
- Supports full-text search (e.g., Elasticsearch)

### 6.5 Comment Service

- Threaded comments, pagination
- Like/dislike comment support
- Cache hot comment threads in Redis

### 6.6 Notification Service

- Listens to video-published events
- Notifies subscribers via:
    - In-app bell
    - Email (optional)
    - Push notification (if mobile)

### 6.7 Recommendation Service

- Suggests related or trending videos
- Initially rule-based (e.g., similar tags, trending)
- Future: ML-based personalized recommendations

## 7. Scalability & Performance

### 7.1 Video Storage

- Store raw and processed files in S3
- Use unique key per video (e.g., UUID-based)

### 7.2 CDN

- Push final video and thumbnail to CDN
- Use HLS/DASH to stream video efficiently

### 7.3 Search

- Use Elasticsearch to support autocomplete, fuzzy match, etc.
- Indexes kept in sync via Kafka (from metadata service)

### 7.4 Caching

- Redis for:
    - User profile cache
    - Video metadata cache
    - Popular videos per tag
    - Comments cache (per video)

### 7.5 Load Balancing & Auto-scaling

- All services behind load balancer (ALB/NLB)
- Scale horizontally per service
- Use Kubernetes or ECS for deployment

### 7.6 Rate Limiting & Abuse Prevention

- Prevent spam uploads, comment abuse
- CAPTCHA or email verification for new users
- Limit uploads per hour

## 8. Trade-offs

| Trade-off | Decision |
|-----------|----------|
| **Latency vs Cost** | Use CDN and multi-resolution streaming, which costs more but ensures fast playback |
| **Eventual vs Strong Consistency** | Video views, like counts can be eventually consistent |
| **Relational DB vs NoSQL** | Use Postgres for strong relations and indexing, Redis/ES for fast access |
| **Monolith vs Microservices** | Microservices preferred for independent scaling of upload, search, comments |

## 9. Summary

We designed a scalable video-sharing platform similar to YouTube that supports:

- Video upload and transcoding pipeline
- CDN-backed streaming
- Video metadata, search, comments
- Notification system for subscribers
- Caching, sharding, and async processing
- Modular services that can scale independently

The system prioritizes durability, performance, and extensibility — making it capable of supporting billions of videos and users.



[Hello System Design Playbook -- Design Youtube](https://www.hellointerview.com/learn/system-design/problem-breakdowns/youtube)

