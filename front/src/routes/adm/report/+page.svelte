<script lang="ts">
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import Pagination from '$lib/components/Pagination.svelte';
  import CourseNav from '../../../lib/components/AdmNav.svelte';

  let reportPost: components['schemas']['ReportedPostDto'][] = $state();

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');

    const responseReportPost = await rq.apiEndPoints().GET(`/api/v1/admin/reports`);

    reportPost = responseReportPost.data?.data;

    return { reportPost };
  }
</script>

{#await load()}
  <p class="text-center">loading...</p>
{:then { reportPost }}
  {#if rq.isAdmin()}
    <div class="flex h-screen bg-gray-100 dark:bg-gray-900">
      <div>
        <CourseNav></CourseNav>
      </div>
      <div class="py-8 px-56 w-full">
        {#if reportPost && reportPost.length > 0}
          <div>
            <div class="flex justify-col justify-end">
              <div>
                <h2 class="text-2xl font-semibold text-gray-800">신고 글 관리</h2>
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
  {:else}
    <a href="/" class="btn btn-outline btn-error m-5">접근 불가 메인으로</a>
    {#if !rq.isLogin()}
      <a href="/member/login" class="btn btn-outline btn-error m-5">로그인</a>
    {/if}
  {/if}
{/await}
