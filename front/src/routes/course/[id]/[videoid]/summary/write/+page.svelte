<script lang="ts">
  import '@toast-ui/editor/dist/toastui-editor.css';
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';

  let content: string = $state('');
  let videos: components['schemas']['VideoDto'][] = $state();

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');
    const responseVideos = await rq.apiEndPoints().GET(`/api/v1/courses/{courseId}/videos`, {
      params: {
        path: {
          courseId: parseInt($page.params.id)
        }
      }
    });
    videos = responseVideos.data?.data!;

    return { videos };
  }
  const submitForm = async () => {
    const { data, error } = await rq.apiEndPoints().POST(`/api/v1/courses/{videoId}/note`, {
      params: { path: { videoId: parseInt($page.params.videoid) } },
      body: {
        content
      }
    });
    if (data) {
      rq.goTo(`/course/${$page.params.id}/${$page.params.videoid}/summary/${data.data.id}`);
    }
  };
</script>

{#await load()}
  <h1>loading...</h1>
{:then { videos }}
  <div class="grid min-h-screen w-full lg:grid-cols-[280px_1fr]">
    <div class="hidden w-64 border-r bg-gray-100/40 lg:block dark:bg-gray-800/40">
      <div class="flex h-full max-h-screen flex-col gap-2">
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
            <a
              class="flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors hover:bg-gray-100 hover:text-gray-900 dark:hover:bg-gray-800 dark:hover:text-gray-50"
              href="#"
            >
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
                  d="m2.25 12 8.954-8.955c.44-.439 1.152-.439 1.591 0L21.75 12M4.5 9.75v10.125c0 .621.504 1.125 1.125 1.125H9.75v-4.875c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125V21h4.125c.621 0 1.125-.504 1.125-1.125V9.75M8.25 21h8.25"
                />
              </svg>

              DashBoard
            </a>
          </li>

          <li>
            <a
              class="flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors hover:bg-gray-100 hover:text-gray-900 dark:hover:bg-gray-800 dark:hover:text-gray-50"
              href="#"
            >
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

              공지사항
            </a>
          </li>
          <li>
            <a
              class="flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors hover:bg-gray-100 hover:text-gray-900 dark:hover:bg-gray-800 dark:hover:text-gray-50"
              href="#"
            >
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
                  d="M12 6.042A8.967 8.967 0 0 0 6 3.75c-1.052 0-2.062.18-3 .512v14.25A8.987 8.987 0 0 1 6 18c2.305 0 4.408.867 6 2.292m0-14.25a8.966 8.966 0 0 1 6-2.292c1.052 0 2.062.18 3 .512v14.25A8.987 8.987 0 0 0 18 18a8.967 8.967 0 0 0-6 2.292m0-14.25v14.25"
                />
              </svg>

              요약 노트
            </a>
          </li>
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
                강의 목록</summary
              >
              <ul>
                {#each videos as video, index}
                  <li><a href={video.url}>강의 {index + 1}</a></li>
                {/each}
              </ul>
            </details>
          </li>
        </ul>
      </div>
    </div>
    <div class="flex flex-col w-full h-80 mt-10">
      {#each videos as video}
        {#if video.id == parseInt($page.params.videoid)}
          <div class="flex items-center text-center">
            <h2 class="text-lg font-semibold text-gray-700 mb-2">Keywords:&nbsp&nbsp</h2>
            <h2 class="text-lg font-semibold text-gray-700 mb-2">{video.keywords}</h2>
          </div>
        {/if}
      {/each}

      <form
        on:submit|preventDefault={submitForm}
        class="flex flex-col w-full h-full p-4 shadow-md rounded-md bg-white dark:bg-gray-800"
      >
        <textarea
          bind:value={content}
          class="textarea textarea-bordered w-full flex-grow p-4 text-gray-700 dark:text-gray-300 dark:border-gray-600 dark:bg-gray-700 focus:ring focus:ring-opacity-50"
          placeholder="작성해주세요..."
        ></textarea>
        <button type="submit" class="btn btn-primary mt-4 self-end"> 저장 </button>
      </form>
    </div>
  </div>
{/await}
