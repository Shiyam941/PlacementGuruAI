import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Navbar as BsNavbar, Nav, Container, Button } from 'react-bootstrap';

function Navbar() {
  const navigate = useNavigate();
  const token = localStorage.getItem('token');
  const user = JSON.parse(localStorage.getItem('user') || '{}');

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    navigate('/login');
  };

  return (
    <BsNavbar bg="dark" variant="dark" expand="lg" className="mb-3">
      <Container>
        <BsNavbar.Brand as={Link} to="/dashboard">
          <i className="bi bi-mortarboard-fill me-2"></i>
          PlacementGuru AI
        </BsNavbar.Brand>
        
        {token && (
          <>
            <BsNavbar.Toggle aria-controls="basic-navbar-nav" />
            <BsNavbar.Collapse id="basic-navbar-nav">
              <Nav className="me-auto">
                <Nav.Link as={Link} to="/dashboard">
                  <i className="bi bi-house-fill me-1"></i>Dashboard
                </Nav.Link>
                <Nav.Link as={Link} to="/chatbot">
                  <i className="bi bi-chat-dots-fill me-1"></i>Chatbot
                </Nav.Link>
                <Nav.Link as={Link} to="/mock-interview">
                  <i className="bi bi-mic-fill me-1"></i>Mock Interview
                </Nav.Link>
                <Nav.Link as={Link} to="/resume-analyzer">
                  <i className="bi bi-file-earmark-text-fill me-1"></i>Resume Analyzer
                </Nav.Link>
                <Nav.Link as={Link} to="/daily-roadmap">
                  <i className="bi bi-calendar-check-fill me-1"></i>Daily Roadmap
                </Nav.Link>
                <Nav.Link as={Link} to="/coding-practice">
                  <i className="bi bi-code-slash me-1"></i>Coding Practice
                </Nav.Link>
                {user.roles && user.roles.includes('ADMIN') && (
                  <Nav.Link as={Link} to="/admin">
                    <i className="bi bi-shield-fill me-1"></i>Admin Panel
                  </Nav.Link>
                )}
              </Nav>
              <Nav>
                <BsNavbar.Text className="me-3">
                  <i className="bi bi-person-circle me-1"></i>
                  {user.name || 'User'}
                </BsNavbar.Text>
                <Button variant="outline-light" size="sm" onClick={handleLogout}>
                  <i className="bi bi-box-arrow-right me-1"></i>Logout
                </Button>
              </Nav>
            </BsNavbar.Collapse>
          </>
        )}
      </Container>
    </BsNavbar>
  );
}

export default Navbar;
