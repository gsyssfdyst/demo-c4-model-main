import React, { useState, useEffect } from 'react';
import { Users, Edit2, Check, X, Shield, User as UserIcon } from 'lucide-react';

const API_URL = 'http://localhost:8080/api/users';

const ManageUsers = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [editingUserId, setEditingUserId] = useState(null);
  const [editForm, setEditForm] = useState({ name: '', email: '' });
  const [message, setMessage] = useState({ text: '', type: '' });

  const fetchUsers = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/users');
      if (response.ok) {
        const data = await response.json();
        setUsers(data);
      }
    } catch (error) {
      console.error('Erro ao buscar usuários:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const showMessage = (text, type) => {
    setMessage({ text, type });
    setTimeout(() => setMessage({ text: '', type: '' }), 4000);
  };

  const startEditing = (user) => {
    setEditingUserId(user.id);
    setEditForm({ name: user.name, email: user.email });
  };

  const cancelEditing = () => {
    setEditingUserId(null);
  };

  const handleUpdate = async (id) => {
    try {
      const response = await fetch(`${API_URL}/${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name: editForm.name, email: editForm.email }),
      });

      if (response.ok) {
        setEditingUserId(null);
        fetchUsers();
        showMessage('Usuário atualizado com sucesso!', 'success');
      } else {
        throw new Error('Falha na atualização');
      }
    } catch (error) {
      console.error('Erro ao atualizar o usuário:', error);
      showMessage('Falha ao atualizar o usuário', 'error');
    }
  };

  if (loading) {
    return <div className="loading">Carregando rede de usuários...</div>;
  }

  return (
    <div className="manage-users-container" style={{ animation: 'fadeInUp 0.5s ease-out' }}>
      <div style={{ display: 'flex', alignItems: 'center', marginBottom: '1.5rem', gap: '0.75rem' }}>
        <Users size={28} color="#c084fc" />
        <h2>Gerenciar Clientes e Funcionários</h2>
      </div>

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

      <div className="glass-card" style={{ padding: '0', overflowX: 'auto' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
          <thead>
            <tr style={{ borderBottom: '1px solid var(--glass-border)', backgroundColor: 'rgba(0, 0, 0, 0.2)' }}>
              <th style={{ padding: '1rem' }}>ID</th>
              <th style={{ padding: '1rem' }}>Nome</th>
              <th style={{ padding: '1rem' }}>E-mail</th>
              <th style={{ padding: '1rem' }}>Perfil</th>
              <th style={{ padding: '1rem', textAlign: 'right' }}>Ações</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id} style={{ borderBottom: '1px solid var(--glass-border)', transition: 'background-color 0.2s' }}>
                <td style={{ padding: '1rem', color: 'var(--text-muted)' }}>#{user.id}</td>
                
                <td style={{ padding: '1rem' }}>
                  {editingUserId === user.id ? (
                    <input 
                      type="text" 
                      className="form-control" 
                      value={editForm.name} 
                      onChange={(e) => setEditForm({...editForm, name: e.target.value})}
                      style={{ padding: '0.4rem', border: '1px solid #c084fc' }}
                    />
                  ) : (
                    <span style={{ fontWeight: '500' }}>{user.name}</span>
                  )}
                </td>
                
                <td style={{ padding: '1rem' }}>
                  {editingUserId === user.id ? (
                    <input 
                      type="email" 
                      className="form-control" 
                      value={editForm.email} 
                      onChange={(e) => setEditForm({...editForm, email: e.target.value})}
                      style={{ padding: '0.4rem', border: '1px solid #c084fc' }}
                    />
                  ) : (
                    <span style={{ color: 'var(--text-muted)' }}>{user.email}</span>
                  )}
                </td>
                
                <td style={{ padding: '1rem' }}>
                  {user.role === 'ADMIN' ? (
                    <span className="badge" style={{ backgroundColor: 'rgba(139, 92, 246, 0.2)', color: '#c084fc', border: '1px solid rgba(139, 92, 246, 0.3)' }}>
                      <Shield size={12} style={{marginRight: '4px', verticalAlign: 'middle'}}/>Admin
                    </span>
                  ) : (
                    <span className="badge" style={{ backgroundColor: 'rgba(59, 130, 246, 0.2)', color: '#93c5fd', border: '1px solid rgba(59, 130, 246, 0.3)' }}>
                      <UserIcon size={12} style={{marginRight: '4px', verticalAlign: 'middle'}}/>Cliente
                    </span>
                  )}
                </td>
                
                <td style={{ padding: '1rem', textAlign: 'right' }}>
                  {editingUserId === user.id ? (
                    <div style={{ display: 'flex', gap: '0.5rem', justifyContent: 'flex-end' }}>
                      <button 
                        onClick={() => handleUpdate(user.id)} 
                        className="btn" 
                        style={{ padding: '0.4rem', background: 'rgba(16, 185, 129, 0.2)', color: '#10b981', border: '1px solid rgba(16, 185, 129, 0.5)' }}
                        title="Salvar Alterações"
                      >
                        <Check size={16} />
                      </button>
                      <button 
                        onClick={cancelEditing} 
                        className="btn" 
                        style={{ padding: '0.4rem', background: 'rgba(239, 68, 68, 0.2)', color: '#ef4444', border: '1px solid rgba(239, 68, 68, 0.5)' }}
                        title="Cancelar"
                      >
                        <X size={16} />
                      </button>
                    </div>
                  ) : (
                    <button 
                      onClick={() => startEditing(user)} 
                      className="btn" 
                      style={{ padding: '0.4rem 0.8rem', background: 'transparent', border: '1px solid var(--glass-border)', color: 'var(--text-main)', width: 'auto' }}
                      title="Editar Usuário"
                    >
                      <Edit2 size={16} style={{marginRight: '0.4rem', verticalAlign: 'middle'}} /> Editar
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        
        {users.length === 0 && (
          <div style={{ padding: '3rem', textAlign: 'center', color: 'var(--text-muted)' }}>
            Nenhum usuário encontrado no sistema.
          </div>
        )}
      </div>
    </div>
  );
};

export default ManageUsers;
