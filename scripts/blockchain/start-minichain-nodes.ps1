# Starts three minichain nodes in separate PowerShell windows.
# Expects minichain as sibling folder of PescaGo-Backend:
#   PescaGo/
#     PescaGo-Backend/
#     minichain/

$ErrorActionPreference = "Stop"

$backendRoot = Split-Path (Split-Path $PSScriptRoot -Parent) -Parent
$minichainPath = Resolve-Path (Join-Path $backendRoot "..\minichain") -ErrorAction Stop

if (-not (Test-Path (Join-Path $minichainPath "index.js"))) {
    throw "minichain not found at: $minichainPath"
}

$ports = @(3001, 3002, 3003)

foreach ($port in $ports) {
    $command = "Set-Location '$minichainPath'; node index.js --port=$port"
    Start-Process powershell -ArgumentList "-NoExit", "-Command", $command
    Write-Host "Started minichain node on port $port"
}

Write-Host ""
Write-Host "Three minichain windows should be open."
Write-Host "Next: .\scripts\blockchain\register-minichain-nodes.ps1"
