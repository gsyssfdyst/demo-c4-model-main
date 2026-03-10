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
    const fetchDiagram = async () => {
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
    <div style={{ backgroundColor: 'white', borderRadius: '8px', padding: '1rem', marginBottom: '1rem', textAlign: 'center' }}>
      <img src={imgUrl} alt={altText} style={{ maxHeight: '350px', width: 'auto', maxWidth: '100%', objectFit: 'contain' }} />
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
          <li style={{ marginBottom: '0.3rem' }}>👥 <strong>Leitores e Bibliotecários:</strong> Controle de perfis e permissões.</li>
          <li style={{ marginBottom: '0.3rem' }}>📚 <strong>Acervo e Circulação:</strong> Consulta, registro de empréstimos e devoluções com validação de prazos.</li>
          <li>⚙️ <strong>Extensões:</strong> Multas, pagamentos e envio de notificações.</li>
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
            <li><strong>Sistemas Externos:</strong> Pagamentos e Notificações (previstos para V2).</li>
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
    title: "6. Requisitos Não Funcionais Críticos",
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
    title: "7. Análise de Trade-offs e Limitações",
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
  const navigate = useNavigate();

  const nextSlide = () => {
    if (currentSlide < slides.length - 1) setCurrentSlide(currentSlide + 1);
  };

  const prevSlide = () => {
    if (currentSlide > 0) setCurrentSlide(currentSlide - 1);
  };

  return (
    <div className="app-container" style={{ display: 'flex', flexDirection: 'column', height: '100vh', justifyContent: 'center' }}>
      
      <div className="glass-card" style={{ maxWidth: '900px', width: '100%', margin: '0 auto', height: '650px', display: 'flex', flexDirection: 'column' }}>
        
        {/* Header Slider */}
        <div style={{ borderBottom: '1px solid var(--glass-border)', paddingBottom: '1rem', marginBottom: '1.5rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
             <PresentationIcon color="#c084fc" /> 
             <span style={{ fontWeight: 'bold' }}>Defesa C4 Model - Slide {currentSlide + 1} de {slides.length}</span>
          </div>
          <button onClick={() => navigate('/login')} className="btn" style={{ width: 'auto', background: 'transparent', border: '1px solid var(--glass-border)' }}>
            Sair da Apresentação
          </button>
        </div>

        {/* Slide Content */}
        <div style={{ flex: 1, overflowY: 'auto' }}>
          <h2 style={{ fontSize: '2rem', marginBottom: '1rem' }}>{slides[currentSlide].title}</h2>
          {slides[currentSlide].subtitle && <h3 style={{ color: '#93c5fd', marginBottom: '2rem' }}>{slides[currentSlide].subtitle}</h3>}
          <div style={{ fontSize: '1.1rem' }}>
            {slides[currentSlide].content}
          </div>
        </div>

        {/* Controls */}
        <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '2rem', borderTop: '1px solid var(--glass-border)', paddingTop: '1.5rem' }}>
          <button 
            onClick={prevSlide} 
            disabled={currentSlide === 0}
            className="btn btn-primary" 
            style={{ width: '120px', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '0.5rem', opacity: currentSlide === 0 ? 0.3 : 1 }}
          >
            <ChevronLeft size={20} /> Anterior
          </button>
          
          <button 
            onClick={nextSlide} 
            disabled={currentSlide === slides.length - 1}
            className="btn btn-primary" 
            style={{ width: '120px', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '0.5rem', opacity: currentSlide === slides.length - 1 ? 0.3 : 1 }}
          >
            Próximo <ChevronRight size={20} />
          </button>
        </div>

      </div>
    </div>
  );
};

export default Presentation;
