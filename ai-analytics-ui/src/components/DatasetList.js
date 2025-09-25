function DatasetList({ datasets, onRefresh }) {
    return (
        <div>
            {datasets.length === 0 ? (
                <p>No datasets loaded</p>
            ) : (
                datasets.map(dataset => (
                    <div key={dataset.id} className="dataset-item">
                        <h4>{dataset.name}</h4>
                        <div className="dataset-meta">
                            <span>📊 {dataset.record_count} records</span>
                            <span>🔢 {dataset.feature_count} features</span>
                            <span>📅 {new Date(dataset.created_at).toLocaleDateString()}</span>
                        </div>
                        {dataset.description && <p style={{ marginTop: '8px', color: '#666' }}>{dataset.description}</p>}
                    </div>
                ))
            )}
        </div>
    );
}

export default DatasetList;