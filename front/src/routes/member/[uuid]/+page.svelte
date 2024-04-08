<script lang="ts">
  import { page } from '$app/stores';
  import { onMount } from 'svelte';
  import { writable } from 'svelte/store';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import { Calendar } from '@fullcalendar/core';
  import dayGridPlugin from '@fullcalendar/daygrid';

  let learningCourses: components['schemas']['CourseDto'][] = $state();
  let favoriteCourses: components['schemas']['CourseDto'][] = $state();
  let summaryNotes: components['schemas']['SummaryNoteDto'][] = $state();
  let attends: components['schemas']['AttendDto'][];
  let initialData: components['schemas']['NickNameDto'] | undefined = $state();

  let calendarEl;
  let calendar;
  let isAlarm = false;
  let newNickname = $state();

  let modalpoint;

  let modalNickname;

  let modalDropMessage;

  const updateNickname = async () => {
    const { data, error } = await rq.apiEndPoints().PUT('/api/v1/members/modifyNickName', {
      body: {
        nickName: newNickname
      }
    });
    if (data) {
      rq.msgInfo('닉네임이 변경 되었습니다.'); //msg
      modalNickname.close();
      console.log(newNickname);
    } else {
      rq.msgError('이미 사용중인 닉네임 입니다.');
    }
  };

  const goodBye = async () => {
    const { data, error } = await rq.apiEndPoints().PUT('/api/v1/members/drop');
    if (data) {
      rq.msgInfo('안녕히 가십시오! 더 밝은 내일을 기원합니다 :)');
      rq.logout();
      rq.goTo('/');
    } else {
      rq.msgError('권한 확인이 필요합니다.');
    }
  };

  let calModal;

  function openModalNickname() {
    modalNickname.showModal();
  }

  function handleOutsideClickNickname(event) {
    if (event.target === modalNickname) {
      modalNickname.close();
    }
  }

  function openModalCal() {
    calModal.showModal();
  }

  function openModalPoint() {
    modalpoint.showModal();
  }

  function handleOutsideClickCal(event) {
    if (event.target === calModal) {
      calModal.close();
    }
  }

  function handleOutsideClickPoint(event) {
    if (event.target === modalpoint) {
      modalpoint.close();
    }
  }

  function openModalDropMessage() {
    modalDropMessage.showModal();
  }

  function handleOutsideDropMessage(event) {
    if (event.target === modalDropMessage) {
      modalDropMessage.close();
    }
  }

  function closeDropMessage() {
    modalDropMessage.close();
  }

  let modalAdvice;

  function openModalAdvice() {
    modalAdvice.showModal();
  }

  function closeModalAdvice(event) {
    event.preventDefault();
    modalAdvice.close();
  }

  function handleOutsideClickAdvice(event) {
    if (event.target === modalAdvice) {
      modalAdvice.close();
    }
  }

  const visit = async () => {
    if (!rq.member.visitedToday) {
      const confirmAttendance = confirm('출석체크 하시겠습니까?');

      if (confirmAttendance) {
        const { data, error } = await rq.apiEndPoints().PUT('/api/v1/members/visit');
        if (data) {
          rq.msgInfo('출석체크 완료');
        } else {
          rq.msgWarning('이미 출석 되었습니다.');
        }
      }
    }
  };

  let point: components['schemas']['PointDto'] = $state();

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');

    const { data } = await rq.apiEndPoints().GET('/api/v1/members/{uuid}', {
      params: { path: { uuid: $page.params.uuid } }
    });
    learningCourses = data?.data.item.learningCourses!;
    favoriteCourses = data?.data.item.favoriteCourses!;

    const dailyAchievement = data?.data.item.member?.dailyAchievement;
    const dailyGoal = data?.data.item.member?.dailyGoal;
    const member = data?.data.item.member;

    const summaryResponse = await rq.apiEndPoints().GET(`/api/v1/courses/summary/{writerId}`, {
      params: {
        path: {
          writerId: member.id
        }
      }
    });
    summaryNotes = summaryResponse.data?.data!;

    const notificationResponse = await rq.apiEndPoints().GET(`/api/v1/notification/isAlarm`);
    isAlarm = notificationResponse.data?.data!;

    const responsePoint = await rq.apiEndPoints().GET(`/api/v1/point/{memberId}`, {
      params: {
        path: {
          memberId: member.id
        }
      }
    });
    point = responsePoint.data?.data!;

    const response = await rq.apiEndPoints().GET(`/api/v1/point/attend`);
    attends = response.data?.data;

    calendar = new Calendar(calendarEl, {
      plugins: [dayGridPlugin],
      initialView: 'dayGridMonth',
      contentHeight: '300px',
      locale: 'ko',
      eventContent: (arg) => {
        // 여기서 사용자 정의 HTML을 반환하여 이벤트 내용을 아이콘으로 대체
        return { html: `<i class="fa-solid fa-splotch text-2xl mb-2 text-blue-800"></i>` };
      }
    });

    calendar.render();

    // attends 배열의 각 항목에 대해 이벤트를 달력에 추가
    attends.forEach((attend) => {
      calendar.addEvent({
        title: '출석체크',
        start: attend.createDate
      });
    });

    return {
      learningCourses,
      favoriteCourses,
      summaryNotes,
      dailyAchievement,
      dailyGoal,
      member,
      point
    };
  }

  function copyInputValue() {
    // 입력 창의 값을 가져옵니다.
    var inputField = document.getElementById('inputUrl');
    var inputValue = inputField!.value;

    // 클립보드에 복사합니다.
    navigator.clipboard
      .writeText(inputValue)
      .then(function () {
        rq.msgInfo('링크가 복사되었습니다');
      })
      .catch(function (error) {
        rq.msgError('복사가 실패했습니다');
      });
  }
</script>

{#await load()}
  <span class="loading loading-spinner loading-xs m-2"></span>
{:then { learningCourses, favoriteCourses, summaryNotes, dailyAchievement, dailyGoal, member }}
  {#if rq.member.id == member.id || rq.isAdmin()}
    <div class="max-w-4xl mx-auto">
      <header class="flex h-16 items-center border-b px-4 md:px-6 justify-between">
        <a class="flex items-center gap-2"
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
            ><path d="M3 9h18v10a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V9Z"></path><path
              d="m3 9 2.45-4.9A2 2 0 0 1 7.24 3h9.52a2 2 0 0 1 1.8 1.1L21 9"
            ></path><path d="M12 3v6"></path></svg
          ><span class="text-lg font-semibold">My Page</span></a
        >

        <div class="flex gap-x-4 relative items-center">
          <button
            onclick={openModalPoint}
            class="mr-2 font-semibold inline-block px-4 py-2 border border-gray-400 text-gray-800 bg-white hover:bg-gray-700 hover:text-white rounded-md shadow-sm text-sm font-medium focus:outline-none"
            >포인트 : {member?.point}</button
          >
          <dialog
            id="my_modal_3"
            class="modal"
            bind:this={modalpoint}
            on:click={handleOutsideClickPoint}
          >
            <div class="modal-box modal-box-1">
              <form method="dialog">
                <button class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
              </form>
              <div class="flex flex-col p-1 bg-white shadow rounded-lg">
                <h2 class="text-xl font-semibold mb-2 border-b pb-2">포인트 내역</h2>
                <table>
                  <tbody>
                    {#each point as point}
                      <tr>
                        <td class="border-b py-2 px-1">{point.createDate.substring(2, 10)}</td>
                        <td class="border-b py-2 px-1">{'['}{point.content}{']'}</td>
                        <td class="border-b py-2 px-1">{point.amount}</td>
                      </tr>
                    {/each}
                  </tbody>
                </table>
              </div>
            </div>
          </dialog>
        </div>
      </header>
      <main class="flex-1 p-4 md:p-6">
        <div class="flex gap-x-4 relative items-center">
          <div class="grid gap-4">
            <div class="flex items-center gap-2">
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
                class="h-6 w-6"
                ><path d="M4 19.5v-15A2.5 2.5 0 0 1 6.5 2H20v20H6.5a2.5 2.5 0 0 1 0-5H20"
                ></path></svg
              ><span class="text-lg font-medium">강좌별 수강 진도</span>
            </div>
            <div class="flex gap-4 overflow-auto">
              {#each learningCourses as learningCourse}
                <a
                  href="/course/{learningCourse.id}"
                  class="flex-none w-48 p-6 rounded-lg shadow border border-gray-400 bg-gray-50"
                >
                  <h3 class="text-sm font-medium">{learningCourse.title}</h3>
                  <p class="text-xs text-gray-500">
                    진도 : {summaryNotes.filter(
                      (item) => item.courseId === learningCourse.id && item.pass
                    ).length}/{learningCourse.videoCount}
                  </p>
                </a>
              {/each}
            </div>

            <div class="flex items-center gap-2">
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
                class="h-6 w-6"
                ><path
                  d="M19 14c1.49-1.46 3-3.21 3-5.5A5.5 5.5 0 0 0 16.5 3c-1.76 0-3 .5-4.5 2-1.5-1.5-2.74-2-4.5-2A5.5 5.5 0 0 0 2 8.5c0 2.3 1.5 4.05 3 5.5l7 7Z"
                ></path></svg
              ><span class="text-lg font-medium">좋아요 한 강좌</span>
            </div>
            <div class="flex gap-4 overflow-auto">
              {#each favoriteCourses as favoriteCourse}
                <a
                  href="/course/{favoriteCourse.id}"
                  class="flex-none w-48 p-6 rounded-lg shadow border border-gray-400 bg-gray-50"
                >
                  <h3 class="text-sm mt-1 font-medium">{favoriteCourse.title}</h3>
                </a>
              {/each}
            </div>
            <div class="flex justify-between gap-2">
              <div class="flex items-center gap-2">
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
                  class="h-6 w-6"
                  ><path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z"></path><path
                    d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z"
                  ></path></svg
                ><span class="text-lg font-medium">오늘의 목표 : 요약노트 {dailyGoal}개</span>
              </div>
            </div>
            <div class="p-4 bg-white rounded-lg shadow">
              <div class="relative pt-1">
                <div class="overflow-hidden h-2 mb-4 text-xs flex rounded bg-gray-200">
                  <div
                    class="transition-all duration-500 ease-in-out bg-green-500 h-full"
                    style="width: {(dailyAchievement / dailyGoal) * 100}%;"
                  ></div>
                </div>
              </div>
              <p class="text-xs text-gray-500">
                {Math.min(100, parseInt(((dailyAchievement / dailyGoal) * 100).toFixed(2)))}% 진행
              </p>
            </div>
          </div>
        </div>
      </main>
      <div class="ml-6">
        <input
          value="{import.meta.env.VITE_CORE_FRONT_BASE_URL}/export?uuid={member?.uuid}"
          type="hidden"
          id="inputUrl"
          placeholder="You can't touch this"
          class="input input-bordered w-full max-w-xs"
          disabled
        />
        <button on:click={copyInputValue} class="btn btn-outline w-[200px] h-3 border-gray-400"
          ><label class="text-l"><div>요약노트 for <br />포트폴리오</div></label><i
            class="fa-regular fa-paste"
          ></i></button
        >
        <button onclick={openModalAdvice}>
          <i class="ml-2 fa-solid fa-circle-question text-blue-900"></i>
        </button>
        <dialog
          id="my_modal_3"
          class="modal"
          bind:this={modalAdvice}
          on:click={handleOutsideClickAdvice}
        >
          <div class="modal-box modal-box-2">
            <button
              class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2"
              onclick={closeModalAdvice}>✕</button
            >
            <div>
              <div>※ 포트폴리오 첨부를 위한 내보내기 안내 ※</div>
              <br />
              <div>CS 공부한 내역을 증명하고 싶을 때, 해당 페이지를 첨부해보세요.</div>
              <div>버튼을 눌러 복사된 URL을 새 창에서 열어보세요. (PC 권장)</div>
              <div>브라우저의 인쇄 기능을 이용하여 pdf로 출력해보세요.</div>
              <div>
                작성한 요약노트의 세부 내용도 넣고 싶다면 URL의 export 뒤에 '/detail'을 추가하세요.
              </div>
            </div>
          </div>
        </dialog>
      </div>
    </div>
  {:else}
    <a href="/" class="btn btn-outline btn-error m-5">접근 불가 메인으로</a>
  {/if}
{/await}
<div class=" max-w-4xl mx-auto">
  <div class="flex gap-x-4 relative items-center">
    <button
      onclick={openModalCal}
      class="ml-6 mt-4 font-semibold inline-block px-4 py-2 border border-gray-400 text-gray-800 bg-white hover:bg-gray-700 hover:text-white rounded-md shadow-sm text-sm font-medium focus:outline-none"
      ><i class="fa-regular fa-calendar-days mr-1"></i>
      출석 달력</button
    >
    <dialog id="my_modal_3" class="modal" bind:this={calModal} on:click={handleOutsideClickCal}>
      <div class="modal-box">
        <form method="dialog">
          <button class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
        </form>
        <div class="flex flex-col bg-white shadow rounded-lg mt-4">
          <div bind:this={calendarEl}></div>
        </div>
      </div>
    </dialog>
    <div class="flex gap-x-4 relative items-center">
      <button
        onclick={visit}
        class="mt-4 font-semibold inline-block px-4 py-2 border border-gray-400 text-gray-800 bg-white hover:bg-gray-700 hover:text-white rounded-md shadow-sm text-sm font-medium focus:outline-none"
        ><i class="fa-regular fa-calendar-check mr-2"></i>출석 체크</button
      >
    </div>
  </div>
</div>

<div class="max-w-4xl mx-auto"></div>

<div class="max-w-4xl mx-auto">
  <div class="flex gap-x-4 relative items-center">
    <button
      onclick={openModalNickname}
      class="ml-6 mt-4 font-semibold inline-block px-4 py-2 border border-gray-400 text-gray-800 bg-white hover:bg-gray-700 hover:text-white rounded-md shadow-sm text-sm font-medium focus:outline-none"
      >닉네임 변경</button
    >
    <dialog
      id="my_modal_3"
      class="modal"
      bind:this={modalNickname}
      on:click={handleOutsideClickNickname}
    >
      <div class="modal-box">
        <form method="dialog">
          <button class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
        </form>
        <div class="m-4 flex flex-col bg-white rounded-lg">
          <label class="font-semibold text-lg mb-2">닉네임 변경</label>
          <input
            type="text"
            bind:value={newNickname}
            id="newNickname"
            class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm placeholder:text-muted-foreground focus:border-gray-700 focus:outline-none disabled:cursor-not-allowed disabled:opacity-50"
            placeholder="새 닉네임 입력"
          />
          <div class="m-4 flex justify-end">
            <button
              onclick={updateNickname}
              class=" w-[100px] font-semibold inline-block px-4 py-2 border border-gray-400 text-gray-800 bg-white hover:bg-gray-700 hover:text-white rounded-md shadow-sm text-sm font-medium focus:outline-none"
              >변경하기</button
            >
          </div>
        </div>
      </div>
    </dialog>
  </div>
</div>

<div class="max-w-4xl mx-auto">
  <div class="flex gap-x-4 relative items-center">
    <button
      onclick={openModalDropMessage}
      class="text-xs ml-6 my-4 font-semibold inline-block px-4 py-2 border border-gray-400 text-gray-800 bg-white hover:bg-gray-700 hover:text-white rounded-md shadow-sm text-sm font-medium focus:outline-none"
      >회원 탈퇴</button
    >
    <dialog
      id="my_modal_3"
      class="modal"
      bind:this={modalDropMessage}
      on:click={handleOutsideDropMessage}
    >
      <div class="modal-box">
        <form method="dialog">
          <button class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
        </form>
        <div class="flex flex-col bg-white shadow rounded-lg mt-4">
          <div>※ 탈퇴 전 안내사항을 숙지해주시기 바랍니다. ※</div>
          <p>
            1. 탈퇴 시, 해당 계정으로 작성된 글, 댓글, 강좌, 로드맵에는 모두 '탈퇴한 회원'으로
            표시됩니다.<br />
            2. 해당 데이터가 남지 않기를 원하시는 경우, 탈퇴 전에 직접 삭제해주시기 바랍니다.<br />
            3. 같은 소셜 계정으로 다시 회원가입 하실 수 있습니다.<br />
            4. 즐거운 마음으로 떠나시기를 기원합니다. 안녕히 가십시오.
          </p>
          <div class="flex justify-between mb-1">
            <button onclick={closeDropMessage} class="btn mt-4">마이 페이지로<br /> 돌아가기</button
            >
            <button onclick={goodBye} class="btn mt-4">눈물을 머금고<br /> 이별하기</button>
          </div>
        </div>
      </div>
    </dialog>
  </div>
</div>
