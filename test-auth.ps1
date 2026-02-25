$baseUrl = "http://localhost:8080"
$passCount = 0
$failCount = 0
$timestamp = Get-Random

Write-Host "`nPlacementGuruAI - COMPLETE API TEST" -ForegroundColor Green

# STEP 1: SIGNUP
Write-Host "`n1. User Registration" -ForegroundColor Cyan
$email = "testuser$timestamp@example.com"
$password = "TestPassword@123"

$signupBody = @{
    name = "Test User $timestamp"
    email = $email
    password = $password
    skillLevel = "INTERMEDIATE"
} | ConvertTo-Json

try {
    Invoke-RestMethod -Uri "$baseUrl/api/auth/signup" -Method Post `
        -Body $signupBody -ContentType "application/json" -ErrorAction Stop | Out-Null
    Write-Host "   PASS - Signup successful" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "   FAIL - Signup: $($_.Exception.Message)" -ForegroundColor Red
    $failCount++
    exit
}

# STEP 2: LOGIN
Write-Host "`n2. User Login (Get Token)" -ForegroundColor Cyan

$loginBody = @{
    email = $email
    password = $password
} | ConvertTo-Json

try {
    $loginRes = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method Post `
        -Body $loginBody -ContentType "application/json" -ErrorAction Stop
    $token = $loginRes.data.token
    Write-Host "   PASS - Login successful, token received" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "   FAIL - Login: $($_.Exception.Message)" -ForegroundColor Red
    $failCount++
    exit
}

# STEP 3: TEST PROTECTED ENDPOINTS
$headers = @{ Authorization = "Bearer $token" }

Write-Host "`n3. Testing Protected Endpoints (with JWT)" -ForegroundColor Cyan

# Test public endpoint
Write-Host "`n   Public Endpoints:" -ForegroundColor Yellow
try {
    Invoke-RestMethod -Uri "$baseUrl/api/coding/problems" -Method Get -ErrorAction Stop | Out-Null
    Write-Host "   PASS - GET /api/coding/problems" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "   FAIL - GET /api/coding/problems" -ForegroundColor Red
    $failCount++
}

# Test protected endpoints
Write-Host "`n   Protected Endpoints:" -ForegroundColor Yellow

try {
    $body = @{ message = "Tips?" } | ConvertTo-Json
    Invoke-RestMethod -Uri "$baseUrl/api/chatbot/message" -Method Post `
        -Body $body -ContentType "application/json" -Headers $headers -ErrorAction Stop | Out-Null
    Write-Host "   PASS - POST /api/chatbot/message" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "   FAIL - POST /api/chatbot/message" -ForegroundColor Red
    $failCount++
}

try {
    Invoke-RestMethod -Uri "$baseUrl/api/chatbot/history" -Method Get `
        -Headers $headers -ErrorAction Stop | Out-Null
    Write-Host "   PASS - GET /api/chatbot/history" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "   FAIL - GET /api/chatbot/history" -ForegroundColor Red
    $failCount++
}

try {
    $body = @{ category = "Technical" } | ConvertTo-Json
    Invoke-RestMethod -Uri "$baseUrl/api/interview/start" -Method Post `
        -Body $body -ContentType "application/json" -Headers $headers -ErrorAction Stop | Out-Null
    Write-Host "   PASS - POST /api/interview/start" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "   FAIL - POST /api/interview/start" -ForegroundColor Red
    $failCount++
}

try {
    Invoke-RestMethod -Uri "$baseUrl/api/interview/history" -Method Get `
        -Headers $headers -ErrorAction Stop | Out-Null
    Write-Host "   PASS - GET /api/interview/history" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "   FAIL - GET /api/interview/history" -ForegroundColor Red
    $failCount++
}

try {
    Invoke-RestMethod -Uri "$baseUrl/api/resume/history" -Method Get `
        -Headers $headers -ErrorAction Stop | Out-Null
    Write-Host "   PASS - GET /api/resume/history" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "   FAIL - GET /api/resume/history" -ForegroundColor Red
    $failCount++
}

try {
    Invoke-RestMethod -Uri "$baseUrl/api/roadmap/today" -Method Get `
        -Headers $headers -ErrorAction Stop | Out-Null
    Write-Host "   PASS - GET /api/roadmap/today" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "   FAIL - GET /api/roadmap/today" -ForegroundColor Red
    $failCount++
}

try {
    Invoke-RestMethod -Uri "$baseUrl/api/roadmap/generate" -Method Get `
        -Headers $headers -ErrorAction Stop | Out-Null
    Write-Host "   PASS - GET /api/roadmap/generate" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "   FAIL - GET /api/roadmap/generate" -ForegroundColor Red
    $failCount++
}

try {
    Invoke-RestMethod -Uri "$baseUrl/api/roadmap/history" -Method Get `
        -Headers $headers -ErrorAction Stop | Out-Null
    Write-Host "   PASS - GET /api/roadmap/history" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "   FAIL - GET /api/roadmap/history" -ForegroundColor Red
    $failCount++
}

# SUMMARY
Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "TEST SUMMARY" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
$total = $passCount + $failCount
Write-Host "Total Tests: $total"
Write-Host "Passed: $passCount" -ForegroundColor Green
Write-Host "Failed: $failCount" -ForegroundColor Red
$rate = [math]::Round(($passCount/$total)*100, 1)
Write-Host "Pass Rate: $rate%" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

if ($failCount -eq 0) {
    Write-Host "`nSUCCESS! All endpoints working!" -ForegroundColor Green
} else {
    Write-Host "`nSome tests failed. Check MongoDB and JWT config." -ForegroundColor Yellow
}
