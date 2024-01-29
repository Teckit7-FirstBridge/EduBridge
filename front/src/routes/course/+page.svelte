<script lang="ts">
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import CourseNav from '../../components/CourseNav.svelte';

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
      <a href="/course/write" class="btn mt-5 ml-6 w-[200px]"> 강좌 등록</a>
      <main class="flex flex-1 flex-col gap-4 p-4 md:gap-8 md:p-6">
        <div class="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {#if items}
            {#each items as item}
              <a
                href="/course/{item.id}"
                class="p-4 border border-gray-200 rounded-lg dark:border-gray-800 flex-col text-center"
              >
                <h2 class="text-lg font-semibold my-2">{item.title}</h2>
                <div class="flex justify-center my-2">
                  <img src={item.imgUrl} />
                </div>
                <p class="text-sm text-gray-500 dark:text-gray-400 my-4">{item.overView}</p>
              </a>
            {/each}
          {/if}
        </div>
      </main>
    </div>
  </div>
{/await}
