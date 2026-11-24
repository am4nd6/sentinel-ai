param([string]$Output = "backups/sentinel-$(Get-Date -Format yyyyMMdd-HHmmss).sql")
$directory = Split-Path $Output -Parent
New-Item -ItemType Directory -Force $directory | Out-Null
docker exec compose-postgres-1 pg_dump -U sentinel -d sentinel --format=custom --file=/tmp/sentinel.backup
docker cp compose-postgres-1:/tmp/sentinel.backup $Output
Write-Output "Backup written to $Output"
