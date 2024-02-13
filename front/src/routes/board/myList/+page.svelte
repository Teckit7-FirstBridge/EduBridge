<script lang="ts">
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';

  let posts: components['schemas']['PostDto'][] = $state();

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');
    const isLoginResponse = await rq.apiEndPoints().GET(`/api/v1/members/isLogin`);
    const { isLogin } = isLoginResponse.data?.data!;
    if (!isLogin) {
      rq.msgWarning('로그인이 필요한 서비스 입니다');
      rq.goTo('/member/login');
    }

    const { data } = await rq.apiEndPoints().GET(`/api/v1/posts/myList`);

    posts = data!.data;

    return { posts };
  }
</script>

<div class="max-w-4xl mx-auto my-8">
  {#await load()}
    <p class="text-center">loading...</p>
  {:then { posts }}
    <div class="flex flex-col w-full min-h-screen p-4 md:p-6">
      <div class="space-y-4">
        <div>
          <div class="mb-5">
            <div class="mt-3 space-y-4">
              <div class="flex justify-between">
                <div>
                  <h2 class="text-2xl font-semibold text-blue-600">내 글 목록</h2>
                </div>
                <div class="flex justify-end">
                  <button
                    class="inline-flex border items-center justify-center whitespace-nowrap rounded-md text-sm font-bold ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 text-primary-foreground hover:bg-gray-100 h-10 px-4 py-2"
                    type="button"
                    on:click={() => {
                      location.href = '/member/qna/write';
                    }}
                  >
                    write
                  </button>
                </div>
              </div>
              {#if posts && posts.length > 0}
                {#each posts as item}
                  <div
                    class="rounded-lg border bg-card text-card-foreground shadow-sm"
                    data-v0-t="card"
                  >
                    <a href="/board/{item.id}">
                      <div class="flex justify-between">
                        <div class="flex flex-col space-y-1.5 p-6">
                          <h3
                            class="text-2xl font-semibold whitespace-nowrap leading-none tracking-tight"
                          >
                            {item.title}
                          </h3>
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
              {:else}
                <div class="py-10">
                  <p>등록된 글이 없습니다.</p>
                </div>
              {/if}
            </div>
          </div>
        </div>
      </div>
    </div>
  {/await}
</div>
