<script lang="ts">
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import { page } from '$app/stores';

  let enrolls: components['schemas']['CourseEnrollDto'][];

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');
    const isLoginResponse = await rq.apiEndPoints().GET(`/api/v1/members/isLogin`);
    const { isLogin } = isLoginResponse.data?.data!;
    if (!isLogin) {
      rq.msgWarning('로그인이 필요한 서비스 입니다');
      rq.goTo('/member/login');
    }

    const { data } = await rq.apiEndPoints().GET('/api/v1/enroll', {
      params: {
        query: {}
      }
    });

    enrolls = data?.data.items;

    return { enrolls };
  }
</script>

{#await load()}
  <p>loading...</p>
{:then { enrolls }}
  <div class="flex">
    <div class="flex flex-col flex-1">
      <main class="flex flex-1 flex-col gap-4 p-4 md:gap-8 md:p-6">
        <h1 class="text-2xl font-bold my-4 text-center">{rq.member.name}의 강의실</h1>
        <div class="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {#each enrolls as enroll}
            <a href="/course/{enroll.courseId}">
              <div
                class="p-4 border border-gray-200 rounded-lg dark:border-gray-800 flex-col text-center"
              >
                <h2 class="text-lg font-semibold my-2">{enroll.title}</h2>
                <div class="flex justify-center my-2">
                  <img src={enroll.imgUrl} />
                </div>
              </div>
            </a>
          {/each}
        </div>
      </main>
    </div>
  </div>
{/await}
