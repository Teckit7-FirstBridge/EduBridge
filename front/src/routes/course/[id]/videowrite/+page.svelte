<script lang="ts">
  import rq from '$lib/rq/rq.svelte';
  import { page } from '$app/stores';
  let url = $state('');
  let overview = $state('');
  let imgUrl = $state('');
  let keywords = $state('');

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');

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
  }

  const submitForm = async () => {
    if (url.length < 1) {
      rq.msgWarning('동영상 주소를 입력 해 주세요.');
      return;
    }
    if (imgUrl.length < 1) {
      rq.msgWarning('썸네일 주소를 입력 해 주세요.');
      return;
    }
    if (!imgUrl.includes('jpg')) {
      rq.msgWarning('썸네일 url을 jpg 형식으로 입력 해 주세요');
      return;
    }
    if (keywords.length < 1) {
      rq.msgWarning('키워드를 입력 해 주세요.');
      return;
    }
    if (overview.length < 1) {
      rq.msgWarning('개요를 입력 해 주세요.');
      return;
    }

    const { data, error } = await rq.apiEndPoints().POST(`/api/v1/admin/{courseId}/videos`, {
      params: { path: { courseId: parseInt($page.params.id) } },
      body: {
        url,
        overView: overview,
        courseId: parseInt($page.params.id),
        imgUrl: imgUrl,
        keywords: keywords
      }
    });
    if (data) {
      rq.goTo(`/course/${$page.params.id}`);
    }
  };
</script>

{#await load()}
  <p>loading...</p>
{:then}
  <div
    class="min-h-screen w-full flex items-center justify-center bg-gray-100 py-12 px-4 sm:px-6 lg:px-8"
  >
    <div class=" w-full space-y-8">
      <div class="container mx-auto p-4 w-full">
        <form
          class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4"
          on:submit|preventDefault={submitForm}
        >
          <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="video-url">
              강의 Url
            </label>
            <input
              class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              id="video-url"
              type="text"
              placeholder="Enter video URL"
              bind:value={url}
            />
          </div>
          <div class="mb-4">
            <div class="mb-2">
              <label
                class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                for="course-imgUrl mr-4">강의 썸네일 Url</label
              ><label
                class="ml-4 text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70 bg-blue-400 text-white p-2 rounded"
                for="course-imgUrl"
              >
                https://img.youtube.com/vi/VIDEO-ID/0.jpg
              </label>
            </div>
            <input
              class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              id="video-imgUrl"
              type="text"
              placeholder="Enter img URL"
              bind:value={imgUrl}
            />
          </div>
          <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="video-keywords">
              Keywords
            </label>
            <input
              class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              id="video-keywords"
              type="text"
              placeholder="Keywords... ex)자바,스프링"
              bind:value={keywords}
            />
          </div>
          <div class="mb-6">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="video-summary">
              강의 개요
            </label>
            <textarea
              class=" h-40 shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              id="video-summary"
              placeholder="Enter a brief summary"
              bind:value={overview}
            ></textarea>
          </div>
          <div class="flex items-center justify-between">
            <button class="btn btn-primary" type="submit"> 등 록 </button>
          </div>
        </form>
      </div>
    </div>
  </div>
{/await}
