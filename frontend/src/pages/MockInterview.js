import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button, Form, Alert, Badge } from 'react-bootstrap';
import { interviewAPI } from '../services/api';

function MockInterview() {
  const [interviewType, setInterviewType] = useState('HR');
  const [sessionActive, setSessionActive] = useState(false);
  const [sessionId, setSessionId] = useState('');
  const [currentQuestion, setCurrentQuestion] = useState('');
  const [answer, setAnswer] = useState('');
  const [feedback, setFeedback] = useState('');
  const [questionNumber, setQuestionNumber] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [completed, setCompleted] = useState(false);
  const [score, setScore] = useState(null);

  const startInterview = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await interviewAPI.startInterview({
        interviewType,
        action: 'START',
      });
      const data = response.data.data;
      setSessionId(data.sessionId);
      setCurrentQuestion(data.question);
      setQuestionNumber(data.questionNumber);
      setSessionActive(true);
      setCompleted(false);
      setFeedback('');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to start interview');
    } finally {
      setLoading(false);
    }
  };

  const submitAnswer = async () => {
    if (!answer.trim()) {
      setError('Please provide an answer');
      return;
    }

    setLoading(true);
    setError('');
    try {
      const response = await interviewAPI.submitAnswer({
        sessionId,
        action: 'ANSWER',
        answer,
      });
      const data = response.data.data;
      setFeedback(data.feedback);

      if (data.completed) {
        setCompleted(true);
        setSessionActive(false);
        setScore(data.totalScore);
      } else {
        setCurrentQuestion(data.nextQuestion);
        setQuestionNumber(data.questionNumber);
        setAnswer('');
        setTimeout(() => setFeedback(''), 5000);
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to submit answer');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container className="py-4">
      <Row className="justify-content-center">
        <Col lg={10}>
          <Card className="shadow">
            <Card.Header className="bg-success text-white">
              <h4 className="mb-0">
                <i className="bi bi-mic-fill me-2"></i>
                Mock Interview Practice
              </h4>
            </Card.Header>

            <Card.Body>
              {error && <Alert variant="danger">{error}</Alert>}

              {!sessionActive && !completed && (
                <div className="text-center p-5">
                  <i className="bi bi-mic" style={{ fontSize: '5rem', color: '#198754' }}></i>
                  <h3 className="mt-4 mb-4">Select Interview Type</h3>
                  
                  <Form.Group className="mb-4">
                    <div className="d-flex justify-content-center gap-3 flex-wrap">
                      {['HR', 'TECHNICAL', 'PROJECT'].map((type) => (
                        <Button
                          key={type}
                          variant={interviewType === type ? 'success' : 'outline-success'}
                          onClick={() => setInterviewType(type)}
                          size="lg"
                        >
                          {type}
                        </Button>
                      ))}
                    </div>
                  </Form.Group>

                  <Button
                    variant="success"
                    size="lg"
                    onClick={startInterview}
                    disabled={loading}
                  >
                    {loading ? 'Starting...' : 'Start Interview'}
                  </Button>
                </div>
              )}

              {sessionActive && (
                <div>
                  <div className="d-flex justify-content-between align-items-center mb-4">
                    <Badge bg="success" className="fs-6">
                      {interviewType} Interview
                    </Badge>
                    <Badge bg="info" className="fs-6">
                      Question {questionNumber}
                    </Badge>
                  </div>

                  <Card className="mb-4 border-success">
                    <Card.Body>
                      <h5 className="text-success mb-3">
                        <i className="bi bi-question-circle me-2"></i>Question:
                      </h5>
                      <p className="fs-5">{currentQuestion}</p>
                    </Card.Body>
                  </Card>

                  {feedback && (
                    <Alert variant="info">
                      <strong>Feedback:</strong> {feedback}
                    </Alert>
                  )}

                  <Form.Group className="mb-3">
                    <Form.Label className="fw-bold">Your Answer:</Form.Label>
                    <Form.Control
                      as="textarea"
                      rows={6}
                      value={answer}
                      onChange={(e) => setAnswer(e.target.value)}
                      placeholder="Type your answer here..."
                      disabled={loading}
                    />
                  </Form.Group>

                  <div className="d-flex justify-content-end">
                    <Button
                      variant="success"
                      onClick={submitAnswer}
                      disabled={loading || !answer.trim()}
                    >
                      {loading ? 'Submitting...' : 'Submit Answer'}
                    </Button>
                  </div>
                </div>
              )}

              {completed && (
                <div className="text-center p-5">
                  <i className="bi bi-check-circle-fill text-success" style={{ fontSize: '5rem' }}></i>
                  <h3 className="mt-4 mb-3">Interview Completed!</h3>
                  <h4 className="text-success">
                    Your Score: {score}/25
                  </h4>
                  <p className="text-muted mb-4">
                    Great job completing the interview! Keep practicing to improve.
                  </p>
                  <Button variant="success" onClick={() => setCompleted(false)}>
                    Start New Interview
                  </Button>
                </div>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
}

export default MockInterview;
