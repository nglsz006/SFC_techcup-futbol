@echo off
for /f "usebackq tokens=1,2 delims==" %%A in (".env") do (
    if not "%%A:~0,1%%"=="#" set "%%A=%%B"
)
mvnw.cmd spring-boot:run
