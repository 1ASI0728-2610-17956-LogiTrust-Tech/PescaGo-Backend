# Starts PescaGo backend locally with blockchain trace enabled.
# Requires a valid .env in PescaGo-Backend root (PostgreSQL, JWT_SECRET, etc.).
# Does not modify .env; reads it if present.

$ErrorActionPreference = "Stop"

$backendRoot = Split-Path (Split-Path $PSScriptRoot -Parent) -Parent
Set-Location $backendRoot

$envFile = Join-Path $backendRoot ".env"
if (Test-Path $envFile) {
    Write-Host "Loading environment from .env (values not printed)."
    Get-Content $envFile | ForEach-Object {
        $line = $_.Trim()
        if ($line -and -not $line.StartsWith("#") -and
            $line -match '^(?:export\s+)?([A-Za-z_][A-Za-z0-9_]*)\s*=\s*(.*)$') {
            $name = $matches[1]
            $value = $matches[2].Trim()
            if (($value.StartsWith('"') -and $value.EndsWith('"')) -or
                ($value.StartsWith("'") -and $value.EndsWith("'"))) {
                $value = $value.Substring(1, $value.Length - 2)
            }
            Set-Item -Path "Env:$name" -Value $value
        }
    }
} else {
    Write-Warning ".env not found. Set DATASOURCE_* and JWT_SECRET before running."
}

# Blockchain demo (non-production)
$env:BLOCKCHAIN_ENABLED = "true"
$env:BLOCKCHAIN_BASE_URL = "http://localhost:3001"
$env:BLOCKCHAIN_CONNECT_TIMEOUT_MS = "2000"
$env:BLOCKCHAIN_READ_TIMEOUT_MS = "3000"

# Avoid collision with another local backend on 8080
$env:SERVER_PORT = "8081"
$env:SPRING_PROFILES_ACTIVE = "local"

# Use documented local JWT from application-local.properties if not set in .env
if (-not $env:JWT_SECRET) {
    $env:JWT_SECRET = "local-dev-only-not-for-production-32chars-min"
}

$javaHome = $env:JAVA_HOME
if (-not $javaHome) {
    $javaExe = (Get-Command java -ErrorAction Stop).Source
    $javaHome = Split-Path (Split-Path $javaExe -Parent) -Parent
    $env:JAVA_HOME = $javaHome
}
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

Write-Host "Starting backend on http://localhost:8081 with BLOCKCHAIN_ENABLED=true"
Write-Host "Ensure minichain is running on http://localhost:3001"
Write-Host ""

mvn spring-boot:run
