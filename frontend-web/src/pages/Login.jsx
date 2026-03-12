import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Library, AlertCircle } from 'lucide-react';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    const result = await login(email, password);
    
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
      {/* Ambient floating orbs */}
      <div className="orb-bg">
        <div className="orb orb-purple" style={{ width: '400px', height: '400px', top: '-100px', left: '-100px', animationDuration: '16s' }} />
        <div className="orb orb-blue" style={{ width: '300px', height: '300px', bottom: '-80px', right: '-80px', animationDuration: '20s', animationDelay: '-8s' }} />
      </div>
      <div className="glass-card animate-scaleIn" style={{ maxWidth: '400px', width: '100%', position: 'relative', zIndex: 1 }}>
        <div className="animate-fadeInDown" style={{ textAlign: 'center', marginBottom: '2rem' }}>
          <Library size={48} color="#c084fc" className="animate-scaleIn delay-100" style={{ marginBottom: '1rem' }} />
          <h2>{`Bem-vindo(a) de volta`}</h2>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Faça login no Gerenciador da Biblioteca</p>
        </div>

        {error && (
          <div className="animate-fadeInDown" style={{ 
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
            <label>Endereço de E-mail</label>
            <input 
              type="email" 
              className="form-control" 
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="ex. admin@biblioteca.com"
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
            />
          </div>
          
          <button 
            type="submit" 
            className="btn btn-primary" 
            style={{ marginTop: '1rem' }}
            disabled={isLoading}
          >
            {isLoading ? 'Entrando...' : 'Entrar'}
          </button>
        </form>

        <div style={{ marginTop: '2rem', fontSize: '0.85rem', color: 'var(--text-muted)', textAlign: 'center' }}>
          <p>
            Não tem uma conta? <Link to="/register" style={{ color: '#c084fc', textDecoration: 'none', fontWeight: 'bold' }}>Cadastre-se</Link>
          </p>
          <p style={{ marginTop: '0.5rem' }}><strong>Contas de Demonstração:</strong></p>
          <p>Admin: admin@biblioteca.com / admin123</p>
          <p>Cliente: joao@email.com / senha123</p>
        </div>

        <div style={{ marginTop: '1.5rem', paddingTop: '1.5rem', borderTop: '1px solid var(--glass-border)', textAlign: 'center' }}>
           <button 
            type="button" 
            className="btn" 
            onClick={() => navigate('/presentation')}
            style={{ width: '100%', background: 'rgba(139, 92, 246, 0.1)', color: '#c084fc', border: '1px solid rgba(139, 92, 246, 0.3)' }}
          >
            📊 Iniciar Apresentação (C4 Model)
          </button>
        </div>
      </div>
    </div>
  );
};

export default Login;
