import './App.css';
import Header from './components/Header';
import StatsGrid from './components/StatsGrid';
import MainContent from './components/MainContent';
import { useState, useEffect, useCallback } from 'react';


function App() {
    const [dashboard, setDashboard] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const loadDashboard = useCallback(async () => {
        try {
            setLoading(true);
            const response = await fetch('api/analytics/dashboard');
            setDashboard(response.data);
            setError(null);
        } catch (err) {
            setError(err.response?.data?.message || err.message);
            setDashboard(null);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        loadDashboard();
        const interval = setInterval(loadDashboard, 30000); // Refresh every 30 seconds
        return () => clearInterval(interval);
    }, [loadDashboard]);

    if (loading &&!dashboard) {
        return (
            <div className="app-container">
                <div className="loading">
                    <span>Platform loading...</span>
                </div>
            </div>
        );
    }

    return (
        <div className="app-container fade-in">
            <Header/>
            {error && (
                <div className="alert alert-error">
                    <span>Loading error: {error}</span>
                </div>
            )}
            {dashboard && <StatsGrid stats={dashboard} />}
            <MainContent onRefresh={loadDashboard} />
        </div>
    );
}

export default App;
