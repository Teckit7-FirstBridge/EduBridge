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
      rq.goTo('/member/qna');
    }
  };
</script>

<!--
  // v0 by Vercel.
  // https://v0.dev/t/BlOM8H6hEQ8
  -->
<div class="px-60">
  <div class="mt-5 text-2xl">1대1 문의</div>
  <div class="flex flex-col h-full px-4 md:px-2 lg:py-10 md:py-6">
    <div class="space-y-4">
      <div class="space-y-2">
        <label
          class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
          for="post-title">제목</label
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
          for="post-body">내용</label
        >
        <ToastUiEditor bind:this={toastUiEditor} />
      </div>
      <button
        on:click={Post__save}
        class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 h-10 px-4 py-2"
        >저장</button
      >
    </div>
  </div>
</div>
