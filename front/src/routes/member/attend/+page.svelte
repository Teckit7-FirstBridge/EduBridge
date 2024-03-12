<script lang="ts">
  import { onMount } from 'svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import rq from '$lib/rq/rq.svelte';
  import { Calendar } from '@fullcalendar/core';
  import dayGridPlugin from '@fullcalendar/daygrid';

  let attends: components['schemas']['AttendDto'][];

  let calendarEl;
  let calendar;

  onMount(async () => {
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

    await load(); // 데이터 로드 후 달력에 이벤트 추가

    calendar.render();
  });

  async function load() {
    const response = await rq.apiEndPoints().GET(`/api/v1/attend`);
    attends = response.data?.data;

    // attends 배열의 각 항목에 대해 이벤트를 달력에 추가
    attends.forEach((attend) => {
      calendar.addEvent({
        title: '출석체크',
        start: attend.createDate
      });
    });
  }
</script>

<div class="p-2" bind:this={calendarEl}></div>
