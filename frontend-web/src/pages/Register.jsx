import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { UserPlus, AlertCircle } from 'lucide-react';

const Register = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  
  const { registerUser } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    const result = await registerUser(name, email, password);
    
    if (result.success) {
      if (result.role === 'ADMIN') {
        navigate('/admin');
      } else {
        navigate('/client');
      }
    } else {
      setError(result.message);
      setIsLoading(false);
    }
  };

  return (
    <div className="app-container" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', minHeight: '80vh', position: 'relative', overflow: 'hidden' }}>
      <div className="orb-bg">
        <div className="orb orb-purple" style={{ width: '400px', height: '400px', top: '-100px', right: '-100px', animationDuration: '18s' }} />
        <div className="orb orb-sky" style={{ width: '280px', height: '280px', bottom: '-80px', left: '-80px', animationDuration: '22s', animationDelay: '-10s' }} />
      </div>
      <div className="glass-card animate-scaleIn" style={{ maxWidth: '400px', width: '100%', position: 'relative', zIndex: 1 }}>
        <div className="animate-fadeInDown" style={{ textAlign: 'center', marginBottom: '2rem' }}>
          <UserPlus size={48} color="#c084fc" style={{ marginBottom: '1rem' }} />
          <h2>Criar uma Conta</h2>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Cadastre-se para acessar o acervo da Biblioteca</p>
        </div>

        {error && (
          <div style={{ 
            backgroundColor: 'rgba(239, 68, 68, 0.2)', 
            border: '1px solid var(--danger)',
            color: '#fca5a5',
            padding: '0.75rem',
            borderRadius: '8px',
            marginBottom: '1.5rem',
            display: 'flex',
            alignItems: 'center',
            gap: '0.5rem',
            fontSize: '0.9rem'
          }}>
            <AlertCircle size={16} />
            {error}
          </div>
        )}

        <form className="animate-fadeInUp delay-200" onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Nome Completo</label>
            <input 
              type="text" 
              className="form-control" 
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Ex: João da Silva"
              required
            />
          </div>
          <div className="form-group">
            <label>Endereço de E-mail</label>
            <input 
              type="email" 
              className="form-control" 
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Ex: joao@email.com"
              required
            />
          </div>
          <div className="form-group">
            <label>Senha</label>
            <input 
              type="password" 
              className="form-control" 
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
              required
              minLength={6}
            />
          </div>
          
          <button 
            type="submit" 
            className="btn btn-primary" 
            style={{ marginTop: '1rem', width: '100%' }}
            disabled={isLoading}
          >
            {isLoading ? 'Cadastrando...' : 'Finalizar Cadastro'}
          </button>
        </form>

        <div style={{ marginTop: '1.5rem', textAlign: 'center' }}>
          <p style={{ fontSize: '0.9rem', color: 'var(--text-muted)' }}>
            Já possui uma conta? <Link to="/login" style={{ color: '#c084fc', textDecoration: 'none', fontWeight: 'bold' }}>Faça login aqui</Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Register;
