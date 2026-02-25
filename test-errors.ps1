# Detailed Error Investigation
$baseUrl = "http://localhost:8080"

Write-Host "`n=== DETAILED ERROR ANALYSIS ===" -ForegroundColor Yellow

# Test Chatbot with error details
Write-Host "`n1. Testing Chatbot Message" -ForegroundColor Cyan
try {
    $body = @{
        message = "What are placement strategies?"
    } | ConvertTo-Json
    
    $response = Invoke-WebRequest -Uri "$baseUrl/api/chatbot/message" `
        -Method Post `
        -Body $body `
        -ContentType "application/json" `
        -ErrorAction Stop
    
    Write-Host "Response: $($response.StatusCode)"
    $response.Content | ConvertFrom-Json | ConvertTo-Json
} catch {
    Write-Host "Error Status: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $stream = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($stream)
        $errorBody = $reader.ReadToEnd()
        Write-Host "Error Response:"
        Write-Host $errorBody -ForegroundColor Red
    }
}

# Test Interview with error details
Write-Host "`n2. Testing Interview Start" -ForegroundColor Cyan
try {
    $body = @{
        category = "Technical"
    } | ConvertTo-Json
    
    $response = Invoke-WebRequest -Uri "$baseUrl/api/interview/start" `
        -Method Post `
        -Body $body `
        -ContentType "application/json" `
        -ErrorAction Stop
    
    Write-Host "Response: $($response.StatusCode)"
} catch {
    Write-Host "Error Status: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $stream = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($stream)
        $errorBody = $reader.ReadToEnd()
        Write-Host "Error Response:"
        Write-Host $errorBody -ForegroundColor Red
    }
}

# Test Roadmap with error details
Write-Host "`n3. Testing Roadmap Today" -ForegroundColor Cyan
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/api/roadmap/today" `
        -Method Get `
        -ErrorAction Stop
    
    Write-Host "Response: $($response.StatusCode)"
} catch {
    Write-Host "Error Status: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $stream = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($stream)
        $errorBody = $reader.ReadToEnd()
        Write-Host "Error Response:"
        Write-Host $errorBody -ForegroundColor Red
    }
}
