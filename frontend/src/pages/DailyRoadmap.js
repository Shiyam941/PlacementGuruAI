import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button, Alert, Badge, ProgressBar, ListGroup } from 'react-bootstrap';
import { roadmapAPI } from '../services/api';
import LoadingSpinner from '../components/LoadingSpinner';

function DailyRoadmap() {
  const [plan, setPlan] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadTodayPlan();
  }, []);

  const loadTodayPlan = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await roadmapAPI.getTodayPlan();
      setPlan(response.data.data);
    } catch (err) {
      setError('Failed to load daily plan');
    } finally {
      setLoading(false);
    }
  };

  const handleTaskComplete = async (taskIndex) => {
    try {
      const response = await roadmapAPI.markTaskComplete(plan.id, taskIndex);
      setPlan(response.data.data);
    } catch (err) {
      setError('Failed to mark task as complete');
    }
  };

  const getCategoryIcon = (category) => {
    const icons = {
      CODING: 'code-slash',
      APTITUDE: 'calculator',
      INTERVIEW_PREP: 'mic',
      PROJECT: 'folder',
    };
    return icons[category] || 'check-circle';
  };

  const getCategoryColor = (category) => {
    const colors = {
      CODING: 'primary',
      APTITUDE: 'success',
      INTERVIEW_PREP: 'warning',
      PROJECT: 'info',
    };
    return colors[category] || 'secondary';
  };

  if (loading) return <LoadingSpinner />;

  return (
    <Container className="py-4">
      <Row className="justify-content-center">
        <Col lg={10}>
          <Card className="shadow">
            <Card.Header className="bg-info text-white">
              <div className="d-flex justify-content-between align-items-center">
                <h4 className="mb-0">
                  <i className="bi bi-calendar-check-fill me-2"></i>
                  Daily Roadmap
                </h4>
                <Button variant="light" size="sm" onClick={loadTodayPlan}>
                  <i className="bi bi-arrow-clockwise me-1"></i>Refresh
                </Button>
              </div>
            </Card.Header>

            <Card.Body>
              {error && <Alert variant="danger">{error}</Alert>}

              {plan && (
                <>
                  <Card className="mb-4 bg-light">
                    <Card.Body>
                      <Row>
                        <Col md={8}>
                          <h5 className="mb-3">
                            <i className="bi bi-trophy me-2 text-warning"></i>
                            Today's Progress
                          </h5>
                          <ProgressBar
                            now={(plan.completedTasks / plan.totalTasks) * 100}
                            label={`${plan.completedTasks}/${plan.totalTasks} Tasks Completed`}
                            variant="info"
                            style={{ height: '30px', fontSize: '16px' }}
                          />
                        </Col>
                        <Col md={4} className="text-center">
                          <Badge bg="info" className="fs-4 p-3">
                            {plan.skillLevel}
                          </Badge>
                        </Col>
                      </Row>
                    </Card.Body>
                  </Card>

                  <Card className="mb-4 border-warning">
                    <Card.Body className="bg-light">
                      <h6 className="text-warning mb-2">
                        <i className="bi bi-lightbulb-fill me-2"></i>
                        Motivation for Today
                      </h6>
                      <p className="mb-0 fst-italic">{plan.motivation}</p>
                    </Card.Body>
                  </Card>

                  <h5 className="mb-3">Today's Tasks</h5>
                  <ListGroup className="mb-4">
                    {plan.tasks.map((task, index) => (
                      <ListGroup.Item
                        key={index}
                        className={task.completed ? 'bg-light' : ''}
                      >
                        <Row className="align-items-center">
                          <Col md={1} className="text-center">
                            <Form.Check
                              type="checkbox"
                              checked={task.completed}
                              onChange={() => handleTaskComplete(index)}
                              disabled={task.completed}
                              style={{ transform: 'scale(1.5)' }}
                            />
                          </Col>
                          <Col md={8}>
                            <h6 className={task.completed ? 'text-decoration-line-through text-muted' : ''}>
                              <i className={`bi bi-${getCategoryIcon(task.category)} me-2 text-${getCategoryColor(task.category)}`}></i>
                              {task.title}
                            </h6>
                            <p className="mb-0 text-muted small">{task.description}</p>
                          </Col>
                          <Col md={2}>
                            <Badge bg={getCategoryColor(task.category)}>
                              {task.category.replace('_', ' ')}
                            </Badge>
                          </Col>
                          <Col md={1} className="text-end">
                            <Badge bg="secondary">
                              {task.estimatedMinutes} min
                            </Badge>
                          </Col>
                        </Row>
                      </ListGroup.Item>
                    ))}
                  </ListGroup>

                  {plan.completedTasks === plan.totalTasks && (
                    <Alert variant="success" className="text-center">
                      <h5>
                        <i className="bi bi-trophy-fill me-2"></i>
                        Congratulations! You've completed all tasks for today! 🎉
                      </h5>
                    </Alert>
                  )}
                </>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
}

// Add Form import at the top
import { Form } from 'react-bootstrap';

export default DailyRoadmap;
