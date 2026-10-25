import DatasetUpload from "./DatasetUpload";
import DatasetList from "./DatasetList";
import { useCallback, useEffect, useState } from "react";
import { apiRequest } from '../api';

function DatasetManager({ onRefresh }) {

    const [activeTab, setActiveTab] = useState('upload');
    const [datasets, setDatasets] = useState([]);
    const [error, setError] = useState(null);

    const loadDatasets = useCallback(async () => {
        try {
            const data = await apiRequest('/api/datasets');
            setDatasets(data?.data?.content || []);
            setError(null);
        } catch (err) {
            setError(err.message);
        }
    }, []);

    useEffect(() => {
        loadDatasets();
    }, [loadDatasets]);

    return (
        <div className="section">
            <h2 className="section-title">Dataset Manager</h2>
            <div className="tabs" role="tablist" aria-label="Dataset manager">
                <button
                    type="button"
                    role="tab"
                    aria-selected={activeTab === 'upload'}
                    className={`tab ${activeTab === 'upload' ? 'active' : ''}`}
                    onClick={() => setActiveTab('upload')}
                >
                    Upload
                </button>
                <button
                    type="button"
                    role="tab"
                    aria-selected={activeTab === 'list'}
                    className={`tab ${activeTab === 'list' ? 'active' : ''}`}
                    onClick={() => setActiveTab('list')}
                >
                    Preview
                </button>
            </div>
            {error && <div className="alert alert-error" role="alert">{error}</div>}
            {activeTab === 'upload' ? (
                <DatasetUpload onSuccess={() => { loadDatasets(); onRefresh(); }} />
            ) : (
                <DatasetList datasets={datasets} />
            )}
        </div>

    );
}

export default DatasetManager;