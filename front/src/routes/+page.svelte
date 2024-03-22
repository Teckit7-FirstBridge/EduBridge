<!--
// v0 by Vercel.
// https://v0.dev/t/Fh6x180GJMV
-->

<script lang="ts">
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';

  let courses: components['schemas']['CourseDto'][] = $state([]);
  const sampleCourse = {
    title: 'Sample Course',
    overView: 'This is a sample course description.'
    // 나머지 필요한 필드 추가
  };

  let hideButtonOnMobile = false;
  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');

    const isMobileResponse = await rq.apiEndPoints().GET(`/api/v1/admin/deviceCheck`);
    const { isMobile } = isMobileResponse.data?.data!;

    if (isMobile) {
      hideButtonOnMobile = true;
      // console.log('모바일');
    }

    const { data } = await rq.apiEndPoints().GET('/api/v1/home', {});
    courses = data!.data.items;
    while (courses.length < 6) {
      courses.push({ ...sampleCourse });
    }

    return courses;
  }

  let activeSlide = 0;
  const totalSlides = 3;
  let touchStartX = 0;
  let touchEndX = 0;

  function handleTouchStart(event) {
    touchStartX = event.touches[0].clientX;
  }

  function handleTouchMove(event) {
    touchEndX = event.touches[0].clientX;
  }

  function handleTouchEnd() {
    if (touchStartX - touchEndX > 50) {
      nextSlide();
    } else if (touchEndX - touchStartX > 50) {
      previousSlide();
    }
  }

  function nextSlide() {
    activeSlide = (activeSlide + 1) % totalSlides;
    updateSlideTransform();
  }

  function previousSlide() {
    activeSlide = (activeSlide - 1 + totalSlides) % totalSlides;
    updateSlideTransform();
  }

  function updateSlideTransform() {
    const slideContainer = document.querySelector('.carousel-container');
    slideContainer.style.transform = `translateX(-${activeSlide * 100}%)`;
    // console.log(activeSlide);
  }
  
</script>

{#await load()}
  <p>loading...</p>
{:then courses}
  <div class="bg-gradient-to-r text-black py-16">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="text-center mb-20">
        <div class="relative w-full max-w-md mx-auto" role="region" aria-roledescription="carousel">
          <div class="overflow-hidden">
            <div
              class="flex -ml-4 transition-transform duration-300 carousel-container"
              on:touchstart={handleTouchStart}
              on:touchmove={handleTouchMove}
              on:touchend={handleTouchEnd}>
              <div
                role="group"
                aria-roledescription="slide"
                class="min-w-0 shrink-0 grow-0 basis-full pl-4"
                style="width: 100%"
                class:selected={activeSlide === 0}
              > 
                <h2 class="text-5xl font-extrabold">환영합니다!</h2>
                <p class="mt-4 text-xl">학습 능률을 끌어올려줄 학습 징검다리, 에듀브릿지</p>
                <p class="mt-4 text-x2">
                  커리큘럼을 따라 차근차근 학습하고, <br />
                  포인트를 받아 다음 강좌로 건너가세요!
                </p>
              </div>
              <div
                role="group"
                aria-roledescription="slide"
                class="min-w-0 shrink-0 grow-0 basis-full pl-4"
                style="width: 100%"
                class:selected={activeSlide === 1}
              >
                <h2 class="text-5xl font-extrabold">공지사항</h2>
                <p class="mt-4 text-xl">무엇을 하시든 환영합니다</p>
                <p class="mt-4 text-x2">
                  열공하세요! <br />
                </p>
              </div>
              <div
                role="group"
                aria-roledescription="slide"
                class="min-w-0 shrink-0 grow-0 basis-full pl-4"
                style="width: 100%"
                class:selected={activeSlide === 2}
              > 
                <h2 class="text-5xl font-extrabold">공지222</h2>
                <p class="mt-4 text-xl">업데이트 점검 안내</p>
                <p class="mt-4 text-x2">
                  3월 31일까지 점검 예정입니다.
                </p>
              </div>
            </div>
          </div>
          <button
            class="inline-flex items-center whitespace-nowrap shrink-0 justify-center text-sm transition-colors 
            focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:pointer-events-none 
            disabled:opacity-50 border border-input bg-background shadow-sm font-medium hover:bg-accent 
            hover:text-accent-foreground absolute h-8 w-8 rounded-full -left-12 top-1/2 -translate-y-1/2 
            {hideButtonOnMobile ? 'hidden' : ''}"
            on:click={previousSlide}
          >
            &lt;
          </button>
          <button
            on:click={nextSlide}
            class="inline-flex items-center whitespace-nowrap shrink-0 justify-center text-sm transition-colors 
            focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:pointer-events-none 
            disabled:opacity-50 border border-input bg-background shadow-sm font-medium hover:bg-accent 
            hover:text-accent-foreground absolute h-8 w-8 rounded-full -right-12 top-1/2 -translate-y-1/2 
            {hideButtonOnMobile ? 'hidden' : ''}"
          >
            &gt;
          </button>
        </div>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
        {#if courses}
          {#each courses as course}
            <div class="text-center">
              <a href="/course/{course.id}">
                <img src={course.imgUrl} class="rounded-xl mb-2" />
                <h3 class="text-2xl font-semibold mb-3">{course.title}</h3>
                <p class="text-gray-500 dark:text-gray-400">
                  {course.overView}
                </p>
              </a>
            </div>
          {/each}
        {/if}
      </div>
    </div>
  </div>
{/await}
