import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import { Book, Library, Calendar, User, LogOut, Bookmark, BookOpen } from 'lucide-react';
import MyRentals from './MyRentals';

const BOOKS_API = 'http://localhost:8080/api/books';
const RENT_API = 'http://localhost:8080/api/loans/rent';

const ClientShowcase = () => {
  const { user, logout, isClient } = useAuth();
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('browse');
  
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState({ text: '', type: '' });

  useEffect(() => {
    if (!isClient()) {
      navigate('/login');
    }
  }, [user, navigate]);

  const fetchBooks = async () => {
    try {
      const res = await fetch(BOOKS_API);
      if (res.ok) {
        setBooks(await res.json());
      }
    } catch (error) {
      console.error('Falha ao buscar livros:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (activeTab === 'browse') {
      fetchBooks();
    }
  }, [activeTab]);

  const showMessage = (text, type) => {
    setMessage({ text, type });
    setTimeout(() => setMessage({ text: '', type: '' }), 4000);
  };

  const handleRent = async (bookId) => {
    if (!window.confirm('Deseja pegar este livro emprestado?')) return;
    
    try {
      const response = await fetch(`${RENT_API}?userId=${user.id}&bookId=${bookId}`, {
        method: 'POST'
      });
      
      if (response.ok) {
        showMessage('Livro solicitado com sucesso!', 'success');
        fetchBooks();
      } else {
        const errorText = await response.text();
        showMessage(errorText || 'Falha ao alugar o livro', 'error');
      }
    } catch (error) {
      console.error('Erro ao alugar livro:', error);
      showMessage('Erro de rede ao solicitar o livro', 'error');
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  if (!user || user.role !== 'CLIENT') return null;

  // Filter ONLY available books
  const availableBooks = books.filter(book => book.available);

  return (
    <div className="app-container" style={{ position: 'relative' }}>
      {/* Ambient orbs */}
      <div className="orb-bg">
        <div className="orb orb-blue" style={{ width: '500px', height: '500px', top: '-200px', left: '-100px', animationDuration: '22s' }} />
        <div className="orb orb-sky" style={{ width: '300px', height: '300px', bottom: '0', right: '-150px', animationDuration: '18s', animationDelay: '-9s' }} />
      </div>
      <header className="header animate-fadeInDown" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem', textAlign: 'left' }}>
        <div>
          <h1 style={{ fontSize: '2.5rem', marginBottom: '0.2rem' }}>
            <Library size={36} style={{verticalAlign: 'middle', marginRight: '0.8rem', color: '#c084fc'}}/>
            Grande Biblioteca
          </h1>
          <p style={{ margin: 0, padding: 0 }}>Descubra sua próxima leitura, <strong style={{color: 'white'}}>{user.name}</strong></p>
        </div>
        <button className="btn" onClick={handleLogout} style={{ width: 'auto', background: 'rgba(255, 255, 255, 0.1)' }}>
          <LogOut size={18} /> Sair
        </button>
      </header>

      <div className="animate-fadeInUp delay-200" style={{ display: 'flex', gap: '1rem', marginBottom: '2rem' }}>
        <button 
          className="btn" 
          onClick={() => setActiveTab('browse')}
          style={{ 
            width: 'auto', 
            background: activeTab === 'browse' ? 'var(--accent)' : 'var(--glass-bg)',
            border: activeTab === 'browse' ? 'none' : '1px solid var(--glass-border)'
          }}
        >
          <BookOpen size={18} /> Livros Disponíveis
        </button>
        <button 
          className="btn" 
          onClick={() => setActiveTab('rentals')}
          style={{ 
            width: 'auto', 
            background: activeTab === 'rentals' ? 'var(--accent)' : 'var(--glass-bg)',
            border: activeTab === 'rentals' ? 'none' : '1px solid var(--glass-border)'
          }}
        >
          <Bookmark size={18} /> Meus Empréstimos
        </button>
      </div>

      {message.text && (
        <div className="animate-popIn" style={{
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

      {activeTab === 'rentals' ? (
        <MyRentals userId={user.id} />
      ) : (
        <main style={{ animation: 'fadeInUp 0.5s ease-out' }}>
          {loading ? (
            <div className="loading">Navegando pelas prateleiras...</div>
          ) : availableBooks.length === 0 ? (
            <div className="glass-card" style={{ textAlign: 'center', padding: '4rem 2rem' }}>
              <Book size={48} color="#94a3b8" style={{ marginBottom: '1rem' }} />
              <h2>Nenhum livro disponível no momento</h2>
            </div>
          ) : (
            <div className="books-grid">
              {availableBooks.map(book => (
                <div key={book.id} className="glass-card book-item">
                  <div className="book-header">
                    <div>
                      <h3 className="book-title">{book.title}</h3>
                      <div className="book-author"><User size={14}/> {book.author}</div>
                    </div>
                  </div>
                  
                  <div className="book-footer">
                    <span className="book-date">
                      <Calendar size={12} style={{verticalAlign: 'middle', marginRight: '4px'}}/>
                      {book.publishedDate ? new Date(book.publishedDate).getFullYear() : 'N/A'}
                    </span>
                    
                    <button 
                      className="btn btn-primary" 
                      onClick={() => handleRent(book.id)}
                      style={{ width: 'auto', padding: '0.4rem 1rem', fontSize: '0.9rem' }}
                    >
                      Alugar Livro
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </main>
      )}
    </div>
  );
};

export default ClientShowcase;
