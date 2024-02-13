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

{#await load()}
  <p class="text-center">loading...</p>
{:then { notes }}
  <div class="flex h-screen">
    <div class="hidden w-64 border-r bg-gray-100/40 lg:block dark:bg-gray-800/40">
      <div class="flex h-full max-h-screen flex-col gap-2">
        <div class="flex items-center h-16 px-4 border-b border-gray-200 dark:border-gray-800">
          <a class="flex items-center gap-2 font-semibold" href="/"
            ><svg
              xmlns="http://www.w3.org/2000/svg"
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
              class="h-6 w-6"
            >
              <path d="m8 3 4 8 5-5 5 15H2L8 3z"></path></svg
            ><span class="">EduBridge</span></a
          >
        </div>
        <ul class="menu w-56 rounded-box">
          <li>
            <a
              class="flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors hover:bg-gray-100 hover:text-gray-900 dark:hover:bg-gray-800 dark:hover:text-gray-50"
              href="/member/course"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke-width="1.5"
                stroke="currentColor"
                class="w-6 h-6"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="m2.25 12 8.954-8.955c.44-.439 1.152-.439 1.591 0L21.75 12M4.5 9.75v10.125c0 .621.504 1.125 1.125 1.125H9.75v-4.875c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125V21h4.125c.621 0 1.125-.504 1.125-1.125V9.75M8.25 21h8.25"
                />
              </svg>

              DashBoard
            </a>
          </li>
          <li>
            <a
              class="flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors hover:bg-gray-100 hover:text-gray-900 dark:hover:bg-gray-800 dark:hover:text-gray-50"
              href="/course/{parseInt($page.params.id)}/notes"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke-width="1.5"
                stroke="currentColor"
                class="w-6 h-6"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="M12 6.042A8.967 8.967 0 0 0 6 3.75c-1.052 0-2.062.18-3 .512v14.25A8.987 8.987 0 0 1 6 18c2.305 0 4.408.867 6 2.292m0-14.25a8.966 8.966 0 0 1 6-2.292c1.052 0 2.062.18 3 .512v14.25A8.987 8.987 0 0 0 18 18a8.967 8.967 0 0 0-6 2.292m0-14.25v14.25"
                />
              </svg>

              요약 노트
            </a>
          </li>
          <li>
            <a
              class="flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors hover:bg-gray-100 hover:text-gray-900 dark:hover:bg-gray-800 dark:hover:text-gray-50"
              href="/course/{parseInt($page.params.id)}"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke-width="1.5"
                stroke="currentColor"
                class="w-6 h-6"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="M9 15 3 9m0 0 6-6M3 9h12a6 6 0 0 1 0 12h-3"
                />
              </svg>

              강좌 돌아가기
            </a>
          </li>
        </ul>
      </div>
    </div>
    <div class="py-8 px-56 w-full">
      <div>
        {#if notes && notes.length > 0}
          <div class="mb-5">
            <div class="mt-3 space-y-4">
              {#each notes as item, index}
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
                          요약 노트 {index + 1}
                        </a>
                      </div>
                      <div class="ml-2 flex-shrink-0 flex">
                        <p class="text-sm text-gray-500 mr-2">점수: {item.score}</p>
                        <p
                          class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800"
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
          <p>요약 노트가 없습니다.</p>
        {/if}
      </div>
    </div>
  </div>
{/await}
