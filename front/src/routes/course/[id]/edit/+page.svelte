<script lang="ts">
  //@ts-ignore
  import Editor from '@toast-ui/editor';
  import '@toast-ui/editor/dist/toastui-editor.css';
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import axios from 'axios';
  import ToastUiEditor from '$lib/components/ToastUiEditor.svelte';

  let overvieweditor: any | undefined = $state();
  let notieditor: any | undefined = $state();
  let initialData: components['schemas']['CourseDto'] | undefined = $state();

  async function load() {
    const isLoginResponse = await rq.apiEndPoints().GET(`/api/v1/members/isLogin`);
    const { isLogin } = isLoginResponse.data?.data!;

    const { data } = await rq.apiEndPoints().GET('/api/v1/courses/{courseId}', {
      params: { path: { courseId: parseInt($page.params.id) } }
    });

    initialData = data!.data;
    if (rq.member.id !== initialData.writer_id && isLogin) {
      rq.msgError(' 권한이 없는 유저입니다');
      rq.goTo('/');
    }
    if (!isLogin) {
      rq.msgWarning('로그인 후 이용 해 주세요');
      rq.goTo('/member/login');
    }
    if (initialData.hashtags && initialData.hashtags?.split('@').length > 0) {
      tags = initialData.hashtags?.split('@');
    }
    return { initialData };
  }

  const Course__save = async () => {
    const newNoti = notieditor.editor.getMarkdown().trim();
    const newOverview = overvieweditor.editor.getMarkdown().trim();
    const title = initialData?.title;

    if (title?.length < 1) {
      console.log('dd');
      rq.msgWarning('제목을 입력 해 주세요');
      return;
    }

    if (newNoti.length < 1) {
      rq.msgWarning('공지를 입력 해 주세요');
      return;
    }

    if (newOverview.length < 1) {
      rq.msgWarning('개요를 입력 해 주세요');
      return;
    }

    const { data, error } = await rq.apiEndPointsWithFetch(fetch).PUT('/api/v1/courses/{id}', {
      params: { path: { id: parseInt($page.params.id) } },
      // url 설정
      body: {
        title: title,
        notice: newNoti,
        overView: newOverview,
        hashtags: tags.join('@')
      }
    });

    if (data) {
      rq.msgInfo(data.msg); //msg
      rq.goTo('/course/' + $page.params.id);
    }
  };

  let tags: string[] = $state([]);
  let newTag: string = $state('');

  function addTag() {
    const trimmedTag = newTag.trim();
    if (trimmedTag === '') {
      rq.msgWarning('태그를 입력하세요');
      return;
    } // 빈 태그인 경우 추가하지 않음

    if (tags.includes(trimmedTag)) {
      rq.msgWarning('이미 등록된 태그입니다');
      return;
    }

    if (tags.length >= 5) {
      rq.msgWarning('태그는 최대 5개까지 등록할 수 있습니다');
      return;
    } // 최대 태그 개수를 초과한 경우 추가하지 않음

    tags = [...tags, trimmedTag];
    newTag = '';
  }

  function removeTag(tag: string) {
    tags = tags.filter((t) => t !== tag);
  }
</script>

<div class="max-w-4xl mx-auto">
  {#await load()}
    <span class="loading loading-spinner loading-xs m-2"></span>
  {:then { initialData }}
    {#if rq.isAdmin() || rq.member.id === initialData.writer_id}
      <div class="">
        <div class="flex flex-col h-full px-4 py-6 md:px-6 lg:py-16 md:py-12">
          <div class="space-y-4">
            <div class="space-y-2">
              <label
                class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                for="course-title">강좌 제목</label
              ><input
                class="flex h-10 w-full rounded-md border px-3 py-2 text-sm placeholder:text-muted-foreground focus:outline-none focus:border-gray-700 disabled:cursor-not-allowed disabled:opacity-50"
                id="course-title"
                placeholder="Enter title"
                bind:value={initialData.title}
              />
            </div>
            <div class="my-4">
              <input
                type="text"
                bind:value={newTag}
                placeholder="태그를 입력하세요"
                class="px-4 py-2 border rounded-lg mr-2 focus:outline-none focus:border-gray-700"
                on:keypress={(e) => {
                  if (e.key === 'Enter') {
                    e.preventDefault(); // 기본 동작인 폼 전송을 막습니다.
                    addTag();
                  }
                }}
              />
              <button
                on:click={addTag}
                class="inline-block px-4 py-2 border border-gray-300 text-gray-700 bg-white hover:bg-black hover:text-white rounded-md shadow-sm text-sm font-medium focus:outline-none"
                >추가</button
              >
            </div>

            <div class="my-4">
              {#each tags as tag}
                <span
                  class="inline-flex items-center bg-gray-200 text-gray-800 px-2 py-1 rounded-full mr-2 mb-2"
                >
                  <span>{tag}</span>
                  <button on:click={() => removeTag(tag)} class="ml-2">&times;</button>
                </span>
              {/each}
            </div>
            <div class="space-y-2">
              <label
                class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                for="course-noti">공지사항</label
              >
              <ToastUiEditor
                id="course-noti"
                bind:this={notieditor}
                body={initialData.notice}
                height={'calc(60dvh - 64px)'}
              ></ToastUiEditor>
              <label
                class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                for="course-overview">강좌 개요</label
              >
              <ToastUiEditor
                id="course-overview"
                bind:this={overvieweditor}
                body={initialData.overView}
                height={'calc(60dvh - 64px)'}
              ></ToastUiEditor>
            </div>

            <div class="flex justify-end">
              <button
                on:click={Course__save}
                class="inline-block px-4 py-2 border border-gray-400 text-gray-700 bg-white hover:bg-gray-700 hover:text-white rounded-md shadow-sm text-sm font-medium focus:outline-none"
                >저장</button
              >
            </div>
          </div>
        </div>
      </div>
    {:else}
      <a href="/" class="btn btn-outline btn-error m-5">접근 불가 메인으로</a>
      {#if !rq.isLogin()}
        <a href="/member/login" class="btn btn-outline btn-error m-5">로그인</a>
      {/if}
    {/if}
  {/await}
</div>
