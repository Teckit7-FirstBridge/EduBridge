<script lang="ts">
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import Pagination from '$lib/components/Pagination.svelte';

  let posts: components['schemas']['PageDtoPostDto'][] = $state();
  let comments: components['schemas']['PageDtoCommentDto'][] = $state();
  let selectedTab = $state('posts');

  function formatTitle(title) {
    return title.length > 8 ? `${title.substring(0, 8)}...` : title;
  }

  function formatComment(comment) {
    return comment.length > 10 ? `${comment.substring(0, 10)}...` : comment;
  }

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');
    const page_ = parseInt($page.url.searchParams.get('page') ?? '1');
    const isLoginResponse = await rq.apiEndPoints().GET(`/api/v1/members/isLogin`);
    const { isLogin } = isLoginResponse.data?.data!;
    if (!isLogin) {
      rq.msgWarning('로그인이 필요한 서비스 입니다');
      rq.goTo('/member/login');
    }

    if (selectedTab === 'posts') {
      const { data } = await rq.apiEndPoints().GET(`/api/v1/posts/myList`, {
        params: {
          query: {
            page: page_
          }
        }
      });
      posts = data!.data.itemPage?.content;
      return data!;
    } else {
      const { data } = await rq.apiEndPoints().GET(`/api/v1/comments/myList`, {
        params: {
          query: {
            page: page_
          }
        }
      });
      comments = data!.data.itemPage?.content;
      return data!;
    }
  }
</script>

<!-- 탭 추가 -->
<div class="tabs flex px-4 max-w-4xl mx-auto mt-8">
  <div class="mt-2">
    <button
      class={`text-2xl font-semibold mr-2 ${selectedTab === 'posts' ? 'text-blue-600' : 'text-black'}`}
      on:click={() => {
        selectedTab = 'posts';
        load();
      }}>내 질문</button
    >
  </div>
  <h1 class="text-2xl">|</h1>
  <div>
    <button
      class={`text-2xl font-semibold ml-2 ${selectedTab === 'comments' ? 'text-blue-600' : 'text-black'}`}
      on:click={() => {
        selectedTab = 'comments';
        load();
      }}>내 답변</button
    >
  </div>
</div>
<div class="max-w-4xl mx-auto mb-4">
  {#await load()}
    <p class="text-center">loading...</p>
  {:then { data: { itemPage } }}
    {#if selectedTab === 'posts'}
      <div class="flex flex-col w-full min-h-screen px-4 md:px-6">
        <div class="space-y-4">
          <div>
            <div class="mb-5">
              <div class="space-y-4">
                <div class="flex justify-end">
                  <button
                    class="inline-flex border items-center justify-center whitespace-nowrap rounded-md text-sm font-bold ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 text-primary-foreground hover:bg-gray-100 h-10 px-4 py-2"
                    type="button"
                    on:click={() => {
                      location.href = '/board/write';
                    }}
                  >
                    글 작성
                  </button>
                </div>

                {#if posts && posts.length > 0}
                  {#each posts as item}
                    <div
                      class="rounded-lg border bg-card text-card-foreground shadow-sm"
                      data-v0-t="card"
                    >
                      <a href="/board/{item.id}">
                        <div class="flex justify-between">
                          <div class="flex flex-col space-y-1.5 px-6 py-3">
                            <div class="flex">
                              <h3
                                class="mr-2 text-2xl font-semibold whitespace-nowrap leading-none tracking-tight"
                              >
                                {formatTitle(item.title)}
                              </h3>
                              <h3
                                class="text-lg font-semibold whitespace-nowrap leading-none tracking-tight"
                              >
                                ({item.commentCount})
                              </h3>
                            </div>
                          </div>
                        </div>
                        <div class="flex items-center justify-between px-6 py-3">
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
                            <p class="text-sm text-gray-500 dark:text-gray-400">
                              {item.authorName} ·
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
                        </div>
                      </a>
                    </div>
                  {/each}
                {:else}
                  <div class="py-10">
                    <p>등록된 글이 없습니다.</p>
                  </div>
                {/if}
                <div class="mt-4 flex justify-center">
                  <Pagination page={itemPage} />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    {:else}
      <div class="flex flex-col w-full min-h-screen px-4 md:px-6">
        <div class="space-y-4">
          <div>
            <div class="mb-5">
              <div class="space-y-4">
                <div class="flex justify-end">
                  <button
                    class="inline-flex border items-center justify-center whitespace-nowrap rounded-md text-sm font-bold ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 text-primary-foreground hover:bg-gray-100 h-10 px-4 py-2"
                    type="button"
                    on:click={() => {
                      location.href = '/board/write';
                    }}
                  >
                    글 작성
                  </button>
                </div>
                {#if posts && posts.length > 0}
                  {#each comments as item}
                    <div
                      class="rounded-lg border bg-card text-card-foreground shadow-sm"
                      data-v0-t="card"
                    >
                      <a href="/board/{item.postId}">
                        <div class="flex justify-between">
                          <div class="flex flex-col space-y-1.5 px-6 py-3">
                            <div class="flex flex-col">
                              <h3
                                class="mr-2 text-lg text-gray-500 font-semibold whitespace-nowrap leading-none tracking-tight"
                              >
                                {formatTitle(item.postTitle)}
                              </h3>
                              <h3
                                class="mr-2 mt-3 text-xl font-semibold whitespace-nowrap leading-none tracking-tight"
                              >
                                ↳ {formatComment(item.body)}
                              </h3>
                            </div>
                          </div>
                        </div>
                        <div class="flex items-center justify-between px-6 py-2">
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
                            <p class="text-sm text-gray-500 dark:text-gray-400">
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
                        </div>
                      </a>
                    </div>
                  {/each}
                {:else}
                  <div class="py-10">
                    <p>등록된 댓글이 없습니다.</p>
                  </div>
                {/if}
                <div class="mt-4 flex justify-center">
                  <Pagination page={itemPage} />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    {/if}
  {/await}
</div>
