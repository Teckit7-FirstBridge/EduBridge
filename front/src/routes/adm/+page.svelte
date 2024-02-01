<script lang="ts">
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import CourseNav from '../../lib/components/CourseNavAdm.svelte';
  import { goto } from '$app/navigation';

  let recentCourse: components['schemas']['RecentCourseDto'][] = $state();
  let recentMember: components['schemas']['RecentMemberDto'][] = $state();
  let reportPost: components['schemas']['ReportedPostDto'][] = $state();
  let recentNote: components['schemas']['RecentSummaryNoteDto'][] = $state();

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');

    const responseCourse = await rq.apiEndPoints().GET(`/api/v1/admin/courses`);
    const responseMember = await rq.apiEndPoints().GET(`/api/v1/admin/members`);
    const responseReportPost = await rq.apiEndPoints().GET(`/api/v1/admin/reports`);
    const responseNote = await rq.apiEndPoints().GET(`/api/v1/admin/summaryNotes`);

    recentCourse = responseCourse.data?.data!;
    recentMember = responseMember.data?.data!;
    reportPost = responseReportPost.data?.data!;
    recentNote = responseNote.data?.data!;

    return { recentCourse, recentMember, reportPost, recentNote };
  }
</script>

{#await load()}
  <p class="text-center">loading...</p>
{:then { recentCourse, recentMember, reportPost, recentNote }}
  <div class="flex h-screen bg-gray-100 dark:bg-gray-900">
    <div>
      <CourseNav></CourseNav>
    </div>
    <div class="flex py-4 pl-16 flex-1">
      <div class="w-1/2">
        <div class="flex-1 p-4">
          {#if recentCourse && recentCourse.length > 0}
            <div class="mb-5">
              <div class="flex justify-col justify-end">
                <div>
                  <h2 class="text-2xl font-semibold text-gray-800">강좌 관리</h2>
                </div>
                <div class="ml-2 mt-1">
                  <a
                    href="/adm/course"
                    class="inline-block px-2 py-1 bg-blue-600 text-white font-medium text-xs leading-tight uppercase rounded shadow-md hover:bg-blue-700 hover:shadow-lg focus:bg-blue-700 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-blue-800 active:shadow-lg transition duration-150 ease-in-out"
                    >+</a
                  >
                </div>
              </div>
              <div class="mt-3 bg-white shadow overflow-hidden rounded-md">
                <table class="min-w-full divide-y divide-gray-200">
                  <thead class="bg-gray-50">
                    <tr>
                      <th
                        scope="col"
                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        강좌 목록
                      </th>
                    </tr>
                  </thead>
                  <tbody class="bg-white divide-y divide-gray-200">
                    {#each recentCourse as item}
                      <tr>
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                          <a href="/course/{item.id}" class="text-blue-600 hover:text-blue-900">
                            {item.title}
                          </a>
                        </td>
                      </tr>
                    {/each}
                  </tbody>
                </table>
              </div>
            </div>
          {:else}
            <p>강좌가 없습니다.</p>
          {/if}
        </div>
        <div class="flex-1 p-4">
          {#if recentMember && recentMember.length > 0}
            <div>
              <div class="flex justify-col justify-end">
                <div>
                  <h2 class="text-2xl font-semibold text-gray-800">사용자 관리</h2>
                </div>
                <div class="ml-2 mt-1">
                  <a
                    href="/adm/member"
                    class="inline-block px-2 py-1 bg-blue-600 text-white font-medium text-xs leading-tight uppercase rounded shadow-md hover:bg-blue-700 hover:shadow-lg focus:bg-blue-700 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-blue-800 active:shadow-lg transition duration-150 ease-in-out"
                    >+</a
                  >
                </div>
              </div>
              <div class="mt-3 bg-white shadow overflow-hidden rounded-md">
                <table class="min-w-full divide-y divide-gray-200">
                  <thead class="bg-gray-50">
                    <tr>
                      <th
                        scope="col"
                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        이름
                      </th>
                      <th
                        scope="col"
                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        가입일
                      </th>
                      <th
                        scope="col"
                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        신고
                      </th>
                    </tr>
                  </thead>
                  <tbody class="bg-white divide-y divide-gray-200">
                    {#each recentMember as member}
                      <tr>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          <a href="/member/{member.id}" class="text-blue-600 hover:text-blue-900">
                            {member.name}
                          </a></td
                        >
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {`${new Date(member.createDate).getFullYear()}년 ${new Date(member.createDate).getMonth() + 1}월 ${new Date(member.createDate).getDate()}일`}
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap">
                          <div
                            class={`inline-flex px-2 text-xs font-semibold rounded-full ${member.report ? 'bg-red-100 text-red-800' : 'bg-green-100 text-green-800'}`}
                          >
                            {member.report ? '신고됨' : '신고 없음'}
                          </div>
                        </td>
                      </tr>
                    {/each}
                  </tbody>
                </table>
              </div>
            </div>
          {:else}
            <p>멤버가 없습니다.</p>
          {/if}
        </div>
      </div>
      <div class="px-16 w-1/2">
        <div class="flex-1 p-4">
          {#if recentNote && recentNote.length > 0}
            <div class="mb-5">
              <div class="flex justify-col justify-end">
                <div>
                  <h2 class="text-2xl font-semibold text-gray-800">요약 노트 관리</h2>
                </div>
                <div class="ml-2 mt-1">
                  <a
                    href="/adm/note"
                    class="inline-block px-2 py-1 bg-blue-600 text-white font-medium text-xs leading-tight uppercase rounded shadow-md hover:bg-blue-700 hover:shadow-lg focus:bg-blue-700 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-blue-800 active:shadow-lg transition duration-150 ease-in-out"
                    >+</a
                  >
                </div>
              </div>
              <div class="mt-3 bg-white shadow overflow-hidden rounded-md">
                <table class="min-w-full divide-y divide-gray-200">
                  <thead class="bg-gray-50">
                    <tr>
                      <th
                        scope="col"
                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        강좌명
                      </th>
                      <th
                        scope="col"
                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        작성일
                      </th>
                      <th
                        scope="col"
                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        작성자
                      </th>
                    </tr>
                  </thead>
                  <tbody class="bg-white divide-y divide-gray-200">
                    {#each recentNote as item}
                      <tr>
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                          <a
                            href="/course/{item.courseId}/{item.videoId}/summary/{item.id}"
                            class="text-blue-600 hover:text-blue-900"
                          >
                            {item.courseName}
                          </a>
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {`${new Date(item.createDate).getFullYear()}년 ${new Date(item.createDate).getMonth() + 1}월 ${new Date(item.createDate).getDate()}일`}
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                          <a
                            href="/course/{item.courseId}/{item.videoId}/summary/{item.id}"
                            class="text-blue-600 hover:text-blue-900"
                          >
                            {item.name}
                          </a>
                        </td>
                      </tr>
                    {/each}
                  </tbody>
                </table>
              </div>
            </div>
          {:else}
            <p>요약 노트가 없습니다.</p>
          {/if}
        </div>
        <div class="flex-1 p-4">
          {#if reportPost && reportPost.length > 0}
            <div>
              <div class="flex justify-col justify-end">
                <div>
                  <h2 class="text-2xl font-semibold text-gray-800">신고 글 관리</h2>
                </div>
                <div class="ml-2 mt-1">
                  <a
                    href="/adm/report"
                    class="inline-block px-2 py-1 bg-blue-600 text-white font-medium text-xs leading-tight uppercase rounded shadow-md hover:bg-blue-700 hover:shadow-lg focus:bg-blue-700 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-blue-800 active:shadow-lg transition duration-150 ease-in-out"
                    >+</a
                  >
                </div>
              </div>
              <div class="mt-3 bg-white shadow overflow-hidden rounded-md">
                <table class="min-w-full divide-y divide-gray-200">
                  <thead class="bg-gray-50">
                    <tr>
                      <th
                        scope="col"
                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        id
                      </th>
                      <th
                        scope="col"
                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        제목
                      </th>
                      <th
                        scope="col"
                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        작성일
                      </th>
                      <th
                        scope="col"
                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        작성자
                      </th>
                    </tr>
                  </thead>
                  <tbody class="bg-white divide-y divide-gray-200">
                    {#each reportPost as post}
                      <tr>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {post.id}
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          <a href="/board/{post.id}" class="text-blue-600 hover:text-blue-900">
                            {post.title}
                          </a></td
                        >
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {`${new Date(post.createDate).getFullYear()}년 ${new Date(post.createDate).getMonth() + 1}월 ${new Date(post.createDate).getDate()}일`}
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {post.authorName}
                        </td>
                      </tr>
                    {/each}
                  </tbody>
                </table>
              </div>
            </div>
          {:else}
            <p>신고된 글이 없습니다.</p>
          {/if}
        </div>
      </div>
    </div>
  </div>
{/await}
