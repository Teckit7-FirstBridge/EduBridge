<script lang="ts">
  import type { components } from '$lib/types/api/v1/schema';

  const { data } = $props<{ data: { post: components['schemas']['GetPostsResponseBody'] } }>();
  const { post } = data;
  const { items } = post;

  function closeDropdown() {
    document.getElementById('dropdownButton')!.tabIndex = -1;
    setTimeout(() => {
      document.getElementById('dropdownButton')!.tabIndex = 0;
    }, 100);
  }
</script>

<div class="flex flex-col w-full min-h-screen p-4 md:p-6">
  <h1 class="text-3xl font-bold mb-4">Q&amp;A</h1>
  <div class="flex items-center gap-4 mb-6">
    <input
      class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50 flex-grow"
      placeholder="Search questions..."
      type="search"
    />
    <div class="dropdown dropdown-end">
      <button
        tabindex="0"
        id="dropdownButton"
        class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 border border-input bg-background hover:bg-gray-100 hover:text-accent-foreground h-10 px-4 py-2"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
          class="w-4 h-4 mr-2"
        >
          <path d="m21 16-4 4-4-4"></path>
          <path d="M17 20V4"></path>
          <path d="m3 8 4-4 4 4"></path>
          <path d="M7 4v16"></path>
        </svg>
        Sort by
      </button>
      <ul tabindex="0" class="dropdown-content menu p-2 shadow bg-base-100 rounded-box w-52">
        <li><a on:click={closeDropdown}>날짜 내림차순</a></li>
        <li><a on:click={closeDropdown}>날짜 오름차순</a></li>
      </ul>
    </div>

    <button
      class="inline-flex border items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 text-primary-foreground hover:bg-gray-100 h-10 px-4 py-2"
      type="submit"
    >
      Search
    </button>

    <button
      class="inline-flex border items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 text-primary-foreground hover:bg-gray-100 h-10 px-4 py-2"
      type="button"
      on:click={() => {
        location.href = '/board/write';
      }}
    >
      Write
    </button>
  </div>
  <div class="space-y-4">
    {#if items}
      {#each items as item}
        <div class="rounded-lg border bg-card text-card-foreground shadow-sm" data-v0-t="card">
          <a href="/board/1">
            <div class="flex flex-col space-y-1.5 p-6">
              <h3 class="text-2xl font-semibold whitespace-nowrap leading-none tracking-tight">
                {item.title}
              </h3>
            </div>
            <div class="p-6">
              <p class="text-sm">{item.body}</p>
            </div>
            <div class="flex items-center p-6">
              <div class="flex items-center justify-between">
                <p class="text-sm text-gray-500 dark:text-gray-400">{item.authorName}</p>
              </div>
            </div>
          </a>
        </div>
      {/each}
    {/if}

    <div class="rounded-lg border bg-card text-card-foreground shadow-sm" data-v0-t="card">
      <a href="/board/1" rel="nofollow">
        <div class="flex flex-col space-y-1.5 p-6">
          <h3 class="text-2xl font-semibold whitespace-nowrap leading-none tracking-tight">제목</h3>
        </div>
        <div class="p-6">
          <p class="text-sm">간단한 내용</p>
        </div>
        <div class="flex items-center p-6">
          <div class="flex items-center justify-between">
            <p class="text-sm text-gray-500 dark:text-gray-400">글쓴이</p>
          </div>
        </div>
      </a>
    </div>

    <div class="rounded-lg border bg-card text-card-foreground shadow-sm" data-v0-t="card">
      <div class="flex flex-col space-y-1.5 p-6">
        <h3 class="text-2xl font-semibold whitespace-nowrap leading-none tracking-tight">
          How does gravity work?
        </h3>
      </div>
      <div class="p-6">
        <p class="text-sm">I'm curious about the concept of gravity. Can anyone explain?</p>
      </div>
      <div class="flex items-center p-6">
        <div class="flex items-center justify-between">
          <p class="text-sm text-gray-500 dark:text-gray-400">Posted by Jane Smith</p>
        </div>
      </div>
    </div>
    <div class="rounded-lg border bg-card text-card-foreground shadow-sm" data-v0-t="card">
      <div class="flex flex-col space-y-1.5 p-6">
        <h3 class="text-2xl font-semibold whitespace-nowrap leading-none tracking-tight">
          Why is the sky blue?
        </h3>
      </div>
      <div class="p-6">
        <p class="text-sm">I've heard different explanations. What's the real reason?</p>
      </div>
      <div class="flex items-center p-6">
        <div class="flex items-center justify-between">
          <p class="text-sm text-gray-500 dark:text-gray-400">Posted by Alice Johnson</p>
        </div>
      </div>
    </div>
  </div>
</div>
