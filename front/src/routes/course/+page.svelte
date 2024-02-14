<script lang="ts">
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import CourseNav from '../../lib/components/CourseNav.svelte';

  let courselist: components['schemas']['CourseDto'][] | undefined;
  let likesList: Boolean[] = $state([]);
  let voteNumList: number[] = $state([]);

  function removeMarkdown(markdownText) {
    // 정규 표현식을 사용하여 마크다운 문법 제거
    const text = markdownText
      .replace(/!\[[^\]]*\]\([^\)]*\)/g, '') // 이미지 링크 제거
      .replace(/\[[^\]]*\]\([^\)]*\)/g, '') // 일반 링크 제거
      .replace(/#{1,6} /g, '') // 헤더 제거
      .replace(/(\*\*|__)(.*?)\1/g, '$2') // 볼드 제거
      .replace(/(\*|_)(.*?)\1/g, '$2') // 이탤릭 제거
      .replace(/~~(.*?)~~/g, '$1') // 취소선 제거
      .replace(/`{3}[\s\S]*?`{3}/g, '') // 코드 블록 제거
      .replace(/`(.+?)`/g, '$1') // 인라인 코드 제거
      .replace(/\n/g, ' ') // 줄바꿈을 공백으로 변경
      .trim();

    return text;
  }

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');

    const kw = $page.url.searchParams.get('kw') ?? '';
    const kwType = ($page.url.searchParams.get('kwType') ?? 'ALL') as KwTypeCourse;
    const page_ = parseInt($page.url.searchParams.get('page') ?? '1');
    const grade = $page.url.searchParams.get('grade') ?? '';

    const { data } = await rq.apiEndPoints().GET('/api/v1/courses', {
      params: {
        query: {
          kw,
          kwType,
          page: page_,
          grade: grade
        }
      }
    });
    courselist = data?.data.items!;
    likesList = courselist!.map((course) => course!.likedByCurrentUser!);
    voteNumList = courselist!.map((course) => course!.voteCount!);

    return { courselist }!;
  }

  async function clickLiked(item: components['schemas']['CourseDto']) {
    if (likesList[courselist!.indexOf(item)]) {
      const { data, error } = await rq.apiEndPoints().DELETE(`/api/v1/courses/{id}/like`, {
        params: { path: { id: item.id } }
      });
      if (data) {
        rq.msgInfo('좋아요 취소');
        likesList[courselist!.indexOf(item)] = !likesList[courselist!.indexOf(item)];
        voteNumList[courselist!.indexOf(item)] -= 1;
      } else if (error) {
        rq.msgError(error.msg);
      }
    } else {
      const { data, error } = await rq.apiEndPoints().POST(`/api/v1/courses/{id}/like`, {
        params: { path: { id: item.id } }
      });
      if (data) {
        rq.msgInfo('좋아요!!');
        likesList[courselist!.indexOf(item)] = !likesList[courselist!.indexOf(item)];
        voteNumList[courselist!.indexOf(item)] += 1;
      } else if (error) {
        rq.msgError(error.msg);
      }
    }
  }
</script>

{#await load()}
  <p>loading...</p>
{:then { courselist }}
  <div class="flex">
    <CourseNav></CourseNav>
    <div class="flex flex-col flex-1">
      <div class="p-4 grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {#if courselist}
          {#each courselist as item}
            <div
              class="border border-gray-200 rounded-lg dark:border-gray-800 flex-col text-center pt-2"
            >
              <a href="/course/{item.id}">
                <div class="flex justify-center gap-2">
                  <h2 class="text-lg font-semibold my-1 ml-2">{item.title}</h2>

                  <div
                    class={`inline-flex px-2 text-lg font-semibold rounded-full mt-1 my-1 ${item.grade === '초급' ? 'bg-blue-100 text-blue-800' : item.grade === '중급' ? 'bg-orange-100 text-orange-800' : 'bg-red-100 text-red-800'}`}
                  >
                    {item.grade}
                  </div>
                </div>
                <div class="flex justify-center p-2 bg-black rounded-lg">
                  <img src={item.imgUrl} />
                </div>

                <p class="text-sm text-gray-500 dark:text-gray-400 my-4">
                  {removeMarkdown(item.overView)}
                </p>
              </a>
              <div class=" flex justify-end gap-2 p-2" on:click={() => clickLiked(item)}>
                {#if likesList[courselist.indexOf(item)]}
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 24 24"
                    width="24"
                    height="24"
                  >
                    <!-- 빨간색 채워진 하트 -->
                    <path
                      fill="red"
                      stroke="red"
                      stroke-width="1.5"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"
                    />
                  </svg>{:else}
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 24 24"
                    width="24"
                    height="24"
                  >
                    <!-- 빨간색 빈 하트 -->
                    <path
                      fill="none"
                      stroke="red"
                      stroke-width="1.5"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"
                    />
                  </svg>
                {/if}
                <span>
                  {voteNumList[courselist!.indexOf(item)]}
                </span>
              </div>
            </div>
          {/each}
        {/if}
      </div>
    </div>
  </div>
{/await}
