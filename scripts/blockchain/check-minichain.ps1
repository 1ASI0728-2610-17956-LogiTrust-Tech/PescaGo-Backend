# Checks minichain health, pending transactions, and chain on port 3001.

param(
    [int] $Port = 3001
)

$ErrorActionPreference = "Stop"
$baseUrl = "http://localhost:$Port"

Write-Host "=== HEALTH GET $baseUrl/ ==="
try {
    $health = Invoke-RestMethod -Method Get -Uri "$baseUrl/"
    Write-Host $health
} catch {
    Write-Host "ERROR: $($_.Exception.Message)"
    exit 1
}

Write-Host ""
Write-Host "=== PENDING GET $baseUrl/transactions/pending ==="
$pending = Invoke-RestMethod -Method Get -Uri "$baseUrl/transactions/pending"
$pending | ConvertTo-Json -Depth 6

Write-Host ""
Write-Host "=== CHAIN GET $baseUrl/chain ==="
$chain = Invoke-RestMethod -Method Get -Uri "$baseUrl/chain"
$pescagoEvents = @()
foreach ($block in $chain) {
    foreach ($tx in $block.transactions) {
        if ($tx.from -like "PESCAGO:*") {
            $pescagoEvents += $tx
        }
    }
}

Write-Host "PESCAGO events in chain: $($pescagoEvents.Count)"
$pescagoEvents | ConvertTo-Json -Depth 6
