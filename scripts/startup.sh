#!/bin/bash

echo "🚀 Starting AI Analytics Platform..."

# Check dependencies
echo "🔍 Checking dependencies..."
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker to proceed."
    exit 1
fi

if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java to proceed."
    exit 1
fi

# Creating necessary directories
echo "📁 Creating necessary directories..."
mkdir -p logs
mkdir -p ssl

# Generating self-signed SSL certificates if not present
if [ ! -f ssl/cert.pem ] || [ ! -f ssl/key.pem ]; then
    echo "🔐 Generating self-signed SSL certificates..."
    openssl req -x509 \
            -newkey rsa:4096 \
            -keyout ssl/key.pem \
            -out ssl/cert.pem \
            -days 365 \
            -nodes \
            -subj "/C=RU/ST=Moscow/L=Moscow/O=AI Analytics/CN=localhost"
    echo "✅ SSL certificates generated."
else
    echo "✅ SSL certificates already exist."
fi

# Building and starting
echo "📦 Application building..."
./gradlew build

echo "🐳 Starting Docker containers..."
docker-compose up -d

# Waiting for services to be ready
echo "⏳ Waiting for services to be ready..."
sleep 30  # Adjust this sleep time as necessary for your services to start

# Health check
echo "🔄 Performing health checks..."
curl -f http://localhost:8080/actuator/health && echo "✅ Application is running!" || echo "❌ Application failed to start."

echo "🚀 AI Analytics Platform started successfully!"
echo "🌐 Access the platform at https://localhost:8080"
echo " API Docs: http://localhost:8080/swagger-ui/index.html"
echo " DB Admin: http://localhost:8080/h2-console"
echo " Actuator: http://localhost:8080/actuator"
echo "📄 Logs are available in the logs/ directory."
echo "🔐 SSL certificates are located in the ssl/ directory."
echo "🛠️  Use 'docker-compose logs -f' to view real-time logs."
echo "🛑 Use 'docker-compose down' to stop the application."
echo "📚 Refer to the README.md for further instructions."
echo "Have a great day! 😊"