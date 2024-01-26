<script lang="ts">
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import { page } from '$app/stores';

  let course: components['schemas']['CourseDto'] = $state();
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

    const responseCourse = await rq.apiEndPoints().GET(`/api/v1/courses/{course-id}`, {
      params: {
        path: {
          'course-id': parseInt($page.params.id)
        }
      }
    });
    course = responseCourse.data?.data!;
    return { videos, course };
  }

  async function deleteCourse() {
    const { data, error } = await rq.apiEndPoints().DELETE(`/api/v1/courses/{id}`, {
      params: { path: { id: parseInt($page.params.id) } }
    });
    if (data) {
      rq.msgInfo('강좌가 삭제되었습니다');
      rq.goTo('/course');
    } else if (error) {
      rq.msgError(error.msg);
    }
  }

  async function deleteVideo(videoId: number) {
    const { data, error } = await rq.apiEndPoints().DELETE(`/api/v1/admin/{courseId}/videos/{id}`, {
      params: { path: { courseId: parseInt($page.params.id), id: videoId } }
    });
    if (data) {
      rq.msgInfo('동영상이 삭제 되었습니다');
      window.location.reload();
    } else if (error) {
      rq.msgError(error.msg);
    }
  }
</script>

{#await load()}
  <div>loading...</div>
{:then { videos, course }}
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
    <div class="flex flex-col">
      <header class="flex h-14 lg:h-[60px] items-center gap-4 border-b px-6 dark:bg-gray-800/40">
        <a class="lg:hidden" href="#"
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
            <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z"></path>
            <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z"></path></svg
          ><span class="sr-only"> Home </span></a
        >
      </header>
      <main class="flex flex-1 flex-col gap-4 p-4 md:gap-8 md:p-6">
        <div class="flex items-center justify-between">
          <h1 class="font-semibold text-lg md:text-2xl">
            {course!.title}
          </h1>
          <!-- {#if rq.member.id == course.} -->
          <div class="mb-5 mx-2 items-center">
            <a href="/course/{$page.params.id}/edit" class="btn btn-sm">수정</a>
            <button on:click={deleteCourse} class="btn btn-sm">삭제</button>
          </div>
          <!-- {/if} -->
        </div>

        <div class="mb-4 bg-white p-4 rounded-lg shadow-md">
          <h2 class="text-md md:text-lg font-semibold">공지사항</h2>
          <p class="text-sm md:text-md mt-2">
            <!-- 강좌에 대한 간단한 설명 -->
            {course!.notice}
          </p>
        </div>
        <div class="mb-4 bg-white p-4 rounded-lg shadow-md">
          <h2 class="text-md md:text-lg font-semibold">강좌 설명</h2>
          <p class="text-sm md:text-md mt-2">
            <!-- 강좌에 대한 간단한 설명 -->
            {course!.overView}
          </p>
        </div>

        <div class="flex justify-end">
          <a class=" mx-10 btn w-24 text-center" href="/course/{$page.params.id}/videowrite"
            >강의 등록</a
          >
        </div>

        <div class="border shadow-sm rounded-lg">
          <div class="relative w-full overflow-auto">
            <table class="w-full caption-bottom text-sm">
              <thead class="[&amp;_tr]:border-b">
                <tr
                  class="border-b transition-colors hover:bg-muted/50 data-[state=selected]:bg-muted"
                >
                  <th
                    class="h-12 px-4 text-left align-middle font-medium text-muted-foreground [&amp;:has([role=checkbox])]:pr-0 max-w-[150px]"
                  >
                    동영상
                  </th>

                  <th
                    class="h-12 px-4 text-left align-middle font-medium text-muted-foreground [&amp;:has([role=checkbox])]:pr-0 hidden md:table-cell"
                  >
                    개요
                  </th>
                  <th
                    class="h-12 px-4 text-left align-middle font-medium text-muted-foreground [&amp;:has([role=checkbox])]:pr-0 hidden md:table-cell"
                  >
                  </th>

                  <th
                    class="h-12 px-4 text-left align-middle font-medium text-muted-foreground [&amp;:has([role=checkbox])]:pr-0 hidden md:table-cell"
                  >
                    요약 노트
                  </th>
                </tr>
              </thead>

              <tbody class="[&amp;_tr:last-child]:border-0">
                {#each videos as video, index}
                  <tr
                    class="border-b transition-colors hover:bg-muted/50 data-[state=selected]:bg-muted"
                  >
                    <td class="p-4 align-middle [&amp;:has([role=checkbox])]:pr-0 font-medium">
                      <img
                        alt="Video thumbnail"
                        class="aspect-square rounded-md object-cover mt-2"
                        height="64"
                        src="static/favicon.png"
                        width="64"
                      />
                    </td>
                    <td
                      class="p-4 align-middle [&amp;:has([role=checkbox])]:pr-0 hidden md:table-cell"
                    >
                      {video.overView}
                    </td>

                    <td
                      class="p-4 align-middle [&amp;:has([role=checkbox])]:pr-0 hidden md:table-cell"
                    >
                      <div class="mb-5 mx-2 items-center">
                        <a href="/course/{video.courseId}/videoedit/{video.id}" class="btn btn-sm"
                          >수정</a
                        >
                        <button on:click={() => deleteVideo(video.id)} class="btn btn-sm"
                          >삭제</button
                        >
                      </div>
                    </td>
                    <td
                      class="p-4 align-middle [&amp;:has([role=checkbox])]:pr-0 hidden md:table-cell"
                    >
                      <a
                        class="flex items-center gap-3 w-10 h-10 rounded-lg px-3 py-2 transition-colors hover:bg-gray-100 hover:text-gray-900 dark:hover:bg-gray-800 dark:hover:text-gray-50"
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
                            d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L6.832 19.82a4.5 4.5 0 0 1-1.897 1.13l-2.685.8.8-2.685a4.5 4.5 0 0 1 1.13-1.897L16.863 4.487Zm0 0L19.5 7.125"
                          />
                        </svg>
                      </a>
                    </td>
                  </tr>
                {/each}
              </tbody>
            </table>
          </div>
        </div>
      </main>
    </div>
  </div>
{/await}