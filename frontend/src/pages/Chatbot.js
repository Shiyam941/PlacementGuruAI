import React, { useState, useEffect, useRef } from 'react';
import { Container, Row, Col, Card, Form, Button, Alert } from 'react-bootstrap';
import { chatbotAPI } from '../services/api';
import LoadingSpinner from '../components/LoadingSpinner';

function Chatbot() {
  const [messages, setMessages] = useState([]);
  const [inputMessage, setInputMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const messagesEndRef = useRef(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  useEffect(() => {
    loadHistory();
  }, []);

  const loadHistory = async () => {
    try {
      const response = await chatbotAPI.getHistory();
      const history = response.data.data || [];
      const formattedHistory = history.reverse().flatMap(chat => [
        { text: chat.userMessage, sender: 'user' },
        { text: chat.botResponse, sender: 'bot' },
      ]);
      setMessages(formattedHistory);
    } catch (err) {
      console.error('Failed to load history:', err);
    }
  };

  const handleSendMessage = async (e) => {
    e.preventDefault();
    if (!inputMessage.trim()) return;

    const userMessage = inputMessage;
    setInputMessage('');
    setMessages(prev => [...prev, { text: userMessage, sender: 'user' }]);
    setLoading(true);
    setError('');

    try {
      const response = await chatbotAPI.sendMessage({ message: userMessage });
      const botResponse = response.data.data.response;
      setMessages(prev => [...prev, { text: botResponse, sender: 'bot' }]);
    } catch (err) {
      setError('Failed to get response. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleClearHistory = async () => {
    if (window.confirm('Are you sure you want to clear chat history?')) {
      try {
        await chatbotAPI.clearHistory();
        setMessages([]);
      } catch (err) {
        setError('Failed to clear history');
      }
    }
  };

  return (
    <Container className="py-4">
      <Row className="justify-content-center">
        <Col lg={10} xl={8}>
          <Card className="shadow">
            <Card.Header className="bg-primary text-white d-flex justify-content-between align-items-center">
              <h4 className="mb-0">
                <i className="bi bi-chat-dots-fill me-2"></i>
                AI Chatbot Assistant
              </h4>
              <Button variant="light" size="sm" onClick={handleClearHistory}>
                <i className="bi bi-trash me-1"></i>Clear History
              </Button>
            </Card.Header>

            <Card.Body style={{ height: '500px', overflowY: 'auto' }}>
              {error && <Alert variant="danger">{error}</Alert>}

              {messages.length === 0 && !loading && (
                <div className="text-center text-muted mt-5">
                  <i className="bi bi-chat-quote" style={{ fontSize: '4rem' }}></i>
                  <p className="mt-3">Start a conversation with PlacementGuru AI!</p>
                </div>
              )}

              {messages.map((msg, index) => (
                <div
                  key={index}
                  className={`d-flex mb-3 ${msg.sender === 'user' ? 'justify-content-end' : 'justify-content-start'}`}
                >
                  <div
                    className={`p-3 rounded ${
                      msg.sender === 'user'
                        ? 'bg-primary text-white'
                        : 'bg-light border'
                    }`}
                    style={{ maxWidth: '70%' }}
                  >
                    {msg.text}
                  </div>
                </div>
              ))}

              {loading && (
                <div className="d-flex justify-content-start mb-3">
                  <div className="bg-light p-3 rounded border">
                    <div className="spinner-border spinner-border-sm text-primary" role="status">
                      <span className="visually-hidden">Loading...</span>
                    </div>
                    <span className="ms-2">Thinking...</span>
                  </div>
                </div>
              )}

              <div ref={messagesEndRef} />
            </Card.Body>

            <Card.Footer>
              <Form onSubmit={handleSendMessage}>
                <div className="d-flex gap-2">
                  <Form.Control
                    type="text"
                    value={inputMessage}
                    onChange={(e) => setInputMessage(e.target.value)}
                    placeholder="Type your question here..."
                    disabled={loading}
                  />
                  <Button type="submit" variant="primary" disabled={loading || !inputMessage.trim()}>
                    <i className="bi bi-send-fill"></i>
                  </Button>
                </div>
              </Form>
            </Card.Footer>
          </Card>
        </Col>
      </Row>
    </Container>
  );
}

export default Chatbot;
