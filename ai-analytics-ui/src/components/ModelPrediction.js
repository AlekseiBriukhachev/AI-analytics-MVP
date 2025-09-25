import {useState} from 'react';

function ModelPrediction({models}) {
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
            const payload = {
                modelId: formData.modelId,
                features: formData.features.split(',').map(x => parseFloat(x.trim()))
            };

            const response = await fetch('/api/models/predict', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(payload)
            });
            const data = await response.json();
            setResult({
                type: 'success',
                message: 'Prediction successful!',
                details: data.data
            });
        } catch (err) {
            console.error('Prediction failed:', err);
            setResult({
                type: 'error',
                message: err.response?.data?.error || err.message
            });
        } finally {
            setLoading(false);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <div className="form-group">
                <label>Model *</label>
                <select
                    className="form-control"
                    value={formData.modelId}
                    onChange={(e) => setFormData({...formData, modelId: e.target.value})}
                    required
                >
                    <option value="">Choose the model</option>
                    {models.filter(m => m.status === 'TRAINED').map(model => (
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
                    onChange={(e) => setFormData({...formData, features: e.target.value})}
                    required
                    placeholder="1.0,2.0,3.0"
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
                    {result.data && (
                        <div className="metrics-grid">
                            <div className="metric">
                                <div className="metric-value">{result.data.prediction.toFixed(4)}</div>
                                <div className="metric-label">Prediction</div>
                            </div>
                            <div className="metric">
                                <div className="metric-value">{(result.data.confidence * 100).toFixed(1)}%</div>
                                <div className="metric-label">Confidence</div>
                            </div>
                            <div className="metric">
                                <div className="metric-value">{result.data.predictedClass}</div>
                                <div className="metric-label">Class</div>
                            </div>
                        </div>
                    )}
                </div>
            )}
        </form>
    );
}

export default ModelPrediction;