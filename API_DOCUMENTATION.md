# PlacementGuru AI - API Documentation

## Base URL
```
Development: http://localhost:8080/api
Production: https://your-app.onrender.com/api
```

## Authentication
All endpoints (except `/auth/signup` and `/auth/login`) require a JWT token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

---

## 🔐 Authentication Endpoints

### 1. Register User
**POST** `/auth/signup`

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "STUDENT",
  "skillLevel": "BEGINNER"
}
```

**Response:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "type": "Bearer",
    "id": "507f1f77bcf86cd799439011",
    "name": "John Doe",
    "email": "john@example.com",
    "roles": ["STUDENT"]
  }
}
```

### 2. Login
**POST** `/auth/login`

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response:** Same as signup

### 3. Test API
**GET** `/auth/test`

**Response:**
```json
{
  "success": true,
  "message": "API is working"
}
```

---

## 💬 Chatbot Endpoints

### 1. Send Message
**POST** `/chatbot/message`

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "message": "What are the key topics for Java interviews?",
  "category": "TECHNICAL"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Response generated",
  "data": {
    "response": "For Java interviews, focus on: 1. OOP Concepts...",
    "chatId": "507f1f77bcf86cd799439011"
  }
}
```

### 2. Get Chat History
**GET** `/chatbot/history`

**Response:**
```json
{
  "success": true,
  "message": "History retrieved",
  "data": [
    {
      "id": "507f1f77bcf86cd799439011",
      "userId": "507f191e810c19729de860ea",
      "userMessage": "What are Java basics?",
      "botResponse": "Java basics include...",
      "category": "TECHNICAL",
      "timestamp": "2024-02-19T10:30:00"
    }
  ]
}
```

### 3. Clear History
**DELETE** `/chatbot/history`

**Response:**
```json
{
  "success": true,
  "message": "History cleared"
}
```

---

## 🎤 Mock Interview Endpoints

### 1. Start Interview
**POST** `/interview/start`

**Request Body:**
```json
{
  "interviewType": "HR",
  "action": "START"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Interview started",
  "data": {
    "sessionId": "507f1f77bcf86cd799439011",
    "question": "Tell me about yourself",
    "questionNumber": 1
  }
}
```

### 2. Submit Answer
**POST** `/interview/answer`

**Request Body:**
```json
{
  "sessionId": "507f1f77bcf86cd799439011",
  "action": "ANSWER",
  "answer": "I am a final year student..."
}
```

**Response:**
```json
{
  "success": true,
  "message": "Answer submitted",
  "data": {
    "completed": false,
    "feedback": "Good answer, but try to be more specific...",
    "nextQuestion": "What are your strengths?",
    "questionNumber": 2
  }
}
```

### 3. Get Interview History
**GET** `/interview/history`

**Response:**
```json
{
  "success": true,
  "message": "History retrieved",
  "data": [
    {
      "id": "507f1f77bcf86cd799439011",
      "interviewType": "HR",
      "startTime": "2024-02-19T10:00:00",
      "endTime": "2024-02-19T10:30:00",
      "score": 18,
      "completed": true
    }
  ]
}
```

---

## 📄 Resume Analyzer Endpoints

### 1. Analyze Resume
**POST** `/resume/analyze`

**Content-Type:** `multipart/form-data`

**Form Data:**
- `file`: PDF file (required)

**Response:**
```json
{
  "success": true,
  "message": "Resume analyzed successfully",
  "data": {
    "reportId": "507f1f77bcf86cd799439011",
    "atsScore": 75,
    "strengths": [
      "Good technical skills listed",
      "Clear project descriptions"
    ],
    "weaknesses": [
      "Missing quantifiable achievements",
      "Limited use of action verbs"
    ],
    "suggestions": [
      "Add metrics to your achievements",
      "Include relevant certifications"
    ],
    "detectedSkills": ["Java", "Spring Boot", "MongoDB", "React"]
  }
}
```

### 2. Get Report History
**GET** `/resume/history`

**Response:**
```json
{
  "success": true,
  "message": "History retrieved",
  "data": [
    {
      "id": "507f1f77bcf86cd799439011",
      "fileName": "john_resume.pdf",
      "atsScore": 75,
      "analyzedAt": "2024-02-19T10:00:00"
    }
  ]
}
```

---

## 🗓️ Daily Roadmap Endpoints

### 1. Get Today's Plan
**GET** `/roadmap/today`

**Response:**
```json
{
  "success": true,
  "message": "Today's plan retrieved",
  "data": {
    "id": "507f1f77bcf86cd799439011",
    "date": "2024-02-19",
    "skillLevel": "BEGINNER",
    "totalTasks": 5,
    "completedTasks": 2,
    "motivation": "Consistency is key to success!",
    "tasks": [
      {
        "title": "Solve Array Problems",
        "description": "Solve 3 easy array problems",
        "category": "CODING",
        "estimatedMinutes": 60,
        "completed": true
      }
    ]
  }
}
```

### 2. Generate Plan for Date
**GET** `/roadmap/generate?date=2024-02-20`

**Response:** Same as Get Today's Plan

### 3. Mark Task Complete
**PUT** `/roadmap/{planId}/task/{taskIndex}/complete`

**Response:**
```json
{
  "success": true,
  "message": "Task marked as complete",
  "data": { /* Updated plan */ }
}
```

---

## 💻 Coding Practice Endpoints

### 1. Get All Problems
**GET** `/coding/problems`

**Response:**
```json
{
  "success": true,
  "message": "Problems retrieved",
  "data": [
    {
      "id": "507f1f77bcf86cd799439011",
      "title": "Two Sum",
      "description": "Given an array of integers...",
      "difficulty": "EASY",
      "category": "ARRAY",
      "tags": ["array", "hash-table"],
      "sampleInput": "[2, 7, 11, 15], target = 9",
      "sampleOutput": "[0, 1]",
      "testCases": [
        {
          "input": "[2,7,11,15]\n9",
          "expectedOutput": "[0,1]",
          "hidden": false
        }
      ]
    }
  ]
}
```

### 2. Get Problem by ID
**GET** `/coding/problems/{id}`

**Response:** Single problem object

### 3. Submit Code
**POST** `/coding/submit`

**Request Body:**
```json
{
  "problemId": "507f1f77bcf86cd799439011",
  "code": "public class Solution {\n  public int[] twoSum...",
  "language": "JAVA"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Code submitted",
  "data": {
    "id": "507f1f77bcf86cd799439012",
    "status": "ACCEPTED",
    "testCasesPassed": 3,
    "totalTestCases": 3,
    "executionTime": 125,
    "testResults": [
      {
        "testCaseNumber": 1,
        "passed": true,
        "actualOutput": "[0,1]",
        "expectedOutput": "[0,1]"
      }
    ]
  }
}
```

### 4. Get User Submissions
**GET** `/coding/submissions`

**Response:**
```json
{
  "success": true,
  "message": "Submissions retrieved",
  "data": [
    {
      "id": "507f1f77bcf86cd799439012",
      "problemId": "507f1f77bcf86cd799439011",
      "status": "ACCEPTED",
      "submittedAt": "2024-02-19T10:00:00"
    }
  ]
}
```

---

## 🛡️ Admin Endpoints

**Note:** All admin endpoints require ADMIN role

### 1. Add Coding Problem
**POST** `/admin/problems`

**Request Body:**
```json
{
  "title": "Two Sum",
  "description": "Given an array of integers nums and an integer target...",
  "difficulty": "EASY",
  "category": "ARRAY",
  "tags": ["array", "hash-table"],
  "sampleInput": "[2,7,11,15], target = 9",
  "sampleOutput": "[0,1]",
  "testCases": [
    {
      "input": "[2,7,11,15]\n9",
      "expectedOutput": "[0,1]",
      "hidden": false
    },
    {
      "input": "[3,2,4]\n6",
      "expectedOutput": "[1,2]",
      "hidden": true
    }
  ]
}
```

**Response:**
```json
{
  "success": true,
  "message": "Problem added successfully",
  "data": { /* Created problem */ }
}
```

### 2. Update Problem
**PUT** `/admin/problems/{id}`

**Request Body:** Same as Add Problem

### 3. Delete Problem
**DELETE** `/admin/problems/{id}`

**Response:**
```json
{
  "success": true,
  "message": "Problem deleted successfully"
}
```

### 4. Get All Students Progress
**GET** `/admin/students/progress`

**Response:**
```json
{
  "success": true,
  "message": "Student progress retrieved",
  "data": [
    {
      "studentName": "John Doe",
      "email": "john@example.com",
      "totalSubmissions": 25,
      "acceptedSubmissions": 18,
      "successRate": 72.0
    }
  ]
}
```

### 5. Get Specific Student Progress
**GET** `/admin/students/{studentId}/progress`

**Response:** Single student progress object

---

## ⚠️ Error Responses

All error responses follow this format:

```json
{
  "success": false,
  "message": "Error message description"
}
```

### Common Error Codes:
- `400 Bad Request` - Invalid input data
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

---

## 📝 Postman Collection

Import this JSON into Postman for quick testing:

```json
{
  "info": {
    "name": "PlacementGuru AI",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Auth",
      "item": [
        {
          "name": "Signup",
          "request": {
            "method": "POST",
            "url": "{{base_url}}/auth/signup",
            "body": {
              "mode": "raw",
              "raw": "{\n  \"name\": \"Test User\",\n  \"email\": \"test@example.com\",\n  \"password\": \"test123\",\n  \"role\": \"STUDENT\",\n  \"skillLevel\": \"BEGINNER\"\n}"
            }
          }
        },
        {
          "name": "Login",
          "request": {
            "method": "POST",
            "url": "{{base_url}}/auth/login",
            "body": {
              "mode": "raw",
              "raw": "{\n  \"email\": \"test@example.com\",\n  \"password\": \"test123\"\n}"
            }
          }
        }
      ]
    }
  ],
  "variable": [
    {
      "key": "base_url",
      "value": "http://localhost:8080/api"
    },
    {
      "key": "token",
      "value": ""
    }
  ]
}
```

---

## 🔧 Rate Limiting

Currently no rate limiting is implemented. In production, consider:
- 100 requests per minute for authenticated users
- 10 requests per minute for unauthenticated users

## 📊 Response Times

Expected response times:
- Auth endpoints: < 500ms
- Chatbot: 2-5 seconds (AI processing)
- Resume Analysis: 3-8 seconds (PDF + AI processing)
- Coding submission: 1-3 seconds
- Other endpoints: < 200ms

---

For more details, refer to the main [README.md](README.md)
