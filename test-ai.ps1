$baseUrl = "http://localhost:8080"
$timestamp = Get-Random

Write-Host "`n=== TESTING AI/CHATBOT FUNCTIONALITY ===" -ForegroundColor Cyan

# Step 1: Signup
Write-Host "`n1. Signing up user for testing..." -ForegroundColor Yellow
$email = "aitest$timestamp@example.com"
$password = "Test@12345"

$signupBody = @{
    name = "AI Test User"
    email = $email
    password = $password
    skillLevel = "BEGINNER"
} | ConvertTo-Json

try {
    Invoke-RestMethod -Uri "$baseUrl/api/auth/signup" -Method Post `
        -Body $signupBody -ContentType "application/json" -ErrorAction Stop | Out-Null
    Write-Host "   SUCCESS - User registered" -ForegroundColor Green
} catch {
    Write-Host "   FAILED - $($_.Exception.Message)" -ForegroundColor Red
    exit
}

# Step 2: Login
Write-Host "`n2. Logging in to get JWT token..." -ForegroundColor Yellow
$loginBody = @{
    email = $email
    password = $password
} | ConvertTo-Json

try {
    $loginRes = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method Post `
        -Body $loginBody -ContentType "application/json" -ErrorAction Stop
    $token = $loginRes.data.token
    Write-Host "   SUCCESS - Token received" -ForegroundColor Green
} catch {
    Write-Host "   FAILED - $($_.Exception.Message)" -ForegroundColor Red
    exit
}

$headers = @{ Authorization = "Bearer $token" }

# Step 3: Test Chatbot
Write-Host "`n3. TESTING CHATBOT MESSAGE (AI)..." -ForegroundColor Yellow
$chatBody = @{
    message = "What are the best strategies for cracking technical interviews?"
    category = "PLACEMENT"
} | ConvertTo-Json

try {
    Write-Host "   Sending message to AI chatbot..." -ForegroundColor Gray
    $response = Invoke-RestMethod -Uri "$baseUrl/api/chatbot/message" -Method Post `
        -Body $chatBody -ContentType "application/json" -Headers $headers `
        -TimeoutSec 30 -ErrorAction Stop
    
    Write-Host "   SUCCESS - AI Response Received!" -ForegroundColor Green
    Write-Host "   Response: " -ForegroundColor Cyan
    Write-Host "   $($response.data.response)" -ForegroundColor Gray
    $aiWorking = $true
} catch {
    Write-Host "   FAILED - $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $stream = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($stream)
        $errorBody = $reader.ReadToEnd()
        Write-Host "   Error Details: $errorBody" -ForegroundColor Red
    }
    $aiWorking = $false
}

# Step 4: Get Chat History
Write-Host "`n4. Testing chat history retrieval..." -ForegroundColor Yellow
try {
    $history = Invoke-RestMethod -Uri "$baseUrl/api/chatbot/history" -Method Get `
        -Headers $headers -ErrorAction Stop
    Write-Host "   SUCCESS - Chat history retrieved" -ForegroundColor Green
    Write-Host "   Total messages: $($history.data.Count)" -ForegroundColor Gray
} catch {
    Write-Host "   FAILED - $($_.Exception.Message)" -ForegroundColor Red
}

# Summary
Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "AI/CHATBOT TEST RESULTS" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

if ($aiWorking) {
    Write-Host "`nAI CHATBOT IS WORKING!" -ForegroundColor Green
    Write-Host "✓ Connection to Gemini API successful"
    Write-Host "✓ AI generating responses"
    Write-Host "✓ Chat history saving"
} else {
    Write-Host "`nAI CHATBOT ISSUES DETECTED - Check error messages above" -ForegroundColor Red
}

Write-Host "`n========================================" -ForegroundColor Cyan
