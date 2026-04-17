@echo off
powershell -Command "Get-Content .env | Where-Object { $_ -notmatch '^#' -and $_ -match '=' } | ForEach-Object { $parts = $_ -split '=', 2; [System.Environment]::SetEnvironmentVariable($parts[0].Trim(), $parts[1].Trim(), 'Process') }"
mvnw.cmd spring-boot:run
