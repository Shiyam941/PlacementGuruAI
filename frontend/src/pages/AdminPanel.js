import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button, Form, Alert, Table, Modal, Badge } from 'react-bootstrap';
import { adminAPI } from '../services/api';
import LoadingSpinner from '../components/LoadingSpinner';

function AdminPanel() {
  const [activeTab, setActiveTab] = useState('problems');
  const [problems, setProblems] = useState([]);
  const [students, setStudents] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const [problemForm, setProblemForm] = useState({
    title: '',
    description: '',
    difficulty: 'EASY',
    category: 'ARRAY',
    tags: '',
    sampleInput: '',
    sampleOutput: '',
    testCases: [{ input: '', expectedOutput: '', hidden: false }],
  });

  useEffect(() => {
    if (activeTab === 'problems') {
      loadProblems();
    } else if (activeTab === 'students') {
      loadStudents();
    }
  }, [activeTab]);

  const loadProblems = async () => {
    setLoading(true);
    try {
      const response = await adminAPI.getAllProblems();
      setProblems(response.data.data || []);
    } catch (err) {
      setError('Failed to load problems');
    } finally {
      setLoading(false);
    }
  };

  const loadStudents = async () => {
    setLoading(true);
    try {
      const response = await adminAPI.getAllStudentsProgress();
      setStudents(response.data.data || []);
    } catch (err) {
      setError('Failed to load student progress');
    } finally {
      setLoading(false);
    }
  };

  const handleAddTestCase = () => {
    setProblemForm({
      ...problemForm,
      testCases: [...problemForm.testCases, { input: '', expectedOutput: '', hidden: false }],
    });
  };

  const handleTestCaseChange = (index, field, value) => {
    const newTestCases = [...problemForm.testCases];
    newTestCases[index][field] = value;
    setProblemForm({ ...problemForm, testCases: newTestCases });
  };

  const handleSubmitProblem = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);

    try {
      const problemData = {
        ...problemForm,
        tags: problemForm.tags.split(',').map(tag => tag.trim()).filter(tag => tag),
      };
      await adminAPI.addProblem(problemData);
      setSuccess('Problem added successfully!');
      setProblemForm({
        title: '',
        description: '',
        difficulty: 'EASY',
        category: 'ARRAY',
        tags: '',
        sampleInput: '',
        sampleOutput: '',
        testCases: [{ input: '', expectedOutput: '', hidden: false }],
      });
      setShowModal(false);
      loadProblems();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to add problem');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteProblem = async (problemId) => {
    if (!window.confirm('Are you sure you want to delete this problem?')) return;

    try {
      await adminAPI.deleteProblem(problemId);
      setSuccess('Problem deleted successfully!');
      loadProblems();
    } catch (err) {
      setError('Failed to delete problem');
    }
  };

  return (
    <Container className="py-4">
      <Card className="shadow">
        <Card.Header className="bg-dark text-white">
          <h4 className="mb-0">
            <i className="bi bi-shield-fill me-2"></i>
            Admin Panel
          </h4>
        </Card.Header>

        <Card.Body>
          {error && <Alert variant="danger" dismissible onClose={() => setError('')}>{error}</Alert>}
          {success && <Alert variant="success" dismissible onClose={() => setSuccess('')}>{success}</Alert>}

          <div className="mb-4">
            <Button
              variant={activeTab === 'problems' ? 'dark' : 'outline-dark'}
              className="me-2"
              onClick={() => setActiveTab('problems')}
            >
              <i className="bi bi-code-slash me-1"></i>
              Manage Problems
            </Button>
            <Button
              variant={activeTab === 'students' ? 'dark' : 'outline-dark'}
              onClick={() => setActiveTab('students')}
            >
              <i className="bi bi-people-fill me-1"></i>
              Student Progress
            </Button>
          </div>

          {activeTab === 'problems' && (
            <div>
              <div className="d-flex justify-content-between align-items-center mb-3">
                <h5>Coding Problems</h5>
                <Button variant="dark" onClick={() => setShowModal(true)}>
                  <i className="bi bi-plus-circle me-1"></i>
                  Add New Problem
                </Button>
              </div>

              {loading ? (
                <LoadingSpinner />
              ) : problems.length === 0 ? (
                <Alert variant="info">No problems added yet</Alert>
              ) : (
                <Table striped bordered hover responsive>
                  <thead className="table-dark">
                    <tr>
                      <th>Title</th>
                      <th>Difficulty</th>
                      <th>Category</th>
                      <th>Test Cases</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {problems.map((problem) => (
                      <tr key={problem.id}>
                        <td>{problem.title}</td>
                        <td>
                          <Badge bg={
                            problem.difficulty === 'EASY' ? 'success' :
                            problem.difficulty === 'MEDIUM' ? 'warning' : 'danger'
                          }>
                            {problem.difficulty}
                          </Badge>
                        </td>
                        <td>{problem.category}</td>
                        <td>{problem.testCases?.length || 0}</td>
                        <td>
                          <Button
                            variant="danger"
                            size="sm"
                            onClick={() => handleDeleteProblem(problem.id)}
                          >
                            <i className="bi bi-trash"></i>
                          </Button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              )}
            </div>
          )}

          {activeTab === 'students' && (
            <div>
              <h5 className="mb-3">Student Progress Overview</h5>

              {loading ? (
                <LoadingSpinner />
              ) : students.length === 0 ? (
                <Alert variant="info">No students registered yet</Alert>
              ) : (
                <Table striped bordered hover responsive>
                  <thead className="table-dark">
                    <tr>
                      <th>Name</th>
                      <th>Email</th>
                      <th>Total Submissions</th>
                      <th>Accepted</th>
                      <th>Success Rate</th>
                    </tr>
                  </thead>
                  <tbody>
                    {students.map((student, index) => (
                      <tr key={index}>
                        <td>{student.studentName}</td>
                        <td>{student.email}</td>
                        <td>{student.totalSubmissions}</td>
                        <td>{student.acceptedSubmissions}</td>
                        <td>
                          <Badge bg={
                            student.successRate >= 70 ? 'success' :
                            student.successRate >= 40 ? 'warning' : 'danger'
                          }>
                            {student.successRate.toFixed(1)}%
                          </Badge>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              )}
            </div>
          )}
        </Card.Body>
      </Card>

      {/* Add Problem Modal */}
      <Modal show={showModal} onHide={() => setShowModal(false)} size="lg">
        <Modal.Header closeButton>
          <Modal.Title>Add New Coding Problem</Modal.Title>
        </Modal.Header>

        <Form onSubmit={handleSubmitProblem}>
          <Modal.Body>
            <Form.Group className="mb-3">
              <Form.Label>Title *</Form.Label>
              <Form.Control
                type="text"
                value={problemForm.title}
                onChange={(e) => setProblemForm({ ...problemForm, title: e.target.value })}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Description *</Form.Label>
              <Form.Control
                as="textarea"
                rows={4}
                value={problemForm.description}
                onChange={(e) => setProblemForm({ ...problemForm, description: e.target.value })}
                required
              />
            </Form.Group>

            <Row>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Difficulty</Form.Label>
                  <Form.Select
                    value={problemForm.difficulty}
                    onChange={(e) => setProblemForm({ ...problemForm, difficulty: e.target.value })}
                  >
                    <option value="EASY">Easy</option>
                    <option value="MEDIUM">Medium</option>
                    <option value="HARD">Hard</option>
                  </Form.Select>
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Category</Form.Label>
                  <Form.Select
                    value={problemForm.category}
                    onChange={(e) => setProblemForm({ ...problemForm, category: e.target.value })}
                  >
                    <option value="ARRAY">Array</option>
                    <option value="STRING">String</option>
                    <option value="DP">Dynamic Programming</option>
                    <option value="GRAPH">Graph</option>
                    <option value="TREE">Tree</option>
                    <option value="MATH">Math</option>
                  </Form.Select>
                </Form.Group>
              </Col>
            </Row>

            <Form.Group className="mb-3">
              <Form.Label>Tags (comma separated)</Form.Label>
              <Form.Control
                type="text"
                value={problemForm.tags}
                onChange={(e) => setProblemForm({ ...problemForm, tags: e.target.value })}
                placeholder="e.g., two-pointer, sorting, binary-search"
              />
            </Form.Group>

            <Row>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Sample Input</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={3}
                    value={problemForm.sampleInput}
                    onChange={(e) => setProblemForm({ ...problemForm, sampleInput: e.target.value })}
                  />
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group className="mb-3">
                  <Form.Label>Sample Output</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={3}
                    value={problemForm.sampleOutput}
                    onChange={(e) => setProblemForm({ ...problemForm, sampleOutput: e.target.value })}
                  />
                </Form.Group>
              </Col>
            </Row>

            <h6 className="mb-2">Test Cases</h6>
            {problemForm.testCases.map((testCase, index) => (
              <Card key={index} className="mb-2">
                <Card.Body>
                  <Row>
                    <Col md={5}>
                      <Form.Group className="mb-2">
                        <Form.Label>Input</Form.Label>
                        <Form.Control
                          type="text"
                          value={testCase.input}
                          onChange={(e) => handleTestCaseChange(index, 'input', e.target.value)}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={5}>
                      <Form.Group className="mb-2">
                        <Form.Label>Expected Output</Form.Label>
                        <Form.Control
                          type="text"
                          value={testCase.expectedOutput}
                          onChange={(e) => handleTestCaseChange(index, 'expectedOutput', e.target.value)}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={2}>
                      <Form.Group className="mb-2">
                        <Form.Label>Hidden</Form.Label>
                        <Form.Check
                          type="checkbox"
                          checked={testCase.hidden}
                          onChange={(e) => handleTestCaseChange(index, 'hidden', e.target.checked)}
                        />
                      </Form.Group>
                    </Col>
                  </Row>
                </Card.Body>
              </Card>
            ))}
            <Button variant="outline-dark" size="sm" onClick={handleAddTestCase}>
              <i className="bi bi-plus-circle me-1"></i>
              Add Test Case
            </Button>
          </Modal.Body>

          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowModal(false)}>
              Cancel
            </Button>
            <Button variant="dark" type="submit" disabled={loading}>
              {loading ? 'Adding...' : 'Add Problem'}
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>
    </Container>
  );
}

export default AdminPanel;
