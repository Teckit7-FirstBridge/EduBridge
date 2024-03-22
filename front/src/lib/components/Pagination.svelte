<script lang="ts">
  import rq from '$lib/rq/rq.svelte';
  import type { PageDto } from '$lib/types';

  const { page, pageDelta = 1 } = $props<{ page: PageDto; pageDelta?: number }>();

  function calculatePaginationRange(current: number, total: number) {
    const maxPageButtons = 5;
    let startPage = (Math.ceil(current / maxPageButtons) - 1) * maxPageButtons + 1;
    let endPage = Math.min(startPage + maxPageButtons - 1, total);

    const range = [];

    // '이전' 버튼의 페이지 번호를 현재 범위의 마지막으로 설정
    const currentPageRangeStart = (Math.ceil(current / maxPageButtons) - 1) * maxPageButtons + 1;
    const previousPage = currentPageRangeStart - 1; // 이전 페이지 범위로 바로 이동

    // '다음' 버튼의 페이지 번호 계산
    const nextPage = endPage + 1;

    // '이전' 버튼 추가
    if (startPage > 1) {
      range.push({ no: previousPage, text: '이전' });
    }

    for (let i = startPage; i <= endPage; i++) {
      range.push({ no: i, text: `${i}` });
    }

    // '다음' 버튼 추가
    if (endPage < total) {
      range.push({ no: nextPage, text: '다음' });
    }

    return range;
  }
</script>

<div class="flex justify-center">
  <div class="join">
    {#each calculatePaginationRange(page.number, page.totalPagesCount) as pageNumber}
      <button
        class={`join-item btn bg-gray-50 ${pageNumber.no == page.number ? 'text-blue-400 bg-gray-100 shadow-inner' : ''} `}
        on:click={() => rq.goToCurrentPageWithNewParam('page', `${pageNumber.no}`)}
      >
        {pageNumber.text}
      </button>
    {/each}
  </div>
</div>
