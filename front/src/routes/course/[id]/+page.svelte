<script lang="ts">
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import { page } from '$app/stores';
  import ToastUiEditor from '$lib/components/ToastUiEditor.svelte';
  import { writable } from 'svelte/store';

  let hashtags: string[] = $state([]);
  function goToVideo(videoUrl) {
    window.location.href = videoUrl;
  }

  let selectedOverView = writable('');
  let modalenroll;
  let modal;
  let modalRoadmap;
  let changeNum;
  let selectedRoadmap = '';

  function openModal(overView) {
    selectedOverView.set(overView);
    modal.showModal();
  }

  function handleOutsideClick(event) {
    if (event.target === modal) {
      modal.close();
    }
  }

  function openModalEnRoll() {
    modalenroll.showModal();
  }

  function openModalRoadmap() {
    modalRoadmap.showModal();
  }

  async function registerCourseToRoadmap() {
    const roadmapId = selectedRoadmap;

    const { data, error } = await rq
      .apiEndPoints()
      .PUT(`/api/v1/roadmap/roadmaps/{roadmapId}/{courseId}/{courseOrder}`, {
        params: {
          path: {
            courseId: parseInt($page.params.id),
            roadmapId: parseInt(roadmapId),
            courseOrder: changeNum
          }
        }
      });

    if (data) {
      rq.msgInfo('로드맵 설정 성공');
      modalRoadmap.close();
    } else {
      rq.msgError('로드맵 설정 실패');
    }
  }

  let isDrawerOpen = false;

  function toggleDrawer() {
    isDrawerOpen = !isDrawerOpen;
    console.log(isDrawerOpen);
  }

  let course: components['schemas']['CourseDto'] = $state();
  let videos = $state<components['schemas']['VideoDto'][]>([]);
  let auth: components['schemas']['CourseAuthDto'] = $state();
  let enroll: components['schemas']['AdminCourseEnrollDto'] = $state();
  let courseConfirm: Boolean = $state();
  let myRoadmap: components['schemas']['RoadmapDto'][] | undefined;
  let reportReason;
  let roadmapList: components['schemas']['RoadmapDto'][] | undefined;

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

    const responseRoadmap = await rq.apiEndPoints().GET(`/api/v1/roadmap/byCourse/{courseId}`, {
      params: {
        path: {
          courseId: parseInt($page.params.id)
        }
      }
    });
    roadmapList = responseRoadmap.data?.data!;

    const responseCourse = await rq.apiEndPoints().GET(`/api/v1/courses/{courseId}`, {
      params: {
        path: {
          courseId: parseInt($page.params.id)
        }
      }
    });

    course = responseCourse.data?.data!;
    courseConfirm = course.confirm!;
    hashtags = course.hashtags!.split('@');

    const responseEnroll = await rq
      .apiEndPoints()
      .GET(`/api/v1/courses/{courseId}/enroll/{writerId}`, {
        params: {
          path: {
            courseId: parseInt($page.params.id),
            writerId: course.writer?.id
          }
        }
      });
    enroll = responseEnroll.data?.data!;

    const responseAuth = await rq.apiEndPoints().GET(`/api/v1/courses/{courseId}/auth`, {
      params: {
        path: {
          courseId: parseInt($page.params.id)
        }
      }
    });
    auth = responseAuth.data?.data!;

    const responseMyRoadmap = await rq.apiEndPoints().GET(`/api/v1/roadmap/myRoadmap`, {
      params: {}
    });
    myRoadmap = responseMyRoadmap.data?.data!;

    changeNum = course.roadmapNum;

    return { videos, course, auth, enroll, hashtags, myRoadmap, roadmapList };
  }

  let modalreport;

  function openModalReport() {
    modalreport.showModal();
  }

  async function reportPost() {
    if (rq.isLogout()) {
      rq.msgError('로그인 후 이용 해 주세요');
      rq.goTo('/member/login');
    }

    if (reportReason) {
      const { data, error } = await rq.apiEndPoints().POST(`/api/v1/report/course/{courseId}`, {
        params: { path: { courseId: parseInt($page.params.id) } },
        body: {
          reportReason: reportReason,
          materialId: parseInt($page.params.id)
        }
      });

      if (data) {
        rq.msgInfo('신고 되었습니다.');
        modalreport.close();
      }
    } else {
      rq.msgWarning('신고 사유를 선택 해 주세요');
    }
  }

  async function deleteCourse() {
    const isConfirmed = confirm('강좌를 삭제하시겠습니까?');

    if (isConfirmed) {
      const { data, error } = await rq.apiEndPoints().DELETE(`/api/v1/courses/{id}/{writer_id}`, {
        params: { path: { id: parseInt($page.params.id), writer_id: course.writer.id } }
      });

      if (data) {
        rq.msgInfo('강좌가 삭제되었습니다');
        rq.goTo('/course');
      } else if (error) {
        rq.msgError(error.msg);
      }
    }
  }

  async function startCourse() {
    if (course.videoCount! <= 5) {
      rq.msgWarning('영상이 5개 이하이면 공개할 수 없습니다');
    } else {
      const isConfirmed = confirm('강좌를 공개하시겠습니까?');

      if (isConfirmed) {
        const { data, error } = await rq
          .apiEndPoints()
          .PUT(`/api/v1/courses/{courseId}/startOrStop/{writer_id}`, {
            params: { path: { courseId: parseInt($page.params.id), writer_id: course.writer.id! } }
          });

        if (data) {
          rq.msgInfo('강좌가 공개되었습니다');
          courseConfirm = true;
          // window.location.reload();
        } else if (error) {
          rq.msgError('영상이 5개 이하이면 공개할 수 없습니다');
        }
      }
    }
  }
  async function stopCourse() {
    const isConfirmed = confirm('강좌를 비공개 하시겠습니까?');

    if (isConfirmed) {
      const { data, error } = await rq
        .apiEndPoints()
        .PUT(`/api/v1/courses/{courseId}/startOrStop/{writer_id}`, {
          params: { path: { courseId: parseInt($page.params.id), writer_id: course.writer.id! } }
        });

      if (data) {
        rq.msgInfo('강좌가 비공개되었습니다');
        courseConfirm = false;
        // window.location.reload();
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

  async function deleteVideo(video: components['schemas']['VideoDto']) {
    const isConfirmed = confirm('동영상을 삭제하시겠습니까?');

    if (isConfirmed) {
      const { data, error } = await rq
        .apiEndPoints()
        .DELETE(`/api/v1/courses/{courseId}/videos/{id}/{writer_id}`, {
          params: {
            path: {
              courseId: parseInt($page.params.id),
              id: video.id,
              writer_id: course.writer.id!
            }
          }
        });

      if (data) {
        rq.msgInfo('동영상이 삭제 되었습니다');
        videos.splice(videos.indexOf(video), 1);
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
{:then { videos, course, auth, enroll, hashtags, myRoadmap, roadmapList }}
  <div class="flex w-full justify-center items-center">
    <div class="flex flex-col">
      <main class="flex flex-1 flex-col gap-4 p-4 md:gap-8 md:p-6">
        <div class="flex items-center justify-between">
          <h1 class="font-semibold text-lg md:text-2xl">
            <div class="flex text-center items-center">
              <div class="mx-2 mt-1">
                {course!.title}
              </div>
              <div class=" flex justify-end gap-2 mt-1 ml-2" on:click={() => clickLiked(course)}>
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
              <div class="mb-2">
                <a onclick={openModalReport} class=""
                  ><svg
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke-width="1.5"
                    stroke="black"
                    class="w-7 h-7 mt-3 ml-4"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126ZM12 15.75h.007v.008H12v-.008Z"
                    />
                  </svg>
                </a>
                <dialog id="my_modal_3" class="modal" bind:this={modalreport}>
                  <div class="modal-box">
                    <button
                      class="mb-2 btn btn-sm btn-circle btn-ghost absolute right-2 top-2 focus:outline-none"
                      on:click={() => modalreport.close()}>✕</button
                    >
                    <div class="flex flex-col p-6 bg-white shadow rounded-lg">
                      <h2 class="text-xl font-semibold mb-4 pb-2">신고 사유 입력</h2>
                      <form>
                        <input bind:value={reportReason} class="border rounded-md shadow-sm mb-2" />

                        <div class="flex justify-end mt-4">
                          <button
                            on:click={reportPost}
                            class="inline-block px-4 py-2 border border-gray-300 text-gray-700 bg-white hover:bg-black hover:text-white rounded-md shadow-sm text-sm font-medium focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                          >
                            제출
                          </button>
                        </div>
                      </form>
                    </div>
                  </div>
                </dialog>
              </div>
            </div>
          </h1>
        </div>
        <div class="flex">
          <details class="dropdown">
            <summary class="flex items-center cursor-pointer">
              <i class="fa-regular fa-user mr-1"></i>
              {course.writer?.nickname}
            </summary>
            <ul class="p-2 shadow menu dropdown-content z-[1] bg-base-100 rounded-box w-52">
              <li>
                <a href="/course?tab=course&kwType=NAME&kw={course.writer?.nickname}"
                  >{course.writer?.nickname} 강좌</a
                >
              </li>
              <li>
                <a href="/course?tab=roadmap&kwType=NAME&kw={course.writer?.nickname}"
                  >{course.writer?.nickname} 로드맵</a
                >
              </li>
            </ul>
          </details>
        </div>
        <div class="flex">
          {#each hashtags as hashtag}
            <div class="">
              <div class="flex text-amber-600 text-sm text-center items-center ml-2">
                #{hashtag}
              </div>
            </div>
          {/each}
        </div>
        <div class="flex justify-end">
          {#if !auth.enroll && !(rq.member.id == course.writer.id) && !rq.isAdmin()}
            <div class="flex">
              <div class="mt-2">
                <p class="course-price mt-4">{course.price}원</p>
              </div>
              <button
                on:click={enrollCourse}
                class="ml-2 btn border border-blue-700 text-gray-800 bg-white hover:bg-blue-700 hover:border-blue-700 hover:text-white active:bg-blue-700 active:text-white active:border-blue-700 px-4 py-2 rounded transition ease-in duration-200 text-center text-base font-semibold shadow-md"
                >수강 등록</button
              >
            </div>
          {/if}
        </div>
        {#if rq.member.id === course.writer.id || rq.isAdmin()}
          <div class="mx-2 items-center">
            <div class="mb-2">
              <a href="/course/{$page.params.id}/edit" class="btn btn-sm">수정</a>
              <button on:click={deleteCourse} class="btn btn-sm">삭제</button>
            </div>
            <div>
              {#if !courseConfirm}
                <button on:click={startCourse} class="btn btn-sm">강좌 공개</button>
              {:else}
                <button on:click={stopCourse} class="btn btn-sm">강좌 비공개</button>
                <button onclick={openModalEnRoll} class="btn btn-sm">수강생 목록</button>
                <dialog id="my_modal_3" class="modal" bind:this={modalenroll}>
                  <div class="modal-box">
                    <form method="dialog">
                      <button class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2"
                        >✕</button
                      >
                    </form>
                    <div class="flex flex-col p-6 bg-white shadow rounded-lg">
                      <h2 class="text-xl font-semibold mb-4 border-b pb-2">수강생 목록</h2>
                      {#each enroll as enroll}
                        <div
                          class="py-2 px-4 bg-gray-100 rounded-md mb-2 shadow-sm hover:bg-gray-200 transition-colors"
                        >
                          {enroll.name}
                        </div>
                      {/each}
                    </div>
                  </div>
                </dialog>
              {/if}
              <button onclick={openModalRoadmap} class="btn btn-sm">로드맵</button>
              <dialog id="my_modal_3" class="modal" bind:this={modalRoadmap}>
                <div class="modal-box">
                  <button
                    class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2"
                    on:click={() => modalRoadmap.close()}>✕</button
                  >
                  <div class="flex flex-col p-6 bg-white shadow rounded-lg">
                    <h2 class="text-xl font-semibold mb-4 pb-2">로드맵 등록</h2>
                    <select
                      bind:value={selectedRoadmap}
                      class="select select-bordered w-full mb-4 focus:outline-none focus:border-gray-700"
                    >
                      <option value="">로드맵을 선택하세요</option>
                      {#each myRoadmap as roadmap}
                        <option value={roadmap.id}>{roadmap.title}</option>
                      {/each}
                    </select>
                    <input
                      type="number"
                      bind:value={changeNum}
                      placeholder="로드맵 순서"
                      class="input input-bordered focus:outline-none focus:border-gray-700 w-full mb-4"
                    />
                    <button
                      on:click={registerCourseToRoadmap}
                      class="btn border border-gray-500 text-gray-800 bg-white hover:bg-gray-700 hover:border-gray-700 hover:text-white active:bg-gray-700 active:text-white active:border-gray-700 px-4 py-2 rounded transition ease-in duration-200 text-center text-base font-semibold shadow-md"
                      >등록</button
                    >
                  </div>
                </div>
              </dialog>
            </div>
          </div>
        {:else}
          <div class="flex justify-end">
            <details class="dropdown dropdown-end">
              <summary class="btn">로드맵</summary>
              {#if roadmapList && roadmapList.length > 0}
                <ul class="p-2 shadow menu dropdown-content z-[1] bg-base-100 rounded-box w-52">
                  {#each roadmapList as roadmap}
                    <a href="/course?tab=roadmap&kwType=ALL&kw={roadmap.title}">
                      <li class="p-2">
                        <div>
                          <i class="fa-solid fa-flag"></i>
                          {roadmap.title}
                        </div>
                      </li>
                    </a>
                  {/each}
                </ul>
              {:else}
                <li>등록된 로드맵이 없습니다.</li>
              {/if}
            </details>
          </div>
        {/if}

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

        {#if rq.member.id === course.writer.id || rq.isAdmin()}
          <div class="flex justify-end">
            <a
              class=" mx-10 btn w-24 text-center"
              href="/course/{$page.params.id}/videowrite?writer_id={course.writer.id}">강의 등록</a
            >
          </div>
        {/if}

        {#if auth.enroll || rq.member.id === course.writer.id}
          <div class="border shadow-sm rounded-lg">
            <div class="relative w-full overflow-auto">
              <table class="w-full table-fixed caption-bottom text-sm">
                <thead class="[&amp;_tr]:border-b">
                  <tr
                    class="border-b transition-colors hover:bg-muted/50 data-[state=selected]:bg-muted"
                  >
                    <th
                      class="h-12 px-4 text-left align-middle font-semi-bold text-muted-foreground [&amp;:has([role=checkbox])]:pr-0 w-40"
                    >
                      동영상
                    </th>

                    <th
                      class="h-12 px-4 text-left align-middle font-semi-bold text-muted-foreground [&amp;:has([role=checkbox])]:pr-0 hidden md:table-cell w-32"
                    >
                      개요
                    </th>
                    <th
                      class="h-12 px-4 text-left align-middle font-semi-bold text-muted-foreground [&amp;:has([role=checkbox])]:pr-0 hidden md:table-cell w-20"
                    >
                    </th>

                    <th
                      class="h-12 px-4 text-left align-middle font-semi-bold text-muted-foreground [&amp;:has([role=checkbox])]:pr-0 hidden md:table-cell w-20"
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
                      <td
                        class="flex justify-start p-4 align-middle rounded-lg [&amp;:has([role=checkbox])]:pr-0 font-medium"
                      >
                        <div class="flex-col justify-center items-center">
                          <img
                            class="rounded-lg"
                            src={video.imgUrl}
                            on:click={() => window.open(video.url, '_blank')}
                          />
                          <div class="flex justify-center">{video.title}</div>
                        </div>
                      </td>
                      <td class="p-4 align-middle [&amp;:has([role=checkbox])]:pr-0">
                        <button
                          class="whitespace-nowrap text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50h-10 px-5 bg-white text-black w-[50px] flex items-center justify-center py-3 rounded-md shadow-md"
                          onclick={() => openModal(video.overView)}>개요</button
                        >
                        <dialog
                          id="my_modal_3"
                          class="modal"
                          bind:this={modal}
                          on:click={handleOutsideClick}
                        >
                          <div class="modal-box" on:click|stopPropagation>
                            <form class="flex flex-col p-6">
                              <button
                                type="button"
                                class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2"
                                onclick={() => modal.close()}>✕</button
                              >
                              {$selectedOverView}
                            </form>
                          </div>
                        </dialog>
                      </td>

                      <td class="p-4 align-middle [&amp;:has([role=checkbox])]:pr-0">
                        {#if rq.isAdmin() || rq.member.id === course.writer.id}
                          <div class="mb-5 mx-2 items-center">
                            <a
                              href="/course/{video.courseId}/videoedit/{video.id}?writer_id={course
                                .writer.id}"
                              class="btn btn-sm">수정</a
                            >
                            <button on:click={() => deleteVideo(video)} class="btn btn-sm"
                              >삭제</button
                            >
                          </div>
                        {/if}
                      </td>
                      <td class="p-4 align-middle [&amp;:has([role=checkbox])]:pr-0">
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
          <table class="w-full table-fixed caption-bottom text-sm"></table>
          <p class="">수강중이 아닙니다</p>
        {/if}
      </main>
    </div>
  </div>
{/await}
