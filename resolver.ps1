$repo = "c:\Users\carpeta nose\SFC_techcup-futbol"
Set-Location $repo

# Resolver todos los conflictos aceptando HEAD (usuario-inicio-sesion)
git checkout --ours -- "src/"
git add "src/"

# Docs: quedarse con la version de usuario-inicio-sesion
git checkout --ours -- "docs/"
git add "docs/"

# Eliminar el archivo renombrado de develop que no queremos
git rm -f "docs/uml/nico clases final.drawio" 2>$null

git add .gitignore
git add .

git commit -m "merge: integrar develop en usuario-inicio-sesion resolviendo conflictos"
git push origin feature/usuario-inicio-sesion

Write-Host "Listo"
