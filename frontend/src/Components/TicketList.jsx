import { useState, useEffect } from 'react';
import axios from 'axios';

function TicketList() {
  const [tickets, setTickets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filters, setFilters] = useState({
    category: '',
    priority: '',
    status: '',
    search: ''
  });

  useEffect(() => {
    fetchTickets();
  }, [filters]);

  const fetchTickets = async () => {
    setLoading(true);
    try {
      const params = new URLSearchParams();
      if (filters.category) params.append('category', filters.category);
      if (filters.priority) params.append('priority', filters.priority);
      if (filters.status) params.append('status', filters.status);
      if (filters.search) params.append('search', filters.search);

      const response = await axios.get(`/api/tickets/?${params.toString()}`);
      setTickets(response.data);
    } catch (err) {
      console.error('Failed to fetch tickets:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleFilterChange = (e) => {
    setFilters(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleStatusUpdate = async (id, newStatus) => {
    try {
      await axios.patch(`/api/tickets/${id}`, { status: newStatus });
      fetchTickets(); // Refresh list
    } catch (err) {
      alert('Failed to update status');
    }
  };

  const getStatusColor = (status) => {
    const colors = {
      OPEN: '#28a745',
      IN_PROGRESS: '#ffc107',
      RESOLVED: '#17a2b8',
      CLOSED: '#6c757d'
    };
    return colors[status] || '#6c757d';
  };

  const getPriorityColor = (priority) => {
    const colors = {
      LOW: '#28a745',
      MEDIUM: '#ffc107',
      HIGH: '#fd7e14',
      CRITICAL: '#dc3545'
    };
    return colors[priority] || '#6c757d';
  };

  return (
    <div className="card">
      <h2>📋 Ticket List</h2>
      
      {/* Filters */}
      <div className="filters">
        <input
          type="text"
          name="search"
          placeholder="🔍 Search tickets..."
          value={filters.search}
          onChange={handleFilterChange}
          className="filter-input"
        />
        <select name="category" value={filters.category} onChange={handleFilterChange}>
          <option value="">All Categories</option>
          <option value="BILLING">Billing</option>
          <option value="TECHNICAL">Technical</option>
          <option value="ACCOUNT">Account</option>
          <option value="GENERAL">General</option>
        </select>
        <select name="priority" value={filters.priority} onChange={handleFilterChange}>
          <option value="">All Priorities</option>
          <option value="LOW">Low</option>
          <option value="MEDIUM">Medium</option>
          <option value="HIGH">High</option>
          <option value="CRITICAL">Critical</option>
        </select>
        <select name="status" value={filters.status} onChange={handleFilterChange}>
          <option value="">All Statuses</option>
          <option value="OPEN">Open</option>
          <option value="IN_PROGRESS">In Progress</option>
          <option value="RESOLVED">Resolved</option>
          <option value="CLOSED">Closed</option>
        </select>
      </div>

      {/* Ticket Table */}
      {loading ? (
        <div className="loading">Loading tickets...</div>
      ) : tickets.length === 0 ? (
        <div className="empty">No tickets found</div>
      ) : (
        <table className="ticket-table">
          <thead>
            <tr>
              <th>Title</th>
              <th>Category</th>
              <th>Priority</th>
              <th>Status</th>
              <th>Created</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {tickets.map(ticket => (
              <tr key={ticket.id}>
                <td>
                  <strong>{ticket.title}</strong>
                  <br />
                  <small>{ticket.description.substring(0, 50)}...</small>
                </td>
                <td><span className="badge">{ticket.category}</span></td>
                <td>
                  <span className="badge" style={{ backgroundColor: getPriorityColor(ticket.priority) }}>
                    {ticket.priority}
                  </span>
                </td>
                <td>
                  <span className="badge" style={{ backgroundColor: getStatusColor(ticket.status) }}>
                    {ticket.status}
                  </span>
                </td>
                <td>{new Date(ticket.createdAt).toLocaleDateString()}</td>
                <td>
                  <select
                    value={ticket.status}
                    onChange={(e) => handleStatusUpdate(ticket.id, e.target.value)}
                    className="status-select"
                  >
                    <option value="OPEN">Open</option>
                    <option value="IN_PROGRESS">In Progress</option>
                    <option value="RESOLVED">Resolved</option>
                    <option value="CLOSED">Closed</option>
                  </select>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default TicketList;