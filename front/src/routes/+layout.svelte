<script lang="ts">
  import '$lib/app.css';
  import { page } from '$app/stores';
  import { untrack } from 'svelte';
  import rq from '$lib/rq/rq.svelte';

  let isMypage = $state(false);
  let isCourse = $state(false);
  let isAlarm = $state(true);
  let isQna = $state(false);
  let isBoard = $state(false);
  let isRoom = $state(false);
  let exportNote = $page.url.pathname.includes('export');

  const { children } = $props();
  rq.effect(async () => {
    untrack(() => {
      rq.initAuth();
    });
    isMypage = $page.url.pathname.includes('/member') ? true : false;
    isCourse = $page.url.pathname.includes('/course') ? true : false;
    isQna = $page.url.pathname.includes('/qna') ? true : false;
    isQna = $page.url.pathname.includes('/qna') ? true : false;
    isBoard = $page.url.pathname.includes('/board') ? true : false;
    isRoom = $page.url.pathname.includes('/member/course') ? true : false;
    if (isRoom) {
      isMypage = false;
      isCourse = false;
    }

    const notificationResponse = await rq.apiEndPoints().GET(`/api/v1/notification/isAlarm`);
    isAlarm = notificationResponse.data?.data!;
    console.log(isAlarm);
    let sse = new EventSource(
      `${import.meta.env.VITE_CORE_API_BASE_URL}/api/notification/subscribe?id=${rq.member.id}`
    );
    sse.addEventListener('addComment', (e) => {
      rq.msgInfo('답변이 등록되었습니다.');
    });
    sse.addEventListener('addSummaryNotePoint', (e) => {
      rq.msgInfo('요약노트 포인트가 지급되었습니다.');
    });
    sse.addEventListener('addAttendPoint', (e) => {
      rq.msgInfo('출석 포인트가 지급되었습니다.');
    });
  });

  async function read() {
    console.log('hi');

    const { data, error } = await rq.apiEndPoints().PUT(`/api/v1/notification/read/{id}`, {
      params: { path: { id: rq.member.id } }
    });
  }
</script>

{#if !exportNote}
  <header class="navbar bg-gray-50 shadow">
    <div class="flex-1">
      <div class="flex-none">
        <div class="dropdown">
          <div tabindex="0" role="button" class="btn btn-ghost btn-circle">
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
                d="M4 6h16M4 12h16M4 18h7"
              /></svg
            >
          </div>
          <ul
            tabindex="0"
            class="menu menu-sm dropdown-content mt-3 z-[1] p-2 shadow bg-base-100 rounded-box w-52"
          >
            {#if rq.isAdmin()}
              <li>
                <a href="/adm" class="font-semi-bold"
                  ><i class="fa-solid fa-right-to-bracket"></i> 관리자</a
                >
              </li>
            {/if}
            {#if rq.isLogout()}
              <li>
                <a class="font-semi-bold" href="/member/login"
                  ><i class="fa-solid fa-right-to-bracket"></i> 로그인</a
                >
              </li>
            {/if}
            {#if rq.isLogin()}
              <li>
                <a class="font-semi-bold" href="/board/myList"
                  ><i class="fa-solid fa-list-check"></i> 내 Q&A</a
                >
              </li>
              {#if !rq.isAdmin()}
                <li>
                  <a class="font-semi-bold" href="/qna"
                    ><i class="fa-regular fa-circle-question"></i> 1대1 문의</a
                  >
                </li>
              {/if}
              <li class="font-semi-bold">
                <button on:click={() => rq.logout()}>
                  <i class="fa-solid fa-right-from-bracket"></i> 로그아웃
                </button>
              </li>
            {/if}
          </ul>
        </div>
      </div>
      <div class="flex-1"></div>
    </div>
    <div class="flex-1 flex justify-center">
      <a href="/" class="font-bold">EduBridge</a>
    </div>

    <div class="flex-1 justify-end mr-4">
      <div class="flex gap-x-4 relative items-center">
        <button
          on:click={() => {
            rq.goTo(`/member/mypage/alarm`);
            read();
          }}
        >
          {#if isAlarm}
            <span class="relative flex h-2 w-2">
              <span
                class=" absolute top-2 left-3 animate-ping absolute inline-flex h-full w-full rounded-full bg-sky-400 opacity-75"
              ></span>
              <span
                class="absolute top-2 left-3 relative inline-flex rounded-full h-2 w-2 bg-sky-500"
              ></span>
            </span>
          {/if}
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
        </button>
      </div>
    </div>
  </header>

  <main>{@render children()}</main>

  <footer class=" bottom-0 w-full bg-white text-gray-300">
    <div class="container mx-auto flex justify-around items-center">
      <div class="flex flex-col items-center flex-1">
        <a href="/qna">
          <div>
            <div
              class={isQna
                ? 'w-9 h-9 text-xl flex items-center justify-center text-blue-600'
                : 'w-9 h-9 text-xl flex items-center justify-center text-gray-300'}
            >
              <i class="text-2xl fa-solid fa-circle-question"></i>
            </div>
          </div>
          <p class={isQna ? 'text-blue-600' : 'text-gray-300'}>문의</p>
        </a>
      </div>

      <div class="flex flex-col items-center flex-1">
        <a href="/board">
          <div>
            <div
              class={isBoard
                ? 'w-9 h-9 text-xl flex items-center justify-center text-blue-600'
                : 'w-9 h-9 text-xl flex items-center justify-center text-gray-300'}
            >
              <i class="text-2xl fa-solid fa-comments"></i>
            </div>
          </div>
          <p class={isBoard ? 'text-blue-600' : 'text-gray-300'}>질문</p>
        </a>
      </div>
      <div class="flex flex-col items-center flex-1">
        <a href="/course">
          <div>
            <div
              class={isCourse
                ? 'w-9 h-9 text-xl flex items-center justify-center text-blue-600'
                : 'w-9 h-9 text-xl flex items-center justify-center text-gray-300'}
            >
              <i class="text-2xl fa-solid fa-book"></i>
            </div>
          </div>
          <p class={isCourse ? 'text-blue-600' : 'text-gray-300'}>강좌</p>
        </a>
      </div>
      <div class="flex flex-col items-center flex-1">
        <a href="/member/course">
          <div>
            <div
              class={isRoom
                ? 'w-9 h-9 text-xl flex items-center justify-center text-blue-600'
                : 'w-9 h-9 text-xl flex items-center justify-center text-gray-300'}
            >
              <i class="text-2xl ml-1 fa-solid fa-book-open"></i>
            </div>
          </div>
          <p class={isRoom ? 'text-blue-600' : 'text-gray-300'}>강의실</p>
        </a>
      </div>
      {#if rq.isAdmin()}
        <div class="flex flex-col items-center flex-1">
          <a href="/adm">
            <div>
              <div class="w-9 h-9 text-xl flex items-center justify-center">
                <i class="text-3xl ml-1 fa-regular fa-circle-user"></i>
              </div>
            </div>
            <p>관리자</p>
          </a>
        </div>
      {:else}
        <div class="flex flex-col items-center flex-1">
          {#if rq.isLogin()}
            <a href="/member/{rq.member.uuid}">
              <div>
                <div
                  class={isMypage
                    ? 'w-9 h-9 text-xl flex items-center justify-center text-blue-600'
                    : 'w-9 h-9 text-xl flex items-center justify-center text-gray-300'}
                >
                  <i class="text-3xl ml-1 fa-regular fa-circle-user"></i>
                </div>
              </div>
              <p class={isMypage ? 'text-blue-600' : 'text-gray-300'}>내정보</p>
            </a>
          {:else}
            <a href="/member/login">
              <div>
                <div class="w-9 h-9 text-xl flex items-center justify-center">
                  <i class="text-3xl ml-1 fa-regular fa-circle-user"></i>
                </div>
              </div>
              <p>내정보</p>
            </a>
          {/if}
        </div>
      {/if}
    </div>
  </footer>
{:else}
  <main>{@render children()}</main>
{/if}
