# Registers minichain peers for local demo (ports 3001, 3002, 3003).

$ErrorActionPreference = "Stop"

$registrations = @(
    @{
        Port = 3001
        Peers = @("http://localhost:3002", "http://localhost:3003")
    },
    @{
        Port = 3002
        Peers = @("http://localhost:3001", "http://localhost:3003")
    },
    @{
        Port = 3003
        Peers = @("http://localhost:3001", "http://localhost:3002")
    }
)

foreach ($registration in $registrations) {
    $port = $registration.Port
    $body = @{ nodes = $registration.Peers } | ConvertTo-Json

    Write-Host "Registering peers on node :$port ..."
    $response = Invoke-RestMethod `
        -Method Post `
        -Uri "http://localhost:$port/nodes/register" `
        -ContentType "application/json" `
        -Body $body

    $response | ConvertTo-Json -Depth 5
    Write-Host ""
}

Write-Host "Peer registration completed."
