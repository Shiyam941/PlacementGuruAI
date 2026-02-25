# Comprehensive API Endpoint Test - All Endpoints PUBLIC

$baseUrl = "http://localhost:8080"
$results = @()
$testCount = 0
$passCount = 0
$failCount = 0

function Test-Endpoint {
    param(
        [string]$Name,
        [string]$Method,
        [string]$Endpoint,
        [hashtable]$Headers = @{},
        [object]$Body = $null,
        [string]$Category = ""
    )
    
    $script:testCount++
    Write-Host "`n[TEST $testCount] $Name" -ForegroundColor Cyan
    Write-Host "  $Method $Endpoint" -ForegroundColor Gray
    
    try {
        $params = @{
            Uri = "$baseUrl$Endpoint"
            Method = $Method
            ErrorAction = "Stop"
        }
        
        if ($Headers.Count -gt 0) {
            $params['Headers'] = $Headers
        }
        
        if ($Body) {
            $params['Body'] = $Body | ConvertTo-Json -Depth 10
            $params['ContentType'] = "application/json"
        }
        
        $response = Invoke-RestMethod @params
        Write-Host "  ✅ SUCCESS" -ForegroundColor Green
        
        $script:passCount++
        return @{
            Name = $Name
            Endpoint = $Endpoint
            Status = "✅ PASS"
            Category = $Category
        }
    } catch {
        Write-Host "  ❌ FAILED: $($_.Exception.Message)" -ForegroundColor Red
        $script:failCount++
        return @{
            Name = $Name
            Endpoint = $Endpoint
            Status = "❌ FAIL"
            Error = $_.Exception.Message
            Category = $Category
        }
    }
}

Write-Host "`n$([char]27)[1;95m" + ("="*70) + "$([char]27)[0m"
Write-Host "PlacementGuruAI - COMPLETE API ENDPOINT TEST" -ForegroundColor Magenta
Write-Host "All Endpoints PUBLIC (No Authentication Required)" -ForegroundColor Yellow
Write-Host "$([char]27)[1;95m" + ("="*70) + "$([char]27)[0m"

# ==========================================
# AUTHENTICATION ENDPOINTS (/api/auth)
# ==========================================
Write-Host "`n$([char]27)[1;36mAUTHENTICATION ENDPOINTS$([char]27)[0m" -ForegroundColor Cyan
Write-Host "────────────────────────" -ForegroundColor Gray

$results += Test-Endpoint -Category "Auth" -Name "Health Check" -Method Get -Endpoint "/api/auth/test"

# Test Signup
$signupBody = @{
    name = "Test User $(Get-Random)"
    email = "testuser$(Get-Random)@example.com"
    password = "Test@12345"
    skillLevel = "BEGINNER"
}
$results += Test-Endpoint -Category "Auth" -Name "User Signup" -Method Post -Endpoint "/api/auth/signup" -Body $signupBody

# Test Login (use first test user)
$loginBody = @{
    email = "testuser@example.com"
    password = "Test@12345"
}
$results += Test-Endpoint -Category "Auth" -Name "User Login" -Method Post -Endpoint "/api/auth/login" -Body $loginBody

# ==========================================
# CODING ENDPOINTS (/api/coding)
# ==========================================
Write-Host "`n$([char]27)[1;36mCODING ENDPOINTS$([char]27)[0m" -ForegroundColor Cyan
Write-Host "────────────────────" -ForegroundColor Gray

$results += Test-Endpoint -Category "Coding" -Name "Get All Problems" -Method Get -Endpoint "/api/coding/problems"
$results += Test-Endpoint -Category "Coding" -Name "Get Problems by Difficulty (EASY)" -Method Get -Endpoint "/api/coding/problems/difficulty/EASY"
$results += Test-Endpoint -Category "Coding" -Name "Get Problems by Difficulty (MEDIUM)" -Method Get -Endpoint "/api/coding/problems/difficulty/MEDIUM"
$results += Test-Endpoint -Category "Coding" -Name "Get Problems by Difficulty (HARD)" -Method Get -Endpoint "/api/coding/problems/difficulty/HARD"

# ==========================================
# CHATBOT ENDPOINTS (/api/chatbot)
# ==========================================
Write-Host "`n$([char]27)[1;36mCHATBOT ENDPOINTS$([char]27)[0m" -ForegroundColor Cyan
Write-Host "──────────────────" -ForegroundColor Gray

$chatBody = @{
    message = "What are placement tips?"
}
$results += Test-Endpoint -Category "Chatbot" -Name "Send Message" -Method Post -Endpoint "/api/chatbot/message" -Body $chatBody
$results += Test-Endpoint -Category "Chatbot" -Name "Get Chat History" -Method Get -Endpoint "/api/chatbot/history"

# ==========================================
# INTERVIEW ENDPOINTS (/api/interview)
# ==========================================
Write-Host "`n$([char]27)[1;36mINTERVIEW ENDPOINTS$([char]27)[0m" -ForegroundColor Cyan
Write-Host "──────────────────────" -ForegroundColor Gray

$interviewBody = @{
    category = "Technical"
}
$results += Test-Endpoint -Category "Interview" -Name "Start Interview" -Method Post -Endpoint "/api/interview/start" -Body $interviewBody
$results += Test-Endpoint -Category "Interview" -Name "Get Interview History" -Method Get -Endpoint "/api/interview/history"

# ==========================================
# RESUME ENDPOINTS (/api/resume)
# ==========================================
Write-Host "`n$([char]27)[1;36mRESUME ENDPOINTS$([char]27)[0m" -ForegroundColor Cyan
Write-Host "─────────────────" -ForegroundColor Gray

$results += Test-Endpoint -Category "Resume" -Name "Get Resume History" -Method Get -Endpoint "/api/resume/history"

# ==========================================
# ROADMAP ENDPOINTS (/api/roadmap)
# ==========================================
Write-Host "`n$([char]27)[1;36mROADMAP ENDPOINTS$([char]27)[0m" -ForegroundColor Cyan
Write-Host "──────────────────" -ForegroundColor Gray

$results += Test-Endpoint -Category "Roadmap" -Name "Get Today's Plan" -Method Get -Endpoint "/api/roadmap/today"
$results += Test-Endpoint -Category "Roadmap" -Name "Generate Daily Plan" -Method Get -Endpoint "/api/roadmap/generate"
$results += Test-Endpoint -Category "Roadmap" -Name "Get Plan History" -Method Get -Endpoint "/api/roadmap/history"

# ==========================================
# TEST SUMMARY
# ==========================================
Write-Host "`n$([char]27)[1;95m" + ("="*70) + "$([char]27)[0m"
Write-Host "TEST SUMMARY" -ForegroundColor Magenta
Write-Host "$([char]27)[1;95m" + ("="*70) + "$([char]27)[0m"

Write-Host "`nTotal Tests: $testCount"
Write-Host "✅ Passed: $passCount" -ForegroundColor Green
Write-Host "❌ Failed: $failCount" -ForegroundColor Red
Write-Host "Pass Rate: $([math]::Round(($passCount/$testCount)*100, 2))%" -ForegroundColor Cyan

# ==========================================
# DETAILED RESULTS BY CATEGORY
# ==========================================
Write-Host "`n$([char]27)[1;36mDETAILED RESULTS$([char]27)[0m"
Write-Host "───────────────" -ForegroundColor Gray

foreach ($category in ($results.Category | Select-Object -Unique)) {
    Write-Host "`n  $category Endpoints:" -ForegroundColor Yellow
    $categoryResults = $results | Where-Object {$_.Category -eq $category}
    foreach ($result in $categoryResults) {
        Write-Host "    $($result.Status) - $($result.Name)" -ForegroundColor $(if ($result.Status -contains "✅") {"Green"} else {"Red"})
    }
}

# ==========================================
# OVERALL STATUS
# ==========================================
Write-Host "`n$([char]27)[1;95m" + ("="*70) + "$([char]27)[0m"
if ($failCount -eq 0) {
    Write-Host "🎉 ALL TESTS PASSED! API IS FULLY FUNCTIONAL!" -ForegroundColor Green
} elseif ($passCount -gt ($testCount * 0.8)) {
    Write-Host "⚠️ MOST TESTS PASSED - Minor issues detected" -ForegroundColor Yellow
} else {
    Write-Host "❌ SIGNIFICANT FAILURES - Please review errors" -ForegroundColor Red
}
Write-Host "$([char]27)[1;95m" + ("="*70) + "$([char]27)[0m`n"
