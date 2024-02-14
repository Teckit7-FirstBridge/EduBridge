<script lang="ts">
  import type { components } from '$lib/types/api/v1/schema';
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import Pagination from '$lib/components/Pagination.svelte';

  let posts: components['schemas']['PageDtoPostDto'][] = $state([]);

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');

    const kw = $page.url.searchParams.get('kw') ?? '';
    const kwType = ($page.url.searchParams.get('kwType') ?? 'ALL') as KwTypeV1;
    const page_ = parseInt($page.url.searchParams.get('page') ?? '1');

    const { data } = await rq.apiEndPoints().GET('/api/v1/posts', {
      params: {
        query: {
          kw,
          kwType,
          page: page_
        }
      }
    });

    posts = data!.data.itemPage?.content;

    return data!;
  }

  function sortPostsByCreateDate(ascending = true) {
    posts.sort((a, b) => {
      const dateA = new Date(a.createDate);
      const dateB = new Date(b.createDate);
      return ascending ? dateA - dateB : dateB - dateA;
    });
  }
  function sortPostsByVote(ascending = true) {
    posts.sort((a, b) => {
      const voteA = a.voteCount;
      const voteB = b.voteCount;

      return ascending ? voteA - voteB : voteB - voteA;
    });
  }
</script>

<div class="max-w-4xl mx-auto my-8">
  {#await load()}
    <p>loading...</p>
  {:then { data: { itemPage } }}
    <div class="flex flex-col w-full min-h-screen p-4 md:p-6">
      <h1 class="text-3xl font-bold mb-4">Q&amp;A</h1>

      <form action="/board" class="bg-base rounded flex flex-col gap-6">
        <div class="flex items-center gap-4 mb-6">
          <input
            class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50 flex-grow"
            placeholder="검색어"
            name="kw"
            type="search"
            value={$page.url.searchParams.get('kw') ?? ''}
            autocomplete="off"
          />
          <div class="dropdown">
            <div
              tabindex="0"
              id="dropdownButton"
              class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 border border-input bg-background hover:bg-gray-100 hover:text-accent-foreground h-10 px-4 py-2"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="24"
                height="24"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
                class="w-4 h-4 mr-2"
              >
                <path d="m21 16-4 4-4-4"></path>
                <path d="M17 20V4"></path>
                <path d="m3 8 4-4 4 4"></path>
                <path d="M7 4v16"></path>
              </svg>
              Sort by
            </div>
            <ul tabindex="0" class="dropdown-content menu p-2 shadow bg-base-100 rounded-box w-36">
              <li>
                <a
                  class="flex justify-between"
                  on:click={() => {
                    sortPostsByCreateDate(false);
                  }}
                  ><span>날짜 최신 순</span>
                </a>
              </li>
              <li>
                <a
                  class="flex justify-between"
                  on:click={() => {
                    sortPostsByCreateDate(true);
                  }}
                  ><span>날짜 오래된 순</span>
                </a>
              </li>
              <li>
                <a
                  class="flex justify-between"
                  on:click={() => {
                    sortPostsByVote(true);
                  }}
                >
                  <span>좋아요 적은 순</span>
                </a>
              </li>
              <li>
                <a
                  class="flex justify-between"
                  on:click={() => {
                    sortPostsByVote(false);
                  }}
                >
                  <span>좋아요 많은 순</span>
                </a>
              </li>
            </ul>
          </div>
          <select
            name="kwType"
            class="select select-bordered"
            value={$page.url.searchParams.get('kwType') ?? 'ALL'}
          >
            <option value="ALL">전체</option>
            <option value="TITLE">제목</option>
            <option value="BODY">내용</option>
            <option value="NAME">작성자</option>
          </select>

          <button
            class="inline-flex border items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 text-primary-foreground hover:bg-gray-100 h-10 px-4 py-2"
            type="submit"
          >
            Search
          </button>

          <button
            class="inline-flex border items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 text-primary-foreground hover:bg-gray-100 h-10 px-4 py-2"
            type="button"
            on:click={() => {
              location.href = '/board/write';
            }}
          >
            Write
          </button>
        </div>
      </form>

      <div class="space-y-4">
        {#if posts}
          {#each posts as item}
            <div class="rounded-lg border bg-card text-card-foreground shadow-sm" data-v0-t="card">
              <a href="/board/{item.id}">
                <div class="flex justify-between">
                  <div class="flex flex-col space-y-1.5 p-6">
                    <div class="flex">
                      <h3
                        class="mr-2 text-2xl font-semibold whitespace-nowrap leading-none tracking-tight"
                      >
                        {item.title}
                      </h3>
                      <h3
                        class="text-lg font-semibold whitespace-nowrap leading-none tracking-tight"
                      >
                        ({item.commentCount})
                      </h3>
                    </div>
                  </div>
                  <p class="text-sm space-y-1.5 p-6">
                    {(() => {
                      const now = new Date();
                      const postDate = new Date(item.createDate);
                      const seconds = Math.floor((now - postDate) / 1000);

                      let interval = seconds / 31536000;
                      if (interval > 1) {
                        return Math.floor(interval) + '년 전';
                      }
                      interval = seconds / 2592000;
                      if (interval > 1) {
                        return Math.floor(interval) + '개월 전';
                      }
                      interval = seconds / 86400;
                      if (interval > 1) {
                        return Math.floor(interval) + '일 전';
                      }
                      interval = seconds / 3600;
                      if (interval > 1) {
                        return Math.floor(interval) + '시간 전';
                      }
                      interval = seconds / 60;
                      if (interval > 1) {
                        return Math.floor(interval) + '분 전';
                      }
                      return Math.floor(seconds) + '초 전';
                    })()}
                  </p>
                </div>
                <div class="flex items-center justify-between p-6">
                  <div class="flex flex-colum">
                    <div class="mr-3">
                      <span>{item.voteCount}</span>
                    </div>
                    <div>
                      {#if item.likedByCurrentUser}
                        <svg
                          xmlns="http://www.w3.org/2000/svg"
                          fill="red"
                          viewBox="0 0 24 24"
                          stroke-width="1.5"
                          stroke="red"
                          class="w-6 h-6"
                        >
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"
                          />
                        </svg>
                      {:else}
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
                            d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"
                          />
                        </svg>
                      {/if}
                    </div>
                  </div>
                  <div class="flex items-center">
                    <p class="text-sm text-gray-500 dark:text-gray-400">{item.authorName}</p>
                  </div>
                </div>
              </a>
            </div>
          {/each}
        {/if}
        <Pagination page={itemPage} />
      </div>
    </div>
  {/await}
</div>
