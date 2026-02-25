# PlacementGuru AI - Deployment Guide

Complete guide to deploy the PlacementGuru AI application to production.

## 📋 Pre-Deployment Checklist

- [ ] MongoDB Atlas account and cluster created
- [ ] OpenAI or Gemini API key obtained
- [ ] GitHub repository set up
- [ ] All environment variables documented
- [ ] Application tested locally
- [ ] Code pushed to GitHub

---

## 🗄️ Step 1: MongoDB Atlas Setup

### 1.1 Create MongoDB Atlas Account
1. Go to [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Sign up for a free account
3. Create a new project named "PlacementGuru"

### 1.2 Create Cluster
1. Click "Build a Cluster"
2. Choose **FREE** tier (M0 Sandbox)
3. Select cloud provider (AWS recommended)
4. Choose region closest to your users
5. Name your cluster: `placementguru-cluster`
6. Click "Create Cluster" (takes 3-5 minutes)

### 1.3 Create Database User
1. Go to "Database Access" in left sidebar
2. Click "Add New Database User"
3. Choose authentication method: Password
4. Username: `placementguru_admin`
5. Password: Generate secure password (save it!)
6. Database User Privileges: Read and write to any database
7. Click "Add User"

### 1.4 Configure Network Access
1. Go to "Network Access" in left sidebar
2. Click "Add IP Address"
3. For development: Click "Allow Access from Anywhere" (0.0.0.0/0)
4. For production: Add specific IP addresses
5. Click "Confirm"

### 1.5 Get Connection String
1. Click "Connect" on your cluster
2. Choose "Connect your application"
3. Driver: Java, Version: 4.3 or later
4. Copy connection string:
```
mongodb+srv://placementguru_admin:<password>@placementguru-cluster.xxxxx.mongodb.net/placementguru
```
5. Replace `<password>` with your actual password
6. Save this connection string for backend deployment

---

## 🚀 Step 2: Deploy Backend (Render)

### 2.1 Prepare Backend for Deployment

1. **Update application.properties** for production:
```properties
# Use environment variables
spring.data.mongodb.uri=${MONGODB_URI}
jwt.secret=${JWT_SECRET}
jwt.expiration=86400000
ai.api.key=${AI_API_KEY}
ai.api.url=${AI_API_URL}
ai.model=${AI_MODEL}
app.cors.allowed-origins=${FRONTEND_URL}
```

2. **Update pom.xml** - ensure proper packaging:
```xml
<packaging>jar</packaging>
```

3. **Commit and push changes**:
```bash
git add .
git commit -m "Prepare for deployment"
git push origin main
```

### 2.2 Deploy on Render

1. **Create Render Account**
   - Go to [Render.com](https://render.com)
   - Sign up with GitHub account

2. **Create New Web Service**
   - Click "New +" → "Web Service"
   - Connect your GitHub repository
   - Select the PlacementGuruAI repository

3. **Configure Service**
   - **Name**: `placementguru-backend`
   - **Region**: Choose closest to your users
   - **Branch**: `main`
   - **Root Directory**: `backend` (if applicable)
   - **Runtime**: `Java`
   - **Build Command**:
   ```bash
   mvn clean install -DskipTests
   ```
   - **Start Command**:
   ```bash
   java -Dserver.port=$PORT -jar target/placement-guru-ai-1.0.0.jar
   ```

4. **Add Environment Variables**
   Click "Advanced" → "Add Environment Variable":
   
   ```
   MONGODB_URI=mongodb+srv://placementguru_admin:PASSWORD@cluster.mongodb.net/placementguru
   
   JWT_SECRET=YourSuperSecretKeyMustBeAtLeast256BitsLongForHS512Algorithm
   
   AI_API_KEY=your-openai-or-gemini-api-key
   
   AI_API_URL=https://api.openai.com/v1/chat/completions
   
   AI_MODEL=gpt-3.5-turbo
   
   FRONTEND_URL=https://your-frontend-url.netlify.app
   
   JAVA_OPTS=-Xmx512m
   ```

5. **Choose Plan**
   - Select **Free** plan
   - Note: Free plan spins down after 15 minutes of inactivity

6. **Create Web Service**
   - Click "Create Web Service"
   - Wait 5-10 minutes for deployment
   - Your backend URL will be: `https://placementguru-backend.onrender.com`

### 2.3 Verify Backend Deployment
Test your backend:
```bash
curl https://placementguru-backend.onrender.com/api/auth/test
```

Expected response:
```json
{"success":true,"message":"API is working"}
```

---

## 🎨 Step 3: Deploy Frontend (Netlify)

### 3.1 Prepare Frontend for Deployment

1. **Create `.env.production` file** in frontend directory:
```env
REACT_APP_API_URL=https://placementguru-backend.onrender.com/api
```

2. **Update api.js** to use environment variable:
```javascript
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';
```

3. **Add `_redirects` file** in `frontend/public`:
```
/*  /index.html  200
```

4. **Commit changes**:
```bash
git add .
git commit -m "Prepare frontend for deployment"
git push origin main
```

### 3.2 Deploy on Netlify

1. **Create Netlify Account**
   - Go to [Netlify.com](https://netlify.com)
   - Sign up with GitHub

2. **Create New Site**
   - Click "Add new site" → "Import an existing project"
   - Choose "GitHub"
   - Select your repository

3. **Configure Build Settings**
   - **Base directory**: `frontend`
   - **Build command**:
   ```bash
   npm install && npm run build
   ```
   - **Publish directory**: `frontend/build`

4. **Add Environment Variables**
   Go to "Site settings" → "Environment variables":
   ```
   REACT_APP_API_URL=https://placementguru-backend.onrender.com/api
   ```

5. **Deploy Site**
   - Click "Deploy site"
   - Wait 2-3 minutes
   - Your frontend URL: `https://your-site-name.netlify.app`

### 3.3 Custom Domain (Optional)
1. Go to "Domain settings"
2. Click "Add custom domain"
3. Follow DNS configuration instructions

### 3.4 Update Backend CORS
Go back to Render and update `FRONTEND_URL` environment variable:
```
FRONTEND_URL=https://your-site-name.netlify.app
```

---

## 🔐 Step 4: Security Configuration

### 4.1 Generate Strong JWT Secret
```bash
# Generate 512-bit secret
openssl rand -base64 64
```

### 4.2 Enable HTTPS
Both Render and Netlify provide free SSL certificates automatically.

### 4.3 Security Headers
Add to `SecurityConfig.java`:
```java
http.headers()
    .contentSecurityPolicy("default-src 'self'")
    .and()
    .frameOptions().deny()
    .and()
    .xssProtection().block(true);
```

---

## 📊 Step 5: Monitoring & Logging

### 5.1 Render Metrics
- Go to your service dashboard
- View logs, metrics, and health checks
- Set up email alerts for downtime

### 5.2 Netlify Analytics
- Go to your site dashboard
- Enable analytics (optional, paid feature)
- Monitor page views and bandwidth

### 5.3 MongoDB Atlas Monitoring
- Go to cluster dashboard
- View metrics: connections, operations, storage
- Set up alerts for high usage

---

## 🐛 Step 6: Troubleshooting

### Backend Issues

**Issue**: Backend not starting
```bash
# Check logs on Render
# Look for common issues:
- MongoDB connection string format
- Missing environment variables
- Java version mismatch
```

**Issue**: CORS errors
```bash
# Verify FRONTEND_URL environment variable
# Check SecurityConfig.java CORS configuration
```

**Issue**: Out of memory
```bash
# Increase memory in Render settings
# Or optimize Java heap size:
JAVA_OPTS=-Xmx512m -Xms256m
```

### Frontend Issues

**Issue**: API calls failing
```bash
# Check REACT_APP_API_URL is correct
# Verify backend is running
# Check browser console for errors
```

**Issue**: Build failing
```bash
# Check Node version compatibility
# Clear cache and rebuild:
rm -rf node_modules package-lock.json
npm install
npm run build
```

---

## 📈 Step 7: Performance Optimization

### 7.1 Backend Optimization
- Enable connection pooling in MongoDB
- Implement caching for frequently accessed data
- Compress responses with GZIP

### 7.2 Frontend Optimization
- Enable code splitting in React
- Lazy load routes and components
- Optimize images and assets
- Enable CDN on Netlify

### 7.3 Database Optimization
- Create indexes on frequently queried fields:
```javascript
// In MongoDB Atlas
db.users.createIndex({ email: 1 });
db.chat_history.createIndex({ userId: 1, timestamp: -1 });
db.coding_problems.createIndex({ difficulty: 1, category: 1 });
```

---

## 💰 Step 8: Cost Management

### Free Tier Limits

**Render Free Plan:**
- 750 hours/month
- Spins down after 15 min inactivity
- 512 MB RAM

**Netlify Free Plan:**
- 100 GB bandwidth/month
- 300 build minutes/month
- Unlimited sites

**MongoDB Atlas Free Plan:**
- 512 MB storage
- Shared RAM
- Shared vCPU

### Upgrade Path
When you outgrow free tier:
- **Render**: $7/month for 24/7 uptime
- **Netlify**: $19/month for Pro features
- **MongoDB Atlas**: $9/month for dedicated cluster

---

## ✅ Step 9: Post-Deployment Testing

### 9.1 Functional Testing
Test all features:
- [ ] User registration and login
- [ ] AI Chatbot conversations
- [ ] Mock interview flow
- [ ] Resume upload and analysis
- [ ] Daily roadmap generation
- [ ] Coding problem submission
- [ ] Admin panel functionality

### 9.2 Performance Testing
- [ ] Test response times
- [ ] Check mobile responsiveness
- [ ] Verify SSL certificates
- [ ] Test with multiple concurrent users

### 9.3 Security Testing
- [ ] Try SQL injection (should fail)
- [ ] Test XSS attacks (should be blocked)
- [ ] Verify JWT expiration
- [ ] Check CORS configuration

---

## 🔄 Step 10: Continuous Deployment

### 10.1 Auto-Deploy on Push
Both Render and Netlify automatically deploy when you push to GitHub.

### 10.2 Deployment Workflow
```bash
# Make changes
git add .
git commit -m "Add new feature"
git push origin main

# Automatic deployment triggers
# Backend: Render rebuilds and deploys
# Frontend: Netlify rebuilds and deploys
```

### 10.3 Rollback Strategy
If deployment fails:
1. Go to Render/Netlify dashboard
2. Click on previous deployment
3. Click "Redeploy"

---

## 📱 Step 11: Mobile Testing

Test on different devices:
- iOS Safari
- Android Chrome
- Tablet browsers

Responsive breakpoints to test:
- Mobile: 320px - 767px
- Tablet: 768px - 1023px
- Desktop: 1024px+

---

## 🎯 Step 12: Production Checklist

Before announcing your app:
- [ ] All features working
- [ ] SSL certificates active
- [ ] Environment variables secured
- [ ] Error handling implemented
- [ ] Logging configured
- [ ] Backup strategy in place
- [ ] User documentation created
- [ ] Terms of service and privacy policy
- [ ] Contact/support page
- [ ] Analytics enabled

---

## 📞 Support Resources

**Render Support:**
- Documentation: https://render.com/docs
- Community: https://community.render.com

**Netlify Support:**
- Documentation: https://docs.netlify.com
- Community: https://answers.netlify.com

**MongoDB Atlas Support:**
- Documentation: https://docs.atlas.mongodb.com
- Community: https://community.mongodb.com

---

## 🎉 Congratulations!

Your PlacementGuru AI application is now live! 🚀

**Your URLs:**
- Backend API: `https://placementguru-backend.onrender.com`
- Frontend App: `https://your-site-name.netlify.app`

Share your application with users and keep improving based on their feedback!

---

**Need Help?**
Create an issue in the GitHub repository or check the troubleshooting section.
