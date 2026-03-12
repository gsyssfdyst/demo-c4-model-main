import React, { useState, useEffect, useCallback } from 'react';
import { Book, Calendar, CheckCircle, RefreshCcw } from 'lucide-react';

const API_URL = 'http://localhost:8080/api/loans';

const MyRentals = ({ userId }) => {
  const [loans, setLoans] = useState([]);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState({ text: '', type: '' });

  const showMessage = useCallback((text, type) => {
    setMessage({ text, type });
    setTimeout(() => setMessage({ text: '', type: '' }), 4000);
  }, []);

  const fetchLoans = useCallback(async () => {
    setLoading(true);
    try {
      const res = await fetch(`${API_URL}/user/${userId}`);
      if (res.ok) {
        const data = await res.json();
        const sorted = data.sort((a, b) => {
          if (a.status === b.status) return new Date(b.loanDate) - new Date(a.loanDate);
          return a.status === 'ACTIVE' ? -1 : 1;
        });
        setLoans(sorted);
      } else {
        throw new Error('Failed to fetch loans');
      }
    } catch (error) {
      console.error('Falha ao buscar empréstimos:', error);
      showMessage('Erro ao carregar o histórico de aluguéis.', 'error');
    } finally {
      setLoading(false);
    }
  }, [userId, showMessage]);

  useEffect(() => {
    fetchLoans();
  }, [userId]);

  const handleReturn = async (loanId) => {
    if (!window.confirm('Devolver este livro?')) return;
    
    try {
      const response = await fetch(`${API_URL}/return/${loanId}`, {
        method: 'POST'
      });
      
      if (response.ok) {
        showMessage('Livro devolvido com sucesso!', 'success');
        fetchLoans();
      } else {
        throw new Error('Falha na devolução');
      }
    } catch (error) {
      console.error('Erro ao devolver o livro:', error);
      showMessage('Falha ao processar a devolução', 'error');
    }
  };

  if (loading) {
    return <div className="loading">Verificando seus registros...</div>;
  }

  return (
    <div style={{ animation: 'fadeInUp 0.5s ease-out' }}>
      
      {message.text && (
        <div style={{
          padding: '1rem',
          borderRadius: '8px',
          marginBottom: '1rem',
          backgroundColor: message.type === 'success' ? 'rgba(16, 185, 129, 0.2)' : 'rgba(239, 68, 68, 0.2)',
          border: `1px solid ${message.type === 'success' ? 'rgba(16, 185, 129, 0.5)' : 'rgba(239, 68, 68, 0.5)'}`,
          color: message.type === 'success' ? '#a7f3d0' : '#fca5a5'
        }}>
          {message.text}
        </div>
      )}

      {loans.length === 0 ? (
        <div className="glass-card" style={{ textAlign: 'center', padding: '4rem 2rem' }}>
          <Book size={48} color="#94a3b8" style={{ marginBottom: '1rem' }} />
          <h2>Você ainda não alugou nenhum livro.</h2>
          <p style={{ color: 'var(--text-muted)' }}>Navegue na biblioteca para encontrar sua próxima leitura!</p>
        </div>
      ) : (
        <div className="books-grid">
          {loans.map(loan => (
            <div key={loan.id} className="glass-card book-item" style={{ opacity: loan.status === 'RETURNED' ? '0.6' : '1' }}>
              <div className="book-header">
                <div>
                  <h3 className="book-title">{loan.book.title}</h3>
                  <div className="book-author" style={{ fontSize: '0.85rem' }}>Transação #{loan.id}</div>
                </div>
                {loan.status === 'ACTIVE' ? (
                  <span className="badge" style={{ backgroundColor: 'rgba(59, 130, 246, 0.2)', color: '#93c5fd', border: '1px solid rgba(59, 130, 246, 0.3)' }}>
                    Lendo
                  </span>
                ) : (
                  <span className="badge badge-unavailable">
                    Devolvido
                  </span>
                )}
              </div>
              
              <div className="book-footer" style={{ flexDirection: 'column', alignItems: 'flex-start', gap: '1rem' }}>
                <div style={{ width: '100%', display: 'flex', justifyContent: 'space-between', fontSize: '0.85rem', color: 'var(--text-muted)' }}>
                  <span><Calendar size={12} style={{marginRight: '4px'}}/> Emprestado em: {new Date(loan.loanDate).toLocaleDateString('pt-BR')}</span>
                  {loan.status === 'ACTIVE' && loan.dueDate && (
                    <span style={{ color: '#fca5a5' }}>Devolver até: {new Date(loan.dueDate).toLocaleDateString('pt-BR')}</span>
                  )}
                  {loan.returnDate && (
                    <span>Devolvido em: {new Date(loan.returnDate).toLocaleDateString('pt-BR')}</span>
                  )}
                </div>
                
                {loan.status === 'ACTIVE' && (
                  <button 
                    className="btn" 
                    onClick={() => handleReturn(loan.id)}
                    style={{ background: 'transparent', border: '1px solid var(--accent)', color: 'var(--text-main)', padding: '0.5rem' }}
                  >
                    <RefreshCcw size={16} /> Devolver Livro
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default MyRentals;
