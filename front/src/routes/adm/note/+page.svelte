<script lang="ts">
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import CourseNav from '../../../lib/components/CourseNavAdm.svelte';
  import { goto } from '$app/navigation';

  let recentNote: components['schemas']['RecentSummaryNoteDto'][] = $state();
  let currentPage: number = 1;
  let totalPages: number = 0;
  let recentNotePage: PageDto<components['schemas']['RecentSummaryNoteDto']>;

  async function load(page: number = 1) {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');

    const response = await rq.apiEndPoints().GET(`/api/v1/admin/summaryNotes/list?page=${page}`);
    recentNotePage = response.data?.data;

    if (recentNotePage) {
      currentPage = recentNotePage.number;
      totalPages = recentNotePage.totalPagesCount;
    }
  }
</script>

{#await load()}
  <p class="text-center">loading...</p>
{:then { recentNote }}
  <div class="flex h-screen bg-gray-100 dark:bg-gray-900">
    <div>
      <CourseNav></CourseNav>
    </div>
    <div class="py-8 px-56 w-full">
      <div>
        {#if recentNote && recentNote.length > 0}
          <div class="mb-5">
            <div class="flex justify-col justify-end">
              <div>
                <h2 class="text-2xl font-semibold text-gray-800">요약 노트 관리</h2>
              </div>
              <div class="ml-2 mt-1">
                <a
                  href="/adm/note"
                  class="inline-block px-2 py-1 bg-blue-600 text-white font-medium text-xs leading-tight uppercase rounded shadow-md hover:bg-blue-700 hover:shadow-lg focus:bg-blue-700 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-blue-800 active:shadow-lg transition duration-150 ease-in-out"
                  >+</a
                >
              </div>
            </div>
            <div class="mt-3 bg-white shadow overflow-hidden rounded-md">
              <table class="min-w-full divide-y divide-gray-200">
                <thead class="bg-gray-50">
                  <tr>
                    <th
                      scope="col"
                      class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                    >
                      강좌명
                    </th>
                    <th
                      scope="col"
                      class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                    >
                      작성일
                    </th>
                    <th
                      scope="col"
                      class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                    >
                      작성자
                    </th>
                  </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                  {#each recentNote as item}
                    <tr>
                      <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        <a
                          href="/course/{item.courseId}/{item.videoId}/summary/{item.id}"
                          class="text-blue-600 hover:text-blue-900"
                        >
                          {item.courseName}
                        </a>
                      </td>
                      <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {`${new Date(item.createDate).getFullYear()}년 ${new Date(item.createDate).getMonth() + 1}월 ${new Date(item.createDate).getDate()}일`}
                      </td>
                      <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        <a
                          href="/course/{item.courseId}/{item.videoId}/summary/{item.id}"
                          class="text-blue-600 hover:text-blue-900"
                        >
                          {item.name}
                        </a>
                      </td>
                    </tr>
                  {/each}
                </tbody>
              </table>
            </div>
          </div>
        {:else}
          <p>요약 노트가 없습니다.</p>
        {/if}
      </div>
    </div>
  </div>
{/await}
