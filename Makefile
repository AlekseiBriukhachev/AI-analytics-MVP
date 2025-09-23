# Variables
APP_NAME := ai-analytics
DOCKER_IMAGE := $(APP_NAME):latest
GRADLE := ./gradlew
DOCKER_COMPOSE := docker-compose

# Default target
.PHONY: help
help:
	@echo "Available targets:"
	@echo "  build       	- Build the application"
	@echo "  test        	- Run tests"
	@echo "  run         	- Run application locally"
	@echo "  docker-build 	- Build Docker image"
	@echo "  docker-run  	- Run with Docker Compose"
	@echo "  docker-stop    - Stop Docker containers"
	@echo "  clean       	- Clean build artifacts"
	@echo "  deploy      	- Deploy to production"
	@echo "  logs        	- View application logs"
	@echo "  backup      	- Backup database"
	@echo "  test-api    	- Test API endpoints"

# Build the application
.PHONY: build
build:
	@echo "📦 Building application..."
	$(GRADLE) clean build

# Run tests
.PHONY: test
test:
	@echo "🧪 Running tests..."
	$(GRADLE) test

# Run application locally
.PHONY: run
run: build
	@echo "🚀 Running application locally..."
	$(GRADLE) bootRun

# Build Docker image
.PHONY: docker-build
docker-build: build
	@echo "🐳 Building Docker image..."
	docker build -t $(DOCKER_IMAGE) .

# Run with Docker Compose
.PHONY: docker-run
docker-run: docker-build
	@echo "🚀 Running application with Docker Compose..."
	$(DOCKER_COMPOSE) up -d

# Stop Docker containers
.PHONY: docker-stop
stop:
	@echo "🛑 Stopping Docker containers..."
	$(DOCKER_COMPOSE) down

# Clean build artifacts
.PHONY: clean
clean:
	@echo "🧹 Cleaning build artifacts..."
	$(GRADLE) clean
	docker system prune -f

# Deploy to production
.PHONY: deploy
deploy: build docker-build
	@echo "🚀 Deploying to production..."
	$(DOCKER_COMPOSE) -f docker-compose.prod.yml up -d --build

# View application logs
.PHONY: logs
logs:
	@echo "📜 Viewing application logs..."
	$(DOCKER_COMPOSE) logs -f ai-analytics

# Backup database
.PHONY: backup
backup:
	@echo "💾 Backing up database..."
	docker exec -t postgres pg_dumpall -c -U postgres > backup_$(shell date +%Y%m%d_%H%M%S).sql
	@echo "Backup saved to db_backup.sql"

# Test API endpoints
.PHONY: test-api
test-api:
	@echo "🔍 Testing API endpoints..."
	curl -f http://localhost:8080/api/health
	curl -X POST http://localhost:8080/api/datasets \
		-H "Content-Type: application/json" \
		-d '{"name":"Test","description":"Test dataset","data":[{"features":[1,2],"label":1}]}'