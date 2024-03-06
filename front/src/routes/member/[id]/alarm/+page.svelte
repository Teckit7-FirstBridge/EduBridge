<script lang="ts">
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');
    console.log('hi');
    const { data } = await rq.apiEndPoints().GET('/api/v1/notification/get', {});
    const list: components['schemas']['NotificationDto'][] = data?.data.dtoList;
    return { list };
  }
</script>

{#await load()}
  <h2>loading...</h2>
{:then { list }}
  <div class="max-w-sm mx-auto bg-white">
    <ul class="divide-y">
      {#each list as li}
        {#if li.type == 'COMMENT'}
          <li class="flex p-4">
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
              <p class="text-sm">
                {li.sender} 님이 내 {li.post_title} 글에 댓글을 남겼습니다.
              </p>
            </div>
          </li>
        {:else}
          <li class="flex p-4">
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
              <p class="text-sm">
                {li.point} 포인트를 획득하였습니다.
              </p>
            </div>
          </li>
        {/if}
      {/each}
    </ul>
  </div>
{/await}
