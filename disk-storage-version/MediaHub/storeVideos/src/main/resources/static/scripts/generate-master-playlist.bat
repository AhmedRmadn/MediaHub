@echo off
setlocal enabledelayedexpansion

REM === Get root folder from argument
set "HLS_ROOT=%~1"
if not exist "%HLS_ROOT%" (
    echo Provided path does not exist: %HLS_ROOT%
    exit /b 1
)

REM === Output master playlist
set "MASTER_PLAYLIST=%HLS_ROOT%\master.m3u8"

(
echo #EXTM3U
echo #EXT-X-VERSION:3
) > "%MASTER_PLAYLIST%"

for /D %%R in ("%HLS_ROOT%\*") do (
    set "RES_FOLDER=%%~nxR"
    set "PLAYLIST=%%~fR\playlist.m3u8"

    REM Map resolutions to bandwidth/resolution
    if "!RES_FOLDER!"=="360" (
        set "BANDWIDTH=800000"
        set "RESOLUTION=640x360"
    ) else if "!RES_FOLDER!"=="720" (
        set "BANDWIDTH=2800000"
        set "RESOLUTION=1280x720"
    ) else if "!RES_FOLDER!"=="1080" (
        set "BANDWIDTH=5000000"
        set "RESOLUTION=1920x1080"
    ) else (
        set "BANDWIDTH=1000000"
        set "RESOLUTION=!RES_FOLDER!x?"
    )

    (
        echo #EXT-X-STREAM-INF:BANDWIDTH=!BANDWIDTH!,RESOLUTION=!RESOLUTION!
        echo !RES_FOLDER!/playlist.m3u8
    ) >> "%MASTER_PLAYLIST%"
)

echo Master playlist created at %MASTER_PLAYLIST%
