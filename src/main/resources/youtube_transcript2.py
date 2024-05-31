# from youtube_transcript_api import YouTubeTranscriptApi
#
# srt = YouTubeTranscriptApi.get_transcript("oFKYzp6gGfc", languages=['ko', 'en'])
# for i in srt:
#     print(i.get('text'))
import sys
from youtube_transcript_api import YouTubeTranscriptApi

def get_transcript(video_id):
    srt = YouTubeTranscriptApi.get_transcript(video_id, languages=['ko', 'en'])
    for i in srt:
        print(i.get('text'))

if __name__ == "__main__":
    if len(sys.argv) > 1:
        video_id = sys.argv[1]
        get_transcript(video_id)
    else:
        print("No video ID provided.")
