import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import ManageUsers from './ManageUsers';
import { Book, Library, Trash2, Calendar, User, CheckCircle, XCircle, LogOut, LayoutDashboard, Users } from 'lucide-react';

const BOOKS_API = 'http://localhost:8080/api/books';

const AdminDashboard = () => {
  const { user, logout, isAdmin } = useAuth();
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('books');
  
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  
  const [title, setTitle] = useState('');
  const [author, setAuthor] = useState('');
  const [date, setDate] = useState('');
  const [available, setAvailable] = useState(true);

  useEffect(() => {
    if (!isAdmin()) {
      navigate('/login');
    }
  }, [user, navigate]);

  const fetchBooks = async () => {
    try {
      const res = await fetch(BOOKS_API);
      if (res.ok) {
        const data = await res.json();
        setBooks(data);
      }
    } catch (error) {
      console.error('Falha ao buscar livros:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (activeTab === 'books') {
      fetchBooks();
    }
  }, [activeTab]);

  const handleBookSubmit = async (e) => {
    e.preventDefault();
    if (!title || !author) return;

    try {
      const response = await fetch(BOOKS_API, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          title,
          author,
          publishedDate: date ? new Date(date).toISOString() : new Date().toISOString(),
          available
        })
      });

      if (response.ok) {
        setTitle('');
        setAuthor('');
        setDate('');
        setAvailable(true);
        fetchBooks();
      }
    } catch (error) {
      console.error('Erro ao salvar livro:', error);
    }
  };

  const handleBookDelete = async (id) => {
    if (!window.confirm('Tem certeza de que deseja excluir este livro?')) return;
    
    try {
      await fetch(`${BOOKS_API}/${id}`, { method: 'DELETE' });
      fetchBooks();
    } catch (error) {
      console.error('Erro ao excluir livro:', error);
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  if (!user || user.role !== 'ADMIN') return null;

  return (
    <div className="app-container">
      <header className="header" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem', textAlign: 'left' }}>
        <div>
          <h1 style={{ fontSize: '2.5rem', marginBottom: '0.2rem' }}>
            <Library size={36} style={{verticalAlign: 'middle', marginRight: '0.8rem', color: '#c084fc'}}/>
            Painel do Administrador
          </h1>
          <p style={{ margin: 0, padding: 0 }}>Bem-vindo de volta, <strong style={{color: 'white'}}>{user.name}</strong></p>
        </div>
        <button className="btn" onClick={handleLogout} style={{ width: 'auto', background: 'rgba(255, 255, 255, 0.1)' }}>
          <LogOut size={18} /> Sair
        </button>
      </header>

      <div style={{ display: 'flex', gap: '1rem', marginBottom: '2rem' }}>
        <button 
          className="btn" 
          onClick={() => setActiveTab('books')}
          style={{ 
            width: 'auto', 
            background: activeTab === 'books' ? 'var(--accent)' : 'var(--glass-bg)',
            border: activeTab === 'books' ? 'none' : '1px solid var(--glass-border)'
          }}
        >
          <LayoutDashboard size={18} /> Gerenciar Livros
        </button>
        <button 
          className="btn" 
          onClick={() => setActiveTab('users')}
          style={{ 
            width: 'auto', 
            background: activeTab === 'users' ? 'var(--accent)' : 'var(--glass-bg)',
            border: activeTab === 'users' ? 'none' : '1px solid var(--glass-border)'
          }}
        >
          <Users size={18} /> Gerenciar Usuários
        </button>
      </div>

      {activeTab === 'users' ? (
        <ManageUsers />
      ) : (
        <main className="main-content" style={{ animation: 'fadeInUp 0.5s ease-out' }}>
          <aside className="sidebar">
            <div className="glass-card">
              <h2 style={{ marginBottom: '1.5rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                <Book size={20} color="#8b5cf6" /> Adicionar Novo Livro
              </h2>
              <form onSubmit={handleBookSubmit}>
                <div className="form-group">
                  <label>Título</label>
                  <input 
                    type="text" 
                    className="form-control" 
                    value={title} 
                    onChange={(e) => setTitle(e.target.value)} 
                    placeholder="ex. 1984"
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Autor</label>
                  <input 
                    type="text" 
                    className="form-control" 
                    value={author} 
                    onChange={(e) => setAuthor(e.target.value)} 
                    placeholder="ex. George Orwell"
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Data de Publicação</label>
                  <input 
                    type="date" 
                    className="form-control" 
                    value={date} 
                    onChange={(e) => setDate(e.target.value)} 
                  />
                </div>
                <div className="form-group">
                  <label className="checkbox-group">
                    <input 
                      type="checkbox" 
                      checked={available} 
                      onChange={(e) => setAvailable(e.target.checked)} 
                      style={{ accentColor: '#8b5cf6', width: '18px', height: '18px' }}
                    />
                    Disponível para Empréstimo
                  </label>
                </div>
                <button type="submit" className="btn btn-primary">
                  Adicionar ao Acervo
                </button>
              </form>
            </div>
          </aside>

          <section className="books-list">
            {loading ? (
              <div className="loading">Carregando acervo...</div>
            ) : books.length === 0 ? (
              <div className="glass-card" style={{ textAlign: 'center', padding: '4rem 2rem' }}>
                <Book size={48} color="#94a3b8" style={{ marginBottom: '1rem' }} />
                <h2>Nenhum livro encontrado</h2>
              </div>
            ) : (
              <div className="books-grid">
                {books.map(book => (
                  <div key={book.id} className="glass-card book-item">
                    <div className="book-header">
                      <div>
                        <h3 className="book-title">{book.title}</h3>
                        <div className="book-author"><User size={14}/> {book.author}</div>
                      </div>
                      {book.available ? (
                        <span className="badge badge-available" title="Disponível">
                          <CheckCircle size={14} style={{verticalAlign: 'middle', marginRight: '4px'}}/>
                          Sim
                        </span>
                      ) : (
                        <span className="badge badge-unavailable" title="Indisponível">
                          <XCircle size={14} style={{verticalAlign: 'middle', marginRight: '4px'}}/>
                          Não
                        </span>
                      )}
                    </div>
                    
                    <div className="book-footer">
                      <span className="book-date">
                        <Calendar size={12} style={{verticalAlign: 'middle', marginRight: '4px'}}/>
                        {book.publishedDate ? new Date(book.publishedDate).toLocaleDateString('pt-BR') : 'N/A'}
                      </span>
                      <button 
                        className="btn btn-danger" 
                        onClick={() => handleBookDelete(book.id)}
                        title="Excluir Livro"
                      >
                        <Trash2 size={16} />
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </section>
        </main>
      )}
    </div>
  );
};

export default AdminDashboard;
