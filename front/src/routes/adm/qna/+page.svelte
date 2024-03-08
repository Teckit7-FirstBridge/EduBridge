<script lang="ts">
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import Pagination from '$lib/components/Pagination.svelte';
  import CourseNav from '../../../lib/components/AdmNav.svelte';

  let qna: components['schemas']['PageDtoAdminQnaDto'][] = $state([]);

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');

    const page_ = parseInt($page.url.searchParams.get('page') ?? '1');

    const { data } = await rq.apiEndPoints().GET(`/api/v1/admin/qna/list`, {
      params: {
        query: {
          page: page_
        }
      }
    });

    const isAdminResponse = await rq.apiEndPoints().GET(`/api/v1/members/isAdmin`);
    const { isAdmin } = isAdminResponse.data?.data!;
    const isLoginResponse = await rq.apiEndPoints().GET(`/api/v1/members/isLogin`);
    const { isLogin } = isLoginResponse.data?.data!;
    if (!isAdmin && isLogin) {
      rq.msgError('관리자 권한이 없습니다');
      rq.goTo('/');
    }
    if (!isAdmin && !isLogin) {
      rq.msgWarning('관리자 로그인 후 이용 해 주세요');
      rq.goTo('/member/login');
    }

    qna = data!.data.itemPage?.content;

    return data!;
  }
</script>

{#await load()}
  <p class="text-center">loading...</p>
{:then { data: { itemPage } }}
  <div class="flex">
    <div>
      <CourseNav></CourseNav>
    </div>
    <div class="py-8 px-56 w-full">
      <div>
        {#if qna && qna.length > 0}
          <div class="mb-5">
            <div class="flex justify-col justify-end">
              <div>
                <h2 class="text-2xl font-semibold text-gray-800">1대1 문의 관리</h2>
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
                      문의 제목
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
                  {#each qna as item}
                    <tr>
                      <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        <a href="/qna/{item.id}" class="text-blue-600 hover:text-blue-900">
                          {item.title}
                        </a>
                      </td>
                      <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {item.authorName}
                      </td>
                      <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {`${new Date(item.createDate).getFullYear()}년 ${new Date(item.createDate).getMonth() + 1}월 ${new Date(item.createDate).getDate()}일`}
                      </td>
                      <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        <div
                          class={`inline-flex px-2 text-xs font-semibold rounded-full ${item.commentCount == 0 ? 'bg-red-100 text-red-800' : 'bg-blue-100 text-blue-800'}`}
                        >
                          {item.commentCount == 0 ? '미완료' : '처리 완료'}
                        </div>
                      </td>
                    </tr>
                  {/each}
                </tbody>
              </table>
            </div>
          </div>
        {:else}
          <p>등록된 문의가 없습니다.</p>
        {/if}
        <div class="mt-4 flex justify-center">
          <Pagination page={itemPage} />
        </div>
      </div>
    </div>
  </div>
{/await}
