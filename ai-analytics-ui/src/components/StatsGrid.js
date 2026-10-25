function StatsGrid({ stats }) {

    const statItems =[
        { label: 'Total Datasets', value: stats?.totalDatasets || 0, icon: '📊' },
        { label: 'Total Models', value: stats?.totalModels || 0, icon: '🤖' },
        { label: 'Total Datasets last 24h', value: (stats?.datasetsLast24h || 0) + (stats?.modelsLast24h || 0), icon: '📈' },
        { label: 'Total Datasets last 7d', value: (stats?.datasetsLast7d || 0) + (stats?.modelsLast7d || 0), icon: '📅' }
    ]
    return(
        <div className="stats-grid">
            {statItems.map((stat, index) => (
                <div key={index} className="stat-card">
                    <div className="stat-value">{stat.icon}</div>
                    <div className="stat-label">{stat.label}</div>
                </div>
            ))}
        </div>
    );
}

export default StatsGrid;