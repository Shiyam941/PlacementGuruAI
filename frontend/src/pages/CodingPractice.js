import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button, Badge, Modal, Form, Alert, ListGroup } from 'react-bootstrap';
import { codingAPI } from '../services/api';
import LoadingSpinner from '../components/LoadingSpinner';

function CodingPractice() {
  const [problems, setProblems] = useState([]);
  const [selectedProblem, setSelectedProblem] = useState(null);
  const [code, setCode] = useState('');
  const [language, setLanguage] = useState('JAVA');
  const [showModal, setShowModal] = useState(false);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');
  const [result, setResult] = useState(null);

  useEffect(() => {
    loadProblems();
  }, []);

  const loadProblems = async () => {
    setLoading(true);
    try {
      const response = await codingAPI.getAllProblems();
      setProblems(response.data.data || []);
    } catch (err) {
      setError('Failed to load problems');
    } finally {
      setLoading(false);
    }
  };

  const handleProblemClick = (problem) => {
    setSelectedProblem(problem);
    setCode(problem.solutionTemplate || '// Write your solution here\n\n');
    setResult(null);
    setShowModal(true);
  };

  const handleSubmitCode = async () => {
    if (!code.trim()) {
      setError('Please write some code first');
      return;
    }

    setSubmitting(true);
    setError('');
    try {
      const response = await codingAPI.submitCode({
        problemId: selectedProblem.id,
        code,
        language,
      });
      setResult(response.data.data);
    } catch (err) {
      setError('Failed to submit code');
    } finally {
      setSubmitting(false);
    }
  };

  const getDifficultyColor = (difficulty) => {
    const colors = {
      EASY: 'success',
      MEDIUM: 'warning',
      HARD: 'danger',
    };
    return colors[difficulty] || 'secondary';
  };

  if (loading) return <LoadingSpinner />;

  return (
    <Container className="py-4">
      <Card className="shadow">
        <Card.Header className="bg-danger text-white">
          <h4 className="mb-0">
            <i className="bi bi-code-slash me-2"></i>
            Coding Practice
          </h4>
        </Card.Header>

        <Card.Body>
          {error && !showModal && <Alert variant="danger">{error}</Alert>}

          {problems.length === 0 ? (
            <Alert variant="info">
              No problems available yet. Check back later!
            </Alert>
          ) : (
            <Row>
              {problems.map((problem) => (
                <Col key={problem.id} md={6} lg={4} className="mb-4">
                  <Card className="h-100 border-danger hover-card">
                    <Card.Body>
                      <div className="d-flex justify-content-between align-items-start mb-2">
                        <Badge bg={getDifficultyColor(problem.difficulty)}>
                          {problem.difficulty}
                        </Badge>
                        <Badge bg="secondary">{problem.category}</Badge>
                      </div>
                      <Card.Title className="mt-2">{problem.title}</Card.Title>
                      <Card.Text className="text-muted small">
                        {problem.description.substring(0, 100)}...
                      </Card.Text>
                      {problem.tags && (
                        <div className="mb-2">
                          {problem.tags.slice(0, 3).map((tag, index) => (
                            <Badge key={index} bg="light" text="dark" className="me-1">
                              {tag}
                            </Badge>
                          ))}
                        </div>
                      )}
                      <Button
                        variant="danger"
                        size="sm"
                        className="w-100"
                        onClick={() => handleProblemClick(problem)}
                      >
                        <i className="bi bi-play-fill me-1"></i>Solve Problem
                      </Button>
                    </Card.Body>
                  </Card>
                </Col>
              ))}
            </Row>
          )}
        </Card.Body>
      </Card>

      <Modal show={showModal} onHide={() => setShowModal(false)} size="xl">
        <Modal.Header closeButton>
          <Modal.Title>
            {selectedProblem?.title}
            <Badge bg={getDifficultyColor(selectedProblem?.difficulty)} className="ms-2">
              {selectedProblem?.difficulty}
            </Badge>
          </Modal.Title>
        </Modal.Header>

        <Modal.Body>
          {error && <Alert variant="danger">{error}</Alert>}

          <Card className="mb-3">
            <Card.Body>
              <h6>Problem Description:</h6>
              <p>{selectedProblem?.description}</p>

              <Row>
                <Col md={6}>
                  <h6>Sample Input:</h6>
                  <pre className="bg-light p-2 rounded">
                    {selectedProblem?.sampleInput}
                  </pre>
                </Col>
                <Col md={6}>
                  <h6>Sample Output:</h6>
                  <pre className="bg-light p-2 rounded">
                    {selectedProblem?.sampleOutput}
                  </pre>
                </Col>
              </Row>
            </Card.Body>
          </Card>

          <Form.Group className="mb-3">
            <Form.Label>Language:</Form.Label>
            <Form.Select value={language} onChange={(e) => setLanguage(e.target.value)}>
              <option value="JAVA">Java</option>
              <option value="PYTHON" disabled>Python (Coming Soon)</option>
              <option value="CPP" disabled>C++ (Coming Soon)</option>
            </Form.Select>
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Your Code:</Form.Label>
            <Form.Control
              as="textarea"
              rows={15}
              value={code}
              onChange={(e) => setCode(e.target.value)}
              style={{ fontFamily: 'monospace', fontSize: '14px' }}
            />
          </Form.Group>

          {result && (
            <Alert variant={result.status === 'ACCEPTED' ? 'success' : 'danger'}>
              <h6>
                <i className={`bi bi-${result.status === 'ACCEPTED' ? 'check-circle' : 'x-circle'} me-2`}></i>
                {result.status.replace('_', ' ')}
              </h6>
              <p className="mb-0">
                Test Cases Passed: {result.testCasesPassed}/{result.totalTestCases}
              </p>
              {result.errorMessage && (
                <p className="mb-0 mt-2">
                  <strong>Error:</strong> {result.errorMessage}
                </p>
              )}
            </Alert>
          )}
        </Modal.Body>

        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Close
          </Button>
          <Button
            variant="danger"
            onClick={handleSubmitCode}
            disabled={submitting}
          >
            {submitting ? 'Submitting...' : 'Submit Code'}
          </Button>
        </Modal.Footer>
      </Modal>

      <style>{`
        .hover-card {
          transition: transform 0.2s, box-shadow 0.2s;
        }
        .hover-card:hover {
          transform: translateY(-5px);
          box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15) !important;
        }
      `}</style>
    </Container>
  );
}

export default CodingPractice;
