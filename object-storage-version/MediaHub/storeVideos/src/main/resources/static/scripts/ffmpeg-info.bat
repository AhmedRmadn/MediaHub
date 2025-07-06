@echo off
REM Usage: ffmpeg-info.bat path\to\video.mp4

set "VID_PATH=%1"

ffprobe -v error -select_streams v:0 -show_entries stream=width,height,duration -of default=noprint_wrappers=1 "%VID_PATH%"
