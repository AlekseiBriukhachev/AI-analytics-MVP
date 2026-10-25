function DatasetList({ datasets }) {
    return (
        <div>
            {datasets.length === 0 ? (
                <p>No datasets loaded</p>
            ) : (
                datasets.map(dataset => (
                    <div key={dataset.id} className="dataset-item">
                        <h4>{dataset.name}</h4>
                        <div className="dataset-meta">
                            <span>📊 {dataset.recordCount} records</span>
                            <span>🔢 {dataset.featureCount} features</span>
                            <span>📅 {new Date(dataset.createdAt).toLocaleDateString()}</span>
                        </div>
                        {dataset.description && <p style={{ marginTop: '8px', color: '#666' }}>{dataset.description}</p>}
                    </div>
                ))
            )}
        </div>
    );
}

export default DatasetList;