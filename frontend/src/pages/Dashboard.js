import React from 'react';
import { Container, Row, Col, Card } from 'react-bootstrap';
import { Link } from 'react-router-dom';

function Dashboard() {
  const user = JSON.parse(localStorage.getItem('user') || '{}');

  const features = [
    {
      title: 'AI Chatbot',
      icon: 'chat-dots-fill',
      description: 'Get instant answers to your placement preparation questions',
      link: '/chatbot',
      color: 'primary',
    },
    {
      title: 'Mock Interview',
      icon: 'mic-fill',
      description: 'Practice interviews with AI feedback and improve your skills',
      link: '/mock-interview',
      color: 'success',
    },
    {
      title: 'Resume Analyzer',
      icon: 'file-earmark-text-fill',
      description: 'Get ATS score and suggestions to improve your resume',
      link: '/resume-analyzer',
      color: 'warning',
    },
    {
      title: 'Daily Roadmap',
      icon: 'calendar-check-fill',
      description: 'Follow AI-generated daily plans tailored to your skill level',
      link: '/daily-roadmap',
      color: 'info',
    },
    {
      title: 'Coding Practice',
      icon: 'code-slash',
      description: 'Solve coding problems and track your progress',
      link: '/coding-practice',
      color: 'danger',
    },
    ...(user.roles && user.roles.includes('ADMIN') ? [{
      title: 'Admin Panel',
      icon: 'shield-fill',
      description: 'Manage problems and view student progress',
      link: '/admin',
      color: 'dark',
    }] : []),
  ];

  return (
    <Container className="py-4">
      <div className="text-center mb-5">
        <h1 className="display-4 fw-bold">Welcome, {user.name}! 👋</h1>
        <p className="lead text-muted">
          Your one-stop platform for placement preparation
        </p>
      </div>

      <Row>
        {features.map((feature, index) => (
          <Col key={index} md={6} lg={4} className="mb-4">
            <Link to={feature.link} className="text-decoration-none">
              <Card className={`h-100 border-${feature.color} shadow-sm hover-shadow`}>
                <Card.Body className="text-center p-4">
                  <div className={`text-${feature.color} mb-3`}>
                    <i className={`bi bi-${feature.icon}`} style={{ fontSize: '3rem' }}></i>
                  </div>
                  <Card.Title className="fw-bold">{feature.title}</Card.Title>
                  <Card.Text className="text-muted">
                    {feature.description}
                  </Card.Text>
                  <div className={`text-${feature.color}`}>
                    <i className="bi bi-arrow-right-circle-fill"></i>
                  </div>
                </Card.Body>
              </Card>
            </Link>
          </Col>
        ))}
      </Row>

      <style>{`
        .hover-shadow {
          transition: transform 0.2s, box-shadow 0.2s;
        }
        .hover-shadow:hover {
          transform: translateY(-5px);
          box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15) !important;
        }
      `}</style>
    </Container>
  );
}

export default Dashboard;
