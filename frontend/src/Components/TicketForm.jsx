import { useState } from 'react';
import axios from 'axios';

const CATEGORIES = ['BILLING', 'TECHNICAL', 'ACCOUNT', 'GENERAL'];
const PRIORITIES = ['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'];

function TicketForm({ onTicketSubmitted }) {
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    category: '',
    priority: ''
  });
  const [loading, setLoading] = useState(false);
  const [classifying, setClassifying] = useState(false);
  const [error, setError] = useState('');

const handleDescriptionBlur = async () => {
  if (formData.description.trim().length < 10) return;
  
  setClassifying(true);
  setError('');
  
  try {
    const response = await axios.post('/api/tickets/classify', {
      description: formData.description.trim()
    });
    
    console.log('Raw response:', response.data);
    
    // FIX: Use correct field names from backend
    if (response.data.category) {
      setFormData(prev => ({ 
        ...prev, 
        category: response.data.category.toUpperCase() 
      }));
      console.log('Category set to:', response.data.category);
    }
    if (response.data.priority) {
      setFormData(prev => ({ 
        ...prev, 
        priority: response.data.priority.toUpperCase() 
      }));
      console.log('Priority set to:', response.data.priority);
    }
  } catch (err) {
    console.error('Classification failed:', err);
    setError('AI classification unavailable. Please select manually.');
  } finally {
    setClassifying(false);
  }
};

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      console.log('Submitting ticket:', formData);
      
      const payload = {
        title: formData.title.trim(),
        description: formData.description.trim(),
        category: formData.category,
        priority: formData.priority
      };
      
      console.log('Payload:', payload);
      
      await axios.post('/api/tickets/', payload, {
        headers: {
          'Content-Type': 'application/json'
        }
      });
      
      // Reset form
      setFormData({ title: '', description: '', category: '', priority: '' });
      
      // Trigger parent refresh
      onTicketSubmitted();
      
      alert('Ticket submitted successfully!');
    } catch (err) {
      console.error('Submit failed:', err);
      console.error('Error response:', err.response?.data);
      setError(err.response?.data?.message || 'Failed to submit ticket');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    console.log(`Field ${name} changed to:`, value);
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  return (
    <div className="card">
      <h2>Submit New Ticket</h2>
      <form onSubmit={handleSubmit} className="form">
        <div className="form-group">
          <label htmlFor="title">Title</label>
          <input
            id="title"
            type="text"
            name="title"
            value={formData.title}
            onChange={handleChange}
            maxLength={200}
            required
            placeholder="Brief summary of the issue"
            autoComplete="off"
          />
        </div>

        <div className="form-group">
          <label htmlFor="description">Description</label>
          <textarea
            id="description"
            name="description"
            value={formData.description}
            onChange={handleChange}
            onBlur={handleDescriptionBlur}
            required
            placeholder="Describe your issue (AI will suggest category and priority after 10 characters)"
            rows={5}
          />
          {classifying && <span className="loading-text">Classifying with AI...</span>}
        </div>

        <div className="form-row">
          <div className="form-group">
          <label htmlFor="category">Category</label>
          <select 
            id="category"
            name="category" 
            value={formData.category || ''} 
            onChange={handleChange} 
            required
          >
            <option value="">Select category</option>
            <option value="BILLING">BILLING</option>
            <option value="TECHNICAL">TECHNICAL</option>
            <option value="ACCOUNT">ACCOUNT</option>
            <option value="GENERAL">GENERAL</option>
          </select>
          {formData.category && (
            <span className="loading-text">Selected: {formData.category}</span>
          )}
        </div>

        <div className="form-group">
          <label htmlFor="priority">Priority</label>
          <select 
            id="priority"
            name="priority" 
            value={formData.priority || ''} 
            onChange={handleChange} 
            required
          >
            <option value="">Select priority</option>
            <option value="LOW">LOW</option>
            <option value="MEDIUM">MEDIUM</option>
            <option value="HIGH">HIGH</option>
            <option value="CRITICAL">CRITICAL</option>
          </select>
          {formData.priority && (
            <span className="loading-text">Selected: {formData.priority}</span>
          )}
        </div>
        </div>

        {error && <div className="error">{error}</div>}

        <button type="submit" className="btn-primary" disabled={loading}>
          {loading ? 'Submitting...' : 'Submit Ticket'}
        </button>
      </form>
    </div>
  );
}

export default TicketForm;