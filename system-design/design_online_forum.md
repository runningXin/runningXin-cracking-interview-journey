# Design: Online Forum System

## 1. Clarify Requirements
## 2. Define APIs and Use Cases
## 3. High-Level Architecture
## 4. Database Design (Schema)
## 5. Core Components
## 6. Scalability and Optimizations
## 7. Non-functional Requirements (NFRs)
## 8. Optional Extensions
## 9. Trade-offs
## 10. Summary

## 1. Clarify Requirements

We are designing an Online Forum platform, similar to Reddit, Stack Overflow, or Discourse.

### Core Functional Requirements:

- Users can register and log in
- Users can create discussion threads (posts)
- Users can comment on posts (nested replies)
- Users can upvote/downvote posts and comments
- Posts can be tagged and searched
- Admins can delete posts or ban users

### Non-functional Requirements:

- High read throughput for popular posts
- Supports millions of users and posts
- Moderation tools (optional at MVP)
- Low latency for post and comment loading
- SEO-friendly URLs and fast content rendering

### Out of Scope:

- Real-time chat
- Media hosting (assume integration with CDN if needed)


## 2. Define APIs and Use Cases

### 2.1 Key Use Cases

- Register a new user
- Log in / authenticate
- Create a new post (thread)
- View a post with all comments
- Comment on a post (supporting nested replies)
- Upvote/downvote a post or comment
- Search posts by keywords or tags
- List trending or recent posts

### 2.2 Example RESTful APIs

#### User

```http
POST /api/users/register
POST /api/users/login
GET /api/users/{userId}

## 3. High-Level Architecture

At a high level, the Online Forum consists of:

- Web/Mobile clients
- API Gateway
- Authentication Service
- Post Service (threads)
- Comment Service (nested replies)
- Vote Service
- User Service
- Search/Tag Service
- Notification Service (optional)
- Moderation/Admin Service (optional)
- Database(s) and Caching layers

### 3.1 Component Diagram

### 3.2 Notes

- The system is microservice-friendly; services can be scaled independently.
- Most read-heavy traffic (viewing threads, comments, votes) should go through caching.
- Writes go through API → Auth → corresponding service → DB → invalidate cache.
- Search engine (e.g. Elasticsearch) indexes posts for full-text + tag-based search.
- Moderation or notification systems can be event-driven using Kafka or SQS.

## 4. Database Design (Schema)

The forum is primarily **read‑heavy**:  
- Many users browse threads and comments
- Fewer users create posts/comments

Therefore, we use **a relational DB (e.g. PostgreSQL)** for strong consistency,  
and **Redis + Elasticsearch** to improve performance & search.

### 4.1 Core Entities

- **User**
- **Post (Thread)**
- **Comment**
- **Vote**
- **Tag / PostTag (many‑to‑many)**

---

### 4.2 Schema

#### `User`
| Field        | Type      | Notes |
|--------------|----------|-------|
| id (PK)      | bigint   | |
| username     | varchar  | unique |
| email        | varchar  | unique |
| passwordHash | varchar  | |
| createdAt    | timestamp | |

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(120) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);

## 5. Core Components

The system is composed of modular, stateless services. Each component is responsible for one domain, making the system easier to scale and extend.

---

### 5.1 Authentication Service

- Handles user registration and login
- Issues JWT tokens for stateless authentication
- Validates token on each request

---

### 5.2 User Service

- Stores user profiles
- Tracks user reputation / karma
- Admin flags: isModerator, isBanned, etc.
- Public profile APIs

---

### 5.3 Post Service

- Create, update, delete posts
- List posts (by tag, user, trending, etc.)
- Retrieves posts from DB or Redis
- Publishes `PostCreated` and `PostUpdated` events to Kafka

---

### 5.4 Comment Service

- Add, update, delete comments
- Supports nested replies
- Fetches comments by post ID (paginated)
- May flatten nested trees using materialized views or recursive queries
- Publishes `CommentCreated` events

---

### 5.5 Vote Service

- Handles upvote/downvote on posts and comments
- Ensures each user can vote once per entity (idempotency)
- Updates score and triggers cache invalidation
- Can batch update post/comment score periodically for performance

---

### 5.6 Tag & Search Service

- Tags are stored in DB and indexed
- For full-text search, use **Elasticsearch**
- Can support autocomplete and fuzzy search
- Keeps ES index in sync via Kafka event consumers

---

### 5.7 Feed Service (optional)

- Aggregates posts into personalized feeds (by tag, user follows, popularity)
- Sorts by score, freshness, trending
- May use Redis sorted sets for fast retrieval

---

### 5.8 Moderation Service (optional)

- Flags inappropriate posts/comments
- Handles abuse reports
- Supports manual moderation dashboard
- Uses basic ML/heuristics for automated flagging

---

### 5.9 Notification Service (optional)

- Sends notifications for replies, mentions, upvotes
- Async, via Kafka queue + worker
- Channels: Web UI, email, mobile push

---

### 5.10 Caching Layer (Redis)

- Caches:
  - Recent posts
  - Popular posts (trending)
  - Comments under a post
  - Post metadata (score, comment count)
- TTL and eviction policies per use case

---

### 5.11 Message Queue (Kafka)

- Used for async event-driven processing:
  - PostCreated → index in Elasticsearch
  - CommentCreated → notify thread author
  - VoteCreated → update post score
- Enables service decoupling and async workflows

---

### 5.12 API Gateway

- Central entry point for all client requests
- Handles authentication, rate limiting, logging
- Routes requests to appropriate services

---

✅ Each component can be scaled independently and communicates via REST or gRPC.  
Writes go through DB; reads go through cache or DB depending on freshness requirements.

## 6. Scalability & Optimizations

This system is read-heavy. We optimize for:

- Low-latency content delivery
- Efficient ranking (trending, hot)
- Search and tag filtering
- Write decoupling via event-driven architecture

---

### 6.1 Caching Strategies (Redis)

- **Post Cache:** `post:{id}` → full post details (body, score, author)
- **Comment Cache:** `comments:{postId}` → recent comments list
- **Feed Cache:** `feed:trending`, `feed:recent`
- **User Profile Cache:** `user:{userId}`

**Eviction strategy:**
- Use LRU + TTL
- Invalidate on update (e.g., vote, edit)

---

### 6.2 Read/Write Path Separation

- **Writes** go through DB
- **Reads** prefer cache → fallback to DB
- DB replica setup:
  - **Primary** for writes
  - **Read replicas** for scale-out read capacity

---

### 6.3 Sharding and Partitioning

- Shard large tables:
  - `posts` by postId or createdAt range
  - `comments` by postId
- Horizontal partitioning allows linear scale-out
- Redis: use consistent hashing (e.g., via `Twemproxy` or Redis Cluster)

---

### 6.4 Asynchronous Processing

- Offload heavy tasks to Kafka + worker:
  - Post indexing in Elasticsearch
  - Notification delivery
  - Post score recalculation
  - Abuse detection pipeline

Benefits:
- Fast user response
- Better resource isolation
- Retries & dead-letter queues supported

---

### 6.5 Rate Limiting and Abuse Prevention

- Per-IP and per-user rate limits (e.g., 60 requests/min)
- Prevent spam via CAPTCHA, email confirmation
- Monitor failed login attempts and suspicious behavior

---

### 6.6 Hot Post Optimization

- Use Redis Sorted Sets to track top posts:
  - Key: `trending_posts_by_tag`
  - Score: time-decayed upvote count
- Periodically refresh via background job
- Keep top N in-memory for fastest access

---

### 6.7 Full-text Search Optimization

- Index `posts.title` and `posts.body` in **Elasticsearch**
- Support:
  - Tokenization, stemming
  - Tag + keyword filter
  - Autocomplete suggestions
- Keep ES in sync via Kafka + Change Data Capture (CDC)

---

### 6.8 CDN + Static Content

- Use CDN (Cloudflare, Akamai) for:
  - Post page rendering
  - Static assets (avatars, images)
- Improves global latency and reduces backend load

---

### 6.9 Observability & Monitoring

- Metrics: request latency, cache hit rate, error rate, Kafka lag
- Tools: Prometheus, Grafana, ELK/EFK stack, OpenTelemetry
- Alerts: 5xx spike, traffic anomaly, queue backlog

## 7. Non-Functional Requirements (NFRs)

| Category         | Goal |
|------------------|------|
| **Availability** | ≥ 99.9% uptime — tolerate instance or zone failure |
| **Latency**      | Reads: < 100ms for post and comment loading<br>Writes: < 500ms for create/update |
| **Scalability**  | Horizontal scaling for each service via Kubernetes / Auto-scaling groups |
| **Durability**   | All writes persisted to PostgreSQL or event log (Kafka) |
| **Consistency**  | Strong consistency for core write operations (post, comment, vote)<br>Eventual consistency for derived data (score, feeds, search) |
| **Maintainability** | Microservices are independently deployable; Configurable via CI/CD |
| **Security**     | JWT-based auth, input validation, XSS & CSRF protection, rate limiting |
| **Monitoring**   | Logs (ELK), Metrics (Prometheus), Traces (OpenTelemetry) |


## 8. Optional Extensions

### 8.1 Rich Text & Media Support
- Support Markdown or rich-text editor (e.g. Tiptap, Quill)
- Upload images or embed videos (integrate with S3 + CDN)

### 8.2 User Notifications
- Notify users on:
  - Reply to their comment
  - Mention via @username
  - Upvotes (optional)
- Delivery channels: in-app, email, push
- Async via Kafka + Notification Service

### 8.3 Reputation & Gamification
- Stack Overflow–like karma system
- Badge achievements
- Leaderboards by tag or time period

### 8.4 Moderation & Reporting
- Manual flagging of abusive content
- Auto-flag based on upvote/downvote ratio or content heuristics
- Moderator dashboard and action logs

### 8.5 Bookmarking & Save for Later
- Let users bookmark posts/comments
- Create reading lists or tags

### 8.6 Custom Feeds & Follows
- Follow tags or users
- Build personalized feeds
- Recommend posts based on interest graph

### 8.7 AI-assisted Features (Advanced)
- Summarize long threads or arguments
- Detect low-quality/spam posts
- Recommend replies or auto-moderation suggestions