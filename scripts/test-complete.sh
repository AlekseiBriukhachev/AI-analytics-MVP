#!/bin/bash

BASE_URL="http://localhost:8080"

echo "🧪 Completeness Test for AI Analytics Platform"

# 1. Health check
echo "1️⃣ Health Check..."
curl -s "$BASE_URL/health" | jq .

# 2. Dataset creating
echo "2️⃣ Creating a Dataset..."
DATASET_RESPONSE=$(curl -s -X POST "$BASE_URL/datasets" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Iris Dataset",
    "description": "Классический датасет Ирисов для тестирования",
    "data": [
      {"features": [5.1, 3.5, 1.4, 0.2], "label": 0},
      {"features": [4.9, 3.0, 1.4, 0.2], "label": 0},
      {"features": [7.0, 3.2, 4.7, 1.4], "label": 1},
      {"features": [6.4, 3.2, 4.5, 1.5], "label": 1},
      {"features": [6.3, 3.3, 6.0, 2.5], "label": 2},
      {"features": [5.8, 2.7, 5.1, 1.9], "label": 2}
    ],
    "featureNames": ["sepal_length", "sepal_width", "petal_length", "petal_width"]
  }')

echo "$DATASET_RESPONSE" | jq .
DATASET_ID=$(echo "$DATASET_RESPONSE" | jq -r '.id')

# 3. Retrieve all datasets
echo "3️⃣ Retrieving all Datasets..."
curl -s "$BASE_URL/datasets" | jq .

# 4. Model learning
echo "4️⃣ Training a Model..."
TRAINING_RESPONSE=$(curl -s -X POST "$BASE_URL/models/train" \
  -H "Content-Type: application/json" \
  -d "{
    \"datasetId\": \"$DATASET_ID\",
    \"config\": {
      \"hiddenLayers\": [8, 4],
      \"learningRate\": 0.01,
      \"epochs\": 2000
    },
    \"testSplit\": 0.3
  }")

echo "$TRAINING_RESPONSE" | jq .
MODEL_ID=$(echo "$TRAINING_RESPONSE" | jq -r '.data.modelId')

# 5. Retrieve model info
echo "5️⃣ Retrieving Model Info..."
curl -s "$BASE_URL/models/$MODEL_ID" | jq .

# 6. Prediction testing
echo "6️⃣ Testing Prediction..."

# Test 1: Iris Setosa
curl -s -X POST "$BASE_URL/models/predict" \
  -H "Content-Type: application/json" \
  -d "{
    \"modelId\": \"$MODEL_ID\",
    \"features\": [5.0, 3.6, 1.3, 0.3]
  }" | jq .

# Test 2: Iris Versicolor
curl -s -X POST "$BASE_URL/models/predict" \
  -H "Content-Type: application/json" \
  -d "{
    \"modelId\": \"$MODEL_ID\",
    \"features\": [6.0, 3.0, 4.5, 1.5]
  }" | jq .

# 7. Analytics check
echo "7️⃣ Analytics Dashboard..."
curl -s "$BASE_URL/analytics/dashboard" | jq .

# 8. System metrics
echo "8️⃣ System Metrics..."
curl -s "$BASE_URL/actuator/metrics" | jq .

echo "✅ Completeness Test Completed!"