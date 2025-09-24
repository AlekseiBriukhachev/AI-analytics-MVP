-- Test data for development and testing purposes
INSERT INTO dataset (id, name, description, data_json, feature_names, record_count, feature_count, created_at,
                      updated_at)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'XOR Dataset', 'Классический XOR датасет для тестирования',
        '[{"features":[0,0],"label":0},{"features":[0,1],"label":1},{"features":[1,0],"label":1},{"features":[1,1],"label":0}]',
        'input1,input2', 4, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

       ('550e8400-e29b-41d4-a716-446655440001', 'AND Dataset', 'Логическое И для тестирования',
        '[{"features":[0,0],"label":0},{"features":[0,1],"label":0},{"features":[1,0],"label":0},{"features":[1,1],"label":1}]',
        'input1,input2', 4, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

---