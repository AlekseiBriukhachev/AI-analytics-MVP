import ModelTraining from './ModelTraining';
import ModelPrediction from './ModelPrediction';
import ModelList from './ModelList';
import { useState, useEffect, useCallback } from 'react';
import { apiRequest } from '../api';

function ModelManager({ onRefresh }) {
    const [activeTab, setActiveTab] = useState('train');
    const [models, setModels] = useState([]);
    const [error, setError] = useState(null);

    const loadModels = useCallback(async () => {
        try {
            const data = await apiRequest('/api/models');
            setModels(data?.data || []);
            setError(null);
        } catch (err) {
            setError(err.message);
        }
    }, []);

    useEffect(() => {
        loadModels();
    }, [loadModels]);

    return (
        <div className="section">
            <h2>🤖 Machine Learning</h2>
            <div className="tabs" role="tablist" aria-label="Machine learning">
                <button
                    type="button"
                    role="tab"
                    aria-selected={activeTab === 'train'}
                    className={`tab ${activeTab === 'train' ? 'active' : ''}`}
                    onClick={() => setActiveTab('train')}
                >
                    Train
                </button>
                <button
                    type="button"
                    role="tab"
                    aria-selected={activeTab === 'models'}
                    className={`tab ${activeTab === 'models' ? 'active' : ''}`}
                    onClick={() => setActiveTab('models')}
                >
                    Models
                </button>
                <button
                    type="button"
                    role="tab"
                    aria-selected={activeTab === 'predict'}
                    className={`tab ${activeTab === 'predict' ? 'active' : ''}`}
                    onClick={() => setActiveTab('predict')}
                >
                    Predict
                </button>
            </div>
            {error && <div className="alert alert-error" role="alert">{error}</div>}

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