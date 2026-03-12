import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { ChevronLeft, ChevronRight, Presentation as PresentationIcon, Shield, Layers, Server, Monitor, Database, Settings } from 'lucide-react';

const contextoPuml = `@startuml BibliotecaContext
!includeurl https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Context.puml

Person(usuario, "Usuário (Leitor/Bibliotecário)", "Interage com o catálogo e empréstimos")
System(sistemaGestao, "Sistema de Gestão de Biblioteca", "Sistema que gerencia o catálogo de livros e empréstimos")
System_Ext(servicoPagamento, "Sistema de Pagamentos", "Sistema externo para processamento de multas")
System_Ext(servicoNotificacao, "Serviço de Notificações", "Serviço externo (ex: e-mail/SMS)")

Rel(usuario, sistemaGestao, "Usa")
Rel(sistemaGestao, servicoPagamento, "Processa pagamentos")
Rel(sistemaGestao, servicoNotificacao, "Envia notificações")
@enduml`;

const containerPuml = `@startuml BibliotecaContainer
!includeurl https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml

Person_Ext(usuario, "Usuário", "Usuário que interage com o sistema")
Container(webapp, "Aplicação Web", "React", "Frontend da aplicação")
Container(mobileapp, "Aplicação Mobile", "React Native", "Frontend da aplicação mobile")
Container(api, "API Backend", "Spring Boot", "API REST para funcionalidades de backend")
ContainerDb(database, "Banco de Dados", "PostgreSQL", "Armazena informações do sistema")

Rel(usuario, webapp, "Usa")
Rel(usuario, mobileapp, "Usa")
Rel(webapp, api, "Requisições HTTP(S)")
Rel(mobileapp, api, "Requisições HTTP(S)")
Rel(api, database, "SQL", "Usa")
@enduml`;

const codigoPuml = `@startuml BibliotecaCodigo
!includeurl https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Component.puml

class Book {
  - String title
  - String author
  - Date publishedDate
  - boolean isAvailable

  + getTitle() : String
  + getAuthor() : String
  + getPublishedDate() : Date
  + checkAvailability() : boolean
}
@enduml`;

const PlantUmlViewer = ({ pumlContent, altText }) => {
  const [imgUrl, setImgUrl] = useState(null);
  const [error, setError] = useState(false);


  useEffect(() => {
    let isMounted = true;
    const fetchDiagram = async () => { // API
      try {
        const response = await fetch('https://kroki.io/plantuml/svg', {
          method: 'POST',
          headers: { 'Content-Type': 'text/plain' },
          body: pumlContent,
        });
        if (!response.ok) throw new Error('Falha ao renderizar diagrama');
        const blob = await response.blob();
        if (isMounted) {
          setImgUrl(URL.createObjectURL(blob));
        }
      } catch (err) {
        console.error(err);
        if (isMounted) setError(true);
      }
    };
    fetchDiagram();
    return () => { isMounted = false; };
  }, [pumlContent]);

  if (error) {
    return <div style={{ color: '#ef4444', textAlign: 'center', padding: '1rem', border: '1px dashed #ef4444', borderRadius: '8px' }}>Erro ao renderizar diagrama {altText}. Verifique a conexão.</div>;
  }
  if (!imgUrl) {
    return <div style={{ color: '#93c5fd', textAlign: 'center', padding: '2rem', border: '1px dashed #93c5fd', borderRadius: '8px' }}>Renderizando diagrama {altText} online via PUML script...</div>;
  }

  return (
    <div style={{ 
      background: 'rgba(255, 255, 255, 0.95)', 
      borderRadius: '16px', 
      padding: '1.5rem', 
      marginBottom: '1rem', 
      textAlign: 'center',
      boxShadow: '0 20px 40px rgba(0,0,0,0.4), inset 0 0 0 1px rgba(255,255,255,0.2)',
      position: 'relative',
      overflow: 'hidden'
    }}>
      <div style={{ position: 'absolute', top: 0, left: 0, right: 0, height: '4px', background: 'linear-gradient(90deg, #c084fc, #3b82f6)' }}></div>
      <img src={imgUrl} alt={altText} style={{ maxHeight: '420px', width: 'auto', maxWidth: '100%', objectFit: 'contain', filter: 'contrast(1.05)' }} />
    </div>
  );
};

const slides = [
  {
    title: "Arquitetura de Software: Trabalho Prático",
    subtitle: "Sistema de Gestão de Biblioteca",
    content: (
      <div style={{ textAlign: 'center', marginTop: '2rem' }}>
        <h3 style={{ color: '#c084fc', marginBottom: '1rem' }}>Membros da Equipe</h3>
        <p style={{ fontSize: '1.2rem', color: 'var(--text-main)', fontWeight: 'bold' }}>ÁLISSON DO AMOR DIVINO e JOÃO VITOR ROCHA SOARES</p>
        <p style={{ marginTop: '2rem', color: 'var(--text-muted)' }}>IFBA – Campus Santo Antônio de Jesus (SAJ)<br/>Professor: Alex Gondim Lima<br/>2026</p>
      </div>
    ),
  },
  {
    title: "1. Introdução e Objetivo",
    content: (
      <div style={{ padding: '0.5rem 1rem' }}>
        <h3 style={{ color: '#c084fc', marginBottom: '0.5rem' }}>O Problema</h3>
        <p style={{ marginBottom: '1rem', lineHeight: '1.5', fontSize: '0.95rem' }}>
          O projeto propõe uma solução de software voltada ao suporte de processos típicos de bibliotecas acadêmicas e comunitárias, como cadastro e consulta ao acervo, controle de empréstimos, devoluções e cálculo de multas.
        </p>
        <h3 style={{ color: '#c084fc', marginBottom: '0.5rem' }}>Objetivo do Trabalho</h3>
        <p style={{ marginBottom: '1rem', lineHeight: '1.5', fontSize: '0.95rem' }}>
          Definir e justificar a arquitetura adotada, evidenciando a coerência entre os requisitos (funcionais e não funcionais) e as decisões estruturais tomadas no projeto.
        </p>
        <h3 style={{ color: '#c084fc', marginBottom: '0.5rem' }}>Escopo</h3>
        <ul style={{ listStyle: 'none', padding: 0, fontSize: '0.95rem' }}>
          <li style={{ marginBottom: '0.3rem' }}>👥 <strong>Autenticação e Acesso:</strong> Perfil Administrativo (Gestão) e Cadastro em tempo real de novos Clientes.</li>
          <li style={{ marginBottom: '0.3rem' }}>📚 <strong>Acervo e Circulação:</strong> Vitrine interativa, registro de aluguel e devolução com controle de status.</li>
          <li>⚙️ <strong>Integrações Externas:</strong> Simulação de processamento de Multas e Notificações (via arquitetura Mock).</li>
        </ul>
      </div>
    ),
  },
  {
    title: "2. Estilo Arquitetural",
    content: (
      <div style={{ padding: '1rem' }}>
        <h3 style={{ color: '#c084fc', marginBottom: '1rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          <Layers /> Arquitetura Cliente-Servidor e em Camadas
        </h3>
        <p style={{ marginBottom: '1.5rem', lineHeight: '1.6' }}>
          Adotamos um estilo híbrido combinando <strong>Cliente-Servidor</strong> (isolando completamente a interface da lógica de negócios e segurança corporativa) com um backend estruturado em <strong>Camadas (Layered Pattern)</strong> via Spring MVC.
        </p>
        <h4 style={{ color: '#93c5fd', marginBottom: '0.5rem' }}>Justificativa:</h4>
        <ul style={{ paddingLeft: '1.5rem', lineHeight: '1.6' }}>
          <li><strong>Desacoplamento:</strong> O frontend (React) e backend (Java) evoluem de forma independente, permitindo construir, no futuro, um App Mobile que consuma as mesmas APIs.</li>
          <li><strong>Separação de Preocupações (SoC):</strong> No backend, dividimos rigidamente Controllers (Tráfego), Services (Negócio) e Repositories (Dados), facilitando a manutenção e testes por especialistas.</li>
        </ul>
      </div>
    ),
  },
  {
    title: "3. C4 Model - Nível 1: Diagrama de Contexto",
    content: (
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '2rem' }}>
        <div>
          <h3 style={{ color: '#c084fc', marginBottom: '1rem' }}>Visão Externa</h3>
          <p style={{ lineHeight: '1.6', marginBottom: '1rem' }}>
            Localiza o Sistema de Gestão de Biblioteca como uma "caixa-preta" rodeada pelos usuários e dependências externas (conforme diagrama ao lado, renderizado ao vivo a partir do arquivo <code>Contexto.puml</code> do projeto).
          </p>
          <ul style={{ paddingLeft: '1.5rem', lineHeight: '1.6' }}>
            <li><strong>Ator:</strong> Usuário (Pode ser Cliente ou Funcionário / Admin).</li>
            <li><strong>Sistema Central:</strong> Biblioteca Online.</li>
            <li><strong>Sistemas Externos:</strong> Pagamentos e Notificações (Serviços independentes simulados usando Padrão Mock no Backend).</li>
          </ul>
        </div>
        <div>
           <PlantUmlViewer pumlContent={contextoPuml} altText="Contexto" />
        </div>
      </div>
    ),
  },
  {
    title: "4. C4 Model - Nível 2: Containers",
    content: (
      <div style={{ padding: '1rem' }}>
        <h3 style={{ color: '#c084fc', marginBottom: '0.5rem' }}>Como a caixa-preta funciona por dentro?</h3>
        <p style={{ lineHeight: '1.6', marginBottom: '1rem' }}>O sistema foi dividido em responsabilidades isoladas (Deployables).</p>
        
        <PlantUmlViewer pumlContent={containerPuml} altText="Containers" />

        <div style={{ display: 'flex', gap: '1rem', marginTop: '1rem' }}>
          <div className="glass-card" style={{ flex: 1, textAlign: 'center', padding: '1rem' }}>
            <h4>SPA Web App</h4>
            <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>React (Vite) interface.</p>
          </div>
          <div className="glass-card" style={{ flex: 1, textAlign: 'center', padding: '1rem' }}>
            <h4>API REST Backend</h4>
            <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>Java + Spring Boot gerenciando locações.</p>
          </div>
          <div className="glass-card" style={{ flex: 1, textAlign: 'center', padding: '1rem' }}>
            <h4>Data Store</h4>
            <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>PostgreSQL transacional.</p>
          </div>
        </div>
      </div>
    ),
  },
  {
    title: "5. Comunicação e Componentes (C4 Níveis 3 e 4)",
    content: (
      <div style={{ padding: '0.5rem 1rem' }}>
         <h3 style={{ color: '#c084fc', marginBottom: '0.5rem' }}>Módulos de Código e Tráfego</h3>
         
         <div style={{ display: 'grid', gridTemplateColumns: '40% 60%', gap: '1.5rem', alignItems: 'start' }}>
           <div>
             <h4 style={{ color: '#93c5fd' }}>Orquestração de Regras</h4>
             <ul style={{ paddingLeft: '1.2rem', lineHeight: '1.4', fontSize: '0.90rem', marginBottom: '1rem' }}>
              <li><strong>Book Entity:</strong> Modelagem de Classes em Java (veja diagrama de classe <code>Codigo.puml</code>).</li>
              <li><strong>Due Date de 14 dias:</strong> Aplicado no backend via Serviços.</li>
             </ul>

             <h4 style={{ color: '#93c5fd' }}>Comunicação Síncrona</h4>
             <ul style={{ paddingLeft: '1.2rem', lineHeight: '1.4', fontSize: '0.90rem' }}>
              <li>O Cliente Web se comunica com o Backend via HTTP (Protocolo REST).</li>
              <li>Payload JSON leve para transferência rápida de dados.</li>
             </ul>
           </div>
           <div>
             <PlantUmlViewer pumlContent={codigoPuml} altText="Componentes de Código" />
           </div>
         </div>
      </div>
    )
  },
  {
    title: "6. Funcionalidades Desenvolvidas na Prática",
    content: (
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1.5rem', marginTop: '1rem' }}>
        <div className="glass-card" style={{ padding: '1.5rem' }}>
          <h4 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: '#93c5fd', marginBottom: '0.5rem' }}>👥 Gestão de Usuários</h4>
          <p style={{ fontSize: '0.95rem', color: 'var(--text-main)', lineHeight: '1.5' }}>
            Diferenciação dinâmica de roteamento no React entre <strong>Administradores</strong> (Gerenciam todo o acervo e clientes) e <strong>Clientes</strong>. Implementamos também o fluxo ponta-a-ponta de <strong>Cadastro de Novas Contas</strong> salvando direto no PostgreSQL.
          </p>
        </div>
        <div className="glass-card" style={{ padding: '1.5rem' }}>
          <h4 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: '#93c5fd', marginBottom: '0.5rem' }}>📖 Vitrine e Aluguel</h4>
          <p style={{ fontSize: '0.95rem', color: 'var(--text-main)', lineHeight: '1.5' }}>
            Painéis assíncronos onde o cliente visualiza o catálogo, realiza a locação em um clique e acompanha o status (Ativo/Devolvido) e prazos de todos os seus <strong>Empréstimos</strong> no seu perfil.
          </p>
        </div>
        <div className="glass-card" style={{ padding: '1.5rem', gridColumn: 'span 2' }}>
          <h4 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: '#c084fc', marginBottom: '0.5rem' }}>⚙️ Padrão Mock para Integrações (Pagamento & Notificação)</h4>
          <p style={{ fontSize: '0.95rem', color: 'var(--text-main)', lineHeight: '1.5' }}>
            Para validar a arquitetura C4 sem depender de serviços TCP/HTTP reais pagos, criamos camadas de integração mockadas (<code>NotificationService</code> e <code>PaymentService</code>). Quando o usuário realiza uma devolução, o Spring Boot dispara requisições locais simulando o faturamento e envio de e-mails, gerando relatórios de log consumíveis pela API.
          </p>
        </div>
      </div>
    )
  },
  {
    title: "7. Requisitos Não Funcionais Críticos",
    content: (
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
        <div className="glass-card" style={{ padding: '1rem' }}>
          <h4 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}><Shield size={18}/> Segurança</h4>
          <p style={{ fontSize: '0.85rem', marginTop: '0.5rem', color: 'var(--text-muted)', lineHeight: '1.4' }}>
            <strong>Tática:</strong> Autenticação via token, Autorização por papéis (RBAC - Leitor/Bibliotecário) e Logs de Auditoria.<br/>
            <strong>Meta:</strong> 100% dos endpoints adm protegidos. Bloqueio após 5 tentativas de login inválidas.
          </p>
        </div>
        <div className="glass-card" style={{ padding: '1rem' }}>
          <h4 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}><Settings size={18}/> Manutenibilidade</h4>
          <p style={{ fontSize: '0.85rem', marginTop: '0.5rem', color: 'var(--text-muted)', lineHeight: '1.4' }}>
            <strong>Tática:</strong> Modularização, baixo acoplamento e contratos internos.<br/>
            <strong>Meta:</strong> Mudança na regra de multa deve exigir alteração apenas no módulo pertinente. MTTR &lt; 2 horas em homologação.
          </p>
        </div>
        <div className="glass-card" style={{ padding: '1rem' }}>
          <h4 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}><Layers size={18}/> Escalabilidade</h4>
          <p style={{ fontSize: '0.85rem', marginTop: '0.5rem', color: 'var(--text-muted)', lineHeight: '1.4' }}>
            <strong>Tática:</strong> Backend stateless, replicação horizontal e índices no DB.<br/>
            <strong>Meta:</strong> Suportar 200 usuários simultâneos com latência p95 &lt; 800ms para consultas. Taxa de erro &lt; 1% sob carga.
          </p>
        </div>
        <div className="glass-card" style={{ padding: '1rem' }}>
          <h4 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}><Server size={18}/> Disponibilidade</h4>
          <p style={{ fontSize: '0.85rem', marginTop: '0.5rem', color: 'var(--text-muted)', lineHeight: '1.4' }}>
            <strong>Tática:</strong> Monitoramento, Health Checks e Backups periódicos.<br/>
            <strong>Meta:</strong> Disponibilidade mensal alvo: ≥ 99,0%. RPO ≤ 24h e RTO ≤ 4h.
          </p>
        </div>
      </div>
    )
  },
  {
    title: "8. Análise de Trade-offs e Limitações",
    content: (
      <div style={{ padding: '0.5rem 1rem' }}>
        <h3 style={{ color: '#c084fc', marginBottom: '1rem' }}>Decisões Conflitantes Assumidas</h3>
        
        <div style={{ marginBottom: '1.5rem' }}>
          <h4 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <span style={{ color: '#fca5a5' }}>Monolito Modular</span> vs <span style={{ color: '#10b981' }}>Microserviços</span>
          </h4>
          <p style={{ lineHeight: '1.5', fontSize: '0.90rem', color: 'var(--text-muted)' }}>
            <strong>Trade-off:</strong> Reduz a complexidade inicial de desenvolvimento e operação, evitando desafios de orquestração distribuída. <strong>Limitação:</strong> Escalabilidade ocorre no nível da aplicação e não por domínio específico.
          </p>
        </div>

        <div style={{ marginBottom: '1.5rem' }}>
           <h4 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <span style={{ color: '#fca5a5' }}>REST/HTTP</span> vs <span style={{ color: '#10b981' }}>Mensageria Assíncrona</span>
          </h4>
          <p style={{ lineHeight: '1.5', fontSize: '0.90rem', color: 'var(--text-muted)' }}>
            <strong>Trade-off:</strong> Favorece interoperabilidade e padronização entre as aplicações (web e mobile). <strong>Limitação:</strong> Introduz overhead de comunicação. Mitigado por paginação, filtros e cache de respostas.
          </p>
        </div>

        <div>
           <h4 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <span style={{ color: '#fca5a5' }}>Banco Relacional (PostgreSQL)</span> vs <span style={{ color: '#10b981' }}>NoSQL</span>
          </h4>
          <p style={{ lineHeight: '1.5', fontSize: '0.90rem', color: 'var(--text-muted)' }}>
            <strong>Trade-off:</strong> Garante consistência transacional e integridade referencial (crítico para empréstimos). <strong>Limitação:</strong> Otimização adicional em altos volumes ou consultas analíticas complexas.
          </p>
        </div>
      </div>
    )
  }
];

const Presentation = () => {
  const [currentSlide, setCurrentSlide] = useState(0);
  const [direction, setDirection] = useState('next');
  const navigate = useNavigate();

  const nextSlide = () => {
    if (currentSlide < slides.length - 1) {
      setDirection('next');
      setCurrentSlide(prev => prev + 1);
    }
  };

  const prevSlide = () => {
    if (currentSlide > 0) {
      setDirection('prev');
      setCurrentSlide(prev => prev - 1);
    }
  };

  useEffect(() => {
    const handleKeyDown = (e) => {
      if (e.key === 'ArrowRight' || e.key === ' ') {
        nextSlide();
      } else if (e.key === 'ArrowLeft') {
        prevSlide();
      } else if (e.key === 'Escape') {
        navigate('/login');
      }
    };
    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [currentSlide, navigate]);

  const progress = ((currentSlide + 1) / slides.length) * 100;

  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column', background: 'radial-gradient(circle at top right, #1e1b4b, #0f172a 60%)', overflow: 'hidden', position: 'relative' }}>

      {/* ✨ Animated floating orbs background */}
      <div aria-hidden="true" style={{ position: 'fixed', inset: 0, pointerEvents: 'none', zIndex: 0 }}>
        <div className="orb orb1" />
        <div className="orb orb2" />
        <div className="orb orb3" />
      </div>

      {/* Top Progress Bar */}
      <div style={{ height: '4px', width: '100%', background: 'rgba(255,255,255,0.05)', position: 'relative', zIndex: 1 }}>
        <div style={{ height: '100%', width: `${progress}%`, background: 'linear-gradient(90deg, #c084fc, #3b82f6)', transition: 'width 0.4s cubic-bezier(0.4, 0, 0.2, 1)', boxShadow: '0 0 10px #c084fc88' }} />
      </div>

      <div style={{ padding: '1.5rem 2rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center', position: 'relative', zIndex: 1 }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', background: 'var(--glass-bg)', padding: '0.5rem 1rem', borderRadius: '8px', border: '1px solid var(--glass-border)', animation: 'fadeInDown 0.6s ease-out' }}>
           <PresentationIcon color="#c084fc" size={24} /> 
           <span style={{ fontWeight: '600', letterSpacing: '0.5px' }}>Defesa C4 Model</span>
           <span style={{ color: 'var(--text-muted)' }}>|</span>
           <span style={{ color: '#93c5fd' }}>Slide {currentSlide + 1} de {slides.length}</span>
        </div>
        <button onClick={() => navigate('/login')} className="btn" style={{ width: 'auto', background: 'rgba(239, 68, 68, 0.1)', color: '#fca5a5', border: '1px solid rgba(239, 68, 68, 0.3)', padding: '0.5rem 1rem', animation: 'fadeInDown 0.6s ease-out' }}>
          Sair (Esc)
        </button>
      </div>

      <div style={{ flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '0 2rem 2rem 2rem', position: 'relative', overflow: 'hidden', zIndex: 1 }}>
        <div 
          key={currentSlide}
          style={{ 
            width: '100%', 
            maxWidth: '1100px', 
            animation: `${direction === 'next' ? 'slideInRight' : 'slideInLeft'} 0.5s cubic-bezier(0.16, 1, 0.3, 1) forwards`
          }}
        >
          <div className="glass-card" style={{ padding: '4rem', display: 'flex', flexDirection: 'column', justifyContent: 'center', boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.5)', borderColor: 'rgba(192, 132, 252, 0.2)' }}>
            <h2 style={{ fontSize: '2.8rem', marginBottom: '1rem', background: 'linear-gradient(to right, #f8fafc, #93c5fd)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent', animation: 'fadeInUp 0.5s 0.1s ease-out both' }}>
              {slides[currentSlide].title}
            </h2>
            {slides[currentSlide].subtitle && <h3 style={{ fontSize: '1.5rem', color: '#c084fc', marginBottom: '3rem', fontWeight: '400', animation: 'fadeInUp 0.5s 0.2s ease-out both' }}>{slides[currentSlide].subtitle}</h3>}
            <div style={{ fontSize: '1.2rem', color: 'rgba(248, 250, 252, 0.9)', animation: 'fadeInUp 0.5s 0.3s ease-out both' }}>
              {slides[currentSlide].content}
            </div>
          </div>
        </div>
      </div>

      {/* Slide dot indicators */}
      <div style={{ display: 'flex', justifyContent: 'center', gap: '0.5rem', paddingBottom: '1.5rem', position: 'relative', zIndex: 1 }}>
        {slides.map((_, i) => (
          <button
            key={i}
            onClick={() => { setDirection(i > currentSlide ? 'next' : 'prev'); setCurrentSlide(i); }}
            title={`Slide ${i + 1}`}
            style={{
              width: i === currentSlide ? '28px' : '8px',
              height: '8px',
              borderRadius: '4px',
              border: 'none',
              cursor: 'pointer',
              background: i === currentSlide ? 'linear-gradient(90deg, #c084fc, #3b82f6)' : 'rgba(255,255,255,0.2)',
              transition: 'all 0.35s cubic-bezier(0.4,0,0.2,1)',
              padding: 0,
              boxShadow: i === currentSlide ? '0 0 8px #c084fc88' : 'none',
            }}
          />
        ))}
      </div>

      {/* Floating Controls */}
      <div style={{ position: 'fixed', bottom: '2rem', right: '2rem', display: 'flex', gap: '0.75rem', background: 'var(--glass-bg)', padding: '0.5rem', borderRadius: '12px', backdropFilter: 'blur(10px)', border: '1px solid var(--glass-border)', zIndex: 10, animation: 'fadeInUp 0.6s 0.4s ease-out both' }}>
        <button 
          onClick={prevSlide} 
          disabled={currentSlide === 0}
          className="btn prev-btn" 
          style={{ width: '50px', height: '50px', padding: 0, borderRadius: '8px', background: currentSlide === 0 ? 'transparent' : 'rgba(255,255,255,0.1)', transition: 'all 0.2s ease' }}
          title="Anterior (Seta Esquerda)"
        >
          <ChevronLeft size={24} color={currentSlide === 0 ? '#475569' : '#f8fafc'} />
        </button>
        <button 
          onClick={nextSlide} 
          disabled={currentSlide === slides.length - 1}
          className="btn next-btn" 
          style={{ width: '50px', height: '50px', padding: 0, borderRadius: '8px', background: currentSlide === slides.length - 1 ? 'transparent' : 'rgba(139, 92, 246, 0.8)', boxShadow: currentSlide === slides.length - 1 ? 'none' : '0 0 16px rgba(139,92,246,0.5)', transition: 'all 0.2s ease' }}
          title="Próximo (Seta Direita/Espaço)"
        >
          <ChevronRight size={24} color={currentSlide === slides.length - 1 ? '#475569' : '#f8fafc'} />
        </button>
      </div>
      
      {/* Global animation styles */}
      <style>{`
        @keyframes slideInRight {
          from { opacity: 0; transform: translateX(60px) scale(0.98); }
          to   { opacity: 1; transform: translateX(0)   scale(1); }
        }
        @keyframes slideInLeft {
          from { opacity: 0; transform: translateX(-60px) scale(0.98); }
          to   { opacity: 1; transform: translateX(0)    scale(1); }
        }
        @keyframes fadeInUp {
          from { opacity: 0; transform: translateY(20px); }
          to   { opacity: 1; transform: translateY(0); }
        }
        @keyframes fadeInDown {
          from { opacity: 0; transform: translateY(-16px); }
          to   { opacity: 1; transform: translateY(0); }
        }
        @keyframes floatOrb {
          0%, 100% { transform: translateY(0px) translateX(0px) scale(1); }
          33%       { transform: translateY(-40px) translateX(20px) scale(1.05); }
          66%       { transform: translateY(20px) translateX(-30px) scale(0.97); }
        }
        .orb {
          position: absolute;
          border-radius: 50%;
          filter: blur(80px);
          opacity: 0.25;
          animation: floatOrb ease-in-out infinite;
        }
        .orb1 {
          width: 500px; height: 500px;
          background: radial-gradient(circle, #7c3aed, transparent 70%);
          top: -150px; left: -150px;
          animation-duration: 14s;
        }
        .orb2 {
          width: 400px; height: 400px;
          background: radial-gradient(circle, #2563eb, transparent 70%);
          bottom: -100px; right: -100px;
          animation-duration: 18s;
          animation-delay: -6s;
        }
        .orb3 {
          width: 300px; height: 300px;
          background: radial-gradient(circle, #0ea5e9, transparent 70%);
          top: 50%; right: 30%;
          animation-duration: 22s;
          animation-delay: -11s;
        }
        .prev-btn:not(:disabled):hover {
          background: rgba(255,255,255,0.2) !important;
          transform: scale(1.1);
        }
        .next-btn:not(:disabled):hover {
          background: rgba(139, 92, 246, 1) !important;
          box-shadow: 0 0 24px rgba(139,92,246,0.7) !important;
          transform: scale(1.1);
        }
      `}</style>
    </div>
  );
};

export default Presentation;
