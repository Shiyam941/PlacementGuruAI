# Test with detailed error responses

$baseUrl = "http://localhost:8080"

Write-Host "=== DETAILED ERROR INVESTIGATION ===" -ForegroundColor Cyan

# Test 1: Signup with error details
Write-Host "`n1. Testing Signup Endpoint:" -ForegroundColor Yellow
$signupBody = @{
    name = "Test User"
    email = "testuser@gmail.com"
    password = "Test@123456"
    skillLevel = "BEGINNER"
}

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/auth/signup" `
        -Method Post `
        -Body ($signupBody | ConvertTo-Json) `
        -ContentType "application/json" `
        -ErrorAction Stop
    Write-Host "Response: " -ForegroundColor Green
    $response | ConvertTo-Json -Depth 10
} catch {
    Write-Host "Error Details:" -ForegroundColor Red
    Write-Host "Status Code: $($_.Exception.Response.StatusCode)" 
    Write-Host "Status Description: $($_.Exception.Response.StatusDescription)"
    
    # Try to read the response body
    if ($_.Exception.Response) {
        $stream = $_.Exception.Response.GetResponseStream()
        $streamReader = New-Object System.IO.StreamReader($stream)
        $errorBody = $streamReader.ReadToEnd()
        Write-Host "Response Body: $errorBody" -ForegroundColor Red
    }
}

# Test 2: Check if we can access /api/coding/problems without auth
Write-Host "`n2. Testing Coding Endpoints (Public):" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/coding/problems" `
        -Method Get `
        -ErrorAction Stop
    Write-Host "Success! Response:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 5
} catch {
    Write-Host "Failed - Status: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $stream = $_.Exception.Response.GetResponseStream()
        $streamReader = New-Object System.IO.StreamReader($stream)
        $errorBody = $streamReader.ReadToEnd()
        Write-Host "Response Body: $errorBody" -ForegroundColor Red
    }
}

# Test 3: Check server logs for detailed info
Write-Host "`n3. Server Configuration Check:" -ForegroundColor Yellow
Write-Host "Server running on: $baseUrl" -ForegroundColor Cyan
Write-Host "JWT Filter enabled: Yes (from SecurityConfig)" -ForegroundColor Cyan
Write-Host "Public endpoints: /api/auth/** (all auth endpoints)" -ForegroundColor Cyan
Write-Host "Protected endpoints: All others require authentication" -ForegroundColor Cyan
