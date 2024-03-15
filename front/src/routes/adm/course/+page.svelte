<script lang="ts">
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import CourseNav from '../../../lib/components/AdmNav.svelte';

  function removeMarkdown(markdownText) {
    // 정규 표현식을 사용하여 마크다운 문법 제거
    const text = markdownText
      .replace(/!\[[^\]]*\]\([^\)]*\)/g, '') // 이미지 링크 제거
      .replace(/\[[^\]]*\]\([^\)]*\)/g, '') // 일반 링크 제거
      .replace(/#{1,6} /g, '') // 헤더 제거
      .replace(/(\*\*|__)(.*?)\1/g, '$2') // 볼드 제거
      .replace(/(\*|_)(.*?)\1/g, '$2') // 이탤릭 제거
      .replace(/~~(.*?)~~/g, '$1') // 취소선 제거
      .replace(/`{3}[\s\S]*?`{3}/g, '') // 코드 블록 제거
      .replace(/`(.+?)`/g, '$1') // 인라인 코드 제거
      .replace(/\n/g, ' ') // 줄바꿈을 공백으로 변경
      .trim();

    return text;
  }

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');

    const kw = $page.url.searchParams.get('kw') ?? '';
    const kwType = ($page.url.searchParams.get('kwType') ?? 'ALL') as KwTypeCourse;
    const page_ = parseInt($page.url.searchParams.get('page') ?? '1');

    const { data } = await rq.apiEndPoints().GET('/api/v1/courses', {
      params: {
        query: {
          kw,
          kwType,
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

    return data!;
  }
</script>

{#await load()}
  <p>loading...</p>
{:then { data: { items } }}
  <div class="flex">
    <div>
      <CourseNav></CourseNav>
    </div>
    <div class="flex flex-col flex-1">
      <a href="/adm/course/write" class="btn bg-gray-200 mt-5 ml-6 w-[200px]"> 강좌 등록</a>
      <main class="flex flex-1 flex-col gap-4 p-4 md:gap-8 md:p-6">
        <div class="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {#if items}
            {#each items as item}
              <a
                href="/course/{item.id}"
                class="border border-gray-200 rounded-lg dark:border-gray-800 flex-col text-center"
              >
                <div class="flex justify-center gap-2">
                  <h2 class="text-lg font-semibold my-1 ml-2">{item.title}</h2>

                  <div class="font-medium text-lg mt-1">
                    [수강 인원 : {item.enrollCount}명]
                  </div>
                </div>
                <div class="flex justify-center my-2">
                  <div class="flex justify-center p-2 bg-black rounded-lg">
                    <img src={item.imgUrl} />
                  </div>
                </div>
                <p class="text-sm text-gray-500 dark:text-gray-400 my-4">
                  {removeMarkdown(item.overView)}
                </p>
              </a>
            {/each}
          {/if}
        </div>
      </main>
    </div>
  </div>
{/await}
