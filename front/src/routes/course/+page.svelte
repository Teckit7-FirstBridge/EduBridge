<script lang="ts">
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import Pagination from '$lib/components/Pagination.svelte';

  let courselist: components['schemas']['PageDtoCourseListDto'][] = $state([]);
  let roadmaplist: components['schemas']['PageDtoRoadmapDto'][] = $state([]);
  let likesList: Boolean[] = $state([]);
  let voteNumList: number[] = $state([]);

  let hashtags: string[] = $state([]);
  let selectedTab = $state('course');

  function formatTitle(title) {
    return title.length > 11 ? `${title.substring(0, 11)}...` : title;
  }

  function changeTab(tabName: string) {
    selectedTab = tabName;
    rq.goTo(`/course?tab=${tabName}`, { replaceState: false }); // URL을 변경하되, 쿼리 파라미터를 제거합니다.
    load();
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

    selectedTab = $page.url.searchParams.get('tab') ?? 'course';

    const kw = $page.url.searchParams.get('kw') ?? '';
    const kwType = ($page.url.searchParams.get('kwType') ?? 'ALL') as KwTypeCourse;
    const page_ = parseInt($page.url.searchParams.get('page') ?? '1');

    if (selectedTab === 'course') {
      const { data } = await rq.apiEndPoints().GET('/api/v1/courses', {
        params: {
          query: {
            kw,
            kwType,
            page: page_
          }
        }
      });
      courselist = data?.data.itemPage.content;
      likesList = courselist!.map((course) => course!.likedByCurrentUser!);
      voteNumList = courselist!.map((course) => course!.voteCount!);

      return data!;
    } else {
      const { data } = await rq.apiEndPoints().GET('/api/v1/roadmap', {
        params: {
          query: {
            kw,
            kwType,
            page: page_
          }
        }
      });
      roadmaplist = data?.data.itemPage.content;

      return data!;
    }
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

<div class="max-w-4xl mx-auto my-8">
  <div role="tablist" class="tabs tabs-bordered flex px-4 max-w-4xl mx-auto mt-4">
    <a
      role="tab"
      class={`tab ${selectedTab === 'course' ? 'tab-active' : ''}`}
      on:click={() => {
        changeTab('course');
        load();
      }}
    >
      강좌
    </a>
    <a
      role="tab"
      class={`tab ${selectedTab === 'roadmap' ? 'tab-active' : ''}`}
      on:click={() => {
        changeTab('roadmap');
        load();
      }}
    >
      로드맵
    </a>
  </div>
  {#await load()}
    <p>loading...</p>
  {:then { data: { itemPage } }}
    {#if selectedTab === 'course'}
      <div class="">
        <div class="flex justify-between items-center justify-center mb-4 ml-2">
          <div>
            <a
              href="/course/write"
              class="ml-2 btn border border-gray-400 text-gray-800 bg-white hover:bg-gray-700 hover:border-gray-700 hover:text-white active:bg-gray-700 active:text-white active:border-gray-700 px-4 py-2 rounded transition ease-in duration-200 text-center text-base font-semibold shadow-md"
            >
              등록
            </a>
            <a
              href="/member/mycourse/?tab=course"
              class="ml-2 btn border border-gray-400 text-gray-800 bg-white hover:bg-gray-700 hover:border-gray-700 hover:text-white active:bg-gray-700 active:text-white active:border-gray-700 px-4 py-2 rounded transition ease-in duration-200 text-center text-base font-semibold shadow-md"
            >
              관리
            </a>
          </div>
          <div class=" mr-4">
            <button
              class="btn btn-ghost mt-4"
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
                  action="/course?tab=course&"
                  class="bg-base rounded flex flex-col gap-6"
                  onsubmit={() => {
            const searchFormModal = (document.querySelector('#searchFormModal') as HTMLDialogElement);
            searchFormModal.close();
          }}
                >
                  <input type="hidden" name="tab" value={selectedTab} />
                  <div class="max-w-md mx-auto bg-white p-5">
                    <div class="form-control">
                      <label for="kwType" class="label text-sm font-bold text-gray-700"
                        >검색필터</label
                      >
                      <select
                        id="kwType"
                        name="kwType"
                        class="select select-bordered w-full max-w-xs focus:outline-none focus:border-gray-700 focus:ring-0"
                      >
                        <option value="ALL">전체</option>
                        <option value="TITLE">제목</option>
                        <option value="NAME">작성자</option>
                        <option value="HASHTAGS">해시태그</option>
                      </select>
                    </div>

                    <div class="form-control mt-4">
                      <label for="kw" class="label text-sm font-bold text-gray-700">검색어</label>
                      <input
                        id="kw"
                        name="kw"
                        type="search"
                        placeholder="검색어"
                        class="input input-bordered w-full max-w-xs focus:outline-none focus:border-gray-700 focus:ring-0"
                      />
                    </div>

                    <div class="mt-4">
                      <button
                        class="w-full inline-block px-4 py-2 border border-gray-400 text-gray-700 bg-white hover:bg-gray-700 hover:text-white rounded-md shadow-sm text-sm font-medium focus:outline-none"
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
            {#if courselist && courselist.length > 0}
              {#each courselist as item}
                <div
                  class="border border-gray-200 rounded-lg dark:border-gray-800 flex-col text-center pt-2"
                >
                  <a href="/course/{item.id}">
                    <div class="flex justify-center gap-2">
                      <h2 class="text-lg font-semibold my-1 ml-2">{formatTitle(item.title)}</h2>
                    </div>
                  </a>
                  <div class="flex justify-end mr-2">
                    <details class="dropdown dropdown-end">
                      <summary class="flex items-center cursor-pointer">
                        <i class="fa-regular fa-user"></i>
                        {item.writer?.nickname}
                      </summary>
                      <ul
                        class="p-2 shadow menu dropdown-content z-[1] bg-base-100 rounded-box w-52"
                      >
                        <li>
                          <a href="/course?tab=course&kwType=NAME&kw={item.writer?.nickname}"
                            >{item.writer?.nickname} 강좌</a
                          >
                        </li>
                        <li>
                          <a href="/course?tab=roadmap&kwType=NAME&kw={item.writer?.nickname}"
                            >{item.writer?.nickname} 로드맵</a
                          >
                        </li>
                      </ul>
                    </details>
                  </div>
                  <a href="/course/{item.id}">
                    <div class="flex justify-center p-2 bg-black rounded-lg m-4">
                      <img src={item.imgUrl} />
                    </div>

                    <p class="text-sm text-gray-500 dark:text-gray-400 my-4 mx-2">
                      {removeMarkdown(item.overView)}
                    </p>
                  </a>
                  <div class="flex items-center justify-between">
                    {#if item.hashtags}
                      <div class="flex ml-4">
                        {#each item.hashtags.split('@') as hashtag}
                          <div class="flex text-amber-600 text-sm text-center items-center ml-2">
                            #{hashtag}
                          </div>
                        {/each}
                      </div>
                    {/if}
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
                        </svg>
                      {:else}
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
                        {item.voteCount}
                      </span>
                    </div>
                  </div>
                </div>
              {/each}
            {:else}
              <div class="p-2">등록된 강좌가 없습니다.</div>
            {/if}
          </div>
          <div class="mt-2">
            <Pagination page={itemPage} />
          </div>
        </div>
      </div>
    {:else}
      <div class="">
        <div class="flex justify-between items-center justify-center mb-4 ml-2">
          <div>
            <a
              href="/roadmap/write"
              class="ml-2 btn border border-gray-400 text-gray-800 bg-white hover:bg-gray-700 hover:border-gray-700 hover:text-white active:bg-gray-700 active:text-white active:border-gray-700 px-4 py-2 rounded transition ease-in duration-200 text-center text-base font-semibold shadow-md"
            >
              등록
            </a>
            <a
              href="/member/mycourse/?tab=roadmap"
              class="ml-2 btn border border-gray-400 text-gray-800 bg-white hover:bg-gray-700 hover:border-gray-700 hover:text-white active:bg-gray-700 active:text-white active:border-gray-700 px-4 py-2 rounded transition ease-in duration-200 text-center text-base font-semibold shadow-md"
            >
              관리
            </a>
          </div>
          <div class=" mr-4">
            <button
              class="btn btn-ghost mt-4"
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
                  action="/course?tab=roadmap&"
                  class="bg-base rounded flex flex-col gap-6"
                  onsubmit={() => {
            const searchFormModal = (document.querySelector('#searchFormModal') as HTMLDialogElement);
            searchFormModal.close();
          }}
                >
                  <input type="hidden" name="tab" value={selectedTab} />

                  <div class="max-w-md mx-auto bg-white p-5">
                    <div class="form-control">
                      <label for="kwType" class="label text-sm font-bold text-gray-700"
                        >검색필터</label
                      >
                      <select
                        id="kwType"
                        name="kwType"
                        class="select select-bordered w-full max-w-xs focus:outline-none focus:border-gray-700 focus:ring-0"
                      >
                        <option value="ALL">전체</option>
                        <option value="TITLE">제목</option>
                        <option value="NAME">작성자</option>
                        <option value="HASHTAGS">해시태그</option>
                      </select>
                    </div>

                    <div class="form-control mt-4">
                      <label for="kw" class="label text-sm font-bold text-gray-700">검색어</label>
                      <input
                        id="kw"
                        name="kw"
                        type="search"
                        placeholder="검색어"
                        class="input input-bordered w-full max-w-xs focus:outline-none focus:border-gray-700 focus:ring-0"
                      />
                    </div>

                    <div class="mt-4">
                      <button
                        class="w-full inline-block px-4 py-2 border border-gray-400 text-gray-700 bg-white hover:bg-gray-700 hover:text-white rounded-md shadow-sm text-sm font-medium focus:outline-none"
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
            {#if roadmaplist && roadmaplist.length > 0}
              {#each roadmaplist as item}
                <div class="border collapse bg-white">
                  <input type="checkbox" class="peer" />
                  <div class="collapse-title bg-white peer-checked:bg-yellow-50">
                    <div class="rounded-lg dark:border-gray-800 flex-col text-center">
                      <div class="flex justify-between gap-2">
                        <h2 class="text-lg font-semibold ml-2">{formatTitle(item.title)}</h2>
                      </div>
                      <div class="flex items-center justify-between">
                        {#if item.hashtags}
                          <div class="flex">
                            {#each item.hashtags.split('@') as hashtag}
                              <div
                                class="flex text-amber-600 text-sm text-center items-center ml-2"
                              >
                                #{hashtag}
                              </div>
                            {/each}
                          </div>
                        {/if}
                      </div>
                    </div>
                  </div>
                  <div class="collapse-content bg-white peer-checked:bg-white mt-1">
                    <div class="flex justify-end">
                      <details class="dropdown dropdown-end">
                        <summary class="flex items-center cursor-pointer">
                          <i class="fa-regular fa-user mr-1"></i>
                          {item.owner?.nickname}
                        </summary>
                        <ul
                          class="p-2 shadow menu dropdown-content z-[1] bg-base-100 rounded-box w-52"
                        >
                          <li>
                            <a href="/course?tab=course&kwType=NAME&kw={item.owner?.nickname}"
                              >{item.owner?.nickname} 강좌</a
                            >
                          </li>
                          <li>
                            <a href="/course?tab=roadmap&kwType=NAME&kw={item.owner?.nickname}"
                              >{item.owner?.nickname} 로드맵</a
                            >
                          </li>
                        </ul>
                      </details>
                    </div>
                    <p class="text-lg text-gray-800 dark:text-gray-400 mx-2 font-semibold">개요</p>
                    <p class="text-sm text-gray-800 dark:text-gray-400 mx-2">
                      {removeMarkdown(item.overView)}
                    </p>
                    {#if item.curriculum}
                      <div class="flex flex-col">
                        {#each item.curriculum as curriculum, index}
                          <a href="/course/{curriculum.course.id}">
                            <div
                              class="mt-2 flex text-gray-800 text-lg font-semibold text-center items-center ml-2"
                            >
                              {index + 1}. {curriculum.course?.title}
                            </div>
                          </a>
                        {/each}
                      </div>
                    {/if}
                  </div>
                </div>
              {/each}
            {:else}
              <p class="p-2">등록된 로드맵이 없습니다.</p>
            {/if}
          </div>
          <div class="mt-2">
            <Pagination page={itemPage} />
          </div>
        </div>
      </div>
    {/if}
  {/await}
</div>
