import React, { useState } from 'react';
import { Container, Row, Col, Card, Button, Form, Alert, ProgressBar, Badge, ListGroup } from 'react-bootstrap';
import { resumeAPI } from '../services/api';

function ResumeAnalyzer() {
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [result, setResult] = useState(null);

  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    if (selectedFile && selectedFile.type === 'application/pdf') {
      setFile(selectedFile);
      setError('');
    } else {
      setFile(null);
      setError('Please select a PDF file');
    }
  };

  const handleAnalyze = async () => {
    if (!file) {
      setError('Please select a file first');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);

    setLoading(true);
    setError('');
    try {
      const response = await resumeAPI.analyzeResume(formData);
      setResult(response.data.data);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to analyze resume');
    } finally {
      setLoading(false);
    }
  };

  const getScoreVariant = (score) => {
    if (score >= 80) return 'success';
    if (score >= 60) return 'warning';
    return 'danger';
  };

  return (
    <Container className="py-4">
      <Row className="justify-content-center">
        <Col lg={10}>
          <Card className="shadow">
            <Card.Header className="bg-warning text-dark">
              <h4 className="mb-0">
                <i className="bi bi-file-earmark-text-fill me-2"></i>
                Resume Analyzer
              </h4>
            </Card.Header>

            <Card.Body>
              {error && <Alert variant="danger">{error}</Alert>}

              <Card className="mb-4 bg-light">
                <Card.Body>
                  <h5 className="mb-3">
                    <i className="bi bi-upload me-2"></i>Upload Your Resume
                  </h5>
                  <Form.Group className="mb-3">
                    <Form.Control
                      type="file"
                      accept=".pdf"
                      onChange={handleFileChange}
                      disabled={loading}
                    />
                    <Form.Text className="text-muted">
                      Only PDF files are supported. Max size: 10MB
                    </Form.Text>
                  </Form.Group>

                  {file && (
                    <Alert variant="info" className="mb-3">
                      <i className="bi bi-file-pdf me-2"></i>
                      Selected: {file.name}
                    </Alert>
                  )}

                  <Button
                    variant="warning"
                    onClick={handleAnalyze}
                    disabled={!file || loading}
                    className="w-100"
                  >
                    {loading ? 'Analyzing...' : 'Analyze Resume'}
                  </Button>
                </Card.Body>
              </Card>

              {result && (
                <div>
                  <Card className="mb-4 border-warning">
                    <Card.Body>
                      <div className="text-center mb-4">
                        <h3 className="mb-2">ATS Score</h3>
                        <h1 className={`display-3 fw-bold text-${getScoreVariant(result.atsScore)}`}>
                          {result.atsScore}/100
                        </h1>
                        <ProgressBar
                          now={result.atsScore}
                          variant={getScoreVariant(result.atsScore)}
                          className="mt-3"
                          style={{ height: '10px' }}
                        />
                      </div>
                    </Card.Body>
                  </Card>

                  <Row>
                    <Col md={6} className="mb-4">
                      <Card className="h-100 border-success">
                        <Card.Header className="bg-success text-white">
                          <h5 className="mb-0">
                            <i className="bi bi-check-circle me-2"></i>Strengths
                          </h5>
                        </Card.Header>
                        <Card.Body>
                          <ListGroup variant="flush">
                            {result.strengths.map((strength, index) => (
                              <ListGroup.Item key={index}>
                                <i className="bi bi-plus-circle text-success me-2"></i>
                                {strength}
                              </ListGroup.Item>
                            ))}
                          </ListGroup>
                        </Card.Body>
                      </Card>
                    </Col>

                    <Col md={6} className="mb-4">
                      <Card className="h-100 border-danger">
                        <Card.Header className="bg-danger text-white">
                          <h5 className="mb-0">
                            <i className="bi bi-exclamation-circle me-2"></i>Weaknesses
                          </h5>
                        </Card.Header>
                        <Card.Body>
                          <ListGroup variant="flush">
                            {result.weaknesses.map((weakness, index) => (
                              <ListGroup.Item key={index}>
                                <i className="bi bi-dash-circle text-danger me-2"></i>
                                {weakness}
                              </ListGroup.Item>
                            ))}
                          </ListGroup>
                        </Card.Body>
                      </Card>
                    </Col>
                  </Row>

                  <Card className="mb-4 border-primary">
                    <Card.Header className="bg-primary text-white">
                      <h5 className="mb-0">
                        <i className="bi bi-lightbulb me-2"></i>Suggestions
                      </h5>
                    </Card.Header>
                    <Card.Body>
                      <ListGroup variant="flush">
                        {result.suggestions.map((suggestion, index) => (
                          <ListGroup.Item key={index}>
                            <i className="bi bi-arrow-right-circle text-primary me-2"></i>
                            {suggestion}
                          </ListGroup.Item>
                        ))}
                      </ListGroup>
                    </Card.Body>
                  </Card>

                  <Card className="border-info">
                    <Card.Header className="bg-info text-white">
                      <h5 className="mb-0">
                        <i className="bi bi-gear me-2"></i>Detected Skills
                      </h5>
                    </Card.Header>
                    <Card.Body>
                      <div className="d-flex flex-wrap gap-2">
                        {result.detectedSkills.map((skill, index) => (
                          <Badge key={index} bg="info" className="fs-6">
                            {skill}
                          </Badge>
                        ))}
                      </div>
                    </Card.Body>
                  </Card>
                </div>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
}

export default ResumeAnalyzer;
