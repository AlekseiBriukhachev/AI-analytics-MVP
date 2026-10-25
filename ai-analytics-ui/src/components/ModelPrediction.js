import { useState } from 'react';
import { apiRequest } from '../api';

function ModelPrediction({ models }) {
    const trainedModels = models.filter(model => model.status === 'TRAINED');
    const [formData, setFormData] = useState({
        modelId: '',
        features: ''
    });
    const [loading, setLoading] = useState(false);
    const [result, setResult] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setResult(null);

        try {
            const featureValues = formData.features.split(',').map(value => value.trim());
            const features = featureValues.map(Number);
            if (featureValues.some(value => value === '') || features.some(value => !Number.isFinite(value))) {
                throw new Error('Enter numeric feature values separated by commas.');
            }
            const payload = {
                modelId: formData.modelId,
                features
            };

            const data = await apiRequest('/api/models/predict', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
            setResult({
                type: 'success',
                message: 'Prediction successful!',
                details: data.data
            });
        } catch (err) {
            setResult({
                type: 'error',
                message: err.message
            });
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            {trainedModels.length === 0 ? (
                <p>No trained models available for prediction.</p>
            ) : (
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Model *</label>
                        <select
                            className="form-control"
                            value={formData.modelId}
                            onChange={(e) => setFormData({ ...formData, modelId: e.target.value })}
                            required
                        >
                            <option value="">Choose the model</option>
                            {trainedModels.map(model => (
                                <option key={model.id} value={model.id}>
                                    Model {model.id.substring(0, 8)}... (accuracy: {(model.accuracy * 100).toFixed(1)}%)
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Input data (separated by comma) *</label>
                        <input
                            type="text"
                            className="form-control"
                            value={formData.features}
                            onChange={(e) => setFormData({ ...formData, features: e.target.value })}
                            required
                            placeholder="Example: 1.0, 2.0, 3.0"
                        />
                    </div>

                    <button type="submit" className="btn" disabled={loading || !formData.modelId}>
                        {loading ? (
                            <div className="loading">
                                <div className="spinner"></div>
                                <span>We predict...</span>
                            </div>
                        ) : '🎯 Make a prediction'}
                    </button>

                    {result && (
                        <div className={`alert alert-${result.type}`}>
                            {result.message}
                            {result.details && (
                                <div className="metrics-grid">
                                    <div className="metric">
                                        <div className="metric-value">{result.details.prediction.toFixed(4)}</div>
                                        <div className="metric-label">Prediction</div>
                                    </div>
                                    <div className="metric">
                                        <div className="metric-value">{(result.details.confidence * 100).toFixed(1)}%</div>
                                        <div className="metric-label">Confidence</div>
                                    </div>
                                    <div className="metric">
                                        <div className="metric-value">{result.details.predictedClass}</div>
                                        <div className="metric-label">Class</div>
                                    </div>
                                </div>
                            )}
                        </div>
                    )}
                </form>
            )}
        </div>
    );
}

export default ModelPrediction;