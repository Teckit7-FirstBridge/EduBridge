<script lang="ts">
  import rq from '$lib/rq/rq.svelte';
  import { page } from '$app/stores';
  let url = $state('');
  let overview = $state('');
  let keywords = $state('');
  let title = $state('');
  let isLoading = $state(false);

  let keywordsAdvice;

  function openModalKAdvice() {
    keywordsAdvice.showModal();
  }

  function closeModalKAdvice(event) {
    event.preventDefault();
    keywordsAdvice.close();
  }

  let videoAdvice;

  function openModalVidAdvice() {
    videoAdvice.showModal();
  }
  function closeModalVidAdvice(event) {
    event.preventDefault();
    videoAdvice.close();
  }

  function extractVideoId(urll) {
    const urlParams = new URLSearchParams(new URL(urll).search);
    return urlParams.get('v');
  }

  async function getKeywords(event) {
    event.preventDefault();

    if (!url) {
      rq.msgWarning('유튜브 URL을 입력해주세요');
      return;
    }
    isLoading = true;
    const { data, error } = await rq.apiEndPoints().GET(`/api/v1/youtube/getKeywords`, {
      params: {
        query: {
          videoId: extractVideoId(url)
        }
      }
    });
    if (data) {
      keywords = data.data;
    } else {
      rq.msgError('키워드 추출에 실패했습니다.');
    }

    isLoading = false;
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

    if (keywords.length < 1) {
      rq.msgWarning('키워드를 입력 해 주세요.');
      return;
    }

    if (overview.length < 1) {
      rq.msgWarning('개요를 입력 해 주세요.');
      return;
    }

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
  <span class="loading loading-spinner loading-xs m-2"></span>
{:then}
  <div class="max-w-lg mx-auto w-full px-4 sm:px-6 lg:px-8 mt-2">
    <div class=" w-full space-y-8">
      <div class="container mx-auto w-full">
        <form
          class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4"
          on:submit|preventDefault={submitForm}
        >
          <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="title"> 제 목 </label>
            <input
              class="appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              id="title"
              type="text"
              placeholder="Enter the title "
              bind:value={title}
            />
          </div>
          <div class="mb-4">
            <label
              class="block text-gray-700 text-sm font-bold mb-2 flex items-center"
              for="video-url"
            >
              강의 Url
              <a href="#" onclick={openModalVidAdvice}>
                <i class="fa-solid fa-circle-exclamation text-red-500"></i>
              </a>
              <button class="btn p-1 py-1" on:click={getKeywords}>키워드 자동 추출</button>
              {#if isLoading}
                <span class="loading loading-spinner loading-xs m-2"></span>
              {/if}

              <dialog id="my_modal_3" class="modal" bind:this={videoAdvice}>
                <div class="modal-box modal-box-2">
                  <button
                    class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2"
                    onclick={closeModalVidAdvice}>✕</button
                  >
                  <div>
                    <div>※ Youtube 영상 URL을 넣어주세요!</div>
                    <div>쇼츠 영상은 넣을 수 없습니다.</div>
                    <div>Youtube 영상 URL이 정확한지 다시 한번 확인해주세요.</div>
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
                  <div>※ 키워드는 해당 강의 요약노트 채점의 기준이 됩니다.</div>
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
          <div class="flex items-center justify-end">
            <button
              class="inline-block px-4 py-2 border border-gray-400 text-gray-700 bg-white hover:bg-gray-700 hover:text-white rounded-md shadow-sm text-sm font-medium focus:outline-none"
              type="submit"
            >
              등 록
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
{/await}
