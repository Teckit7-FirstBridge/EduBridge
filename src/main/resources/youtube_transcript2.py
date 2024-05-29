from youtube_transcript_api import YouTubeTranscriptApi

srt = YouTubeTranscriptApi.get_transcript("oFKYzp6gGfc", languages=['ko', 'en'])
for i in srt:
    print(i.get('text'))