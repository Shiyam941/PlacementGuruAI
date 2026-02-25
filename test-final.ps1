# API Test Script
$baseUrl = "http://localhost:8080"
$passCount = 0
$failCount = 0

Write-Host "`n=== TESTING ALL ENDPOINTS ===" -ForegroundColor Cyan

# Test 1: Health Check
Write-Host "`n1. Health Check" -ForegroundColor Yellow
try {
    Invoke-RestMethod -Uri "$baseUrl/api/auth/test" -Method Get -ErrorAction Stop | Out-Null
    Write-Host "   PASS - GET /api/auth/test" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "   FAIL - $($_.Exception.Message)" -ForegroundColor Red
    $failCount++
}

# Test 2: Signup
Write-Host "`n2. Signup" -ForegroundColor Yellow
try {
    $body = @{
        name = "TestUser123"
        email = "testuser123@example.com"
        password = "Test@12345"
        skillLevel = "BEGINNER"
    } | ConvertTo-Json
    Invoke-RestMethod -Uri "$baseUrl/api/auth/signup" -Method Post -Body $body -ContentType "application/json" -ErrorAction Stop | Out-Null
    Write-Host "   PASS - POST /api/auth/signup" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "   FAIL - $($_.Exception.Message)" -ForegroundColor Red
    $failCount++
}

# Test 3: Get All Coding Problems
Write-Host "`n3. Coding - Get All Problems" -ForegroundColor Yellow
try {
    Invoke-RestMethod -Uri "$baseUrl/api/coding/problems" -Method Get -ErrorAction Stop | Out-Null
    Write-Host "   PASS - GET /api/coding/problems" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "   FAIL - $($_.Exception.Message)" -ForegroundColor Red
    $failCount++
}

# Test 4: Get Problems by Difficulty
Write-Host "`n4. Coding - Get by Difficulty" -ForegroundColor Yellow
try {
    Invoke-RestMethod -Uri "$baseUrl/api/coding/problems/difficulty/EASY" -Method Get -ErrorAction Stop | Out-Null
    Write-Host "   PASS - GET /api/coding/problems/difficulty/EASY" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "   FAIL - $($_.Exception.Message)" -ForegroundColor Red
    $failCount++
}

# Test 5: Send Chat Message
Write-Host "`n5. Chatbot - Send Message" -ForegroundColor Yellow
try {
    $body = @{
        message = "What are placement strategies?"
    } | ConvertTo-Json
    Invoke-RestMethod -Uri "$baseUrl/api/chatbot/message" -Method Post -Body $body -ContentType "application/json" -ErrorAction Stop | Out-Null
    Write-Host "   PASS - POST /api/chatbot/message" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "   FAIL - $($_.Exception.Message)" -ForegroundColor Red
    $failCount++
}

# Test 6: Get Chat History
Write-Host "`n6. Chatbot - Get History" -ForegroundColor Yellow
try {
    Invoke-RestMethod -Uri "$baseUrl/api/chatbot/history" -Method Get -ErrorAction Stop | Out-Null
    Write-Host "   PASS - GET /api/chatbot/history" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "   FAIL - $($_.Exception.Message)" -ForegroundColor Red
    $failCount++
}

# Test 7: Start Interview
Write-Host "`n7. Interview - Start" -ForegroundColor Yellow
try {
    $body = @{
        category = "Technical"
    } | ConvertTo-Json
    Invoke-RestMethod -Uri "$baseUrl/api/interview/start" -Method Post -Body $body -ContentType "application/json" -ErrorAction Stop | Out-Null
    Write-Host "   PASS - POST /api/interview/start" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "   FAIL - $($_.Exception.Message)" -ForegroundColor Red
    $failCount++
}

# Test 8: Get Interview History
Write-Host "`n8. Interview - Get History" -ForegroundColor Yellow
try {
    Invoke-RestMethod -Uri "$baseUrl/api/interview/history" -Method Get -ErrorAction Stop | Out-Null
    Write-Host "   PASS - GET /api/interview/history" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "   FAIL - $($_.Exception.Message)" -ForegroundColor Red
    $failCount++
}

# Test 9: Get Resume History
Write-Host "`n9. Resume - Get History" -ForegroundColor Yellow
try {
    Invoke-RestMethod -Uri "$baseUrl/api/resume/history" -Method Get -ErrorAction Stop | Out-Null
    Write-Host "   PASS - GET /api/resume/history" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "   FAIL - $($_.Exception.Message)" -ForegroundColor Red
    $failCount++
}

# Test 10: Get Today's Roadmap
Write-Host "`n10. Roadmap - Get Today" -ForegroundColor Yellow
try {
    Invoke-RestMethod -Uri "$baseUrl/api/roadmap/today" -Method Get -ErrorAction Stop | Out-Null
    Write-Host "   PASS - GET /api/roadmap/today" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "   FAIL - $($_.Exception.Message)" -ForegroundColor Red
    $failCount++
}

# Test 11: Generate Daily Plan
Write-Host "`n11. Roadmap - Generate Plan" -ForegroundColor Yellow
try {
    Invoke-RestMethod -Uri "$baseUrl/api/roadmap/generate" -Method Get -ErrorAction Stop | Out-Null
    Write-Host "   PASS - GET /api/roadmap/generate" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "   FAIL - $($_.Exception.Message)" -ForegroundColor Red
    $failCount++
}

# Test 12: Get Plan History
Write-Host "`n12. Roadmap - Get History" -ForegroundColor Yellow
try {
    Invoke-RestMethod -Uri "$baseUrl/api/roadmap/history" -Method Get -ErrorAction Stop | Out-Null
    Write-Host "   PASS - GET /api/roadmap/history" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "   FAIL - $($_.Exception.Message)" -ForegroundColor Red
    $failCount++
}

# Summary
Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "TEST SUMMARY" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Total Tests: 12"
Write-Host "Passed: $passCount" -ForegroundColor Green
Write-Host "Failed: $failCount" -ForegroundColor $(if ($failCount -eq 0) {"Green"} else {"Red"})
Write-Host "Pass Rate: $(($passCount/12)*100)%" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

if ($failCount -eq 0) {
    Write-Host "`nSUCCESS! All API endpoints are working!" -ForegroundColor Green
} else {
    Write-Host "`nWARNING: Some endpoints failed. Check MongoDB connection." -ForegroundColor Yellow
}
