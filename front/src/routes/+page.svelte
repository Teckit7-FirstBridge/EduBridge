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

  let activeSlide = 0;
  const totalSlides = 2; // 슬라이드 총 개수

  function nextSlide() {
    activeSlide = (activeSlide + 1) % totalSlides;
  }

  function previousSlide() {
    activeSlide = (activeSlide - 1 + totalSlides) % totalSlides;
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
              class="flex -ml-4 transition-transform duration-300"
              style="transform: translateX(-{activeSlide * 100}%)"
            >
              <div
                role="group"
                aria-roledescription="slide"
                class="min-w-0 shrink-0 grow-0 basis-full pl-4"
              >
                <h2 class="text-5xl font-extrabold">Smart Inbox</h2>
                <p class="mt-4 text-xl">
                  Our Smart Inbox feature helps you manage your emails efficiently by prioritizing
                  important emails.
                </p>
              </div>
              <div
                role="group"
                aria-roledescription="slide"
                class="min-w-0 shrink-0 grow-0 basis-full pl-4"
              >
                <h2 class="text-5xl font-extrabold">Discover Our Unique Features</h2>
                <p class="mt-4 text-xl">
                  Our features are designed to enhance your productivity and streamline your
                  workflow.
                </p>
              </div>
            </div>
          </div>
          <button
            class="inline-flex items-center whitespace-nowrap shrink-0 justify-center text-sm transition-colors focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:pointer-events-none disabled:opacity-50 border border-input bg-background shadow-sm font-medium hover:bg-accent hover:text-accent-foreground absolute h-8 w-8 rounded-full -left-12 top-1/2 -translate-y-1/2"
            on:click={previousSlide}
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
              class="h-4 w-4"
            >
              <path d="m12 19-7-7 7-7"></path>
              <path d="M19 12H5"></path>
            </svg>
            <span class="sr-only">Previous slide</span>
          </button>
          <button
            on:click={nextSlide}
            class="inline-flex items-center whitespace-nowrap shrink-0 justify-center text-sm transition-colors focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:pointer-events-none disabled:opacity-50 border border-input bg-background shadow-sm font-medium hover:bg-accent hover:text-accent-foreground absolute h-8 w-8 rounded-full -right-12 top-1/2 -translate-y-1/2"
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
              class="h-4 w-4"
            >
              <path d="M5 12h14"></path>
              <path d="m12 5 7 7-7 7"></path>
            </svg>
            <span class="sr-only">Next slide</span>
          </button>
        </div>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
        {#if courses}
          {#each courses as course}
            <div class="text-center">
              <img
                alt="Course Thumbnail"
                class="mx-auto mb-4 object-cover rounded-md"
                height="200"
                src="https://generated.vusercontent.net/placeholder.svg"
                style="aspect-ratio: 200/200; object-fit: cover"
                width="200"
              />
              <h3 class="text-2xl font-semibold mb-3">{course.title}</h3>
              <p class="text-gray-500 dark:text-gray-400">
                {course.overView}
              </p>
            </div>
          {/each}
        {/if}
      </div>
    </div>
  </div>
{/await}
