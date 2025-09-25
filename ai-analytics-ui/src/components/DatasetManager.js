import DatasetUpload from "./DatasetUpload";
import DatasetList from "./DatasetList";
import { useCallback, useEffect, useState } from "react";

function DatasetManager({ onRefresh }) {

    const [activeTab, setActiveTab] = useState('upload');
    const [datasets, setDatasets] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const loadDatasets = useCallback(async () => {
        try {
            const response = await fetch('http://localhost:8080/api/datasets');
            const data = await response.json();
            setDatasets(data?.data.content || []);
            setError(null);
        } catch (err) {
            console.error('Failed to load datasets:', err);
            setError(err.message || 'Failed to load datasets');
            setDatasets([]);
        }
    }, []);

    useEffect(() => {
        loadDatasets();
    }, [loadDatasets]);

    return (
        <div className="section">
            <h2 className="section-title">Dataset Manager</h2>
            <div
                className={`tab ${activeTab === 'upload' ? 'active' : ''}`}
                onClick={() => setActiveTab('upload')}
            >
                Upload
            </div>
            <div
                className={`tab ${activeTab === 'list' ? 'active' : ''}`}
                onClick={() => setActiveTab('list')}
            >
                Preview
            </div>
            {activeTab === 'upload' ? (
                <DatasetUpload onSuccess={() => { loadDatasets(); onRefresh(); }} />
            ) : (
                <DatasetList datasets={datasets} onRefresh={loadDatasets} />
            )}
        </div>

    );

}

export default DatasetManager;