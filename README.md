# 🧠 AI Analytics Platform - Spring Boot MVP

A professional platform for creating and deploying machine learning models based on Spring Boot and a custom ML engine.

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Docker & Docker Compose
- Git

### Local Development

```bash
# Clone the repository
git clone https://github.com/your-username/ai-analytics-platform.git
cd ai-analytics-platform

# Run in development mode
./gradlew bootRun

# Or via Make
make run
```

### Docker Deployment

```bash
# Quick start
chmod +x scripts/startup.sh
./scripts/startup.sh

# Or manually
docker-compose up -d
```

## 📋 Features

### ✨ Main Capabilities

#### 🔧 Platform
- **REST API** with OpenAPI/Swagger documentation
- **Modern web interface** built with React
- **Relational DB** using JPA/Hibernate
- **Caching** for high performance
- **Monitoring** via Spring Boot Actuator

#### 🤖 Machine Learning
- **Custom ML engine** written in pure Kotlin
- **Neural networks** with configurable architecture
- **Training and prediction** via REST API
- **Model quality metrics**

#### 📊 Data Management
- **Dataset upload** (CSV/JSON)
- **Data validation** with detailed errors
- **Dataset versioning**
- **Usage analytics**

## 🏗️ Architecture

```
┌─────────────────────────────────────────────┐
│                Frontend                     │
│          React + Modern UI                  │
└─────────────────┬───────────────────────────┘
                  │ HTTP/REST
┌─────────────────▼───────────────────────────┐
│              Spring Boot                    │
│  ┌───────────┐  ┌─────────────────────┐   │
│  │ Controllers │  │     Services        │   │
│  └─────────────┘  └─────────────────────┘   │
│  ┌─────────────┐  ┌─────────────────────┐   │
│  │ Validation  │  │   ML Engine         │   │
│  └─────────────┘  └─────────────────────┘   │
└─────────────────┬───────────────────────────┘
                  │ JPA/Hibernate
┌─────────────────▼───────────────────────────┐
│            PostgreSQL/H2                    │
│        Datasets + Models + Metrics          │
└─────────────────────────────────────────────┘
```

## 📡 API Reference

### Base URL: `/api`

#### Datasets
```http
POST   /datasets              # Create dataset
GET    /datasets              # List datasets (with pagination)
GET    /datasets/{id}         # Get dataset
GET    /datasets/{id}/data    # Get data
DELETE /datasets/{id}         # Delete dataset
```

#### Models
```http
POST   /models/train          # Train model
POST   /models/train-async    # Train asynchronously
POST   /models/predict        # Prediction
GET    /models                # List models
GET    /models/{id}           # Model info
DELETE /models/{id}           # Delete model
```

#### Analytics
```http
GET    /analytics/dashboard   # Metrics dashboard
GET    /health                # Health check
```

### Request Examples

#### Create Dataset
```bash
curl -X POST http://localhost:8080/api/datasets \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sales Analytics",
    "description": "Sales data for Q4 2024",
    "data": [
      {"features": [1000, 5, 1], "label": 1},
      {"features": [500, 2, 0], "label": 0}
    ],
    "featureNames": ["revenue", "employees", "quarter"]
  }'
```

#### Train Model
```bash
curl -X POST http://localhost:8080/api/models/train \
  -H "Content-Type: application/json" \
  -d '{
    "datasetId": "your-dataset-id",
    "config": {
      "hiddenLayers": [16, 8, 4],
      "learningRate": 0.001,
      "epochs": 5000
    },
    "testSplit": 0.2
  }'
```

#### Prediction
```bash
curl -X POST http://localhost:8080/api/models/predict \
  -H "Content-Type: application/json" \
  -d '{
    "modelId": "your-model-id",
    "features": [750, 3, 1]
  }'
```

## 🔧 Configuration

### Spring Boot Profiles

#### Development (`dev`)
```yaml
spring:
  profiles:
    active: dev
  datasource:
    url: jdbc:h2:mem:devdb
  jpa:
    show-sql: true
server:
  port: 8080
```

#### Production (`prod`)
```yaml
spring:
  profiles:
    active: prod
  datasource:
    url: ${DATABASE_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
server:
  port: ${PORT:8080}
```

### Environment Variables

```bash
# Database
DATABASE_URL=jdbc:postgresql://localhost:5432/aianalytics
DB_USERNAME=postgres
DB_PASSWORD=your_password

# Application
SPRING_PROFILES_ACTIVE=prod
PORT=8080

# Optional
REDIS_URL=redis://localhost:6379
LOG_LEVEL=INFO
```

## 🧪 Testing

### Run Tests
```bash
# All tests
./gradlew test

# Only unit tests
./gradlew test --tests "*UnitTest"

# Integration tests
./gradlew test --tests "*IntegrationTest"
```

### API Testing
```bash
# Full system test
chmod +x scripts/test-complete.sh
./scripts/test-complete.sh

# Simple check
make test-api
```

## 📊 Monitoring and Metrics

### Available Endpoints
- **Health**: `/actuator/health`
- **Metrics**: `/actuator/metrics`
- **Info**: `/actuator/info`
- **Prometheus**: `/actuator/prometheus`

### Swagger UI
- **API Documentation**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

### Logging
```bash
# View logs in Docker
docker-compose logs -f ai-analytics

# Application logs
tail -f logs/ai-analytics.log
```

## 🚀 Production Deployment

### Docker Compose (recommended)
```bash
# Create production configuration
cp docker-compose.yml docker-compose.prod.yml

# Edit variables
vim docker-compose.prod.yml

# Deploy
docker-compose -f docker-compose.prod.yml up -d
```

### Cloud Platforms

#### Heroku
```bash
# Install Heroku CLI
heroku create ai-analytics-platform

# Set variables
heroku config:set SPRING_PROFILES_ACTIVE=prod
heroku config:set DATABASE_URL=your_postgres_url

# Deploy
git push heroku main
```

#### AWS ECS
```bash
# Build image
docker build -t ai-analytics .

# Tag for ECR
docker tag ai-analytics:latest 123456789.dkr.ecr.region.amazonaws.com/ai-analytics:latest

# Push to ECR
docker push 123456789.dkr.ecr.region.amazonaws.com/ai-analytics:latest
```

## 💰 Business Model and Monetization

### Pricing Plans

#### 🆓 Starter (Free)
- 100 predictions/month
- 1 dataset
- 1 model
- Basic support

#### 💼 Professional ($99/mo)
- 10,000 predictions/month
- Unlimited datasets
- 10 models
- Priority support
- API keys

#### 🏢 Enterprise ($499/mo)
- Unlimited predictions
- White label
- Dedicated support
- SLA 99.9%
- Custom integrations

### Billing Implementation
```kotlin
@Entity
class Subscription(
    val plan: PlanType,
    val monthlyLimit: Int,
    val currentUsage: Int,
    val renewalDate: LocalDateTime
)

@Component
class BillingService {
    fun checkUsageLimit(userId: String): Boolean {
        // Check user limits
    }
    
    fun trackApiCall(userId: String) {
        // Track API calls
    }
}
```

## 📈 Development Roadmap

### Phase 1: MVP (✅ Done)
- [x] Basic neural network
- [x] REST API with validation
- [x] Modern UI
- [x] Docker containerization
- [x] API documentation

### Phase 2: Growth (3 months)
- [ ] JWT authentication
- [ ] Stripe integration for payments
- [ ] Rate limiting by plan
- [ ] Email notifications
- [ ] Deep neural networks

### Phase 3: Scale (6 months)
- [ ] Microservices architecture
- [ ] Kubernetes deployment
- [ ] ML Pipeline with MLOps
- [ ] Batch predictions
- [ ] Auto-scaling

### Phase 4: Enterprise (12 months)
- [ ] Multi-tenancy
- [ ] SSO integration
- [ ] Audit logs
- [ ] Custom ML algorithms
- [ ] White-label solutions

## 👥 Team and Contributors

### Founders
- **CTO**: ML engine and architecture development
- **CEO**: Business development and sales
- **Lead Developer**: Full-stack development

### How to Contribute
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Create a Pull Request

### Code Style
- Kotlin coding conventions
- 100% test coverage for business logic
- Documentation for all public APIs
- Semantic commit messages

## 📞 Support and Contacts

### Technical Support
- 📧 **Email**: support@aianalytics.platform
- 💬 **Slack**: [aianalytics.slack.com](https://aianalytics.slack.com)
- 📖 **Docs**: [docs.aianalytics.platform](https://docs.aianalytics.platform)

### Business Inquiries
- 📧 **Sales**: sales@aianalytics.platform
- 📞 **Phone**: +7 (XXX) XXX-XX-XX
- 🌐 **Website**: [aianalytics.platform](https://aianalytics.platform)

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgements

- Spring Boot team for an excellent framework
- Kotlin team for a modern language
- Open source community for inspiration

---

**Made with ❤️ and ☕ by the AI Analytics Platform team**

> 💡 **Tip**: This MVP is ready for demonstration to investors and first clients. The next step is to raise a seed round of $500K-$1M to scale the team and product.
