<script lang="ts">
  //@ts-ignore
  import Editor from '@toast-ui/editor';
  import '@toast-ui/editor/dist/toastui-editor.css';
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import ToastUiEditor from '$lib/components/ToastUiEditor.svelte';

  let divNoti: HTMLDivElement | undefined = $state();
  let overvieweditor: any | undefined = $state();
  let notieditor: any | undefined = $state();
  let title: '';
  let imgUrl: '';
  let grade: '';

  const Course__save = async () => {
    const newNoti = notieditor.editor.getMarkdown().trim();
    const newOverview = overvieweditor.editor.getMarkdown().trim();

    if (title == null) {
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

    if (imgUrl == null) {
      rq.msgWarning('썸네일 주소를 입력 해 주세요');
      return;
    }
    if (!imgUrl.includes('png')) {
      rq.msgWarning('썸네일 url을 png 형식으로 입력 해 주세요');
      return;
    }

    if (grade.length < 1) {
      rq.msgWarning('등급을 선택 해 주세요');
      return;
    }

    const { data, error } = await rq.apiEndPointsWithFetch(fetch).POST('/api/v1/admin/courses', {
      // url 설정
      body: {
        title: title,
        notice: newNoti,
        overView: newOverview,
        imgUrl: imgUrl,
        grade: grade
      }
    });

    if (data) {
      rq.msgInfo(data.msg); //msg
      rq.goTo('/adm/course');
    }
  };
</script>

{#if rq.isAdmin()}
  <div class="px-60">
    <div class="flex flex-col h-full px-4 py-6 md:px-6 lg:py-16 md:py-12">
      <div class="space-y-4">
        <div class="space-y-2">
          <label
            class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
            for="post-title">Title</label
          ><input
            class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
            id="post-title"
            placeholder="Enter title"
            bind:value={title}
          />
        </div>
        <div class="space-y-2">
          <label
            class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
            for="post-noti">Notification</label
          >
          <div bind:this={divNoti} id="post-noti"></div>
          <ToastUiEditor bind:this={notieditor} height={'calc(60dvh - 64px)'}></ToastUiEditor>
          <label
            class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
            for="post-overview">OverView</label
          >
          <ToastUiEditor bind:this={overvieweditor} height={'calc(60dvh - 64px)'}></ToastUiEditor>
          <label
            class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
            for="post-imgUrl">ImgUrl</label
          ><input
            class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
            id="post-imgUrl"
            placeholder="Enter ImgUrl"
            bind:value={imgUrl}
          />
          <label
            class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
            for="post-price">grade</label
          ><select
            class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
            id="post-grade"
            bind:value={grade}
            placeholder="Select Grade"
          >
            <option disabled selected value="">Select Grade</option>
            <option value="초급">초급</option>
            <option value="중급">중급</option>
            <option value="고급">고급</option>
          </select>
        </div>

        <button
          on:click={Course__save}
          class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 h-10 px-4 py-2"
          >Save</button
        >
      </div>
    </div>
  </div>
{:else}
  <a href="/" class="btn btn-outline btn-error m-5">접근 불가 메인으로</a>
  {#if !rq.isLogin()}
    <a href="/member/login" class="btn btn-outline btn-error m-5">로그인</a>
  {/if}
{/if}
