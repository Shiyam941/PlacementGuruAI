# PlacementGuru AI - Quick Start Guide

Get the application running in 5 minutes!

## 🚀 Prerequisites Installation

### 1. Install Java 17
**Windows:**
- Download from [Oracle](https://www.oracle.com/java/technologies/downloads/#java17) or [Adoptium](https://adoptium.net/)
- Run installer and follow instructions
- Verify: `java -version`

**Mac:**
```bash
brew install openjdk@17
```

**Linux:**
```bash
sudo apt-get update
sudo apt-get install openjdk-17-jdk
```

### 2. Install Maven
**Windows:**
- Download from [Apache Maven](https://maven.apache.org/download.cgi)
- Extract and add to PATH

**Mac:**
```bash
brew install maven
```

**Linux:**
```bash
sudo apt-get install maven
```

Verify: `mvn -version`

### 3. Install Node.js & npm
Download and install from [nodejs.org](https://nodejs.org/) (LTS version)

Verify:
```bash
node -version
npm -version
```

### 4. Install MongoDB
**Option A: Local MongoDB**
- Download from [mongodb.com/try/download/community](https://www.mongodb.com/try/download/community)
- Install and start service

**Option B: MongoDB Atlas (Recommended)**
- Create free account at [mongodb.com/cloud/atlas](https://www.mongodb.com/cloud/atlas)
- Create free cluster (M0)
- Get connection string

## 📦 Quick Setup

### Step 1: Clone & Navigate
```bash
cd "C:\Users\SHYAM\Downloads\New folder\PlacementGuruAI"
```

### Step 2: Configure Backend

Edit `backend/src/main/resources/application.properties`:

```properties
# MongoDB (choose one)
# Local:
spring.data.mongodb.uri=mongodb://localhost:27017/placementguru

# Atlas:
# spring.data.mongodb.uri=mongodb+srv://username:password@cluster.mongodb.net/placementguru

# JWT Secret (change this!)
jwt.secret=mySecretKeyForJWTTokenGeneration1234567890PlacementGuruAI2024
jwt.expiration=86400000

# AI API (get free API key from OpenAI or Google)
ai.api.key=your-api-key-here
ai.api.url=https://api.openai.com/v1/chat/completions
ai.model=gpt-3.5-turbo

# Frontend URL
app.cors.allowed-origins=http://localhost:3000
```

### Step 3: Start Backend
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Backend will run on `http://localhost:8080`

### Step 4: Start Frontend (New Terminal)
```bash
cd frontend
npm install
npm start
```

Frontend will run on `http://localhost:3000`

## 🎯 First Steps

### 1. Create Admin Account
Open browser to `http://localhost:3000`

- Click "Sign up"
- Fill in details:
  - Name: Admin User
  - Email: admin@test.com
  - Password: admin123
  - Role: **Admin**
  - Skill Level: Advanced
- Click "Sign Up"

### 2. Create Student Account
- Logout
- Click "Sign up"
- Fill in details:
  - Name: Test Student
  - Email: student@test.com
  - Password: student123
  - Role: **Student**
  - Skill Level: Beginner
- Click "Sign Up"

### 3. Explore Features

**As Student:**
- ✅ Chat with AI Chatbot
- ✅ Take Mock Interview
- ✅ Upload and Analyze Resume
- ✅ View Daily Roadmap
- ✅ Solve Coding Problems

**As Admin:**
- ✅ Add Coding Problems
- ✅ View Student Progress
- ✅ Manage Question Bank

## 🔑 Getting API Keys

### OpenAI API Key (Recommended)
1. Go to [platform.openai.com](https://platform.openai.com)
2. Sign up / Login
3. Go to API Keys section
4. Create new secret key
5. Copy and paste into `application.properties`

**Free Tier:** $5 free credit for new users

### Google Gemini API Key (Alternative)
1. Go to [makersuite.google.com/app/apikey](https://makersuite.google.com/app/apikey)
2. Sign in with Google account
3. Create API key
4. Update `application.properties`:
```properties
ai.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent
ai.model=gemini-pro
```

**Free Tier:** 60 requests per minute

## 🧪 Test the Application

### Test Backend API
```bash
# Test health
curl http://localhost:8080/api/auth/test

# Test signup
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "test123",
    "role": "STUDENT",
    "skillLevel": "BEGINNER"
  }'
```

### Test Frontend
1. Open `http://localhost:3000`
2. Should see login page
3. Try signup and login
4. Navigate through features

## 🐛 Common Issues & Solutions

### Issue: Backend won't start
```
Error: Failed to configure a DataSource
```
**Solution:** Check MongoDB connection string in `application.properties`

### Issue: Port 8080 already in use
```
Error: Port 8080 is already in use
```
**Solution:** 
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Mac/Linux
lsof -ti:8080 | xargs kill -9
```

### Issue: Frontend can't connect to backend
```
Error: Network Error
```
**Solution:**
1. Verify backend is running on port 8080
2. Check CORS configuration in `application.properties`
3. Check `api.js` has correct base URL

### Issue: AI responses not working
```
Error: AI service connection failed
```
**Solution:**
1. Verify API key is correct
2. Check API URL format
3. Ensure you have API credits
4. Test API key directly:
```bash
curl https://api.openai.com/v1/models \
  -H "Authorization: Bearer YOUR_API_KEY"
```

### Issue: Resume upload fails
```
Error: File upload failed
```
**Solution:**
1. Ensure file is PDF format
2. Check file size < 10MB
3. Verify PDFBox dependency in pom.xml

### Issue: MongoDB connection timeout
```
Error: Timed out after 30000 ms
```
**Solution:**
1. Check MongoDB is running: `mongod --version`
2. If using Atlas, whitelist your IP: 0.0.0.0/0
3. Verify connection string format

## 📝 Sample Data

### Add Sample Coding Problem (as Admin)

```json
{
  "title": "Two Sum",
  "description": "Given an array of integers nums and an integer target, return indices of the two numbers that add up to target.",
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
      "hidden": false
    }
  ]
}
```

## 🎓 Next Steps

1. ✅ Complete setup and test all features
2. 📚 Read [API_DOCUMENTATION.md](API_DOCUMENTATION.md) for API details
3. 🚀 Follow [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md) to deploy
4. 🎨 Customize UI colors and branding
5. 📊 Add more coding problems
6. 🔧 Implement additional features

## 🆘 Need Help?

- 📖 Read [README.md](README.md) for complete documentation
- 🔍 Check [API_DOCUMENTATION.md](API_DOCUMENTATION.md) for API details
- 🚀 See [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md) for deployment
- 🐛 Create GitHub issue for bugs
- 💬 Join community discussions

## 🎉 Success Indicators

You're ready when:
- ✅ Backend runs without errors
- ✅ Frontend displays correctly
- ✅ Can create accounts and login
- ✅ AI chatbot responds to messages
- ✅ Can upload and analyze resume
- ✅ Daily roadmap generates tasks
- ✅ Coding problems load and submit

## ⚡ Quick Commands Reference

```bash
# Start Backend
cd backend
mvn spring-boot:run

# Start Frontend
cd frontend
npm start

# Build Backend
cd backend
mvn clean package

# Build Frontend
cd frontend
npm run build

# Run Tests
cd backend
mvn test

# Install Dependencies
cd frontend
npm install

# Clear Cache
cd frontend
rm -rf node_modules package-lock.json
npm install
```

---

**Happy Coding! 🚀**

If you encounter any issues, check the troubleshooting section first, then refer to the detailed documentation files.
