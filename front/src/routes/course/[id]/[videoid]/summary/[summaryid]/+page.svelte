<script lang="ts">
  import rq from '$lib/rq/rq.svelte';
  import { page } from '$app/stores';
  import type { components } from '$lib/types/api/v1/schema';

  let summarynote: components['schemas']['SummaryNoteDto'];
  async function deleteSummary() {
    const { data } = await rq.apiEndPoints().DELETE(`/api/v1/courses/{videoId}/note/{noteId}`, {
      params: {
        path: {
          videoId: parseInt($page.params.videoid),
          noteId: parseInt($page.params.summaryid)
        }
      }
    });
    if (data) {
      rq.msgInfo('삭제가 완료되었습니다');
      rq.goTo(`/course/${$page.params.id}`);
    }
  }
  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');
    const { data } = await rq.apiEndPoints().GET(`/api/v1/courses/{videoId}/note/{noteId}`, {
      params: {
        path: {
          videoId: parseInt($page.params.videoid),
          noteId: parseInt($page.params.summaryid)
        }
      }
    });
    summarynote = data?.data!;
    return { summarynote };
  }
</script>

{#await load()}
  <h1>loading...</h1>
{:then { summarynote }}
  <div class="max-w-4xl mx-auto my-8">
    <div class="flex items-center justify-between mt-3 mb-20">
      <a href="/course/{$page.params.id}" class="btn btn-sm flex items-center gap-2">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
          class="w-4 h-4"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M15 19l-7-7 7-7"
          />
        </svg>
      </a>

      <div>
        <p class="text-gray-600 mb-2">작성자: {summarynote.member?.nickname}</p>
        {#if summarynote.score! > 0}
          <p class="text-gray-600 mb-2">점 수: {summarynote.score}</p>
        {/if}
        <!-- 글 작성자인 경우 -->
        {#if rq.member.id == summarynote.member?.id && summarynote.score!<70}
          <div class="flex gap-2">
            <a
              href="/course/{$page.params.id}/{$page.params.videoid}/summary/{$page.params
                .summaryid}/edit"
              class="btn btn-sm">수정</a
            >
            <button class="btn btn-sm" on:click={deleteSummary}>삭제</button>
          </div>
        {/if}
      </div>
    </div>

    <div class="p-5 bg-white shadow rounded-md">
      <p class="w-auto break-all text-gray-700 text-lg leading-relaxed">
        {summarynote.content}
      </p>
    </div>
  </div>
{/await}
