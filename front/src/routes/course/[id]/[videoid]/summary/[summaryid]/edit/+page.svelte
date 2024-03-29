<script lang="ts">
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';

  let content: string = $state('');
  let videos: components['schemas']['VideoDto'][] = $state();
  let summarynote: components['schemas']['SummaryNoteDto'];

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');
    const responseVideos = await rq.apiEndPoints().GET(`/api/v1/courses/{courseId}/videos`, {
      params: {
        path: {
          courseId: parseInt($page.params.id)
        }
      }
    });
    videos = responseVideos.data?.data!;

    const { data } = await rq.apiEndPoints().GET(`/api/v1/courses/{videoId}/note/{noteId}`, {
      params: {
        path: {
          videoId: parseInt($page.params.videoid),
          noteId: parseInt($page.params.summaryid)
        }
      }
    });
    summarynote = data?.data!;
    content = summarynote.content;

    return { videos, summarynote };
  }
  const submitForm = async () => {
    const { data, error } = await rq.apiEndPoints().PUT(`/api/v1/courses/{videoId}/note/{noteId}`, {
      params: {
        path: { videoId: parseInt($page.params.videoid), noteId: parseInt($page.params.summaryid) }
      },
      body: {
        content
      }
    });
    if (data) {
      rq.goTo(`/course/${$page.params.id}/${$page.params.videoid}/summary/${data.data.id}`);
    } else if (error) {
      rq.msgError('이미 통과 된 요약노트는 수정이 불가합니다.');
    }
  };
</script>

{#await load()}
  <span class="loading loading-spinner loading-xs m-2"></span>
{:then { videos }}
  <div class="max-w-lg mx-auto w-full px-4 sm:px-6 lg:px-8 mt-2">
    <div class="flex flex-col w-full h-80 mt-10">
      {#each videos as video}
        {#if video.id == parseInt($page.params.videoid)}
          <div class="flex items-center text-center">
            <h2 class="text-lg font-semibold text-gray-700 mb-2">Keywords:&nbsp&nbsp</h2>
            <h2 class="text-lg font-semibold text-gray-700 mb-2">{video.keywords}</h2>
          </div>
        {/if}
      {/each}
      <form
        on:submit|preventDefault={submitForm}
        class="flex flex-col w-full h-full p-4 shadow-md rounded-md bg-white dark:bg-gray-800"
      >
        <textarea
          bind:value={content}
          class=" h-40 shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline focus:border-gray-700"
          placeholder="작성해주세요..."
        ></textarea>
        <div class="flex justify-end">
          <button
            type="submit"
            class="mt-2 w-[60px] inline-block px-4 py-2 border border-gray-400 text-gray-700 bg-white hover:bg-gray-700 hover:text-white rounded-md shadow-sm text-sm font-medium focus:outline-none"
          >
            저장
          </button>
        </div>
      </form>
    </div>
  </div>
{/await}
