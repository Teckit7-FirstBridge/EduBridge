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

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');

    const { data } = await rq.apiEndPoints().GET('/api/v1/home', {});
    courses = data!.data.items;
    while (courses.length < 6) {
      courses.push({ ...sampleCourse });
    }

    return courses;
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
              role="group"
              aria-roledescription="slide"
              class="min-w-0 shrink-0 grow-0 basis-full pl-4"
            >
              <h2 class="text-5xl font-extrabold">환영합니다!</h2>
              <p class="mt-4 text-xl">학습 능률을 끌어올려줄 학습 징검다리, 에듀브릿지</p>
              <p class="mt-4 text-x2">
                커리큘럼을 따라 차근차근 학습하고, <br />
                포인트를 받아 다음 강좌로 건너가세요!
              </p>
            </div>
          </div>
        </div>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
        {#if courses}
          {#each courses as course}
            <div class="text-center">
              <a href="/course/{course.id}">
                <div class="image-container mb-2">
                  <img src={course.imgUrl} class="rounded-image" />
                </div>
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
