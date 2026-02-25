# PlacementGuruAI - API Endpoint Test Script
$baseUrl = "http://localhost:8080"
$results = @()

function Test-Endpoint {
    param(
        [string]$Name,
        [string]$Method,
        [string]$Endpoint,
        [hashtable]$Headers = @{},
        [object]$Body = $null
    )
    
    Write-Host "`n========================================" -ForegroundColor Cyan
    Write-Host "TEST: $Name" -ForegroundColor Yellow
    Write-Host "Method: $Method | Endpoint: $Endpoint" -ForegroundColor Gray
    
    try {
        $params = @{
            Uri = "$baseUrl$Endpoint"
            Method = $Method
            Headers = $Headers
            ErrorAction = "Stop"
        }
        
        if ($Body) {
            $params['Body'] = $Body | ConvertTo-Json -Depth 10
            $params['ContentType'] = "application/json"
        }
        
        $response = Invoke-RestMethod @params
        Write-Host "✅ Status: SUCCESS" -ForegroundColor Green
        Write-Host "Response:" -ForegroundColor Cyan
        $response | ConvertTo-Json -Depth 5
        
        return @{
            Name = $Name
            Status = "SUCCESS"
            Response = $response
        }
    } catch {
        Write-Host "❌ Status: FAILED" -ForegroundColor Red
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
        if ($_.Exception.Response) {
            Write-Host "HTTP Status: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
        }
        return @{
            Name = $Name
            Status = "FAILED"
            Error = $_.Exception.Message
        }
    }
}

# ==========================================
# PUBLIC ENDPOINTS (No Authentication)
# ==========================================
Write-Host "`n" -ForegroundColor White
Write-Host "██████████████████████████████████████████████████" -ForegroundColor Magenta
Write-Host "PUBLIC ENDPOINTS (No Authentication Required)" -ForegroundColor Magenta
Write-Host "██████████████████████████████████████████████████" -ForegroundColor Magenta

$results += Test-Endpoint -Name "Health Check" -Method Get -Endpoint "/api/auth/test"

# Test Signup
$signupBody = @{
    email = "testuser@gmail.com"
    password = "Test@123456"
    name = "Test User"
}
$results += Test-Endpoint -Name "User Signup" -Method Post -Endpoint "/api/auth/signup" -Body $signupBody

# ==========================================
# Test Login & Get Token
# ==========================================
Write-Host "`n" -ForegroundColor White
Write-Host "██████████████████████████████████████████████████" -ForegroundColor Magenta
Write-Host "AUTHENTICATION FLOW" -ForegroundColor Magenta
Write-Host "██████████████████████████████████████████████████" -ForegroundColor Magenta

$loginBody = @{
    email = "testuser@gmail.com"
    password = "Test@123456"
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "TEST: User Login" -ForegroundColor Yellow
Write-Host "Method: POST | Endpoint: /api/auth/login" -ForegroundColor Gray

try {
    $loginResponse = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method Post -Body ($loginBody | ConvertTo-Json) -ContentType "application/json"
    Write-Host "✅ Status: SUCCESS" -ForegroundColor Green
    Write-Host "Response:" -ForegroundColor Cyan
    $loginResponse | ConvertTo-Json -Depth 5
    
    $token = $loginResponse.data.token
    if ($token) {
        Write-Host "`n✅ Token obtained successfully" -ForegroundColor Green
        $authHeaders = @{Authorization = "Bearer $token"}
    } else {
        Write-Host "`n⚠️ No token in response" -ForegroundColor Yellow
        $authHeaders = @{}
    }
} catch {
    Write-Host "❌ Status: FAILED" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        Write-Host "HTTP Status: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    }
    $authHeaders = @{}
}

# ==========================================
# CODING ENDPOINTS
# ==========================================
Write-Host "`n" -ForegroundColor White
Write-Host "██████████████████████████████████████████████████" -ForegroundColor Magenta
Write-Host "CODING ENDPOINTS" -ForegroundColor Magenta
Write-Host "██████████████████████████████████████████████████" -ForegroundColor Magenta

$results += Test-Endpoint -Name "Get All Coding Problems" -Method Get -Endpoint "/api/coding/problems"

# ==========================================
# CHATBOT ENDPOINTS (Requires Auth)
# ==========================================
Write-Host "`n" -ForegroundColor White
Write-Host "██████████████████████████████████████████████████" -ForegroundColor Magenta
Write-Host "CHATBOT ENDPOINTS (Requires Authentication)" -ForegroundColor Magenta
Write-Host "██████████████████████████████████████████████████" -ForegroundColor Magenta

if ($authHeaders.Authorization) {
    $chatBody = @{
        message = "What are the tips for facing interviews?"
    }
    $results += Test-Endpoint -Name "Send Chat Message" -Method Post -Endpoint "/api/chatbot/message" -Headers $authHeaders -Body $chatBody
    $results += Test-Endpoint -Name "Get Chat History" -Method Get -Endpoint "/api/chatbot/history" -Headers $authHeaders
} else {
    Write-Host "⚠️ Skipping authenticated endpoints - No valid token" -ForegroundColor Yellow
}

# ==========================================
# ROADMAP ENDPOINTS (Requires Auth)
# ==========================================
Write-Host "`n" -ForegroundColor White
Write-Host "██████████████████████████████████████████████████" -ForegroundColor Magenta
Write-Host "ROADMAP ENDPOINTS (Requires Authentication)" -ForegroundColor Magenta
Write-Host "██████████████████████████████████████████████████" -ForegroundColor Magenta

if ($authHeaders.Authorization) {
    $results += Test-Endpoint -Name "Get Today's Plan" -Method Get -Endpoint "/api/roadmap/today" -Headers $authHeaders
    $results += Test-Endpoint -Name "Generate Daily Plan" -Method Get -Endpoint "/api/roadmap/generate" -Headers $authHeaders
    $results += Test-Endpoint -Name "Get Plan History" -Method Get -Endpoint "/api/roadmap/history" -Headers $authHeaders
} else {
    Write-Host "⚠️ Skipping authenticated endpoints - No valid token" -ForegroundColor Yellow
}

# ==========================================
# INTERVIEW ENDPOINTS (Requires Auth)
# ==========================================
Write-Host "`n" -ForegroundColor White
Write-Host "██████████████████████████████████████████████████" -ForegroundColor Magenta
Write-Host "INTERVIEW ENDPOINTS (Requires Authentication)" -ForegroundColor Magenta
Write-Host "██████████████████████████████████████████████████" -ForegroundColor Magenta

if ($authHeaders.Authorization) {
    $interviewBody = @{
        category = "Technical"
    }
    $results += Test-Endpoint -Name "Start Interview" -Method Post -Endpoint "/api/interview/start" -Headers $authHeaders -Body $interviewBody
    $results += Test-Endpoint -Name "Get Interview History" -Method Get -Endpoint "/api/interview/history" -Headers $authHeaders
} else {
    Write-Host "⚠️ Skipping authenticated endpoints - No valid token" -ForegroundColor Yellow
}

# ==========================================
# RESUME ENDPOINTS (Requires Auth)
# ==========================================
Write-Host "`n" -ForegroundColor White
Write-Host "██████████████████████████████████████████████████" -ForegroundColor Magenta
Write-Host "RESUME ENDPOINTS (Requires Authentication)" -ForegroundColor Magenta
Write-Host "██████████████████████████████████████████████████" -ForegroundColor Magenta

if ($authHeaders.Authorization) {
    $results += Test-Endpoint -Name "Get Resume History" -Method Get -Endpoint "/api/resume/history" -Headers $authHeaders
} else {
    Write-Host "⚠️ Skipping authenticated endpoints - No valid token" -ForegroundColor Yellow
}

# ==========================================
# SUMMARY
# ==========================================
Write-Host "`n" -ForegroundColor White
Write-Host "██████████████████████████████████████████████████" -ForegroundColor Magenta
Write-Host "TEST SUMMARY" -ForegroundColor Magenta
Write-Host "██████████████████████████████████████████████████" -ForegroundColor Magenta

$successCount = ($results | Where-Object {$_.Status -eq "SUCCESS"}).Count
$failCount = ($results | Where-Object {$_.Status -eq "FAILED"}).Count

Write-Host "`nTotal Tests: $($results.Count)"
Write-Host "✅ Successful: $successCount" -ForegroundColor Green
Write-Host "❌ Failed: $failCount" -ForegroundColor Red

Write-Host "`nDetailed Results:" -ForegroundColor Cyan
$results | Format-Table -Property Name, Status -AutoSize
