Add-Type -AssemblyName System.Drawing
$path = "app/src/main/res/drawable/yawn.png"
$bitmap = New-Object System.Drawing.Bitmap($path)
$bitmap.MakeTransparent([System.Drawing.Color]::White)
$bitmap.Save("app/src/main/res/drawable/yawn_temp.png", [System.Drawing.Imaging.ImageFormat]::Png)
$bitmap.Dispose()
Move-Item "app/src/main/res/drawable/yawn_temp.png" $path -Force
Write-Host "Background removed successfully from yawn.png"
