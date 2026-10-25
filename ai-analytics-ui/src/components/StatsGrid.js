function StatsGrid({ stats }) {

    const statItems = [
        { label: 'Total datasets', value: stats?.totalDatasets ?? 0, icon: '📊' },
        { label: 'Total models', value: stats?.totalModels ?? 0, icon: '🤖' },
        {
            label: 'Created in the last 24 hours',
            value: (stats?.datasetsLast24h ?? 0) + (stats?.modelsLast24h ?? 0),
            icon: '📈'
        },
        {
            label: 'Created in the last 7 days',
            value: (stats?.datasetsLast7d ?? 0) + (stats?.modelsLast7d ?? 0),
            icon: '📅'
        }
    ];
    return (
        <div className="stats-grid">
            {statItems.map(stat => (
                <div key={stat.label} className="stat-card">
                    <div className="stat-icon" aria-hidden="true">{stat.icon}</div>
                    <div className="stat-value">{stat.value}</div>
                    <div className="stat-label">{stat.label}</div>
                </div>
            ))}
        </div>
    );
}

export default StatsGrid;