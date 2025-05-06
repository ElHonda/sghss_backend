# Script para configurar o ambiente Java/Maven do projeto SGHSS

$env:JAVA_HOME = "C:\Program Files\Java\jdk-17.0.2"
$env:M2_HOME = "D:\dev\programas\apache-maven-3.9.9"
Remove-Item Env:MAVEN_OPTS -ErrorAction SilentlyContinue

# Adiciona o Maven do projeto no início do PATH
$env:PATH = "$env:JAVA_HOME\bin;$env:M2_HOME\bin;" + $env:PATH

Write-Host "JAVA_HOME definido para $env:JAVA_HOME"
Write-Host "M2_HOME definido para $env:M2_HOME"
Write-Host "PATH atualizado para priorizar o Maven do projeto"
Write-Host "MAVEN_OPTS limpo" 