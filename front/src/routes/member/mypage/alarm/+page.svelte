<script lang="ts">
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');
    const isLoginResponse = await rq.apiEndPoints().GET(`/api/v1/members/isLogin`);
    const { isLogin } = isLoginResponse.data?.data!;
    if (!isLogin) {
      rq.msgWarning('로그인이 필요한 서비스 입니다');
      rq.goTo('/member/login');
    }
    const { data } = await rq.apiEndPoints().GET('/api/v1/notification/get', {});
    const list: components['schemas']['NotificationDto'][] = data?.data.dtoList;

    return { list };
  }

  async function read(id) {}
</script>

{#await load()}
  <span class="loading loading-spinner loading-xs m-2"></span>
{:then { list }}
  <div class="max-w-sm mx-auto bg-white mt-2">
    <ul class="divide-y">
      {#if list.length == 0}
        <div class=" text-gray-400 text-center mt-20">알림이 없습니다...</div>
      {:else}
        {#each list as li}
          {#if li.type == 'COMMENT'}
            <li
              class="flex p-4 hover:bg-gray-100 cursor-pointer {li.read ? 'bg-gray-100' : ''}"
              on:click={() => {
                read(li.id);
                rq.goTo(`/board/${li.post_id}#comment__${li.comment_id}`);
              }}
            >
              <span class="relative flex h-10 w-10 shrink-0 overflow-hidden rounded-full mr-4">
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
                    d="M14.857 17.082a23.848 23.848 0 0 0 5.454-1.31A8.967 8.967 0 0 1 18 9.75V9A6 6 0 0 0 6 9v.75a8.967 8.967 0 0 1-2.312 6.022c1.733.64 3.56 1.085 5.455 1.31m5.714 0a24.255 24.255 0 0 1-5.714 0m5.714 0a3 3 0 1 1-5.714 0"
                  />
                </svg>
              </span>
              <div>
                <button class="text-sm">
                  {li.sender} 님이 내 {li.post_title} 글에 댓글을 남겼습니다.
                </button>
              </div>
            </li>
          {:else if li.type == 'QnA'}
            <li
              class="flex p-4 hover:bg-gray-100 cursor-pointer {li.read ? 'bg-gray-100' : ''}"
              on:click={() => {
                read();
                rq.goTo(`/qna/${li.post_id}`);
              }}
            >
              <span class="relative flex h-10 w-10 shrink-0 overflow-hidden rounded-full mr-4">
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
                    d="M9.879 7.519c1.171-1.025 3.071-1.025 4.242 0 1.172 1.025 1.172 2.687 0 3.712-.203.179-.43.326-.67.442-.745.361-1.45.999-1.45 1.827v.75M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Zm-9 5.25h.008v.008H12v-.008Z"
                  />
                </svg>
              </span>
              <div>
                <button class="text-sm">
                  {li.sender} 님이 내 {li.post_title}에 답변을 해주셨습니다.
                </button>
              </div>
            </li>
          {:else}
            <li class="flex p-4 hover:bg-gray-100 cursor-pointer {li.read ? 'bg-gray-100' : ''}">
              <span class="relative flex h-10 w-10 shrink-0 overflow-hidden rounded-full mr-4">
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
                    d="M20.25 6.375c0 2.278-3.694 4.125-8.25 4.125S3.75 8.653 3.75 6.375m16.5 0c0-2.278-3.694-4.125-8.25-4.125S3.75 4.097 3.75 6.375m16.5 0v11.25c0 2.278-3.694 4.125-8.25 4.125s-8.25-1.847-8.25-4.125V6.375m16.5 0v3.75m-16.5-3.75v3.75m16.5 0v3.75C20.25 16.153 16.556 18 12 18s-8.25-1.847-8.25-4.125v-3.75m16.5 0c0 2.278-3.694 4.125-8.25 4.125s-8.25-1.847-8.25-4.125"
                  />
                </svg>
              </span>
              <div>
                <p class="text-sm">
                  {li.point} 포인트를 획득하였습니다.
                </p>
              </div>
            </li>
          {/if}
        {/each}
      {/if}
    </ul>
  </div>
{/await}
