<script lang="ts">
  import '$lib/app.css';
  import { page } from '$app/stores';
  import { untrack } from 'svelte';
  import rq from '$lib/rq/rq.svelte';

  let isMypage = $page.url.pathname.includes('/member/');
  let isCourse = $page.url.pathname.includes('/course');

  const { children } = $props();
  rq.effect(async () => {
    untrack(() => {
      rq.initAuth();
    });
  });
</script>

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

  <div class="flex-1 justify-end"></div>
</header>

<main>{@render children()}</main>
<!--
// v0 by Vercel.
// https://v0.dev/t/6BHvI4qIs6U
-->
<footer class=" bottom-0 w-full bg-white text-gray-300">
  <div class="container mx-auto flex justify-around items-center">
    <div class="flex flex-col items-center flex-1">
      <a href="/qna">
        <div>
          <div class="w-9 h-9 text-xl flex items-center justify-center">
            <i class="text-2xl fa-solid fa-circle-question"></i>
          </div>
        </div>
        <p>문의</p>
      </a>
    </div>

    <div class="flex flex-col items-center flex-1">
      <a href="/board">
        <div>
          <div class="w-9 h-9 text-xl flex items-center justify-center">
            <i class="text-2xl fa-solid fa-comments"></i>
          </div>
        </div>
        <p>질문</p>
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
          <div class="w-9 h-9 text-xl flex items-center justify-center">
            <i class="text-2xl ml-1 fa-solid fa-book-open"></i>
          </div>
        </div>
        <p>강의실</p>
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
          <a href="/member/{rq.member.id}">
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
