<center>

# <span style="color: #3b82f6">Support Ticket System</span>

### <span style="color: #9A8678">Intelligent Ticket Management Platform | Spring Boot + React + PostgreSQL + AI Classification</span>

</center>

---

## <span style="color: #CAAA98">Project Description</span>

The Support Ticket System is a production-ready application for managing customer support requests. Users can submit tickets with descriptions, receive AI-powered suggestions for categorization and priority, track ticket status through resolution, and view aggregated analytics on support workload.

The platform integrates large language model capabilities to automatically analyze ticket descriptions and recommend appropriate categories and priorities, reducing manual triage effort while maintaining user control over final classifications.

Built with a modern technology stack and fully containerized deployment, the system is designed for reliability, scalability, and ease of maintenance.

---

## <span style="color: #CAAA98">Requirements</span>

### <span style="color: #9A8678">Infrastructure</span>

| Component | Version | Purpose |
| :--- | :--- | :--- |
| Docker Engine | 20.10 or higher | Container runtime |
| Docker Compose | V2 or higher | Service orchestration |
| System Memory | 4 GB minimum | Container execution |
| Network Access | Outbound HTTPS | LLM API communication |

### <span style="color: #9A8678">Configuration</span>

| Variable | Description | Source |
| :--- | :--- | :--- |
| LLM_API_KEY | API key for AI classification service | Groq Console |
| POSTGRES_PASSWORD | Database authentication credential | Generated or custom |

---

## <span style="color: #CAAA98">Project Scope</span>

### <span style="color: #9A8678">Core Capabilities</span>

- Create and manage support tickets with structured metadata
- AI-assisted classification of ticket category and priority
- Filter and search tickets by multiple criteria
- Update ticket status through resolution workflow
- View aggregated statistics on ticket volume and distribution
- Responsive web interface optimized for desktop and tablet

### <span style="color: #9A8678">Integration Boundaries</span>

- External LLM service for text classification (Groq API)
- PostgreSQL database for persistent storage
- Nginx reverse proxy for request routing and static asset delivery

---

## <span style="color: #CAAA98">System Architecture</span>

### <span style="color: #9A8678">High-Level Component Diagram</span>

```
                    ┌─────────────────────────────┐
                    │         Client Browser       │
                    │    (React Single Page App)   │
                    └────────────┬────────────────┘
                                 │ HTTPS
                                 ▼
                    ┌─────────────────────────────┐
                    │         Nginx Proxy          │
                    │  ┌─────────────────────┐    │
                    │  │ Static Assets: /    │    │
                    │  │ API Proxy: /api/*   │    │
                    │  └─────────────────────┘    │
                    └────────────┬────────────────┘
                                 │
            ┌────────────────────┴────────────────────┐
            │                                         │
            ▼                                         ▼
┌─────────────────────┐             ┌─────────────────────────┐
│   Spring Boot App   │             │   PostgreSQL Database   │
│   (Port 8080)       │◄───────────►│   (Port 5432)           │
│                     │   JDBC      │                         │
│  • REST API Layer   │             │  • Tickets Table        │
│  • Business Logic   │             │  • Constraints & Indexes│
│  • LLM Integration  │             │  • Aggregation Queries  │
└────────┬────────────┘             └─────────────────────────┘
         │ HTTPS
         ▼
┌─────────────────────────┐
│   Groq LLM Service      │
│   (External API)        │
│                         │
│  • Text Classification  │
│  • JSON Response Format │
└─────────────────────────┘
```

### <span style="color: #9A8678">Request Flow: Ticket Submission</span>

```
User Action                          System Components
────────────                         ─────────────────

1. Enter ticket description    ──►  Frontend (React)
                                      │
2. Description blur (10+ chars) ──►  POST /api/tickets/classify/
                                      │
                                   Spring Boot Controller
                                      │
                                   LLM Service Layer
                                      │
                                   HTTPS Request
                                      ▼
                                Groq API (External)
                                      │
                                   JSON Response
                                      │
                                   Frontend receives suggestions
                                      │
3. User reviews/adjusts values  ──►  Form pre-filled with AI suggestions
                                      │
4. Submit ticket form           ──►  POST /api/tickets/
                                      │
                                   Spring Boot Controller
                                      │
                                   Validation Layer
                                      │
                                   Service Layer (@Transactional)
                                      │
                                   Repository Layer (JPA)
                                      ▼
                                PostgreSQL (INSERT)
                                      │
                                   Response with created ticket
                                      │
                                   Frontend updates ticket list
```

### <span style="color: #9A8678">Request Flow: Statistics Aggregation</span>

```
User navigates to Statistics
              │
              ▼
      GET /api/tickets/stats/
              │
              ▼
    Spring Boot Controller
              │
              ▼
    Stats Service Layer
              │
    ┌────────┴────────┬────────┬────────┐
    ▼                 ▼        ▼        ▼
 COUNT(*)      COUNT(*)   COUNT/    GROUP BY
 total         WHERE     date_range  priority
 tickets       status    for avg     category
               = 'open'  per day
    │                 │        │        │
    └────────┬────────┴────────┴────────┘
             ▼
    PostgreSQL executes all queries
             │
             ▼
    Results assembled into response DTO
             │
             ▼
    Frontend renders dashboard cards
```

### <span style="color: #9A8678">Data Flow Summary</span>

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│   User Input │────▶│  Application │────▶│  Persistence │
│   (Browser)  │     │   (Backend)  │     │  (Database)  │
└──────────────┘     └──────┬───────┘     └──────────────┘
                            │
                            ▼
                   ┌─────────────────┐
                   │  External APIs  │
                   │  (LLM Service)  │
                   └─────────────────┘
```

---

## <span style="color: #CAAA98">Setup and Deployment</span>

### <span style="color: #9A8678">Quick Start</span>

```bash
# 1. Clone or download the project
cd support-ticket-system

# 2. Create environment configuration
echo "LLM_API_KEY=gsk_your_key_here" > .env

# 3. Start all services
docker compose up --build

# 4. Access the application
# Web Interface: http://localhost:80
# API Documentation: http://localhost:8080/actuator (if enabled)
```

### <span style="color: #9A8678">Environment Configuration</span>

Create a .env file in the project root:

```bash
# LLM Service Configuration
LLM_API_KEY=gsk_your_actual_api_key

# Optional: Override database credentials (defaults provided in compose file)
POSTGRES_PASSWORD=your_secure_password
```

The .env file should not be committed to version control. Add it to .gitignore.

### <span style="color: #9A8678">Service Endpoints</span>

| Service | Internal Port | External Port | Access URL |
| :--- | :--- | :--- | :--- |
| Frontend (Nginx) | 80 | 80 | http://localhost:80 |
| Backend API | 8080 | 8080 | http://localhost:8080 |
| Database | 5432 | 5432 | localhost:5432 (external tools) |

### <span style="color: #9A8678">Development Workflow</span>

For local development with hot reload:

```bash
# Terminal 1: Start backend
cd backend
mvn spring-boot:run

# Terminal 2: Start frontend
cd frontend
npm install
npm run dev

# Access at http://localhost:5173
# API requests are proxied to http://localhost:8080 via Vite config
```

### <span style="color: #9A8678">Database Management</span>

```bash
# View database logs
docker compose logs db

# Connect to PostgreSQL CLI
docker exec -it support_db psql -U postgres -d support_db

# Reset all data (destructive)
docker compose down -v
docker compose up --build
```

### <span style="color: #9A8678">Troubleshooting</span>

| Symptom | Likely Cause | Resolution |
| :--- | :--- | :--- |
| Frontend cannot reach API | Proxy misconfiguration | Verify nginx.conf proxy_pass or Vite proxy settings |
| LLM suggestions return null | Invalid or missing API key | Confirm LLM_API_KEY in .env matches Groq console |
| Statistics show zero values | No tickets in database | Create test tickets via API or frontend |
| Container fails to start | Port conflict or resource limits | Check docker compose logs for specific error |
| Build fails with import errors | Case-sensitive path mismatch | Ensure file names match import statements exactly |

---

## <span style="color: #CAAA98">API Reference</span>

### <span style="color: #9A8678">Ticket Management</span>

| Endpoint | Method | Description | Request Parameters | Response |
| :--- | :--- | :--- | :--- | :--- |
| /api/tickets/ | POST | Create a new ticket | {"title", "description", "category", "priority"} | 201 Created with ticket object |
| /api/tickets/ | GET | List tickets with filters | Query: category, priority, status, search | 200 OK with array of tickets |
| /api/tickets/{id} | PATCH | Update ticket fields | {"status"} or other updatable fields | 200 OK with updated ticket |

### <span style="color: #9A8678">Analytics and Classification</span>

| Endpoint | Method | Description | Request Parameters | Response |
| :--- | :--- | :--- | :--- | :--- |
| /api/tickets/stats/ | GET | Retrieve aggregated metrics | None | 200 OK with statistics object |
| /api/tickets/classify/ | POST | Get AI suggestions for new ticket | {"description"} | 200 OK with suggested category and priority |

### <span style="color: #9A8678">Filter Parameters</span>

The GET /api/tickets/ endpoint supports these query parameters:

| Parameter | Values | Behavior |
| :--- | :--- | :--- |
| category | billing, technical, account, general | Filter tickets by category |
| priority | low, medium, high, critical | Filter tickets by priority |
| status | open, in_progress, resolved, closed | Filter tickets by status |
| search | Any string | Case-insensitive match on title or description |

Multiple filters can be combined. Omitted parameters are treated as unrestricted.

---

## <span style="color: #CAAA98">Key Features</span>

<span style="color: #9A8678">AI-Powered Triage Assistance</span>
- Automatic analysis of ticket descriptions using large language models
- Suggested category and priority displayed before submission
- Users retain full control to accept or modify AI recommendations
- Graceful fallback when AI service is unavailable

<span style="color: #9A8678">Efficient Data Aggregation</span>
- Statistics computed directly in the database using aggregate functions
- No application-level iteration over result sets
- Optimized queries for responsive dashboard performance
- Accurate average-per-day calculation using date arithmetic

<span style="color: #9A8678">Production-Ready Deployment</span>
- Fully containerized with Docker Compose for consistent environments
- Nginx reverse proxy for static asset delivery and API routing
- Environment-based configuration for sensitive credentials
- Health checks and dependency ordering for reliable startup

<span style="color: #9A8678">User-Centered Interface</span>
- Dark theme with carefully selected color palette for readability
- Minimalistic layout focused on task completion
- Clear visual feedback for loading states and errors
- Responsive design adapting to various screen sizes

---

## <span style="color: #CAAA98">Operational Considerations</span>

<span style="color: #9A8678">Security</span>
- API keys and credentials managed via environment variables
- CORS configured to allow only expected origins
- Input validation at API boundary to prevent injection
- No sensitive data logged in application output

<span style="color: #9A8678">Reliability</span>
- Database health checks ensure backend starts only after PostgreSQL is ready
- LLM service failures do not block ticket submission
- Graceful error messages guide users when operations fail
- Transactional boundaries ensure data consistency

<span style="color: #9A8678">Maintainability</span>
- Clear separation between frontend, backend, and infrastructure concerns
- Configuration externalized for environment-specific customization
- Modular service structure simplifies future extensions
- Standardized logging facilitates operational monitoring

---

## <span style="color: #CAAA98">Support and Contributions</span>

This project is maintained as a reference implementation for modern full-stack development patterns. For issues or enhancements, please review the existing documentation before submitting requests.

When extending this system, maintain the established architectural boundaries:
- Keep frontend and backend as independently deployable units
- Preserve environment-based configuration for sensitive values
- Ensure new features include appropriate error handling and user feedback
- Update documentation to reflect any changes to API contracts or deployment procedures

---

<center>

<span style="color: #9A8678">Support Ticket System | Designed for clarity, built for reliability</span>

</center>