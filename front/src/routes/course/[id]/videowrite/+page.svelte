<script lang="ts">
  import rq from '$lib/rq/rq.svelte';
  import { page } from '$app/stores';
  let url = $state('');
  let overview = $state('');
  let imgUrl = $state('');
  let keywords = $state('');
  let title = $state('');

  let keywordsAdvice;

  function openModalKAdvice() {
    keywordsAdvice.showModal();
  }

  function closeModalKAdvice(event) {
    event.preventDefault();
    keywordsAdvice.close();
  }

  let thumbnailAdvice;

  function openModalThAdvice() {
    thumbnailAdvice.showModal();
  }

  function closeModalThAdvice(event) {
    event.preventDefault();
    thumbnailAdvice.close();
  }

  let videoAdvice;

  function openModalVidAdvice() {
    videoAdvice.showModal();
  }

  function closeModalVidAdvice(event) {
    event.preventDefault();
    videoAdvice.close();
  }

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');

    const isAdminResponse = await rq.apiEndPoints().GET(`/api/v1/members/isAdmin`);
    const { isAdmin } = isAdminResponse.data?.data!;
    const isLoginResponse = await rq.apiEndPoints().GET(`/api/v1/members/isLogin`);
    const { isLogin } = isLoginResponse.data?.data!;

    if (
      !isAdmin &&
      rq.member.id !== parseInt($page.url.searchParams.get('writer_id')!) &&
      isLogin
    ) {
      rq.msgError('권한이 없습니다');
      rq.goTo('/');
    }
    if (!isLogin) {
      rq.msgWarning('로그인 후 이용 해 주세요');
      rq.goTo('/member/login');
    }
  }

  const submitForm = async () => {
    if (title.length < 1) {
      rq.msgWarning('제목을 입력 해 주세요');
      return;
    }

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
    console.log(title);

    const { data, error } = await rq
      .apiEndPoints()
      .POST(`/api/v1/courses/{courseId}/videos/{writer_id}`, {
        params: {
          path: {
            courseId: parseInt($page.params.id),
            writer_id: parseInt($page.url.searchParams.get('writer_id')!)
          }
        },
        body: {
          url,
          overView: overview,
          courseId: parseInt($page.params.id),
          imgUrl: imgUrl,
          keywords: keywords,
          title: title
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
            <label class="block text-gray-700 text-sm font-bold mb-2" for="title"> 제 목 </label>
            <input
              class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              id="title"
              type="text"
              placeholder="Enter the title "
              bind:value={title}
            />
          </div>
          <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="video-url">
              강의 Url
              <a href="#" onclick={openModalVidAdvice}>
                <i class="fa-solid fa-circle-exclamation text-red-500"></i>
              </a>
              <dialog id="my_modal_3" class="modal" bind:this={videoAdvice}>
                <div class="modal-box modal-box-2">
                  <button
                    class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2"
                    onclick={closeModalVidAdvice}>✕</button
                  >
                  <div>
                    <div>형식에 맞추어 Youtube 영상의 url을 입력해주세요.</div>
                    <div>https://www.youtube.com/watch?v=VIDEO-ID</div>
                    <div>VIDEO-ID 뒤의 값(ex/ &list로 시작하는 부분 등)은 지우고 입력해주세요.</div>
                  </div>
                </div>
              </dialog>
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
                class="text-sm font-bold leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                for="course-imgUrl mr-4"
                >강의 썸네일 Url
                <a href="#" onclick={openModalThAdvice}>
                  <i class="fa-solid fa-circle-exclamation text-red-500"></i>
                </a>
                <dialog id="my_modal_3" class="modal" bind:this={thumbnailAdvice}>
                  <div class="modal-box modal-box-2">
                    <button
                      class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2"
                      onclick={closeModalThAdvice}>✕</button
                    >
                    <div>
                      <div>제시된 형식에 맞춰 썸네일 이미지를 입력해주세요.</div>
                      <br />
                      <div>VIDEO-ID 위치에 Youtube 영상 id를 넣어주세요.</div>
                      <br />
                      <div>Youtube 영상 id : URL의 v= 혹은 vi= 다음 값</div>
                    </div>
                  </div>
                </dialog>
              </label><label
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
              <a href="#" onclick={openModalKAdvice}>
                <i class="fa-solid fa-circle-exclamation text-red-500"></i>
              </a>
              <dialog id="my_modal_3" class="modal" bind:this={keywordsAdvice}>
                <div class="modal-box modal-box-2">
                  <button
                    class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2"
                    onclick={closeModalKAdvice}>✕</button
                  >
                  <div>등록하는 강의 영상의 내용을 숙지하고 키워드를 작성해주세요.</div>
                  <div>키워드 개수는 3~5개를 권장합니다.</div>
                </div>
              </dialog>
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
