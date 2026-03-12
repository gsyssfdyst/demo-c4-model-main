import React, { createContext, useContext, useState } from 'react';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    const savedUser = localStorage.getItem('libraryUser');
    return savedUser ? JSON.parse(savedUser) : null;
  });

  const login = async (email, password) => {
    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
      });

      if (response.ok) {
        const userData = await response.json();
        setUser(userData);
        localStorage.setItem('libraryUser', JSON.stringify(userData));
        return { success: true, role: userData.role };
      } else {
        const errorMsg = await response.text();
        return { success: false, message: errorMsg || 'Credenciais inválidas' };
      }
    } catch (error) {
      console.error('Erro de login:', error);
      return { success: false, message: 'Servidor inacessível' };
    }
  };

  const registerUser = async (name, email, password) => {
    try {
      const response = await fetch('http://localhost:8080/api/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name, email, password }),
      });

      if (response.ok) {
        const userData = await response.json();
        setUser(userData);
        localStorage.setItem('libraryUser', JSON.stringify(userData));
        return { success: true, role: userData.role };
      } else {
        const errorMsg = await response.text();
        return { success: false, message: errorMsg || 'Erro ao realizar cadastro.' };
      }
    } catch (error) {
      console.error('Erro no cadastro:', error);
      return { success: false, message: 'Servidor inacessível' };
    }
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem('libraryUser');
  };

  const isAdmin = () => user?.role === 'ADMIN';
  const isClient = () => user?.role === 'CLIENT';

  return (
    <AuthContext.Provider value={{ user, login, registerUser, logout, isAdmin, isClient }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
