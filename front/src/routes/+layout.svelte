<script lang="ts">
  import '$lib/app.css';
  import { page } from '$app/stores';
  import { untrack } from 'svelte';
  import rq from '$lib/rq/rq.svelte';

  const { children } = $props();
  rq.effect(async () => {
    untrack(() => {
      rq.initAuth();
    });
  });
</script>

<header class="navbar bg-base-100 shadow">
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
                ><i class="fa-solid fa-list-check"></i> 내 글</a
              >
            </li>
            {#if !rq.isAdmin()}
              <li>
                <a class="font-semi-bold" href="/member/qna"
                  ><i class="fa-solid fa-list-check"></i> 1대1 문의</a
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
    <div class="flex-1">
      <a href="/board" class="btn btn-ghost font-semi-bold">게 시 판</a>
      <a href="/course" class="btn btn-ghost font-semi-bold">강의</a>
    </div>
  </div>
  <div class="flex-1 flex justify-center">
    <a href="/" class="font-bold">EduBridge</a>
  </div>

  <div class="flex-1 justify-end">
    <div class="flex-none flex justify-col">
      <div>
        <button
          class="btn btn-ghost"
          onclick={() => {
        const searchFormModal = (document.querySelector('#searchFormModal') as HTMLDialogElement);
        const searchFormInputSearch = (document.querySelector('#searchFormModal input[type=search]') as HTMLDialogElement);

        searchFormModal.showModal();

        searchFormInputSearch.focus();
      }}
        >
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
              d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
            /></svg
          >
        </button>
      </div>
      {#if rq.isLogin()}
        <div>
          {#if !rq.isAdmin()}
            <a href="/member/course" class="btn btn-ghost font-semi-bold">내 강의실</a>
          {/if}
          {#if rq.isAdmin()}
            <a href="/adm" class="btn btn-ghost font-semi-bold">관 리 자</a>
          {/if}
        </div>
        <div>
          <a href="/member/{rq.member.id}" class="btn btn-ghost">
            <img
              class="inline-block rounded-circle"
              src={rq.member.profileImgUrl}
              width="30"
              alt=""
            />
            {rq.member.name}
          </a>
        </div>
      {/if}
    </div>
  </div>
  <dialog id="searchFormModal" class="modal">
    <div class="modal-box">
      <form method="dialog">
        <button class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
      </form>

      <form
        action="/course"
        class="bg-base rounded flex flex-col gap-6"
        onsubmit={() => {
        const searchFormModal = (document.querySelector('#searchFormModal') as HTMLDialogElement);
        searchFormModal.close();
      }}
      >
        <div class="form-control">
          <label class="label">
            <span class="label-text">검색필터</span>
          </label>

          <select
            name="kwType"
            class="select select-bordered"
            value={$page.url.searchParams.get('kwType') ?? 'ALL'}
          >
            <option value="ALL">전체</option>
            <option value="TITLE">제목</option>
            <option value="NAME">작성자</option>
          </select>
        </div>

        <div class="form-control">
          <label class="label">
            <span class="label-text">검색어</span>
          </label>

          <input
            placeholder="검색어"
            class="input input-bordered"
            name="kw"
            type="search"
            value={$page.url.searchParams.get('kw') ?? ''}
            autocomplete="off"
          />
        </div>

        <div>
          <button class="btn btn-block btn-primary gap-1">
            <i class="fa-solid fa-magnifying-glass"></i>
            <span>검색</span>
          </button>
        </div>
      </form>
    </div>

    <form method="dialog" class="modal-backdrop">
      <button>close</button>
    </form>
  </dialog>
</header>

<main>{@render children()}</main>
<!--
// v0 by Vercel.
// https://v0.dev/t/6BHvI4qIs6U
-->
<footer class=" bottom-0 w-full bg-gray-900 text-white py-4">
  <div class="container mx-auto px-4 flex justify-around items-center">
    <a class="flex items-center gap-2" href="https://github.com/harriet221" rel="ugc"
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
        class="h-6 w-6 text-white"
        ><path
          d="M15 22v-4a4.8 4.8 0 0 0-1-3.5c3 0 6-2 6-5.5.08-1.25-.27-2.48-1-3.5.28-1.15.28-2.35 0-3.5 0 0-1 0-3 1.5-2.64-.5-5.36-.5-8 0C6 2 5 2 5 2c-.3 1.15-.3 2.35 0 3.5A5.403 5.403 0 0 0 4 9c0 3.5 3 5.5 6 5.5-.39.49-.68 1.05-.85 1.65-.17.6-.22 1.23-.15 1.85v4"
        ></path><path d="M9 18c-4.51 2-5-2-7-2"></path></svg
      ><span>harriet221</span></a
    ><a class="flex items-center gap-2" href="https://github.com/potato0221" rel="ugc"
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
        class="h-6 w-6 text-white"
        ><path
          d="M15 22v-4a4.8 4.8 0 0 0-1-3.5c3 0 6-2 6-5.5.08-1.25-.27-2.48-1-3.5.28-1.15.28-2.35 0-3.5 0 0-1 0-3 1.5-2.64-.5-5.36-.5-8 0C6 2 5 2 5 2c-.3 1.15-.3 2.35 0 3.5A5.403 5.403 0 0 0 4 9c0 3.5 3 5.5 6 5.5-.39.49-.68 1.05-.85 1.65-.17.6-.22 1.23-.15 1.85v4"
        ></path><path d="M9 18c-4.51 2-5-2-7-2"></path></svg
      ><span>potato0221</span></a
    ><a class="flex items-center gap-2" href="https://github.com/onk108" rel="ugc"
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
        class="h-6 w-6 text-white"
        ><path
          d="M15 22v-4a4.8 4.8 0 0 0-1-3.5c3 0 6-2 6-5.5.08-1.25-.27-2.48-1-3.5.28-1.15.28-2.35 0-3.5 0 0-1 0-3 1.5-2.64-.5-5.36-.5-8 0C6 2 5 2 5 2c-.3 1.15-.3 2.35 0 3.5A5.403 5.403 0 0 0 4 9c0 3.5 3 5.5 6 5.5-.39.49-.68 1.05-.85 1.65-.17.6-.22 1.23-.15 1.85v4"
        ></path><path d="M9 18c-4.51 2-5-2-7-2"></path></svg
      ><span>onk108</span></a
    ><a class="flex items-center gap-2" href="https://github.com/chanw12" rel="ugc"
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
        class="h-6 w-6 text-white"
        ><path
          d="M15 22v-4a4.8 4.8 0 0 0-1-3.5c3 0 6-2 6-5.5.08-1.25-.27-2.48-1-3.5.28-1.15.28-2.35 0-3.5 0 0-1 0-3 1.5-2.64-.5-5.36-.5-8 0C6 2 5 2 5 2c-.3 1.15-.3 2.35 0 3.5A5.403 5.403 0 0 0 4 9c0 3.5 3 5.5 6 5.5-.39.49-.68 1.05-.85 1.65-.17.6-.22 1.23-.15 1.85v4"
        ></path><path d="M9 18c-4.51 2-5-2-7-2"></path></svg
      ><span>chanw12</span></a
    ><a class="flex items-center gap-2" href="https://github.com/sleo113" rel="ugc"
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
        class="h-6 w-6 text-white"
        ><path
          d="M15 22v-4a4.8 4.8 0 0 0-1-3.5c3 0 6-2 6-5.5.08-1.25-.27-2.48-1-3.5.28-1.15.28-2.35 0-3.5 0 0-1 0-3 1.5-2.64-.5-5.36-.5-8 0C6 2 5 2 5 2c-.3 1.15-.3 2.35 0 3.5A5.403 5.403 0 0 0 4 9c0 3.5 3 5.5 6 5.5-.39.49-.68 1.05-.85 1.65-.17.6-.22 1.23-.15 1.85v4"
        ></path><path d="M9 18c-4.51 2-5-2-7-2"></path></svg
      ><span>sleo113</span></a
    ><a class="flex items-center gap-2" href="https://github.com/pakjongwook" rel="ugc"
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
        class="h-6 w-6 text-white"
        ><path
          d="M15 22v-4a4.8 4.8 0 0 0-1-3.5c3 0 6-2 6-5.5.08-1.25-.27-2.48-1-3.5.28-1.15.28-2.35 0-3.5 0 0-1 0-3 1.5-2.64-.5-5.36-.5-8 0C6 2 5 2 5 2c-.3 1.15-.3 2.35 0 3.5A5.403 5.403 0 0 0 4 9c0 3.5 3 5.5 6 5.5-.39.49-.68 1.05-.85 1.65-.17.6-.22 1.23-.15 1.85v4"
        ></path><path d="M9 18c-4.51 2-5-2-7-2"></path></svg
      ><span>pakjongwook</span></a
    >
  </div>
</footer>
