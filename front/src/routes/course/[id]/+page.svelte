<script lang="ts">
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import { page } from '$app/stores';
  import ToastUiEditor from '$lib/components/ToastUiEditor.svelte';

  function goToVideo(videoUrl) {
    window.location.href = videoUrl;
  }

  let course: components['schemas']['CourseDto'] = $state();
  let videos: components['schemas']['VideoDto'][] = $state();
  let auth: components['schemas']['CourseAuthDto'] = $state();

  let overviewviewr: any | undefined = $state();
  let notiviewer: any | undefined = $state();

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');
    const { data } = await rq.apiEndPoints().GET(`/api/v1/members/isLogin`);
    const { isLogin } = data?.data!;
    if (!isLogin) {
      rq.msgWarning('로그인이 필요한 서비스 입니다');
      rq.goTo('/member/login');
    }
    const responseVideos = await rq.apiEndPoints().GET(`/api/v1/courses/{courseId}/videos`, {
      params: {
        path: {
          courseId: parseInt($page.params.id)
        }
      }
    });
    videos = responseVideos.data?.data!;

    const responseCourse = await rq.apiEndPoints().GET(`/api/v1/courses/{courseId}`, {
      params: {
        path: {
          courseId: parseInt($page.params.id)
        }
      }
    });
    course = responseCourse.data?.data!;

    const responseAuth = await rq.apiEndPoints().GET(`/api/v1/courses/{courseId}/auth`, {
      params: {
        path: {
          courseId: parseInt($page.params.id)
        }
      }
    });
    auth = responseAuth.data?.data!;

    return { videos, course, auth };
  }

  async function deleteCourse() {
    const isConfirmed = confirm('강좌를 삭제하시겠습니까?');

    if (isConfirmed) {
      const { data, error } = await rq.apiEndPoints().DELETE(`/api/v1/admin/courses/{id}`, {
        params: { path: { id: parseInt($page.params.id) } }
      });

      if (data) {
        rq.msgInfo('강좌가 삭제되었습니다');
        rq.goTo('/adm/course');
      } else if (error) {
        rq.msgError(error.msg);
      }
    }
  }

  async function startCourse() {
    const isConfirmed = confirm('강좌를 공개하시겠습니까?');

    if (isConfirmed) {
      const { data, error } = await rq.apiEndPoints().PUT(`/api/v1/admin/{courseId}/startOrStop`, {
        params: { path: { courseId: parseInt($page.params.id) } }
      });

      if (data) {
        rq.msgInfo('강좌가 공개되었습니다');
        window.location.reload();
      } else if (error) {
        rq.msgError(error.msg);
        window.location.reload();
      }
    }
  }
  async function stopCourse() {
    const isConfirmed = confirm('강좌를 비공개 하시겠습니까?');

    if (isConfirmed) {
      const { data, error } = await rq.apiEndPoints().PUT(`/api/v1/admin/{courseId}/startOrStop`, {
        params: { path: { courseId: parseInt($page.params.id) } }
      });

      if (data) {
        rq.msgInfo('강좌가 비공개되었습니다');
        window.location.reload();
      } else if (error) {
        rq.msgError(error.msg);
        window.location.reload();
      }
    }
  }

  async function enrollCourse() {
    const coursePrice = course.price;
    const memberPoint = rq.member.point;

    let confirmMessage = '수강등록을 하시겠습니까?';

    if (memberPoint < coursePrice) {
      confirmMessage = `수강등록을 위한 포인트가 부족합니다. (필요 포인트: ${coursePrice}, 현재 포인트: ${memberPoint})`;
    }

    const isConfirmed = confirm(confirmMessage);

    if (isConfirmed) {
      if (memberPoint < coursePrice) {
        rq.msgError('포인트가 부족하여 수강등록을 진행할 수 없습니다.');
        return;
      }

      const { data, error } = await rq.apiEndPoints().POST(`/api/v1/enroll/{courseId}`, {
        params: { path: { courseId: parseInt($page.params.id) } }
      });

      if (data) {
        rq.msgInfo('수강이 등록 되었습니다.');
        window.location.reload();
      } else if (error) {
        rq.msgError(error.msg);
      }
    }
  }

  async function deleteVideo(videoId: number) {
    const isConfirmed = confirm('동영상을 삭제하시겠습니까?');

    if (isConfirmed) {
      const { data, error } = await rq
        .apiEndPoints()
        .DELETE(`/api/v1/admin/{courseId}/videos/{id}`, {
          params: { path: { courseId: parseInt($page.params.id), id: videoId } }
        });

      if (data) {
        rq.msgInfo('동영상이 삭제 되었습니다');
        window.location.reload();
      } else if (error) {
        rq.msgError(error.msg);
      }
    }
  }

  async function clickLiked(item: components['schemas']['CourseDto']) {
    if (item.likedByCurrentUser) {
      const { data, error } = await rq.apiEndPoints().DELETE(`/api/v1/courses/{id}/like`, {
        params: { path: { id: item.id } }
      });
      if (data) {
        rq.msgInfo('좋아요 취소');
        window.location.reload();
      } else if (error) {
        rq.msgError(error.msg);
      }
    } else {
      const { data, error } = await rq.apiEndPoints().POST(`/api/v1/courses/{id}/like`, {
        params: { path: { id: item.id } }
      });
      if (data) {
        rq.msgInfo('좋아요!!');
        window.location.reload();
      } else if (error) {
        rq.msgError(error.msg);
      }
    }
  }
</script>

{#await load()}
  <div>loading...</div>
{:then { videos, course, auth }}
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
              href="/member/course"
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
              href="/course/{course.id}/notes"
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
                  {#if auth.enroll || rq.isAdmin()}
                    <li><a href={video.url}>강의 {index + 1}</a></li>
                  {/if}
                {/each}
              </ul>
            </details>
          </li>
        </ul>
      </div>
    </div>
    <div class="flex flex-col">
      <main class="flex flex-1 flex-col gap-4 p-4 md:gap-8 md:p-6">
        <div class="flex items-center justify-between">
          <h1 class="font-semibold text-lg md:text-2xl">
            <div class="flex">
              <div class="mx-4 mt-1">
                {course!.title}
              </div>
              <div class=" flex justify-end gap-2 mt-1" on:click={() => clickLiked(course)}>
                {#if course.likedByCurrentUser}
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 24 24"
                    width="24"
                    height="24"
                  >
                    <!-- 빨간색 채워진 하트 -->
                    <path
                      fill="red"
                      stroke="red"
                      stroke-width="1.5"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"
                    />
                  </svg>{:else}
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 24 24"
                    width="24"
                    height="24"
                  >
                    <!-- 빨간색 빈 하트 -->
                    <path
                      fill="none"
                      stroke="red"
                      stroke-width="1.5"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"
                    />
                  </svg>
                {/if}
              </div>
            </div>
          </h1>

          <div class="flex">
            {#if !auth.enroll && !rq.isAdmin()}
              <div class="flex">
                <div class="mt-2">
                  <p class="course-price">{course.price}원</p>
                </div>
                <button on:click={enrollCourse} class="enroll-button ml-2">수강 등록</button>
              </div>
            {/if}
          </div>
          {#if rq.isAdmin()}
            <div class="mb-5 mx-2 items-center">
              <a href="/course/{$page.params.id}/edit" class="btn btn-sm">수정</a>
              <button on:click={deleteCourse} class="btn btn-sm">삭제</button>
              {#if !course.confirm}
                <button on:click={startCourse} class="btn btn-sm">강좌 공개</button>
              {:else}
                <button on:click={stopCourse} class="btn btn-sm">강좌 비공개</button>
              {/if}
            </div>
          {/if}
        </div>

        <div class="mb-4 bg-white p-4 rounded-lg shadow-md">
          <h2 class="text-md md:text-lg font-semibold">공지사항</h2>

          <ToastUiEditor
            bind:this={notiviewer}
            body={course.notice}
            height={'calc(50dvh - 64px)'}
            viewer={true}
          ></ToastUiEditor>
        </div>
        <div class="mb-4 bg-white p-4 rounded-lg shadow-md">
          <h2 class="text-md md:text-lg font-semibold">강좌 설명</h2>
          <ToastUiEditor
            bind:this={overviewviewr}
            body={course!.overView}
            height={'calc(50dvh - 64px)'}
            viewer={true}
          ></ToastUiEditor>
        </div>

        {#if rq.isAdmin()}
          <div class="flex justify-end">
            <a class=" mx-10 btn w-24 text-center" href="/course/{$page.params.id}/videowrite"
              >강의 등록</a
            >
          </div>
        {/if}

        {#if auth.enroll || rq.isAdmin()}
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
                          class="rounded-md object-cover mt-2 w-60"
                          src={video.imgUrl}
                          on:click={() => window.open(video.url, '_blank')}
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
                        {#if rq.isAdmin()}
                          <div class="mb-5 mx-2 items-center">
                            <a
                              href="/course/{video.courseId}/videoedit/{video.id}"
                              class="btn btn-sm">수정</a
                            >
                            <button on:click={() => deleteVideo(video.id)} class="btn btn-sm"
                              >삭제</button
                            >
                          </div>
                        {/if}
                      </td>
                      <td
                        class="p-4 align-middle [&amp;:has([role=checkbox])]:pr-0 hidden md:table-cell"
                      >
                        {#if video.summaryNotes.length > 0}
                          <a
                            class="flex items-center gap-3 w-10 h-10 rounded-lg px-3 py-2 transition-colors hover:bg-gray-100 hover:text-gray-900 dark:hover:bg-gray-800 dark:hover:text-gray-50"
                            href="/course/{video.courseId}/{video.id}/summary/{video.summaryNotes[0]
                              .id}"
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
                                d="M21.75 17.25v-.228a4.5 4.5 0 0 0-.12-1.03l-2.268-9.64a3.375 3.375 0 0 0-3.285-2.602H7.923a3.375 3.375 0 0 0-3.285 2.602l-2.268 9.64a4.5 4.5 0 0 0-.12 1.03v.228m19.5 0a3 3 0 0 1-3 3H5.25a3 3 0 0 1-3-3m19.5 0a3 3 0 0 0-3-3H5.25a3 3 0 0 0-3 3m16.5 0h.008v.008h-.008v-.008Zm-3 0h.008v.008h-.008v-.008Z"
                              />
                            </svg>
                          </a>
                        {:else}
                          <a
                            class="flex items-center gap-3 w-10 h-10 rounded-lg px-3 py-2 transition-colors hover:bg-gray-100 hover:text-gray-900 dark:hover:bg-gray-800 dark:hover:text-gray-50"
                            href="/course/{video.courseId}/{video.id}/summary/write"
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
                        {/if}
                      </td>
                    </tr>
                  {/each}
                </tbody>
              </table>
            </div>
          </div>
        {:else}
          <p>수강중이 아닙니다</p>
        {/if}
      </main>
    </div>
  </div>
{/await}
