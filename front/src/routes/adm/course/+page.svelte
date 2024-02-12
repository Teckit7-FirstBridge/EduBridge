<script lang="ts">
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import CourseNav from '../../../lib/components/AdmNav.svelte';

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
  {#if rq.isAdmin()}
    <div class="flex">
      <div>
        <CourseNav></CourseNav>
      </div>
      <div class="flex flex-col flex-1">
        <a href="/adm/course/write" class="btn my-5 ml-6 w-[200px]"> 강좌 등록</a>
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
                <p class="text-sm text-gray-500 dark:text-gray-400">{item.overView}</p>
              </a>
            {/each}
          {/if}
        </div>
      </div>
    </div>
  {:else}
    <a href="/" class="btn btn-outline btn-error m-5">접근 불가 메인으로</a>
    {#if !rq.isLogin()}
      <a href="/member/login" class="btn btn-outline btn-error m-5">로그인</a>
    {/if}
  {/if}
{/await}
