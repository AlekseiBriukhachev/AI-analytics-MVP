import ModelTraining from './ModelTraining';
import ModelPrediction from './ModelPrediction';
import ModelList from './ModelList';
import { useState, useEffect, useCallback } from 'react';

function ModelManager({ onRefresh }) {
    const [activeTab, setActiveTab] = useState('train');
    const [models, setModels] = useState([]);

    const loadModels = useCallback(async () => {
        try {
            const response = await fetch('/api/models');
            setModels(response.data.data || [])
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
                    className={`tab ${activeTab === 'deploy' ? 'active' : ''}`}
                    onClick={() => setActiveTab('models')}
                >
                    Models
                </div>
            </div>

            {activeTab === 'train' && (
                <ModelTraining onSuccess={() => { loadModels(); onRefresh(); }} />
            )}
            {activeTab === 'predict' && (
                <ModelPrediction models={models} />
            )}
            {activeTab === 'models' && (
                <ModelList models={models} onRefresh={loadModels} />
            )}
        </div>
    );
}

export default ModelManager;