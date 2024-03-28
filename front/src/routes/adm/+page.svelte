<script lang="ts">
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import AdmNav from '../../lib/components/AdmNav.svelte';
  import { onMount } from 'svelte';

  let recentCourse: components['schemas']['AdminCourseDto'][] = $state();
  let recentMember: components['schemas']['AdminMemberDto'][] = $state();
  let reportPost: components['schemas']['ReportedPostDto'][] = $state();
  let recentQna: components['schemas']['AdminQnaDto'][] = $state();

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');

    const isMobileResponse = await rq.apiEndPoints().GET(`/api/v1/admin/deviceCheck`);
    const { isMobile } = isMobileResponse.data?.data!;

    if (isMobile) {
      rq.msgError('관리자 페이지는 pc로 접속 바랍니다.');
      rq.goTo('/');
      return;
    }

    const isAdminResponse = await rq.apiEndPoints().GET(`/api/v1/members/isAdmin`);
    const { isAdmin } = isAdminResponse.data?.data!;
    const isLoginResponse = await rq.apiEndPoints().GET(`/api/v1/members/isLogin`);
    const { isLogin } = isLoginResponse.data?.data!;
    if (!isAdmin && isLogin) {
      rq.msgError('관리자 권한이 없습니다');
      rq.goTo('/');
      return;
    }
    if (!isAdmin && !isLogin) {
      rq.msgWarning('관리자 로그인 후 이용 해 주세요');
      rq.goTo('/member/login');
      return;
    }

    const responseCourse = await rq.apiEndPoints().GET(`/api/v1/admin/courses`);
    const responseMember = await rq.apiEndPoints().GET(`/api/v1/admin/members`);
    const responseReportPost = await rq.apiEndPoints().GET(`/api/v1/admin/reports`);
    const responseNote = await rq.apiEndPoints().GET(`/api/v1/admin/summaryNotes`);
    const responseQna = await rq.apiEndPoints().GET(`/api/v1/admin/qna`);

    recentCourse = responseCourse.data?.data!;
    recentMember = responseMember.data?.data!;
    reportPost = responseReportPost.data?.data!;
    recentQna = responseQna.data?.data!;

    return { recentCourse, recentMember, reportPost, recentQna };
  }
</script>

{#await load()}
  <p class="text-center">loading...</p>
{:then { recentCourse, recentMember, reportPost, recentQna }}
  <div class="flex">
    <div>
      <AdmNav></AdmNav>
    </div>
    <div class="flex py-4 px-16 grid grid-cols-1 sm:grid-cols-1 lg:grid-cols-2">
      <div>
        <div class="p-4">
          {#if recentCourse && recentCourse.length > 0}
            <div class="mb-5">
              <div class="flex justify-col justify-end">
                <div>
                  <h2 class="text-2xl font-semibold text-gray-800">강좌 관리</h2>
                </div>
                <div class="ml-2 mt-1">
                  <a
                    href="/adm/course"
                    class="inline-block px-2 py-1 bg-blue-100 text-blue-800 font-bold text-sm leading-tight uppercase rounded shadow-md hover:bg-gray-900 hover:text-white hover:shadow-lg focus:bg-gray-200 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-gray-300 active:shadow-lg transition duration-150 ease-in-out"
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
                        강좌 제목
                      </th>
                      <th
                        scope="col"
                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        등급
                      </th>
                      <th
                        scope="col"
                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        수강 인원
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
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                          <div
                            class={`inline-flex px-2 text-sm font-semibold rounded-full ${item.grade === '초급' ? 'bg-blue-100 text-blue-800' : item.grade === '중급' ? 'bg-orange-100 text-orange-800' : 'bg-red-100 text-red-800'}`}
                          >
                            {item.grade}
                          </div>
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                          {item.enrollCount}명
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
        <div class="p-4">
          {#if recentMember && recentMember.length > 0}
            <div>
              <div class="flex justify-col justify-end">
                <div>
                  <h2 class="text-2xl font-semibold text-gray-800">사용자 관리</h2>
                </div>
                <div class="ml-2 mt-1">
                  <a
                    href="/adm/member"
                    class="inline-block px-2 py-1 bg-blue-100 text-blue-800 font-bold text-sm leading-tight uppercase rounded shadow-md hover:bg-gray-900 hover:text-white hover:shadow-lg focus:bg-gray-200 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-gray-300 active:shadow-lg transition duration-150 ease-in-out"
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
      <div class="px-16">
        <div class="p-4">
          {#if reportPost && reportPost.length > 0}
            <div>
              <div class="flex justify-col justify-end">
                <div>
                  <h2 class="text-2xl font-semibold text-gray-800">신고 글 관리</h2>
                </div>
                <div class="ml-2 mt-1">
                  <a
                    href="/adm/report"
                    class="inline-block px-2 py-1 bg-blue-100 text-blue-800 font-bold text-sm leading-tight uppercase rounded shadow-md hover:bg-gray-900 hover:text-white hover:shadow-lg focus:bg-gray-200 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-gray-300 active:shadow-lg transition duration-150 ease-in-out"
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
                          <a href="/qna/{post.id}" class="text-blue-600 hover:text-blue-900">
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
        <div class="p-4">
          {#if recentQna && recentQna.length > 0}
            <div>
              <div class="flex justify-col justify-end">
                <div>
                  <h2 class="text-2xl font-semibold text-gray-800">문의 관리</h2>
                </div>
                <div class="ml-2 mt-1">
                  <a
                    href="/adm/qna"
                    class="inline-block px-2 py-1 bg-blue-100 text-blue-800 font-bold text-sm leading-tight uppercase rounded shadow-md hover:bg-gray-900 hover:text-white hover:shadow-lg focus:bg-gray-200 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-gray-300 active:shadow-lg transition duration-150 ease-in-out"
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
                        제목
                      </th>
                      <th
                        scope="col"
                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        작성자
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
                        처리 여부
                      </th>
                    </tr>
                  </thead>
                  <tbody class="bg-white divide-y divide-gray-200">
                    {#each recentQna as qna}
                      <tr>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          <a href="/board/{qna.id}" class="text-blue-600 hover:text-blue-900">
                            {qna.title}
                          </a></td
                        >
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {qna.authorName}
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {`${new Date(qna.createDate).getFullYear()}년 ${new Date(qna.createDate).getMonth() + 1}월 ${new Date(qna.createDate).getDate()}일`}
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                          <div
                            class={`inline-flex px-2 text-xs font-semibold rounded-full ${qna.commentCount == 0 ? 'bg-red-100 text-red-800' : 'bg-blue-100 text-blue-800'}`}
                          >
                            {qna.commentCount == 0 ? '미완료' : '처리 완료'}
                          </div>
                        </td>
                      </tr>
                    {/each}
                  </tbody>
                </table>
              </div>
            </div>
          {:else}
            <p>문의 글이 없습니다.</p>
          {/if}
        </div>
      </div>
    </div>
  </div>
{/await}
