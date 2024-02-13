<script lang="ts">
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';

  let learningCourses: components['schemas']['CourseDto'][] = $state();
  let favoriteCourses: components['schemas']['CourseDto'][] = $state();
  let summaryNotes: components['schemas']['SummaryNoteDto'][] = $state();
  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');

    const { data } = await rq.apiEndPoints().GET('/api/v1/members/{id}', {
      params: { path: { id: parseInt($page.params.id) } }
    });
    learningCourses = data?.data.item.learningCourses!;
    favoriteCourses = data?.data.item.favoriteCourses!;

    const dailyAchievement = data?.data.item.member?.dailyAchievement;
    const dailyGoal = data?.data.item.member?.dailyGoal;
    const member = data?.data.item.member;

    const summaryResponse = await rq.apiEndPoints().GET(`/api/v1/courses/summary/{writerid}`, {
      params: {
        path: {
          writerid: member.id
        }
      }
    });
    summaryNotes = summaryResponse.data?.data!;
    console.log(summaryNotes);
    console.log(learningCourses);

    return { learningCourses, favoriteCourses, summaryNotes, dailyAchievement, dailyGoal, member };
  }
</script>

{#await load()}
  <h2>loading...</h2>
{:then { learningCourses, favoriteCourses, summaryNotes, dailyAchievement, dailyGoal, member }}
  {#if rq.member.id == member.id || rq.isAdmin()}
    <div class="max-w-4xl mx-auto my-8">
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
        <p>point: {member?.point}</p>
      </header>
      <main class="flex-1 p-4 md:p-6">
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
              ><path d="M4 19.5v-15A2.5 2.5 0 0 1 6.5 2H20v20H6.5a2.5 2.5 0 0 1 0-5H20"></path></svg
            ><span class="text-sm font-medium">Learning Status</span>
          </div>
          <div class="flex gap-4 overflow-auto">
            {#each learningCourses as learningCourse}
              <a
                href="/course/{learningCourse.id}"
                class="flex-none w-48 p-6 bg-white rounded-lg shadow"
              >
                <h3 class="text-sm font-medium">{learningCourse.title}</h3>
                <p class="text-xs text-gray-500">
                  Attended: {summaryNotes.filter(
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
            ><span class="text-sm font-medium">Favorite Courses</span>
          </div>
          <div class="flex gap-4 overflow-auto">
            {#each favoriteCourses as favoriteCourse}
              <a
                href="/course/{favoriteCourse.id}"
                class="flex-none w-48 p-6 bg-white rounded-lg shadow"
              >
                <h3 class="text-sm font-medium">{favoriteCourse.title}</h3>
                <p class="text-xs text-gray-500">{favoriteCourse.price} 원</p>
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
              ><span class="text-sm font-medium">Goals Progress</span>
            </div>
            <span>Daily Goal:{dailyGoal}</span>
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
              {Math.min(100, parseInt(((dailyAchievement / dailyGoal) * 100).toFixed(2)))}%
              Completed
            </p>
          </div>
        </div>
      </main>
    </div>
  {:else}
    <a href="/" class="btn btn-outline btn-error m-5">접근 불가 메인으로</a>
  {/if}
{/await}
