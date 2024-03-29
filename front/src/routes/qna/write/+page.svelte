<script lang="ts">
  //@ts-ignore
  import '@toast-ui/editor/dist/toastui-editor.css';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import ToastUiEditor from '$lib/components/ToastUiEditor.svelte';
  let oldBody: string = '';
  let title = '';
  let dto: components['schemas']['PostDto'][] = $state([]);
  let toastUiEditor: any | undefined = $state();

  const Post__save = async () => {
    const newBody = toastUiEditor.editor.getMarkdown().trim();

    if (title.length < 1) {
      rq.msgWarning('제목을 입력 해 주세요');
      return;
    }

    if (newBody.length < 1) {
      rq.msgWarning('내용을 입력 해 주세요');
      return;
    }

    const { data, error } = await rq.apiEndPoints().POST('/api/v1/posts/qna', {
      // url 설정
      body: {
        body: newBody,
        title: title
      }
    });

    oldBody = newBody;

    if (data) {
      rq.msgInfo(data.msg); //msg
      rq.goTo('/qna');
    }
  };
</script>

<!--
  // v0 by Vercel.
  // https://v0.dev/t/BlOM8H6hEQ8
  -->
<div class="">
  <div class="max-w-lg mx-auto text-2xl font-semibold w-full px-4 sm:px-6 lg:px-8 mt-2">
    1대1 문의
  </div>
  <div class="max-w-lg mx-auto w-full px-4 sm:px-6 lg:px-8 mt-2">
    <div class="space-y-4">
      <div class="space-y-2">
        <label
          class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
          for="post-title">제목</label
        ><input
          class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm placeholder:text-muted-foreground focus:border-gray-700 focus:outline-none disabled:cursor-not-allowed disabled:opacity-50"
          id="post-title"
          placeholder="Enter title"
          bind:value={title}
        />
      </div>
      <div class="space-y-2">
        <label
          class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
          for="post-body">내용</label
        >
        <ToastUiEditor bind:this={toastUiEditor} />
      </div>
      <div class="flex justify-end">
        <button
          on:click={Post__save}
          class="inline-block px-4 py-2 border border-gray-400 text-gray-700 bg-white hover:bg-gray-700 hover:text-white rounded-md shadow-sm text-sm font-medium focus:outline-none"
          >저장</button
        >
      </div>
    </div>
  </div>
</div>
