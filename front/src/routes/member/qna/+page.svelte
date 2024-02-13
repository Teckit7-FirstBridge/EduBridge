<script lang="ts">
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';

  let qna: components['schemas']['QnaDto'][] = $state();

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');
    const isLoginResponse = await rq.apiEndPoints().GET(`/api/v1/members/isLogin`);
    const { isLogin } = isLoginResponse.data?.data!;
    if (!isLogin) {
      rq.msgWarning('로그인이 필요한 서비스 입니다');
      rq.goTo('/member/login');
    }

    const { data } = await rq.apiEndPoints().GET(`/api/v1/posts/qna`);

    qna = data!.data;

    return { qna };
  }
</script>

<div class="max-w-4xl mx-auto my-8">
  {#await load()}
    <p class="text-center">loading...</p>
  {:then { qna }}
    <div class="flex flex-col w-full min-h-screen p-4 md:p-6">
      <div class="space-y-4">
        <div>
          <div class="mb-5">
            <div class="mt-3 space-y-4">
              <div class="flex justify-between">
                <div>
                  <h2 class="text-2xl font-semibold text-blue-600">1대1 문의</h2>
                </div>
                <div class="flex justify-end">
                  <button
                    class="inline-flex border items-center justify-center whitespace-nowrap rounded-md text-sm font-bold ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 text-primary-foreground hover:bg-gray-100 h-10 px-4 py-2"
                    type="button"
                    on:click={() => {
                      location.href = '/member/qna/write';
                    }}
                  >
                    문의 등록
                  </button>
                </div>
              </div>
              {#if qna && qna.length > 0}
                {#each qna as item}
                  <div class="py-1">
                    <a href="/member/qna/{item.id}">
                      <div class="bg-white shadow overflow-hidden rounded-lg">
                        <div class="px-4 py-5 sm:p-6">
                          <div class="flex items-center justify-between">
                            <div class="ml-2 flex-shrink-0 flex">
                              <p class="text-xl text-gray-500 mr-2">{item.title}</p>
                            </div>
                            <div class="ml-2 flex-shrink-0 flex">
                              <p class="text-sm text-gray-500">
                                작성일: {`${new Date(item.createDate).getFullYear()}년 ${new Date(item.createDate).getMonth() + 1}월 ${new Date(item.createDate).getDate()}일`}
                              </p>
                            </div>
                          </div>
                          <div class="mt-5 flex justify-end">
                            <p class="text-sm text-gray-500 mr-2">작성자: {item.authorName}</p>
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
            </div>
          </div>
        </div>
      </div>
    </div>
  {/await}
</div>
