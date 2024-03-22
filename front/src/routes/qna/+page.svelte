<script lang="ts">
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import Pagination from '$lib/components/Pagination.svelte';

  let qna: components['schemas']['PageDtoQnaDto'][] = $state();

  function formatTitle(title) {
    return title.length > 8 ? `${title.substring(0, 8)}...` : title;
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

    const { data } = await rq.apiEndPoints().GET(`/api/v1/posts/qna`, {
      params: {
        query: {
          page: page_
        }
      }
    });

    qna = data!.data.itemPage?.content;

    return data!;
  }
</script>

<div class="max-w-4xl mx-auto my-8">
  {#await load()}
    <p class="text-center">loading...</p>
  {:then { data: { itemPage } }}
    <div class="flex flex-col w-full max-w-screen min-h-screen px-4 md:px-6">
      <div class="space-y-4">
        <div>
          <div class="mb-5">
            <div class=" space-y-4">
              <div class="flex justify-between">
                <div>
                  <h2 class="text-2xl font-semibold text-blue-600">1대1 문의</h2>
                </div>
                <div class="flex justify-end">
                  <button
                    class="inline-flex border items-center justify-center whitespace-nowrap rounded-md text-sm font-bold ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 text-primary-foreground hover:bg-gray-100 h-10 px-4 py-2"
                    type="button"
                    on:click={() => {
                      location.href = '/qna/write';
                    }}
                  >
                    문의 등록
                  </button>
                </div>
              </div>
              {#if qna && qna.length > 0}
                {#each qna as item}
                  <div class="py-1">
                    <a href="/qna/{item.id}">
                      <div class="bg-white shadow overflow-hidden rounded-lg">
                        <div class="px-4 py-5 sm:p-6">
                          <div class="flex items-center justify-between">
                            <div class="ml-2 flex-shrink-0 flex">
                              <p class="text-xl text-gray-500 mr-2">{formatTitle(item.title)}</p>
                              <div
                                class={`inline-flex px-2 py-1 mt-1 text-xs font-semibold rounded-full ${item.commentCount == 0 ? 'bg-red-100 text-red-800' : 'bg-blue-100 text-blue-800'}`}
                              >
                                {item.commentCount == 0 ? '미완료' : '답변 완료'}
                              </div>
                            </div>
                            <div class="ml-2 flex-shrink-0 flex">
                              <p class="text-sm text-gray-500"></p>
                            </div>
                          </div>
                          <div class="mt-5 flex justify-end">
                            <p class="text-sm text-gray-500 mr-2">
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
                      </div>
                    </a>
                  </div>
                {/each}
              {:else}
                <div class="py-10">
                  <p>등록된 1대1 문의가 없습니다.</p>
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
  {/await}
</div>
