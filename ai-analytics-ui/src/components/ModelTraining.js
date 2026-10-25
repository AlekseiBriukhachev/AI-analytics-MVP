import { useState, useEffect } from 'react';
import { apiRequest } from '../api';

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
    const [error, setError] = useState(null);

    useEffect(() => {
        const loadDatasets = async () => {
            try {
                const data = await apiRequest('/api/datasets');
                setDatasets(data?.data?.content || []);
            } catch (err) {
                setError(err.message);
                setDatasets([]);
            }
        };
        loadDatasets();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setResult(null);

        try {
            const hiddenLayers = formData.hiddenLayers
                .split(',')
                .map(value => Number(value.trim()));
            if (hiddenLayers.some(size => !Number.isInteger(size) || size <= 0)) {
                throw new Error('Hidden layers must be positive whole numbers separated by commas.');
            }

            const payload = {
                datasetId: formData.datasetId,
                config: {
                    hiddenLayers,
                    learningRate: parseFloat(formData.learningRate),
                    epochs: parseInt(formData.epochs)
                },
                testSplit: parseFloat(formData.testSplit)
            };

            const data = await apiRequest('/api/models/train', {
                headers: { 'Content-Type': 'application/json' },
                method: 'POST',
                body: JSON.stringify(payload)
            });
            setResult({
                type: 'success',
                message: 'Model trained successfully!',
                details: data.data
            });
            onSuccess();
        } catch (err) {
            setResult({
                type: 'error',
                message: err.message
            });
        } finally {
            setLoading(false);
        }
    }
    return (
        <div>
            {error && <div className="alert alert-error" role="alert">{error}</div>}
            {datasets === null ? (
                <p>Loading datasets...</p>
            ) : datasets.length === 0 ? (
                <p>No datasets to train models.</p>
            ) : (
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Dataset *</label>
                        <select
                            className="form-control"
                            value={formData.datasetId}
                            onChange={(e) => setFormData({ ...formData, datasetId: e.target.value })}
                            required
                        >
                            <option value="">Choose dataset</option>
                            {datasets.map(dataset => (
                                <option key={dataset.id} value={dataset.id}>
                                    {dataset.name} ({dataset.recordCount} records, {dataset.featureCount} features)
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
                            onChange={(e) => setFormData({ ...formData, hiddenLayers: e.target.value })}
                            placeholder="Example: 10, 5"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Learning speed</label>
                        <input
                            type="number"
                            className="form-control"
                            value={formData.learningRate}
                            onChange={(e) => setFormData({ ...formData, learningRate: e.target.value })}
                            step="0.001"
                            min="0.0001"
                            max="1"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Number of epochs</label>
                        <input
                            type="number"
                            className="form-control"
                            value={formData.epochs}
                            onChange={(e) => setFormData({ ...formData, epochs: e.target.value })}
                            min="10"
                            max="50000"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Test data split</label>
                        <input
                            type="number"
                            className="form-control"
                            value={formData.testSplit}
                            onChange={(e) => setFormData({ ...formData, testSplit: e.target.value })}
                            step="0.05"
                            min="0.1"
                            max="0.5"
                            required
                        />
                    </div>

                    <button type="submit" className="btn" disabled={loading                    }>
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
                            {result.details && (
                                <div className="metrics-grid">
                                    <div className="metric">
                                        <div className="metric-value">{(result.details.accuracy * 100).toFixed(1)}%</div>
                                        <div className="metric-label">Accuracy</div>
                                    </div>
                                    <div className="metric">
                                        <div className="metric-value">{result.details.loss.toFixed(4)}</div>
                                        <div className="metric-label">Losses</div>
                                    </div>
                                    <div className="metric">
                                        <div className="metric-value">{result.details.trainingTime}ms</div>
                                        <div className="metric-label">Time</div>
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

export default ModelTraining;