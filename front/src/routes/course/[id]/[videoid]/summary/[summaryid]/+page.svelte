<script lang="ts">
  import rq from '$lib/rq/rq.svelte';
  import { page } from '$app/stores';
  import type { components } from '$lib/types/api/v1/schema';
  import ToastUiEditor from '$lib/components/ToastUiEditor.svelte';

  let summarynote: components['schemas']['SummaryNoteDto'];
  let noteviewer: any | undefined = $state();
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

  function goBack() {
    window.history.back();
  }
</script>

{#await load()}
  <span class="loading loading-spinner loading-xs m-2"></span>
{:then { summarynote }}
  <div class="max-w-4xl mx-auto my-8">
    <div class="flex items-center justify-between mt-3 mb-20">
      <a
        href="/course/{$page.params.id}"
        class="ml-2 btn btn-sm flex items-center gap-2 bg-white border-gray-400 hover:bg-white hover:border-gray-700 focus:bg-white focus:border-gray-700"
      >
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
        <p class="text-gray-600 mb-2">작성자: {summarynote.member_nickname}</p>
        {#if summarynote.score! >= 0}
          <div class="flex gap-2">
            <div>
              <p class="text-gray-600 mb-2">점 수: {summarynote.score}</p>
            </div>
            <div>
              <p
                class={`inline-flex px-2 text-xs leading-5 font-semibold rounded-full ${!summarynote.pass ? 'bg-red-100 text-red-800' : 'bg-green-100 text-green-800'}`}
              >
                {summarynote.pass ? 'Pass' : 'Fail'}
              </p>
            </div>
          </div>
        {/if}
        <!-- 글 작성자인 경우 -->
        {#if rq.member.id == summarynote.member_id && summarynote.score!<70}
          <div class="flex gap-2">
            <a
              href="/course/{$page.params.id}/{$page.params.videoid}/summary/{$page.params
                .summaryid}/edit"
              class="font-semibold inline-block px-4 py-2 border border-gray-400 text-gray-800 bg-white hover:bg-gray-700 hover:text-white rounded-md shadow-sm text-sm font-medium focus:outline-none"
              >수정</a
            >
            <button
              class="mr-2 font-semibold inline-block px-4 py-2 border border-gray-400 text-gray-800 bg-white hover:bg-gray-700 hover:text-white rounded-md shadow-sm text-sm font-medium focus:outline-none"
              on:click={deleteSummary}>삭제</button
            >
          </div>
        {/if}
      </div>
    </div>

    <div class="p-5 bg-white shadow rounded-md">
      <ToastUiEditor
        bind:this={noteviewer}
        body={summarynote.content}
        height={'calc(50dvh - 64px)'}
        viewer={true}
      ></ToastUiEditor>
    </div>
  </div>
{/await}
