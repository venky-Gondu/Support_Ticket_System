import { useState } from 'react';
import TicketForm from './components/TicketForm';
import TicketList from './components/TicketList';
import StatsDashboard from './components/StatsDashboard';
import './App.css';

function App() {
  const [activeTab, setActiveTab] = useState('list');
  const [refreshTrigger, setRefreshTrigger] = useState(0);

  const triggerRefresh = () => setRefreshTrigger(prev => prev + 1);

  return (
    <div className="app">
      <header className="header">
        <h1>Support Ticket System</h1>
        <nav className="nav">
          <button 
            className={activeTab === 'list' ? 'active' : ''} 
            onClick={() => setActiveTab('list')}
          >
            Tickets
          </button>
          <button 
            className={activeTab === 'submit' ? 'active' : ''} 
            onClick={() => setActiveTab('submit')}
          >
            Submit Ticket
          </button>
          <button 
            className={activeTab === 'stats' ? 'active' : ''} 
            onClick={() => setActiveTab('stats')}
          >
            Statistics
          </button>
        </nav>
      </header>

      <main className="main">
        {activeTab === 'submit' && (
          <TicketForm onTicketSubmitted={triggerRefresh} />
        )}
        {activeTab === 'list' && (
          <TicketList key={refreshTrigger} />
        )}
        {activeTab === 'stats' && (
          <StatsDashboard key={refreshTrigger} />
        )}
      </main>
    </div>
  );
}

export default App;