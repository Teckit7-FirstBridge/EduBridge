<script lang="ts">
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import CourseNav from '../../lib/components/CourseNav.svelte';

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');

    const kw = $page.url.searchParams.get('kw') ?? '';
    const kwType = ($page.url.searchParams.get('kwType') ?? 'ALL') as KwTypeCourse;
    const page_ = parseInt($page.url.searchParams.get('page') ?? '1');

    const { data } = await rq.apiEndPoints().GET('/api/v1/courses', {
      params: {
        query: {
          kw,
          kwType,
          page: page_
        }
      }
    });

    return data!;
  }
</script>

{#await load()}
  <p>loading...</p>
{:then { data: { items } }}
  <div class="flex h-screen bg-gray-100 dark:bg-gray-900">
    <CourseNav></CourseNav>
    <div class="flex flex-col flex-1">
      <main class="flex flex-1 flex-col p-4 md:p-6">
        {#if items && items.length > 0}
          <div class="flex justify-between items-center">
            <h2 class="text-xl font-semibold">최신 강좌</h2>
            <a href="/admin/course" class="btn">더보기</a>
          </div>
          <table class="min-w-full leading-normal mt-5">
            <thead>
              <tr>
                <th
                  class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider"
                >
                  강좌 목록
                </th>
              </tr>
            </thead>
            <tbody>
              {#each items as item}
                <tr>
                  <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
                    <a href="/course/{item.id}" class="text-blue-500 hover:text-blue-800">
                      {item.title}
                    </a>
                  </td>
                </tr>
              {/each}
            </tbody>
          </table>
        {:else}
          <p>강좌가 없습니다.</p>
        {/if}
      </main>
    </div>
  </div>
{/await}
