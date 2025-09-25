function ModelList({ models, onRefresh }) {
    const getStatusColor = (status) => {
        switch (status) {
            case 'TRAINED': return '#28a745';
            case 'TRAINING': return '#ffc107';
            case 'ERROR': return '#dc3545';
            default: return '#6c757d';
        }
    };

    const getStatusIcon = (status) => {
        switch (status) {
            case 'TRAINED': return '✅';
            case 'TRAINING': return '⏳';
            case 'ERROR': return '❌';
            default: return '⚪';
        }
    };

    return (
        <div>
            {models.length === 0 ? (
                <p>No trained models</p>
            ) : (
                models.map(model => (
                    <div key={model.id} className="model-item">
                        <h4>
                            {getStatusIcon(model.status)} Model {model.id.substring(0, 8)}...
                        </h4>
                        <div className="model-meta">
                                <span style={{color: getStatusColor(model.status)}}>
                                    📊 {model.status}
                                </span>
                            {model.accuracy > 0 && (
                                <span>🎯 {(model.accuracy * 100).toFixed(1)}% accuracy</span>
                            )}
                            <span>⚡ {model.epochs} epoch</span>
                            <span>⏱️ {model.trainingTime}ms</span>
                            <span>📅 {new Date(model.createdAt).toLocaleDateString()}</span>
                        </div>
                    </div>
                ))
            )}
        </div>
    );
}