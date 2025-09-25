import { useState, useEffect } from 'react';

function ModelTraining({ onSuccess }) {
    const [datasets, setDatasets] = useState(null);
    const [formData, setFormData] = useState({
        datasetId: '',
        hiddenLayers: '10,5',
        learningRate: '0.01',
        epochs: '1000',
        testSplit: '0.2'
    });
    const [loading, setLoading] = useState(false);
    const [result, setResult] = useState(null);

    useEffect(() => {
        const loadDatasets = async () => {
            try {
                const response = await fetch('/api/datasets');
                setDatasets(response.data.data?.content || []);
            } catch (err) {
                console.error('Failed to load datasets:', err);
            }
        };
        loadDatasets();
    }, [])

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setResult(null);

        try {
            const payload = {
                datasetId: formData.datasetId,
                config: {
                    hiddenLayers: formData.hiddenLayers.split(',').map(n => parseInt(n.trim())),
                    learningRate: parseFloat(formData.learningRate),
                    epochs: parseInt(formData.epochs)
                },
                testSplit: parseFloat(formData.testSplit)
            };

            const response = await fetch('api/models/train', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
            const data = await response.json();
            setResult({
                type: 'success',
                message: 'Model trained successfully!',
                details: data.data
            });
            onSuccess();
        } catch (err) {
            console.error('Training failed:', err);
            setResult({
                type: 'error',
                message: err.response?.data?.error || err.message
            });
        } finally {
            setLoading(false);
        }
    }
    return (
        <form onSubmit={handleSubmit}>
            <div className="form-group">
                <label>Dataset *</label>
                <select
                    className="form-control"
                    value={formData.datasetId}
                    onChange={(e) => setFormData({...formData, datasetId: e.target.value})}
                    required
                >
                    <option value="">Choose dataset</option>
                    {datasets.map(dataset => (
                        <option key={dataset.id} value={dataset.id}>
                            {dataset.name} ({dataset.recordCount} records)
                        </option>
                    ))}
                </select>
            </div>

            <div className="form-group">
                <label>Hidden layers (separated by comma)</label>
                <input
                    type="text"
                    className="form-control"
                    value={formData.hiddenLayers}
                    onChange={(e) => setFormData({...formData, hiddenLayers: e.target.value})}
                    placeholder="10,5"
                />
            </div>

            <div className="form-group">
                <label>Learning speed</label>
                <input
                    type="number"
                    className="form-control"
                    value={formData.learningRate}
                    onChange={(e) => setFormData({...formData, learningRate: e.target.value})}
                    step="0.001"
                    min="0.0001"
                    max="1"
                />
            </div>

            <div className="form-group">
                <label>Number of epochs</label>
                <input
                    type="number"
                    className="form-control"
                    value={formData.epochs}
                    onChange={(e) => setFormData({...formData, epochs: e.target.value})}
                    min="10"
                    max="50000"
                />
            </div>

            <button type="submit" className="btn" disabled={loading}>
                {loading ? (
                    <div className="loading">
                        <div className="spinner"></div>
                        <span>Training the model...</span>
                    </div>
                ) : '🚀 Start Training'}
            </button>

            {result && (
                <div className={`alert alert-${result.type}`}>
                    {result.message}
                    {result.data && (
                        <div className="metrics-grid">
                            <div className="metric">
                                <div className="metric-value">{(result.data.accuracy * 100).toFixed(1)}%</div>
                                <div className="metric-label">Accuracy</div>
                            </div>
                            <div className="metric">
                                <div className="metric-value">{result.data.loss.toFixed(4)}</div>
                                <div className="metric-label">Losses</div>
                            </div>
                            <div className="metric">
                                <div className="metric-value">{result.data.trainingTime}ms</div>
                                <div className="metric-label">Time</div>
                            </div>
                        </div>
                    )}
                </div>
            )}
        </form>
    );
}

export default ModelTraining;