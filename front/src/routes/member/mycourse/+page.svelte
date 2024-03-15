<script lang="ts">
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';

  let courselist: components['schemas']['CourseDto'][] | undefined;
  let likesList: Boolean[] = $state([]);
  let voteNumList: number[] = $state([]);

  function formatTitle(title) {
    return title.length > 11 ? `${title.substring(0, 11)}...` : title;
  }

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

    const page_ = parseInt($page.url.searchParams.get('page') ?? '1');

    const { data } = await rq.apiEndPoints().GET('/api/v1/courses/mycourse', {
      params: {
        query: {
          page: page_
        }
      }
    });
    console.log(data);
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
  <div class="">
    <div class="flex justify-between items-center justify-center mb-4">
      <a href="/course/write" class="btn bg-gray-200 mt-5 ml-6 w-[200px]"> 강좌 등록</a>
      <div class=" mr-4">
        <button
          class="btn btn-ghost"
          onclick={() => {
        const searchFormModal = (document.querySelector('#searchFormModal') as HTMLDialogElement);
        const searchFormInputSearch = (document.querySelector('#searchFormModal input[type=search]') as HTMLDialogElement);
  
        searchFormModal.showModal();
  
        searchFormInputSearch.focus();
      }}
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-5 w-5"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
            ><path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
            /></svg
          >
        </button>
        <dialog id="searchFormModal" class="modal">
          <div class="modal-box">
            <form method="dialog">
              <button class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
            </form>

            <form
              action="/course"
              class="bg-base rounded flex flex-col gap-6"
              onsubmit={() => {
              const searchFormModal = (document.querySelector('#searchFormModal') as HTMLDialogElement);
              searchFormModal.close();
            }}
            >
              <div class="max-w-md mx-auto bg-white p-5">
                <div class="form-control">
                  <label for="kwType" class="label text-sm font-bold text-gray-700">검색필터</label>
                  <select
                    id="kwType"
                    name="kwType"
                    class="select select-bordered w-full max-w-xs focus:outline-none focus:ring-0"
                  >
                    <option value="ALL">전체</option>
                    <option value="TITLE">제목</option>
                    <option value="NAME">작성자</option>
                  </select>
                </div>

                <div class="form-control mt-4">
                  <label for="kw" class="label text-sm font-bold text-gray-700">검색어</label>
                  <input
                    id="kw"
                    name="kw"
                    type="search"
                    placeholder="검색어"
                    class="input input-bordered w-full max-w-xs focus:outline-none focus:ring-0"
                  />
                </div>

                <div class="mt-4">
                  <button
                    class="btn border border-gray-300 text-gray-800 bg-white hover:bg-gray-50 active:bg-gray-200 focus:ring-2 focus:ring-gray-200 px-4 py-2 rounded transition ease-in duration-200 text-center text-base font-semibold shadow-md focus:outline-none focus:ring-offset-2 focus:ring-offset-gray-100 w-full"
                  >
                    검색
                  </button>
                </div>
              </div>
            </form>
          </div>

          <form method="dialog" class="modal-backdrop">
            <button>close</button>
          </form>
        </dialog>
      </div>
    </div>
    <div class="flex flex-col flex-1">
      <div class="px-4 grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {#if courselist}
          {#each courselist as item}
            <div
              class="border border-gray-200 rounded-lg dark:border-gray-800 flex-col text-center pt-2"
            >
              <a href="/course/{item.id}">
                <div class="flex justify-center gap-2">
                  <h2 class="text-lg font-semibold my-1 ml-2">{formatTitle(item.title)}</h2>
                </div>
                <div class="flex justify-center p-2 bg-black rounded-lg">
                  <img src={item.imgUrl} />
                </div>

                <p class="text-sm text-gray-500 dark:text-gray-400 my-4 mx-2">
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