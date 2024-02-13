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
    <div class="flex flex-col w-64 border-r">
      <div class="flex items-center h-16 px-4 border-b border-gray-200 dark:border-gray-800">
        <a class="flex items-center gap-2 font-semibold" href="/"
          ><svg
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            class="h-6 w-6"
          >
            <path d="m8 3 4 8 5-5 5 15H2L8 3z"></path></svg
          ><span class="">EduBridge</span></a
        >
      </div>
      <ul class="menu w-56 rounded-box">
        <li>
          <details open>
            <summary
              ><svg
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
                  d="M8.25 6.75h12M8.25 12h12m-12 5.25h12M3.75 6.75h.007v.008H3.75V6.75Zm.375 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0ZM3.75 12h.007v.008H3.75V12Zm.375 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm-.375 5.25h.007v.008H3.75v-.008Zm.375 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Z"
                />
              </svg>
              수강중인 강좌</summary
            >
            <ul>
              {#each enrolls as enroll}
                <li><a href="/course/{enroll.courseId}">{enroll.title}</a></li>
              {/each}
            </ul>
          </details>
        </li>
      </ul>
    </div>
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
