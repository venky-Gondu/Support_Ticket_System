import { useState, useEffect } from 'react';
import axios from 'axios';

function StatsDashboard() {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await axios.get('/api/tickets/stats', {
        timeout: 5000,
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        }
      });
      
      console.log('Stats API Response:', response.data);
      
      // Ensure we have valid data with defaults
      const data = response.data;
      const safeStats = {
        total_tickets: data.total_tickets ?? data.totalTickets ?? 0,
        open_tickets: data.open_tickets ?? data.openTickets ?? 0,
        avg_tickets_per_day: data.avg_tickets_per_day ?? data.avgTicketsPerDay ?? 0,
        priority_breakdown: data.priority_breakdown ?? data.priorityBreakdown ?? {},
        category_breakdown: data.category_breakdown ?? data.categoryBreakdown ?? {}
      };
      
      console.log('Processed Stats:', safeStats);
      setStats(safeStats);
    } catch (err) {
      console.error('Stats fetch error:', err);
      console.error('Error response:', err.response?.data);
      console.error('Error status:', err.response?.status);
      
      let errorMsg = 'Failed to load statistics';
      if (err.response?.status === 404) {
        errorMsg = 'Stats endpoint not found. Check if backend is running on port 8080.';
      } else if (err.response?.status === 500) {
        errorMsg = 'Server error. Check backend logs.';
      } else if (err.code === 'ECONNREFUSED') {
        errorMsg = 'Cannot connect to backend. Make sure Spring Boot is running on port 8080.';
      }
      
      setError(errorMsg);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="card">
        <div className="loading">Loading statistics...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="card">
        <div className="error">{error}</div>
        <button onClick={fetchStats} className="btn-primary" style={{marginTop: '20px'}}>
          Retry
        </button>
      </div>
    );
  }

  if (!stats) {
    return (
      <div className="card">
        <div className="error">No data available</div>
      </div>
    );
  }

  return (
    <div className="stats-container">
      <div className="card">
        <h2>Statistics Dashboard</h2>
        
        <div className="stats-grid">
          <div className="stat-card">
            <h3>{stats.total_tickets}</h3>
            <p>Total Tickets</p>
          </div>
          <div className="stat-card">
            <h3>{stats.open_tickets}</h3>
            <p>Open Tickets</p>
          </div>
          <div className="stat-card">
            <h3>{typeof stats.avg_tickets_per_day === 'number' ? stats.avg_tickets_per_day.toFixed(1) : '0.0'}</h3>
            <p>Avg Per Day</p>
          </div>
        </div>

        <div className="breakdowns">
          <div className="breakdown">
            <h3>Priority Breakdown</h3>
            {Object.keys(stats.priority_breakdown).length === 0 ? (
              <p className="empty">No tickets yet</p>
            ) : (
              Object.entries(stats.priority_breakdown).map(([priority, count]) => (
                <div key={priority} className="breakdown-item">
                  <span className="priority-label">{priority}</span>
                  <div className="progress-bar">
                    <div 
                      className="progress-fill" 
                      style={{ 
                        width: `${stats.total_tickets > 0 ? (count / stats.total_tickets) * 100 : 0}%`
                      }}
                    ></div>
                  </div>
                  <span className="count">{count}</span>
                </div>
              ))
            )}
          </div>

          <div className="breakdown">
            <h3>Category Breakdown</h3>
            {Object.keys(stats.category_breakdown).length === 0 ? (
              <p className="empty">No tickets yet</p>
            ) : (
              Object.entries(stats.category_breakdown).map(([category, count]) => (
                <div key={category} className="breakdown-item">
                  <span className="category-label">{category}</span>
                  <div className="progress-bar">
                    <div 
                      className="progress-fill" 
                      style={{ 
                        width: `${stats.total_tickets > 0 ? (count / stats.total_tickets) * 100 : 0}%`
                      }}
                    ></div>
                  </div>
                  <span className="count">{count}</span>
                </div>
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default StatsDashboard;