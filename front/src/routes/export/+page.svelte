<script lang="ts">
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';

  let notes: components['schemas']['SummaryNoteDto'][] | undefined;
  let memberName: string = $state('');
  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');

    const { data } = await rq.apiEndPoints().GET(`/api/v1/courses/summary`, {
      params: {
        query: {
          uuid: $page.url.searchParams.get('uuid')!
        }
      }
    });
    memberName = data?.data.member_nickname;

    if (data?.data.items) {
      notes = data.data.items.sort((a, b) => a.courseId - b.courseId || a.id - b.id);
    }

    return { notes };
  }
</script>

{#await load()}
  <span class="loading loading-spinner loading-xs m-2"></span>
{:then { notes }}
  {#if notes && notes.length > 0}
    <div class="flex flex-col w-full max-w-screen min-h-screen px-4 mt-10 md:px-6 lg:px-40">
      <p class="text-3xl font-semibold mb-4">{memberName} 님의 통과된 요약노트</p>
      <div class="mb-5">
        {#each notes as item, index}
          <a href="/course/{item.courseId}/{item.videoId}/summary/{item.id}">
            {#if index === 0 || item.courseId !== notes[index - 1].courseId}
              <div class="mt-3">
                <h2 class="text-2xl font-semibold text-gray-900">
                  {item.courseName}
                </h2>
              </div>
            {/if}
            <div class="mt-2 bg-white shadow overflow-hidden rounded-lg">
              <div class="px-4 sm:p-6">
                <div class="flex items-center justify-between">
                  <div class="text-lg font-semibold text-gray-900">
                    {item.title}
                  </div>
                  <div class="ml-2 flex-shrink-0 flex">
                    <p class="text-sm text-gray-500">
                      수강일: {`${new Date(item.createDate).getFullYear()}년 ${new Date(item.createDate).getMonth() + 1}월 ${new Date(item.createDate).getDate()}일`}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </a>
        {/each}
      </div>
    </div>
  {:else}
    <p class="m-10">통과 된 요약 노트가 없습니다.</p>
  {/if}
{/await}
