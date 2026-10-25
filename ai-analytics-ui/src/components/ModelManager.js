import ModelTraining from './ModelTraining';
import ModelPrediction from './ModelPrediction';
import ModelList from './ModelList';
import { useState, useEffect, useCallback } from 'react';

function ModelManager({ onRefresh }) {
    const [activeTab, setActiveTab] = useState('train');
    const [models, setModels] = useState([]);

    const loadModels = useCallback(async () => {
        try {
            const response = await fetch('http://localhost:8080/api/models');
            const data = await response.json();
            setModels(data?.data || [])
        } catch (err) {
            console.error('Failed to load models:', err);
        }
    }, []);

    useEffect(() => {
        loadModels();
    }, [loadModels]);

    return (
        <div className="section">
            <h2>🤖 Machine Learning</h2>
            <div className="tabs">
                <div
                    className={`tab ${activeTab === 'train' ? 'active' : ''}`}
                    onClick={() => setActiveTab('train')}
                >
                    Train
                </div>
                <div
                    className={`tab ${activeTab === 'models' ? 'active' : ''}`}
                    onClick={() => setActiveTab('models')}
                >
                    Models
                </div>
                <div
                    className={`tab ${activeTab === 'predict' ? 'active' : ''}`}
                    onClick={() => setActiveTab('predict')}
                >
                    Predict
                </div>
            </div>

            {activeTab === 'train' && (
                <ModelTraining onSuccess={() => { loadModels(); onRefresh(); }} />
            )}
            {activeTab === 'models' && (
                <ModelList models={models} onRefresh={loadModels} />
            )}
            {activeTab === 'predict' && (
                <ModelPrediction models={models} />
            )}
        </div>
    );
}

export default ModelManager;