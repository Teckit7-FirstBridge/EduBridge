<script lang="ts">
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';

  let notes: components['schemas']['SummaryNoteDto'][] = $state();

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');

    const { data } = await rq.apiEndPoints().GET(`/api/v1/courses/summary/{writerId}/{courseId}`, {
      params: {
        path: {
          writerId: rq.member.id,
          courseId: parseInt($page.params.id)
        }
      }
    });

    notes = data!.data;

    return { notes };
  }
</script>

<div class="max-w-4xl mx-auto">
  {#await load()}
    <p class="text-center">loading...</p>
  {:then { notes }}
    <div class="flex">
      <div class="flex flex-col w-full max-w-screen min-h-screen px-4 md:px-6">
        <div>
          {#if notes && notes.length > 0}
            <div class="mb-5">
              <div class="mt-3 space-y-4">
                {#each notes as item}
                  <div class="flex justify-col justify-end">
                    <div>
                      <a href="/course/{item.courseId}">
                        <h2 class="text-2xl font-semibold text-blue-600 hover:text-blue-900">
                          {item.courseName}
                        </h2>
                      </a>
                    </div>
                  </div>
                  <div class="bg-white shadow overflow-hidden rounded-lg">
                    <div class="px-4 py-5 sm:p-6">
                      <div class="flex items-center justify-between">
                        <div class="text-sm font-medium text-gray-900">
                          <a
                            href="/course/{item.courseId}/{item.videoId}/summary/{item.id}"
                            class="text-xl text-blue-600 hover:text-blue-900"
                          >
                            {item.title}
                          </a>
                        </div>
                        <div class="ml-2 flex-shrink-0 flex">
                          <p class="text-sm text-gray-500 mr-2">점수: {item.score}</p>
                          <p
                            class={`inline-flex px-2 text-xs leading-5 font-semibold rounded-full ${!item.pass ? 'bg-red-100 text-red-800' : 'bg-green-100 text-green-800'}`}
                          >
                            {item.pass ? 'Pass' : 'Fail'}
                          </p>
                        </div>
                      </div>
                      <div class="mt-2">
                        <p class="text-sm text-gray-500">
                          작성일: {`${new Date(item.createDate).getFullYear()}년 ${new Date(item.createDate).getMonth() + 1}월 ${new Date(item.createDate).getDate()}일`}
                        </p>
                      </div>
                    </div>
                  </div>
                {/each}
              </div>
            </div>
          {:else}
            <div class="m-4">해당 강좌에 등록된 요약 노트가 없습니다.</div>
          {/if}
        </div>
      </div>
    </div>
  {/await}
</div>
