import './App.css';
import Header from './components/Header';
import StatsGrid from './components/StatsGrid';
import MainContent from './components/MainContent';
import { useState, useEffect, useCallback } from 'react';
import { apiRequest } from './api';


function App() {
    const [dashboard, setDashboard] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const loadDashboard = useCallback(async () => {
        try {
            setLoading(true);
            const data = await apiRequest('/api/analytics/dashboard');
            setDashboard(data);
            setError(null);
        } catch (err) {
            setError(err.message);
            setDashboard(null);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        loadDashboard();
    }, [loadDashboard]);

    return (
        <div className="app-container fade-in">
            <Header />
            {loading && !dashboard && (
                <div className="loading" role="status">
                    <div className="spinner"></div>
                    <span>Loading platform...</span>
                </div>
            )}
            {error && (
                <div className="alert alert-error" role="alert">
                    <span>Loading error: {error}</span>
                    <button type="button" className="btn btn-secondary retry-button" onClick={loadDashboard}>
                        Retry
                    </button>
                </div>
            )}
            {dashboard && <StatsGrid stats={dashboard} />}
            <MainContent onRefresh={loadDashboard} />
        </div>
    );
}

export default App;
