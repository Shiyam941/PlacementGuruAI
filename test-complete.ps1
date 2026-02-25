# Complete API Test with Authentication

$baseUrl = "http://localhost:8080"
$passCount = 0
$failCount = 0
$timestamp = Get-Random

Write-Host "`n========================================" -ForegroundColor Green
Write-Host "PlacementGuruAI - COMPLETE API TEST" -ForegroundColor Green
Write-Host "With JWT Authentication" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Green

# ========== STEP 1: SIGNUP ==========
Write-Host "`nSTEP 1: User Registration" -ForegroundColor Cyan
$email = "testuser$timestamp@example.com"
$password = "TestPassword@123"

try {
    $signupBody = @{
        name = "Test User $timestamp"
        email = $email
        password = $password
        skillLevel = "INTERMEDIATE"
    } | ConvertTo-Json
    
    $signupResponse = Invoke-RestMethod -Uri "$baseUrl/api/auth/signup" `
        -Method Post `
        -Body $signupBody `
        -ContentType "application/json" `
        -ErrorAction Stop
    
    Write-Host "✓ Signup successful"
    Write-Host "  Email: $email" -ForegroundColor Gray
    $passCount++
} catch {
    Write-Host "✗ Signup failed: $($_.Exception.Message)" -ForegroundColor Red
    $failCount++
    exit
}

# ========== STEP 2: LOGIN ==========
Write-Host "`nSTEP 2: User Login (Get JWT Token)" -ForegroundColor Cyan

try {
    $loginBody = @{
        email = $email
        password = $password
    } | ConvertTo-Json
    
    $loginResponse = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" `
        -Method Post `
        -Body $loginBody `
        -ContentType "application/json" `
        -ErrorAction Stop
    
    $token = $loginResponse.data.token
    if ($token) {
        Write-Host "✓ Login successful" -ForegroundColor Green
        $shortToken = $token.Substring(0, 20) + "..."
        Write-Host "  Token received: $shortToken" -ForegroundColor Gray
        $passCount++
    } else {
        Write-Host "✗ No token in response" -ForegroundColor Red
        $failCount++
        exit
    }
} catch {
    Write-Host "✗ Login failed: $($_.Exception.Message)" -ForegroundColor Red
    $failCount++
    exit
}

# ========== STEP 3: TEST PROTECTED ENDPOINTS ==========
$headers = @{ Authorization = "Bearer $token" }

Write-Host "`nSTEP 3: Testing Protected Endpoints" -ForegroundColor Cyan
Write-Host "──────────────────────────────────" -ForegroundColor Gray

# Public endpoints (no auth needed)
Write-Host "`n[PUBLIC] Coding Endpoints:" -ForegroundColor Yellow

try {
    Invoke-RestMethod -Uri "$baseUrl/api/coding/problems" -Method Get `
        -ErrorAction Stop | Out-Null
    Write-Host "  ✓ GET /api/coding/problems" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "  ✗ GET /api/coding/problems" -ForegroundColor Red
    $failCount++
}

# Protected endpoints (require auth)
Write-Host "`n[PROTECTED] Chatbot Endpoints:" -ForegroundColor Yellow

try {
    $chatBody = @{ message = "Tips for placement?" } | ConvertTo-Json
    Invoke-RestMethod -Uri "$baseUrl/api/chatbot/message" `
        -Method Post -Body $chatBody -ContentType "application/json" `
        -Headers $headers -ErrorAction Stop | Out-Null
    Write-Host "  ✓ POST /api/chatbot/message" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "  ✗ POST /api/chatbot/message: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    $failCount++
}

try {
    Invoke-RestMethod -Uri "$baseUrl/api/chatbot/history" -Method Get `
        -Headers $headers -ErrorAction Stop | Out-Null
    Write-Host "  ✓ GET /api/chatbot/history" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "  ✗ GET /api/chatbot/history: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    $failCount++
}

Write-Host "`n[PROTECTED] Interview Endpoints:" -ForegroundColor Yellow

try {
    $intBody = @{ category = "Technical" } | ConvertTo-Json
    Invoke-RestMethod -Uri "$baseUrl/api/interview/start" `
        -Method Post -Body $intBody -ContentType "application/json" `
        -Headers $headers -ErrorAction Stop | Out-Null
    Write-Host "  ✓ POST /api/interview/start" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "  ✗ POST /api/interview/start: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    $failCount++
}

try {
    Invoke-RestMethod -Uri "$baseUrl/api/interview/history" -Method Get `
        -Headers $headers -ErrorAction Stop | Out-Null
    Write-Host "  ✓ GET /api/interview/history" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "  ✗ GET /api/interview/history: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    $failCount++
}

Write-Host "`n[PROTECTED] Resume Endpoints:" -ForegroundColor Yellow

try {
    Invoke-RestMethod -Uri "$baseUrl/api/resume/history" -Method Get `
        -Headers $headers -ErrorAction Stop | Out-Null
    Write-Host "  ✓ GET /api/resume/history" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "  ✗ GET /api/resume/history: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    $failCount++
}

Write-Host "`n[PROTECTED] Roadmap Endpoints:" -ForegroundColor Yellow

try {
    Invoke-RestMethod -Uri "$baseUrl/api/roadmap/today" -Method Get `
        -Headers $headers -ErrorAction Stop | Out-Null
    Write-Host "  ✓ GET /api/roadmap/today" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "  ✗ GET /api/roadmap/today: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    $failCount++
}

try {
    Invoke-RestMethod -Uri "$baseUrl/api/roadmap/generate" -Method Get `
        -Headers $headers -ErrorAction Stop | Out-Null
    Write-Host "  ✓ GET /api/roadmap/generate" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "  ✗ GET /api/roadmap/generate: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    $failCount++
}

try {
    Invoke-RestMethod -Uri "$baseUrl/api/roadmap/history" -Method Get `
        -Headers $headers -ErrorAction Stop | Out-Null
    Write-Host "  ✓ GET /api/roadmap/history" -ForegroundColor Green
    $passCount++
} catch {
    Write-Host "  ✗ GET /api/roadmap/history: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    $failCount++
}

# ========== SUMMARY ==========
Write-Host "`n========================================" -ForegroundColor Green
Write-Host "TEST SUMMARY" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green

$total = $passCount + $failCount
$passRate = [math]::Round(($passCount / $total) * 100, 1)

Write-Host "`nTotal Tests: $total"
Write-Host "Passed: $passCount" -ForegroundColor Green
Write-Host "Failed: $failCount" -ForegroundColor $(if ($failCount -eq 0) {"Green"} else {"Red"})
Write-Host "Pass Rate: $passRate%"

Write-Host "`n========================================" -ForegroundColor Green
if ($failCount -eq 0) {
    Write-Host "SUCCESS! All APIs are working correctly!" -ForegroundColor Cyan
    Write-Host "✓ Authentication flow working" -ForegroundColor Green
    Write-Host "✓ JWT tokens validated" -ForegroundColor Green
    Write-Host "✓ All endpoints accessible" -ForegroundColor Green
} else {
    Write-Host "Some tests failed. Review errors above." -ForegroundColor Yellow
}
Write-Host "========================================" -ForegroundColor Green
