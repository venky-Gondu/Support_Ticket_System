<center>

# <span style="color: #3b82f6">Support Ticket System</span>

### <span style="color: #9A8678">A Full-Stack Learning Project | Spring Boot + React + PostgreSQL + LLM Integration</span>

</center>

---

## <span style="color: #CAAA98">Project Description</span>

The Support Ticket System is a comprehensive full-stack application developed as a learning exercise to understand modern software architecture, API design, and AI integration. This project demonstrates how to build a production-ready ticketing platform where users can submit support requests, track their status, and leverage AI-powered classification to automatically suggest categories and priorities.

Built with a clean separation of concerns, the system features a reactive React frontend, a robust Spring Boot backend with JPA/Hibernate, PostgreSQL for persistent storage, and Groq-powered LLM integration for intelligent ticket classification. The application is fully containerized using Docker Compose for simplified deployment and environment consistency.

This project was developed to explore real-world patterns including transactional management, database-level aggregation, graceful error handling, CORS configuration, reverse proxy setup with Nginx, and environment-based configuration management.

---

## <span style="color: #CAAA98">Requirements</span>

### <span style="color: #9A8678">Runtime Requirements</span>

| Component | Version | Purpose |
| :--- | :--- | :--- |
| Docker Engine | 20.10+ | Container orchestration |
| Docker Compose | V2+ | Multi-container management |
| Node.js | 18+ (local dev only) | Frontend development |
| Java JDK | 17+ (local dev only) | Backend development |
| PostgreSQL | 16+ (via Docker) | Data persistence |

### <span style="color: #9A8678">API Keys</span>

| Service | Purpose | Acquisition |
| :--- | :--- | :--- |
| Groq API | LLM-powered ticket classification | https://console.groq.com/keys |

### <span style="color: #9A8678">System Resources</span>

| Resource | Minimum | Recommended |
| :--- | :--- | :--- |
| Memory | 4 GB | 8 GB |
| CPU | 2 cores | 4 cores |
| Disk Space | 2 GB | 5 GB |
| Network | Internet access for LLM API | Stable connection |

---

## <span style="color: #CAAA98">Project Scope</span>

This project focuses on implementing a complete support ticket workflow with the following boundaries:

<span style="color: #9A8678">Included Features</span>
- Ticket creation with title, description, category, and priority
- AI-powered suggestion of category and priority based on description
- Ticket listing with filtering by category, priority, status, and text search
- Ticket status updates (open, in_progress, resolved, closed)
- Aggregated statistics dashboard with database-level computations
- Responsive, dark-themed frontend with minimalistic design
- Full Docker Compose orchestration for all services
- Environment-based configuration for sensitive values

<span style="color: #9A8678">Out of Scope</span>
- User authentication and authorization
- Email notifications or webhooks
- File attachments or rich text editing
- Multi-tenant or role-based access control
- Advanced analytics or reporting beyond basic aggregations
- Mobile application or PWA support

The project prioritizes architectural clarity and educational value over feature completeness, making it suitable as a reference implementation for learning full-stack development patterns.

---

## <span style="color: #CAAA98">System Architecture</span>

### <span style="color: #9A8678">Component Overview</span>

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   Frontend      │     │   Backend       │     │   Database      │
│   (React + Vite)│────▶│   (Spring Boot) │────▶│   (PostgreSQL)  │
│   Port 80       │     │   Port 8080     │     │   Port 5432     │
└─────────────────┘     └─────────────────┘     └─────────────────┘
         │                       │
         │                       │
         ▼                       ▼
┌─────────────────┐     ┌─────────────────┐
│   Nginx         │     │   LLM Service   │
│   (Reverse Proxy)│     │   (Groq API)    │
│   Static Files  │     │   External API  │
└─────────────────┘     └─────────────────┘
```

### <span style="color: #9A8678">Frontend Architecture</span>

The frontend is built with React using Vite as the build tool. It follows a component-based structure with minimal state management using React hooks.

<span style="color: #9A8678">Key Components</span>
- App.jsx: Main layout and navigation state management
- TicketForm.jsx: Ticket submission form with AI classification on description blur
- TicketList.jsx: Filterable ticket table with inline status updates
- StatsDashboard.jsx: Aggregated statistics visualization with progress indicators

<span style="color: #9A8678">State Management</span>
- Local component state via useState for form inputs and UI state
- Lifted state via callback props for cross-component communication
- No external state library to maintain minimalism and learning focus

<span style="color: #9A8678">API Integration</span>
- Axios for HTTP requests with relative paths for environment flexibility
- Vite proxy configuration for local development to avoid CORS issues
- Graceful error handling with user-friendly messages

### <span style="color: #9A8678">Backend Architecture</span>

The backend follows Spring Boot best practices with layered architecture.

<span style="color: #9A8678">Layer Structure</span>
- Controller Layer: REST endpoint definitions with request/response mapping
- Service Layer: Business logic with transactional boundaries and LLM integration
- Repository Layer: JPA interfaces with JPQL and native queries for aggregation
- Entity Layer: JPA entities with enum-based constrained fields

<span style="color: #9A8678">Key Design Decisions</span>
- Method-level @Transactional for explicit transaction control
- Database-level aggregation via GROUP BY queries for stats endpoint
- Graceful LLM failure handling with null fallbacks to maintain availability
- CORS configuration via Filter bean for flexible origin management

### <span style="color: #9A8678">Database Schema</span>

```sql
CREATE TABLE tickets (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(20) CHECK (category IN ('billing', 'technical', 'account', 'general')),
    priority VARCHAR(20) CHECK (priority IN ('low', 'medium', 'high', 'critical')),
    status VARCHAR(20) CHECK (status IN ('open', 'in_progress', 'resolved', 'closed')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

<span style="color: #9A8678">Indexing Strategy</span>
- Primary key index on id (automatic)
- Consider adding indexes on category, priority, status for filter-heavy workloads
- Full-text search not implemented; LIKE-based search sufficient for learning scope

### <span style="color: #9A8678">System Flow</span>

<span style="color: #9A8678">Ticket Submission Flow</span>
1. User enters ticket details in frontend form
2. On description blur (10+ characters), frontend calls /api/tickets/classify/
3. Backend sends prompt to Groq API with few-shot examples
4. LLM returns suggested category and priority as JSON
5. Frontend pre-fills dropdowns; user may override suggestions
6. User submits form; frontend POSTs to /api/tickets/
7. Backend validates, persists to PostgreSQL, returns created ticket
8. Frontend refreshes ticket list and shows success message

<span style="color: #9A8678">Statistics Aggregation Flow</span>
1. User navigates to Statistics tab
2. Frontend GETs /api/tickets/stats/
3. Backend executes five aggregation queries at database level:
   - COUNT(*) for total tickets
   - COUNT(*) WHERE status = 'open' for open tickets
   - COUNT(*) / date_range for average per day
   - GROUP BY priority for priority breakdown
   - GROUP BY category for category breakdown
4. Results assembled into StatsResponse DTO
5. Frontend renders cards and progress bars

---

## <span style="color: #CAAA98">Setup and Deployment</span>

### <span style="color: #9A8678">Prerequisites</span>

Ensure Docker and Docker Compose are installed and running on your system. Verify with:

```bash
docker --version
docker compose version
```

### <span style="color: #9A8678">Environment Configuration</span>

Create a .env file in the project root with your Groq API key:

```bash
LLM_API_KEY=gsk_your_actual_api_key_here
```

Add .env to .gitignore to prevent accidental commit of sensitive values.

### <span style="color: #9A8678">One-Command Deployment</span>

```bash
# Build and start all services
docker compose up --build

# Access the application
# Frontend: http://localhost:80
# Backend API: http://localhost:80/api/
# Database: localhost:5432 (for external tools)
```

### <span style="color: #9A8678">Development Mode</span>

For local development with hot reload:

```bash
# Terminal 1: Start backend
cd backend
mvn spring-boot:run

# Terminal 2: Start frontend
cd frontend
npm install
npm run dev

# Access frontend at http://localhost:5173
# Vite proxy forwards /api requests to http://localhost:8080
```

### <span style="color: #9A8678">Database Management</span>

```bash
# View database logs
docker compose logs db

# Connect to PostgreSQL directly
docker exec -it support_db psql -U postgres -d support_db

# Reset database (destructive)
docker compose down -v
docker compose up --build
```

### <span style="color: #9A8678">Troubleshooting</span>

| Issue | Solution |
| :--- | :--- |
| Frontend shows 404 for /api endpoints | Verify Vite proxy config or Nginx proxy_pass |
| LLM classification returns null | Check LLM_API_KEY in .env and Groq account status |
| Stats endpoint returns empty | Ensure tickets exist in database with valid enum values |
| Build fails with CustomEvent error | Use Node 20 in Dockerfile, not Node 18 |
| Import errors in Docker build | Check .dockerignore does not exclude src/ or components/ |

---

## <span style="color: #CAAA98">API Endpoints</span>

| Endpoint | Method | Purpose | Request Body | Response |
| :--- | :--- | :--- | :--- | :--- |
| /api/tickets/ | POST | Create a new support ticket | {"title", "description", "category", "priority"} | Created ticket with id and timestamps |
| /api/tickets/ | GET | List tickets with optional filters | Query params: category, priority, status, search | Array of ticket objects |
| /api/tickets/{id} | PATCH | Update ticket status or fields | {"status"} or other updatable fields | Updated ticket object |
| /api/tickets/stats/ | GET | Retrieve aggregated statistics | None | {"total_tickets", "open_tickets", "avg_tickets_per_day", "priority_breakdown", "category_breakdown"} |
| /api/tickets/classify/ | POST | Get AI suggestions for category and priority | {"description"} | {"suggested_category", "suggested_priority"} |

<span style="color: #9A8678">Filter Parameters for GET /api/tickets/</span>
- category: Filter by billing, technical, account, or general
- priority: Filter by low, medium, high, or critical
- status: Filter by open, in_progress, resolved, or closed
- search: Case-insensitive substring match on title or description

All endpoints return JSON with appropriate HTTP status codes. Validation errors return 400 with field-level messages. Server errors return 500 with generic messages to avoid information leakage.

---

## <span style="color: #CAAA98">Key Features</span>

<span style="color: #9A8678">Intelligent Ticket Classification</span>
- Automatic suggestion of category and priority using LLM
- Few-shot prompting for consistent JSON output
- Graceful fallback when AI service is unavailable
- User retains full control to override suggestions

<span style="color: #9A8678">Efficient Data Aggregation</span>
- Statistics computed at database level using GROUP BY and aggregate functions
- No application-level loops over result sets
- PostgreSQL-native date arithmetic for average-per-day calculation
- Responsive dashboard even with large datasets

<span style="color: #9A8678">Production-Ready Architecture</span>
- Full Docker Compose orchestration with health checks
- Nginx reverse proxy for static file serving and API routing
- Environment-based configuration for sensitive values
- CORS properly configured for cross-origin development

<span style="color: #9A8678">User Experience Focus</span>
- Dark theme with carefully selected color palette for reduced eye strain
- Minimalistic interface with clear visual hierarchy
- Loading states and error messages for all async operations
- Responsive layout that adapts to different screen sizes

<span style="color: #9A8678">Code Quality Practices</span>
- Layered architecture with clear separation of concerns
- Transactional boundaries explicitly defined at service layer
- Global exception handler for consistent error responses
- SLF4J logging with configurable levels for observability

---

## <span style="color: #CAAA98">Learning Outcomes</span>

This project served as a comprehensive learning vehicle for modern full-stack development. Key takeaways include:

<span style="color: #9A8678">Backend Development</span>
- Understanding of Spring Boot auto-configuration and component scanning
- Practical experience with JPA repository patterns and query derivation
- Transaction management with explicit @Transactional boundaries
- Designing REST APIs with proper HTTP semantics and status codes
- Integrating external APIs with WebClient and handling transient failures

<span style="color: #9A8678">Frontend Development</span>
- React component composition and props-based communication
- Managing async state with useEffect and error boundaries
- Configuring Vite for development proxy and production build
- CSS architecture with variables and responsive design patterns
- Form handling with validation and user feedback

<span style="color: #9A8678">DevOps and Deployment</span>
- Multi-stage Docker builds for optimized image sizes
- Docker Compose for orchestrating multi-service applications
- Nginx configuration for reverse proxy and static file serving
- Environment variable management for configuration separation
- Health checks and dependency ordering in container startup

<span style="color: #9A8678">AI Integration Patterns</span>
- Prompt engineering with few-shot examples for consistent output
- Parsing and validating LLM responses with fallback strategies
- Balancing AI assistance with user control and transparency
- Handling API rate limits and service unavailability gracefully

<span style="color: #9A8678">Architectural Thinking</span>
- Trade-offs between JPQL portability and native query performance
- When to use database aggregation versus application-level processing
- Designing for graceful degradation when external dependencies fail
- Separating concerns between presentation, business logic, and data access

---

## <span style="color: #CAAA98">Future Enhancements</span>

While this project meets its learning objectives, several extensions could further develop its capabilities:

<span style="color: #9A8678">Short-Term Improvements</span>
- Add pagination to ticket listing for large datasets
- Implement optimistic UI updates for status changes
- Add keyboard navigation and accessibility attributes
- Include unit and integration tests for critical paths

<span style="color: #9A8678">Medium-Term Extensions</span>
- Add user authentication with JWT or OAuth2
- Implement email notifications for status changes
- Support file attachments with cloud storage integration
- Add full-text search with PostgreSQL tsvector

<span style="color: #9A8678">Long-Term Vision</span>
- Multi-tenant support with organization-level isolation
- Advanced analytics with time-series visualizations
- Webhook integrations for external system notifications
- Mobile-responsive PWA with offline capability

---

## <span style="color: #CAAA98">Acknowledgments</span>

This project was developed as a learning exercise. Special thanks to the open-source communities behind Spring Boot, React, Vite, PostgreSQL, and Groq for providing the tools that make modern application development accessible.

The architecture and implementation decisions prioritize educational clarity over production optimization, making this codebase suitable as a reference for learners exploring full-stack development patterns.

---

<center>

<span style="color: #9A8678">Built with intention to learn, designed to inspire.</span>

</center>