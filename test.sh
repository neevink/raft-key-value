#!/bin/bash
set -e

BASE_URL="http://localhost:8080/api/v1/kv"

echo "=== Testing Key-Value API ==="

# 1. Put a key-value pair
echo -e "\n> Adding key 'hello' with value 'world'"
curl -s -X POST $BASE_URL/put \
  -H "Content-Type: application/json" \
  -d '{"key": "aGVsbG8=", "value": "d29ybGQ="}' | jq

# 2. Get the value
echo -e "\n> Getting value for key 'hello'"
curl -s -X POST $BASE_URL/get \
  -H "Content-Type: application/json" \
  -d '{"key": "aGVsbG8="}' | jq

# 3. Add binary data
echo -e "\n> Adding binary data"
curl -s -X POST $BASE_URL/put \
  -H "Content-Type: application/json" \
  -d '{"key": "YmluYXJ5LWtleQ==", "value": "AAECAwQFBgcICQoLDA0ODxAREhM="}' | jq

# 4. Get binary data
echo -e "\n> Getting binary data"
curl -s -X POST $BASE_URL/get \
  -H "Content-Type: application/json" \
  -d '{"key": "YmluYXJ5LWtleQ=="}' | jq

# 5. Delete a key
echo -e "\n> Deleting key 'hello'"
curl -s -X POST $BASE_URL/delete \
  -H "Content-Type: application/json" \
  -d '{"key": "aGVsbG8="}' | jq

# 6. Try to get deleted key
echo -e "\n> Trying to get deleted key 'hello' (should fail)"
curl -s -X POST $BASE_URL/get \
  -H "Content-Type: application/json" \
  -d '{"key": "aGVsbG8="}' | jq

echo -e "\n=== Test completed ==="
