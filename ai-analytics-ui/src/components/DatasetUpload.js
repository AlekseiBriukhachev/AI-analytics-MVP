import { useState } from 'react';
import { apiRequest } from '../api';

export function parseCsv(text) {
    const rows = text
        .split(/\r?\n/)
        .map(line => line.trim())
        .filter(Boolean)
        .map(line => line.split(',').map(value => value.trim()));

    if (rows.length === 0) {
        throw new Error('Enter at least one row of data.');
    }

    const hasHeader = rows[0].every(value => value !== '' && !Number.isFinite(Number(value)));
    const headers = hasHeader ? rows.shift() : null;
    if (headers && headers.length < 2) {
        throw new Error('CSV headers must include at least one feature and a label.');
    }
    const data = rows.map((row, index) => {
        if (row.length < 2 || row.some(value => value === '' || !Number.isFinite(Number(value)))) {
            throw new Error(`Row ${index + (hasHeader ? 2 : 1)} must contain numeric features and a numeric label.`);
        }

        return {
            features: row.slice(0, -1).map(Number),
            label: Number(row[row.length - 1])
        };
    });

    if (data.length === 0) {
        throw new Error('Enter at least one row of data.');
    }

    const featureCount = data[0].features.length;
    if (data.some((point, index) => (
        point.features.length !== featureCount ||
        (headers && rows[index].length !== headers.length)
    ))) {
        throw new Error('Each row must contain the same number of features.');
    }

    return { data, headers };
}

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
            let csvHeaders = null;
            if (formData.data.trim().startsWith('[')) {
                data = JSON.parse(formData.data);
                if (!Array.isArray(data)) {
                    throw new Error('JSON dataset data must be an array.');
                }
            } else {
                const parsedCsv = parseCsv(formData.data);
                data = parsedCsv.data;
                csvHeaders = parsedCsv.headers;
            }
            const featureNames = formData.featureNames
                .split(',')
                .map(name => name.trim())
                .filter(Boolean);
            const payload = {
                name: formData.name,
                description: formData.description || null,
                data: data,
                featureNames: featureNames.length
                    ? featureNames
                    : (csvHeaders ? csvHeaders.slice(0, -1) : [])
            };

            const responseData = await apiRequest('/api/datasets', {
                headers: { 'Content-Type': 'application/json' },
                method: 'POST',
                body: JSON.stringify(payload)
            });
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
                message: err.message
            });
        } finally {
            setLoading(false)
        }
    };

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