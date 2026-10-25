import { useState } from 'react';

function DatasetUpload({ onSuccess }) {
    const [formData, setFormData] = useState({
        name: '',
        description: '',
        data: '',
        featureNames: ''
    });
    const [loading, setLoading] = useState(false);
    const [result, setResult] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setResult(null);

        try {
            let data;
            if (formData.data.trim().startsWith('[')) {
                data = JSON.parse(formData.data);
            } else {
                data = parseCSV(formData.data);
            }
            const payload = {
                name: formData.name,
                description: formData.description || null,
                data: data,
                featureNames: formData.featureNames ? formData.featureNames.split(', ').map(s => s.trim()) : []
            }

            const response = await fetch('http://localhost:8080/api/datasets', {
                headers: { 'Content-Type': 'application/json' },
                method: 'POST',
                body: JSON.stringify(payload)
            });
            const responseData = await response.json();
            setResult({ type: 'success', message: `Dataset created! ID: ${responseData.data.id}` });
            setFormData({
                name: '',
                description: '',
                data: '',
                featureNames: ''
            });
            onSuccess();
        } catch (err) {
            setResult({
                type: 'error',
                message: err.response?.data?.error || err.message
            });
        } finally {
            setLoading(false)
        }
    };

    const parseCSV = (text) => {
        const lines = text.split('\n');
        const data = [];
        for (let i = 1; i < lines.length; i++) {
            const value = lines[i].split(', ').map(v => parseFloat(v.trim()));
            const feature = value.slice(0, -1);
            const label = value[value.length - 1];
            data.push({ feature, label });
        }
        return data;
    }

    return (
        <form onSubmit={handleSubmit}>
            <div className="form-group">
                <label>Dataset name *</label>
                <input
                    type="text"
                    className="form-control"
                    value={formData.name}
                    onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                    required
                    placeholder="Example: Sales Analyse"
                />
            </div>

            <div className="form-group">
                <label>Description</label>
                <textarea
                    className="form-control"
                    value={formData.description}
                    onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                    rows="3"
                    placeholder="Short description of datas" />
            </div>

            <div className="form-group">
                <label>Data (CSV or JSON) *</label>
                <textarea
                    className="form-control"
                    value={formData.data}
                    onChange={(e) => setFormData({ ...formData, data: e.target.value })}
                    rows="8"
                    required
                    placeholder={`CSV example:\nfeature1, feature2, label\n1.0, 2.0,1\n0.5, 1.5,0\n\nJSON example:\n[{"features":[1.0, 2.0], "label":1}]`}
                />
            </div>

            <div className="form-group">
                <label>Names of features (separated by commas)</label>
                <input
                    type="text"
                    className="form-control"
                    value={formData.featureNames}
                    onChange={(e) => setFormData({ ...formData, featureNames: e.target.value })}
                    placeholder="price, quantity, category"
                />
            </div>

            <button type="submit" className="btn" disabled={loading}>
                {loading ? (
                    <div className="loading">
                        <div className="spinner"></div>
                        <span>Loading...</span>
                    </div>
                ) : '📤 Upload Dataset'}
            </button>

            {result && (
                <div className={`alert alert-${result.type}`}>
                    {result.message}
                </div>
            )}
        </form>
    );
}

export default DatasetUpload;