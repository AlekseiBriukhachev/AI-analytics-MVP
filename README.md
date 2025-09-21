# 🧠 AI Analytics Platform - Spring Boot MVP

Профессиональная платформа для создания и развертывания моделей машинного обучения на базе Spring Boot и собственного ML движка.

## 🚀 Быстрый старт

### Предварительные требования
- Java 17+
- Docker & Docker Compose
- Git

### Локальная разработка

```bash
# Клонирование репозитория
git clone https://github.com/your-username/ai-analytics-platform.git
cd ai-analytics-platform

# Запуск в режиме разработки
./gradlew bootRun

# Или через Make
make run
```

### Docker развертывание

```bash
# Быстрый старт
chmod +x scripts/startup.sh
./scripts/startup.sh

# Или вручную
docker-compose up -d
```

## 📋 Функциональность

### ✨ Основные возможности

#### 🔧 Платформа
- **REST API** с OpenAPI/Swagger документацией
- **Современный веб-интерфейс** на React
- **Реляционная БД** с JPA/Hibernate
- **Кэширование** для высокой производительности
- **Мониторинг** через Spring Boot Actuator

#### 🤖 Машинное обучение
- **Собственный ML движок** на чистом Kotlin
- **Нейронные сети** с настраиваемой архитектурой
- **Обучение и предсказания** через REST API
- **Метрики качества** модели

#### 📊 Управление данными
- **Загрузка датасетов** (CSV/JSON)
- **Валидация данных** с подробными ошибками
- **Версионирование** датасетов
- **Аналитика** использования

## 🏗️ Архитектура

```
┌─────────────────────────────────────────────┐
│                Frontend                     │
│          React + Modern UI                  │
└─────────────────┬───────────────────────────┘
                  │ HTTP/REST
┌─────────────────▼───────────────────────────┐
│              Spring Boot                    │
│  ┌─────────────┐  ┌─────────────────────┐   │
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

### Базовый URL: `/api`

#### Датасеты
```http
POST   /datasets              # Создать датасет
GET    /datasets              # Список датасетов (с пагинацией)
GET    /datasets/{id}         # Получить датасет
GET    /datasets/{id}/data    # Получить данные
DELETE /datasets/{id}         # Удалить датасет
```

#### Модели
```http
POST   /models/train          # Обучить модель
POST   /models/train-async    # Асинхронное обучение
POST   /models/predict        # Предсказание
GET    /models               # Список моделей
GET    /models/{id}          # Информация о модели
DELETE /models/{id}          # Удалить модель
```

#### Аналитика
```http
GET    /analytics/dashboard   # Дашборд метрик
GET    /health                # Health check
```

### Примеры запросов

#### Создание датасета
```bash
curl -X POST http://localhost:8080/api/datasets \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sales Analytics",
    "description": "Данные продаж за Q4 2024",
    "data": [
      {"features": [1000, 5, 1], "label": 1},
      {"features": [500, 2, 0], "label": 0}
    ],
    "featureNames": ["revenue", "employees", "quarter"]
  }'
```

#### Обучение модели
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

#### Предсказание
```bash
curl -X POST http://localhost:8080/api/models/predict \
  -H "Content-Type: application/json" \
  -d '{
    "modelId": "your-model-id",
    "features": [750, 3, 1]
  }'
```

## 🔧 Конфигурация

### Профили Spring Boot

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

### Переменные окружения

```bash
# База данных
DATABASE_URL=jdbc:postgresql://localhost:5432/aianalytics
DB_USERNAME=postgres
DB_PASSWORD=your_password

# Приложение
SPRING_PROFILES_ACTIVE=prod
PORT=8080

# Опциональные
REDIS_URL=redis://localhost:6379
LOG_LEVEL=INFO
```

## 🧪 Тестирование

### Запуск тестов
```bash
# Все тесты
./gradlew test

# Только unit тесты
./gradlew test --tests "*UnitTest"

# Интеграционные тесты
./gradlew test --tests "*IntegrationTest"
```

### API тестирование
```bash
# Полный тест системы
chmod +x scripts/test-complete.sh
./scripts/test-complete.sh

# Простая проверка
make test-api
```

## 📊 Мониторинг и метрики

### Доступные endpoints
- **Health**: `/actuator/health`
- **Metrics**: `/actuator/metrics`
- **Info**: `/actuator/info`
- **Prometheus**: `/actuator/prometheus`

### Swagger UI
- **Документация API**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

### Логирование
```bash
# Просмотр логов в Docker
docker-compose logs -f ai-analytics

# Логи приложения
tail -f logs/ai-analytics.log
```

## 🚀 Деплой в продакшн

### Docker Compose (рекомендуется)
```bash
# Создание продакшн конфигурации
cp docker-compose.yml docker-compose.prod.yml

# Редактирование переменных
vim docker-compose.prod.yml

# Деплой
docker-compose -f docker-compose.prod.yml up -d
```

### Cloud платформы

#### Heroku
```bash
# Установка Heroku CLI
heroku create ai-analytics-platform

# Настройка переменных
heroku config:set SPRING_PROFILES_ACTIVE=prod
heroku config:set DATABASE_URL=your_postgres_url

# Деплой
git push heroku main
```

#### AWS ECS
```bash
# Сборка образа
docker build -t ai-analytics .

# Тегирование для ECR
docker tag ai-analytics:latest 123456789.dkr.ecr.region.amazonaws.com/ai-analytics:latest

# Push в ECR
docker push 123456789.dkr.ecr.region.amazonaws.com/ai-analytics:latest
```

## 💰 Бизнес-модель и монетизация

### Тарифные планы

#### 🆓 Starter (Бесплатный)
- 100 предсказаний/месяц
- 1 датасет
- 1 модель
- Базовая поддержка

#### 💼 Professional ($99/мес)
- 10,000 предсказаний/месяц
- Неограниченное количество датасетов
- 10 моделей
- Приоритетная поддержка
- API ключи

#### 🏢 Enterprise ($499/мес)
- Неограниченные предсказания
- Белый лейбл
- Dedicated support
- SLA 99.9%
- Custom интеграции

### Реализация биллинга
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
        // Проверка лимитов пользователя
    }
    
    fun trackApiCall(userId: String) {
        // Учет вызовов API
    }
}
```

## 📈 Roadmap развития

### Phase 1: MVP (✅ Готово)
- [x] Базовая нейронная сеть
- [x] REST API с валидацией
- [x] Современный UI
- [x] Docker контейнеризация
- [x] Документация API

### Phase 2: Growth (3 месяца)
- [ ] Аутентификация JWT
- [ ] Stripe интеграция для платежей
- [ ] Rate limiting по тарифам
- [ ] Email уведомления
- [ ] Глубокие нейронные сети

### Phase 3: Scale (6 месяцев)
- [ ] Microservices архитектура
- [ ] Kubernetes deployment
- [ ] ML Pipeline с MLOps
- [ ] Batch предсказания
- [ ] Auto-scaling

### Phase 4: Enterprise (12 месяцев)
- [ ] Multi-tenancy
- [ ] SSO интеграция
- [ ] Audit logs
- [ ] Custom ML алгоритмы
- [ ] White-label решения

## 👥 Команда и контрибьюторы

### Основатели
- **CTO**: Разработка ML движка и архитектуры
- **CEO**: Бизнес-развитие и продажи
- **Lead Developer**: Full-stack разработка

### Как контрибьютить
1. Fork репозиторий
2. Создайте feature branch (`git checkout -b feature/amazing-feature`)
3. Commit изменения (`git commit -m 'Add amazing feature'`)
4. Push в branch (`git push origin feature/amazing-feature`)
5. Создайте Pull Request

### Кодстайл
- Kotlin coding conventions
- 100% test coverage для бизнес-логики
- Документация для всех public API
- Semantic commit messages

## 📞 Поддержка и контакты

### Техническая поддержка
- 📧 **Email**: support@aianalytics.platform
- 💬 **Slack**: [aianalytics.slack.com](https://aianalytics.slack.com)
- 📖 **Docs**: [docs.aianalytics.platform](https://docs.aianalytics.platform)

### Бизнес-запросы
- 📧 **Sales**: sales@aianalytics.platform
- 📞 **Телефон**: +7 (XXX) XXX-XX-XX
- 🌐 **Сайт**: [aianalytics.platform](https://aianalytics.platform)

## 📄 Лицензия

Этот проект лицензирован под MIT License - см. файл [LICENSE](LICENSE) для деталей.

## 🙏 Благодарности

- Spring Boot team за отличный фреймворк
- Kotlin team за современный язык
- Open source community за вдохновение

---

**Сделано с ❤️ и ☕ командой AI Analytics Platform**

> 💡 **Tip**: Это MVP готов для демонстрации инвесторам и первым клиентам. Следующий шаг - привлечение seed раунда $500K-$1M для масштабирования команды и продукта.