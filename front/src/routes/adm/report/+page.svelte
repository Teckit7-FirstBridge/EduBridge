<script lang="ts">
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import Pagination from '$lib/components/Pagination.svelte';
  import CourseNav from '../../../lib/components/AdmNav.svelte';

  let reportList: components['schemas']['GetReportList'][] = $state();

  async function deleteReport(report: components['schemas']['ReportDto']) {
    const isConfirmed = confirm('신고를 취소하시겠습니까?');

    if (isConfirmed) {
      const { data, error } = await rq.apiEndPoints().DELETE(`/api/v1/report/{id}`, {
        params: { path: { id: report.id } }
      });

      if (data) {
        location.reload();
        rq.msgInfo('신고가 취소되었습니다.');
      } else if (error) {
        rq.msgError('신고 취소 실패');
      }
    }
  }

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');

    const page_ = parseInt($page.url.searchParams.get('page') ?? '1');

    const { data } = await rq.apiEndPoints().GET(`/api/v1/report/all`, {
      params: {
        query: {
          page: page_
        }
      }
    });

    const isAdminResponse = await rq.apiEndPoints().GET(`/api/v1/members/isAdmin`);
    const isLoginResponse = await rq.apiEndPoints().GET(`/api/v1/members/isLogin`);
    const isMobileResponse = await rq.apiEndPoints().GET(`/api/v1/admin/deviceCheck`);

    const { isAdmin } = isAdminResponse.data?.data!;
    const { isLogin } = isLoginResponse.data?.data!;
    const { isMobile } = isMobileResponse.data?.data!;

    if (isMobile) {
      rq.msgError('관리자 페이지는 pc로 접속 바랍니다.');
      rq.goTo('/');
    }

    if (!isAdmin && isLogin) {
      rq.msgError('관리자 권한이 없습니다');
      rq.goTo('/');
    }
    if (!isAdmin && !isLogin) {
      rq.msgWarning('관리자 로그인 후 이용 해 주세요');
      rq.goTo('/member/login');
    }

    reportList = data!.data.itemPage?.content;

    return { reportList };
  }
</script>

{#await load()}
  <p class="text-center">loading...</p>
{:then { reportList }}
  <div class="flex">
    <div>
      <CourseNav></CourseNav>
    </div>
    <div class="py-8 px-56 w-full">
      {#if reportList && reportList.length > 0}
        <div>
          <div class="flex justify-col justify-end">
            <div>
              <h2 class="text-2xl font-semibold text-gray-800">신고 관리</h2>
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
                    글/강좌 번호
                  </th>
                  <th
                    scope="col"
                    class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                  >
                    신고 타입
                  </th>
                  <th
                    scope="col"
                    class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                  >
                    신고 사유
                  </th>
                  <th
                    scope="col"
                    class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                  >
                    신고 날짜
                  </th>
                  <th
                    scope="col"
                    class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                  >
                  </th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
                {#each reportList as report}
                  <tr>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {#if report.reportType == 'Course'}
                        <a
                          href="/course/{report.materialId}"
                          class="text-blue-600 hover:text-blue-900"
                        >
                          Course/{report.materialId}
                        </a>
                      {:else}
                        <a
                          href="/board/{report.materialId}"
                          class="text-blue-600 hover:text-blue-900"
                        >
                          Post/{report.materialId}
                        </a>
                      {/if}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {report.reportType}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {report.reportReason}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {`${new Date(report.createDate).getFullYear()}년 ${new Date(report.createDate).getMonth() + 1}월 ${new Date(report.createDate).getDate()}일`}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      <div class="mb-5 mx-1 flex">
                        <a
                          class="inline-block px-3 py-2 border border-gray-400 text-gray-700 bg-white hover:bg-gray-700 hover:text-white rounded-md shadow-sm text-sm font-medium focus:outline-none"
                          on:click={() => deleteReport(report)}>삭제</a
                        >
                      </div>
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
{/await}
