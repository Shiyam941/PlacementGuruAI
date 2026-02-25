# 🎓 PlacementGuru AI - Complete Setup & Run Guide

**A Full-Stack AI-Powered Placement Preparation Platform**

Welcome! This guide will help you get PlacementGuru AI running on your machine in **under 30 minutes**.

---

## 📋 **WHAT YOU NEED TO INSTALL**

Before starting, you'll need these installed on your computer:

### **1. Java 17 (JDK)**
Java is required for the backend server.

**Download & Install:**
- **Windows**: [Download from Oracle](https://www.oracle.com/java/technologies/downloads/#java17) or [Adoptium](https://adoptium.net/)
- **Mac**: Run `brew install openjdk@17`
- **Linux**: Run `sudo apt install openjdk-17-jdk`

**Verify Installation:**
```bash
java -version
```
✅ Should show: `java version "17.x.x"`

---

### **2. Maven (Build Tool)**
Maven compiles and packages the Java backend.

**Download & Install:**
- **Windows**: [Download from Apache Maven](https://maven.apache.org/download.cgi), extract, and add to PATH
- **Mac**: Run `brew install maven`
- **Linux**: Run `sudo apt install maven`

**Verify Installation:**
```bash
mvn -version
```
✅ Should show: `Apache Maven 3.x.x`

---

### **3. Node.js & npm (JavaScript Runtime)**
Node.js is required for the React frontend.

**Download & Install:**
- Download from [nodejs.org](https://nodejs.org/) (LTS version)
- Installer includes npm automatically

**Verify Installation:**
```bash
node -v
npm -v
```
✅ Should show versions like `v18.x.x` and `9.x.x`

---

### **4. MongoDB (Database)**

**OPTION A: MongoDB Atlas (Recommended - Free Cloud Database)**

1. Go to [mongodb.com/cloud/atlas](https://www.mongodb.com/cloud/atlas)
2. Create FREE account
3. Click "Build a Database" → Choose **FREE (M0)** tier
4. Select cloud provider and region (any is fine)
5. Create Cluster (takes 3-5 minutes)
6. **Database Access** → Add Database User:
   - Username: `student`
   - Password: `student123` (or your choice)
7. **Network Access** → Add IP Address:
   - Select "Allow Access from Anywhere" (0.0.0.0/0)
8. Click "Connect" → "Connect your application"
9. Copy connection string:
   ```
   mongodb+srv://student:student123@cluster0.xxxxx.mongodb.net/placementguru
   ```

**OPTION B: Local MongoDB (For Advanced Users)**

- **Windows**: [Download MongoDB Community](https://www.mongodb.com/try/download/community)
- **Mac**: Run `brew install mongodb-community`
- **Linux**: Follow [MongoDB docs](https://docs.mongodb.com/manual/administration/install-on-linux/)

---

### **5. AI API Key (REQUIRED)**

You need one of these for AI features to work:

**OPTION A: OpenAI (Recommended)**
1. Go to [platform.openai.com](https://platform.openai.com)
2. Sign up (Google/Microsoft account works)
3. Go to [API Keys](https://platform.openai.com/api-keys)
4. Click "Create new secret key"
5. Copy the key (starts with `sk-...`)
6. ✅ New accounts get $5 FREE credit

**OPTION B: Google Gemini (Free Alternative)**
1. Go to [makersuite.google.com/app/apikey](https://makersuite.google.com/app/apikey)
2. Sign in with Google
3. Click "Create API Key"
4. Copy the key
5. ✅ Free tier: 60 requests/minute

---

## 🚀 **STEP-BY-STEP SETUP**

### **STEP 1: Open Terminal/Command Prompt**

**Windows**: Press `Win + R`, type `cmd`, press Enter

**Mac/Linux**: Open Terminal application

### **STEP 2: Navigate to Project Folder**

```bash
cd "C:\Users\SHYAM\Downloads\New folder\PlacementGuruAI"
```

### **STEP 3: Configure Backend**

Open this file in any text editor (Notepad, VS Code, etc.):
```
backend\src\main\resources\application.properties
```

**IMPORTANT: Update these 2 things:**

1. **MongoDB Connection** (around line 8):
   ```properties
   # If using MongoDB Atlas (replace with YOUR connection string):
   spring.data.mongodb.uri=mongodb+srv://student:student123@cluster0.xxxxx.mongodb.net/placementguru
   
   # If using Local MongoDB (uncomment this):
   # spring.data.mongodb.uri=mongodb://localhost:27017/placementguru
   ```

2. **AI API Key** (around line 18):
   ```properties
   # If using OpenAI (replace with YOUR API key):
   ai.api.key=sk-your-actual-openai-api-key-here
   
   # If using Gemini (uncomment these and replace key):
   # ai.api.key=your-gemini-api-key
   # ai.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent
   # ai.model=gemini-pro
   ```

**Save the file** after editing.

---

### **STEP 4: Install Backend Dependencies**

In your terminal:

```bash
cd backend
mvn clean install
```

⏳ **This will take 2-3 minutes**. Maven is downloading all required libraries.

**Expected Output:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: 2.5 min
```

✅ If you see `BUILD SUCCESS`, you're good!

❌ If you see errors, check Java and Maven are installed correctly.

---

### **STEP 5: Start the Backend Server**

**Keep the same terminal open** and run:

```bash
mvn spring-boot:run
```

⏳ **Wait 10-15 seconds** for the server to start.

**Success Indicators:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

PlacementGuru AI Platform is running...
Started PlacementGuruApplication in 8.5 seconds
```

✅ **Backend is now running on:** `http://localhost:8080`

**⚠️ KEEP THIS TERMINAL OPEN** (don't close it!)

---

### **STEP 6: Install Frontend Dependencies**

**Open a NEW terminal/command prompt** (keep backend running)

Navigate to frontend folder:

```bash
cd "C:\Users\SHYAM\Downloads\New folder\PlacementGuruAI\frontend"
npm install
```

⏳ **This will take 2-3 minutes**. npm is downloading React and all dependencies.

**Expected Output:**
```
added 1500+ packages in 2-3 minutes
```

✅ If you see `added X packages`, you're good!

---

### **STEP 7: Start the Frontend Server**

**In the same terminal** (frontend folder):

```bash
npm start
```

⏳ **Wait 10-20 seconds**. Your browser will automatically open!

**Expected Output:**
```
Compiled successfully!

You can now view placementguru-frontend in the browser.

  Local:            http://localhost:3000
  On Your Network:  http://192.168.x.x:3000
```

✅ **Frontend is now running on:** `http://localhost:3000`

**Browser should automatically open to the login page!** 🎉

---

## 👥 **CREATE YOUR FIRST ACCOUNTS**

### **Create Admin Account (For Full Access)**

1. Click **"Sign up here"** link on login page
2. Fill in the form:
   - **Name**: `Admin User`
   - **Email**: `admin@test.com`
   - **Password**: `admin123`
   - **Confirm Password**: `admin123`
   - **Skill Level**: `Advanced`
   - **Role**: `Admin` ⭐
3. Click **"Sign Up"**
4. You'll be automatically logged in!

### **Create Student Account**

1. Click **Logout** (top right corner)
2. Click **"Sign up here"**
3. Fill in the form:
   - **Name**: `Test Student`
   - **Email**: `student@test.com`
   - **Password**: `student123`
   - **Confirm Password**: `student123`
   - **Skill Level**: `Beginner`
   - **Role**: `Student`
4. Click **"Sign Up"**

---

## 🧪 **TEST THE FEATURES**

### **Test as Student** (Login: `student@test.com` / `student123`)

#### 1️⃣ **AI Chatbot**
- Click **"Chatbot"** in navigation
- Type: `"What is a binary search tree?"`
- Press **Enter** or click **Send**
- ✅ AI should respond with explanation!

#### 2️⃣ **Mock Interview**
- Click **"Mock Interview"** in navigation
- Select **Interview Type**: `Technical`
- Select **Topic**: `Data Structures`
- Number of Questions: `3`
- Click **"Start Interview"**
- Answer the questions typed by AI
- ✅ You'll get feedback after each answer!

#### 3️⃣ **Resume Analyzer**
- Click **"Resume Analyzer"** in navigation
- Upload any PDF resume file
- Click **"Analyze Resume"**
- ✅ You'll get ATS score + improvement suggestions!

#### 4️⃣ **Daily Roadmap**
- Click **"Daily Roadmap"** in navigation
- Select **Topic**: `DSA Fundamentals`
- Select **Duration**: `30 Days`
- Click **"Generate Roadmap"**
- ✅ AI creates personalized day-by-day study plan!

#### 5️⃣ **Coding Practice**
- First, you need problems added (login as admin first)

### **Test as Admin** (Login: `admin@test.com` / `admin123`)

#### 1️⃣ **Add Coding Problem**
- Click **"Admin Panel"** in navigation
- Go to **"Problems"** tab
- Click **"Add New Problem"**
- Fill in:
  - **Title**: `Two Sum`
  - **Description**: `Given array of integers, return indices of two numbers that add up to target`
  - **Difficulty**: `Easy`
  - **Tags**: `Array, Hash Table`
  - **Sample Input**: `[2,7,11,15], target = 9`
  - **Sample Output**: `[0,1]`
  - Add test cases with input/output/explanation
- Click **"Save Problem"**
- ✅ Problem is now available for students!

#### 2️⃣ **View Student Progress**
- Go to **"Student Progress"** tab
- ✅ See all student submissions and statistics!

---

## 📁 **PROJECT STRUCTURE**

```
PlacementGuruAI/
├── backend/
│   ├── src/main/java/com/placementguru/
│   │   ├── PlacementGuruApplication.java        # Main entry point
│   │   ├── config/                               # JWT, Security, MongoDB config
│   │   ├── controller/                           # REST API endpoints
│   │   ├── dto/                                  # Request/Response objects
│   │   ├── model/                                # MongoDB entities
│   │   ├── repository/                           # Database repositories
│   │   └── service/                              # Business logic
│   ├── src/main/resources/
│   │   └── application.properties                # 🔑 Configuration file
│   └── pom.xml                                    # Maven dependencies
│
├── frontend/
│   ├── public/                                    # Static files
│   ├── src/
│   │   ├── components/                            # Reusable components
│   │   ├── pages/                                 # Page components
│   │   ├── services/
│   │   │   └── api.js                             # API calls to backend
│   │   ├── App.js                                 # Main app component
│   │   └── index.js                               # Entry point
│   ├── package.json                               # npm dependencies
│   └── .env                                       # Environment variables (optional)
│
├── README.md                                      # This file!
└── QUICKSTART.md                                  # 5-minute quick guide
```

---

## 🌐 **API ENDPOINTS**

### **Authentication** (`/api/auth`)
- `POST /api/auth/signup` - Register new user
- `POST /api/auth/login` - Login user (returns JWT token)

### **Chatbot** (`/api/chatbot`)
- `POST /api/chatbot/chat` - Send message, get AI response

### **Mock Interview** (`/api/interview`)
- `POST /api/interview/start` - Start new interview session
- `POST /api/interview/{id}/answer` - Submit answer, get next question
- `POST /api/interview/{id}/complete` - End interview, get feedback
- `GET /api/interview/history` - Get past interview sessions

### **Resume Analyzer** (`/api/resume`)
- `POST /api/resume/analyze` - Upload PDF, get ATS analysis

### **Daily Roadmap** (`/api/roadmap`)
- `POST /api/roadmap/generate` - Generate daily study plan
- `GET /api/roadmap` - Get all roadmaps for logged-in user

### **Coding Practice** (`/api/coding`)
- `GET /api/coding/problems` - Get all coding problems
- `GET /api/coding/problems/{id}` - Get specific problem details
- `POST /api/coding/submit` - Submit code solution
- `GET /api/coding/submissions` - Get user's submissions

### **Admin** (`/api/admin`) - Requires ADMIN role
- `POST /api/admin/problems` - Add new coding problem
- `PUT /api/admin/problems/{id}` - Update coding problem
- `DELETE /api/admin/problems/{id}` - Delete coding problem
- `GET /api/admin/students/progress` - View all student progress

---

## 🐛 **TROUBLESHOOTING**

### **❌ Backend doesn't start**

**Problem**: Port 8080 already in use
```
Address already in use: bind
```

**Solution**: Kill process on port 8080
- **Windows**: `netstat -ano | findstr :8080` then `taskkill /PID <PID> /F`
- **Mac/Linux**: `lsof -ti:8080 | xargs kill -9`

---

**Problem**: MongoDB connection refused
```
com.mongodb.MongoTimeoutException: Timed out after 30000 ms
```

**Solutions**:
1. Check MongoDB is running: `mongod --version`
2. For Atlas: Check Network Access allows your IP (0.0.0.0/0)
3. For Atlas: Check username/password in connection string
4. Verify `application.properties` has correct MongoDB URI

---

**Problem**: AI API not working
```
401 Unauthorized
```

**Solutions**:
1. Check your API key is valid
2. For OpenAI: Verify you have credits at [platform.openai.com/usage](https://platform.openai.com/usage)
3. Check `ai.api.key` in `application.properties` has no extra spaces
4. If using Gemini, verify `ai.api.url` is set correctly

---

### **❌ Frontend doesn't start**

**Problem**: npm install fails
```
npm ERR! code ERESOLVE
```

**Solution**: Use legacy peer deps
```bash
npm install --legacy-peer-deps
```

---

**Problem**: "Proxy error" or CORS issues
```
Proxy error: Could not proxy request
```

**Solutions**:
1. Make sure backend is running on port 8080
2. Check `package.json` has: `"proxy": "http://localhost:8080"`
3. Clear browser cache (Ctrl + Shift + Delete)
4. Restart both frontend and backend

---

**Problem**: White blank screen after login

**Solutions**:
1. Open browser console (F12) and check for errors
2. Clear localStorage: In console type `localStorage.clear()` 
3. Hard refresh page (Ctrl + Shift + R)

---

### **❌ Features not working**

**Problem**: Chat/Interview shows "Error connecting to AI"

**Solutions**:
1. Check backend console logs for API errors
2. Verify AI API key has credits remaining
3. Test API key:
   - OpenAI: `curl https://api.openai.com/v1/models -H "Authorization: Bearer YOUR_KEY"`
   - Gemini: Check key at [makersuite.google.com/app/apikey](https://makersuite.google.com/app/apikey)

---

**Problem**: Resume analyzer fails

**Solutions**:
1. Make sure file is a valid PDF (not image or Word doc)
2. File size should be under 10MB
3. Check backend logs for PDF parsing errors

---

**Problem**: Can't login as admin

**Solution**: Make sure you selected **"Admin"** during signup. Or create new admin account.

---

## 🌍 **DEPLOYMENT (OPTIONAL)**

### **Backend Deployment** - Render.com (FREE)

1. Push code to GitHub
2. Go to [render.com](https://render.com)
3. New → Web Service
4. Connect GitHub repo
5. Settings:
   - **Build Command**: `cd backend && mvn clean install`
   - **Start Command**: `cd backend && mvn spring-boot:run`
   - **Environment Variables**: Add `MONGODB_URI`, `AI_API_KEY`, `JWT_SECRET`

### **Frontend Deployment** - Netlify (FREE)

1. Go to [netlify.com](https://netlify.com)
2. New Site from Git → Connect GitHub
3. Build Settings:
   - **Base Directory**: `frontend`
   - **Build Command**: `npm run build`
   - **Publish Directory**: `frontend/build`
4. Add environment variable: `REACT_APP_API_URL` = your Render backend URL

---

## 📊 **DATABASE SCHEMA**

MongoDB Collections:

1. **users** - User authentication and profiles
2. **chat_history** - Chatbot conversations
3. **interview_sessions** - Mock interview records with Q&A pairs
4. **resume_reports** - Resume analysis results
5. **daily_plans** - Generated study roadmaps
6. **coding_problems** - Coding challenge problems
7. **submissions** - Student code submissions and results

---

## 🎯 **WHAT'S NEXT?**

After successfully running the project:

✅ **Customize the Platform**:
- Add more coding problems via Admin Panel
- Adjust AI prompts in `AIService.java` for better responses
- Customize UI colors in Bootstrap classes

✅ **Enhance Features**:
- Add email notifications (integrate SendGrid or Nodemailer)
- Add video interview practice (integrate WebRTC)
- Add collaborative code editor (integrate Monaco Editor)

✅ **Deploy to Production**:
- Follow deployment guide above
- Get custom domain name
- Setup SSL certificate (automatic with Netlify/Render)

---

## 👨‍💻 **NEED HELP?**

If you're stuck:

1. **Check the logs**:
   - Backend: Look at the terminal where `mvn spring-boot:run` is running
   - Frontend: Look at browser console (F12)

2. **Common fixes**:
   - Restart backend and frontend servers
   - Clear browser cache and localStorage
   - Verify all API keys are correct in `application.properties`
   - Check MongoDB connection

3. **Still stuck?**:
   - Check the QUICKSTART.md file for alternative setup methods
   - Review application.properties for configuration errors
   - Ensure all prerequisites are installed correctly

---

## 📝 **KEY REMINDERS**

✅ **Always keep TWO terminals open**: One for backend (`mvn spring-boot:run`), one for frontend (`npm start`)

✅ **Backend runs on**: `http://localhost:8080`

✅ **Frontend runs on**: `http://localhost:3000`

✅ **Database**: MongoDB Atlas (free cloud) OR local MongoDB

✅ **AI API**: OpenAI ($5 free credit) OR Gemini (free tier)

✅ **First admin**: Create manually via signup with "Admin" role

---

## 🎉 **YOU'RE READY!**

If your backend shows "PlacementGuru AI Platform is running..." and your frontend opened in browser, **congratulations!** 🎊

You now have a fully functional AI-powered placement preparation platform!

Start by creating admin and student accounts, add some coding problems, and test all the features.

**Happy Coding!** 💻🚀

### 2. Backend Setup

#### Configure MongoDB
Edit `backend/src/main/resources/application.properties`:

```properties
# For local MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/placementguru

# For MongoDB Atlas
spring.data.mongodb.uri=mongodb+srv://<username>:<password>@cluster.mongodb.net/placementguru
```

#### Configure AI API
Add your API key in `application.properties`:

```properties
# For OpenAI
ai.api.key=your-openai-api-key
ai.api.url=https://api.openai.com/v1/chat/completions
ai.model=gpt-3.5-turbo

# For Gemini
ai.api.key=your-gemini-api-key
ai.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent
ai.model=gemini-pro
```

#### Build and Run Backend
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Backend will start on `http://localhost:8080`

### 3. Frontend Setup

```bash
cd frontend
npm install
npm start
```

Frontend will start on `http://localhost:3000`

## 📁 Project Structure

```
PlacementGuruAI/
├── backend/
│   ├── src/main/java/com/placementguru/
│   │   ├── config/           # Security, JWT, CORS configuration
│   │   ├── controller/       # REST API controllers
│   │   ├── dto/              # Data Transfer Objects
│   │   ├── model/            # MongoDB entities
│   │   ├── repository/       # MongoDB repositories
│   │   └── service/          # Business logic
│   └── src/main/resources/
│       └── application.properties
├── frontend/
│   ├── public/
│   └── src/
│       ├── components/       # Reusable React components
│       ├── pages/            # Page components
│       └── services/         # API service layer
├── pom.xml
├── package.json
└── README.md
```

## 🔐 Default User Credentials

### Admin Account
After signup, set role to "ADMIN" to access admin features.

### Student Account
Default role is "STUDENT" for regular users.

## 📡 API Endpoints

### Authentication
- `POST /api/auth/signup` - Register new user
- `POST /api/auth/login` - User login
- `GET /api/auth/test` - Test API connection

### Chatbot
- `POST /api/chatbot/message` - Send message to chatbot
- `GET /api/chatbot/history` - Get chat history
- `DELETE /api/chatbot/history` - Clear chat history

### Mock Interview
- `POST /api/interview/start` - Start interview session
- `POST /api/interview/answer` - Submit answer
- `GET /api/interview/history` - Get interview history
- `GET /api/interview/{sessionId}` - Get session details

### Resume Analyzer
- `POST /api/resume/analyze` - Analyze resume (multipart/form-data)
- `GET /api/resume/history` - Get analysis history
- `GET /api/resume/{reportId}` - Get report details

### Daily Roadmap
- `GET /api/roadmap/today` - Get today's plan
- `GET /api/roadmap/generate` - Generate new plan
- `PUT /api/roadmap/{planId}/task/{taskIndex}/complete` - Mark task complete
- `GET /api/roadmap/history` - Get plan history

### Coding Practice
- `GET /api/coding/problems` - Get all problems
- `GET /api/coding/problems/{id}` - Get problem by ID
- `POST /api/coding/submit` - Submit code solution
- `GET /api/coding/submissions` - Get user submissions

### Admin (Requires ADMIN role)
- `POST /api/admin/problems` - Add coding problem
- `PUT /api/admin/problems/{id}` - Update problem
- `DELETE /api/admin/problems/{id}` - Delete problem
- `GET /api/admin/students/progress` - Get all students progress
- `GET /api/admin/students/{id}/progress` - Get specific student progress

## 🧪 Testing

### Test API Connection
```bash
curl http://localhost:8080/api/auth/test
```

### Create Admin User
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Admin User",
    "email": "admin@example.com",
    "password": "admin123",
    "role": "ADMIN",
    "skillLevel": "ADVANCED"
  }'
```

## 🚀 Deployment

### Deploy Backend (Render)

1. Create account on [Render.com](https://render.com)
2. Create new Web Service
3. Connect your GitHub repository
4. Configure:
   - Build Command: `mvn clean install`
   - Start Command: `java -jar target/placement-guru-ai-1.0.0.jar`
   - Add environment variables:
     - `MONGODB_URI`
     - `JWT_SECRET`
     - `AI_API_KEY`

### Deploy Frontend (Netlify)

1. Create account on [Netlify](https://netlify.com)
2. Connect GitHub repository
3. Configure:
   - Build Command: `cd frontend && npm install && npm run build`
   - Publish Directory: `frontend/build`
4. Add environment variable:
   - `REACT_APP_API_URL` = Your backend URL

### MongoDB Atlas Setup

1. Create free cluster on [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Create database user
3. Whitelist IP addresses (use 0.0.0.0/0 for development)
4. Get connection string
5. Update `application.properties` with connection string

## 🔑 Environment Variables

### Backend (.env or application.properties)
```
MONGODB_URI=mongodb+srv://username:password@cluster.mongodb.net/placementguru
JWT_SECRET=your-secret-key-min-256-bits
JWT_EXPIRATION=86400000
AI_API_KEY=your-ai-api-key
AI_API_URL=https://api.openai.com/v1/chat/completions
AI_MODEL=gpt-3.5-turbo
CORS_ALLOWED_ORIGINS=http://localhost:3000
```

### Frontend (.env)
```
REACT_APP_API_URL=http://localhost:8080/api
```

## 📝 Database Schema

### Collections:
- **users**: User authentication and profile data
- **chat_history**: Chat conversations with AI
- **interview_sessions**: Mock interview data and feedback
- **resume_reports**: Resume analysis results
- **daily_plans**: AI-generated daily study plans
- **coding_problems**: Coding practice problems
- **submissions**: Code submission results

## 🐛 Troubleshooting

### Backend Issues
- **Port 8080 already in use**: Change port in `application.properties`
- **MongoDB connection failed**: Check MongoDB is running and connection string is correct
- **JWT errors**: Ensure JWT secret is at least 256 bits (32 characters)

### Frontend Issues
- **API connection failed**: Check backend is running and CORS is configured
- **Login not working**: Verify JWT token is being stored in localStorage
- **Module not found**: Run `npm install` again

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev)
- [MongoDB Documentation](https://docs.mongodb.com)
- [OpenAI API Documentation](https://platform.openai.com/docs)

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👨‍💻 Author

PlacementGuru AI Team

## 🙏 Acknowledgments

- OpenAI for GPT API
- Google for Gemini API
- Spring Boot Team
- React Team
- MongoDB Team

---

**Note**: Replace placeholder values (API keys, database URLs) with your actual credentials before deployment.

For issues or questions, please create an issue in the repository.
