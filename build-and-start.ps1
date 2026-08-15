$ErrorActionPreference = "Stop"

Write-Host "=== Maven build ===" -ForegroundColor Cyan

mvn clean package

if ($LASTEXITCODE -ne 0) {
    Write-Host "Maven build failed!" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "=== Maven build completed successfully ===" -ForegroundColor Green


Write-Host "=== Docker images build ===" -ForegroundColor Cyan

docker compose -f .\docker\docker-compose.yml build

if ($LASTEXITCODE -ne 0) {
    Write-Host "Docker images build failed!" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "=== Docker images build completed successfully ===" -ForegroundColor Green


Write-Host "=== Starting containers ===" -ForegroundColor Cyan

docker compose -f .\docker\docker-compose.yml up -d

if ($LASTEXITCODE -ne 0) {
    Write-Host "Docker Compose failed!" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "=== Containers started successfully ===" -ForegroundColor Green


Write-Host "=== Container status ===" -ForegroundColor Cyan

docker compose -f .\docker\docker-compose.yml ps

Read-Host "Press Enter to exit"