@echo off
set "VIDEO_PATH=%1"
set "OUTPUT_PATH=%2"
set "SEGMENT_DURATION=%3"
set "RES_HEIGHT=%4"
set "RES_WIDTH=%5"
set "VIDEO_BITRATE=%6"
set "AUDIO_BITRATE=%7"

REM Create output directory if it doesn't exist
if not exist "%OUTPUT_PATH%\%RES_HEIGHT%" (
  mkdir "%OUTPUT_PATH%\%RES_HEIGHT%"
)

ffmpeg -i "%VIDEO_PATH%" ^
  -vf "scale=w=%RES_WIDTH%:h=-2" ^
  -c:v libx264 -b:v %VIDEO_BITRATE% ^
  -c:a aac -b:a %AUDIO_BITRATE% ^
  -f hls ^
  -hls_time %SEGMENT_DURATION% ^
  -hls_playlist_type vod ^
  -hls_segment_filename "%OUTPUT_PATH%\%RES_HEIGHT%\segment_%%03d.ts" ^
  "%OUTPUT_PATH%\%RES_HEIGHT%\playlist.m3u8"
