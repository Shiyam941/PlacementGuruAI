import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import PrivateRoute from './components/PrivateRoute';
import Login from './pages/Login';
import Signup from './pages/Signup';
import Dashboard from './pages/Dashboard';
import Chatbot from './pages/Chatbot';
import MockInterview from './pages/MockInterview';
import ResumeAnalyzer from './pages/ResumeAnalyzer';
import DailyRoadmap from './pages/DailyRoadmap';
import CodingPractice from './pages/CodingPractice';
import AdminPanel from './pages/AdminPanel';

function App() {
  return (
    <Router>
      <div className="App">
        <Navbar />
        <Routes>
          <Route path="/" element={<Navigate to="/login" />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          
          <Route path="/dashboard" element={
            <PrivateRoute>
              <Dashboard />
            </PrivateRoute>
          } />
          
          <Route path="/chatbot" element={
            <PrivateRoute>
              <Chatbot />
            </PrivateRoute>
          } />
          
          <Route path="/mock-interview" element={
            <PrivateRoute>
              <MockInterview />
            </PrivateRoute>
          } />
          
          <Route path="/resume-analyzer" element={
            <PrivateRoute>
              <ResumeAnalyzer />
            </PrivateRoute>
          } />
          
          <Route path="/daily-roadmap" element={
            <PrivateRoute>
              <DailyRoadmap />
            </PrivateRoute>
          } />
          
          <Route path="/coding-practice" element={
            <PrivateRoute>
              <CodingPractice />
            </PrivateRoute>
          } />
          
          <Route path="/admin" element={
            <PrivateRoute>
              <AdminPanel />
            </PrivateRoute>
          } />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
