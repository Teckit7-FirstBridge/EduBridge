<script lang="ts">
  //@ts-ignore
  import Editor from '@toast-ui/editor';
  import '@toast-ui/editor/dist/toastui-editor.css';
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  let div: HTMLDivElement;
  let editor: Editor;
  let oldBody: string = '';
  let title = '';
  let dto: components['schemas']['PostDto'][] = $state([]);

  $effect(() => {
    editor = new Editor({
      el: div,
      height: 'calc(100dvh - 60px)',
      initialEditType: 'markdown',
      previewStyle: 'vertical'
    });
    return () => {
      editor.destroy();
    };
  });

  function saveToLocalStorage(id: number, body: string) {
    const key = 'posts_' + id;
    // LocalStorage에서 기존 데이터를 가져옵니다.
    const existingData = localStorage.getItem(key);

    // 기존 데이터가 있으면 JSON으로 파싱하고, 없으면 빈 배열을 사용합니다.
    const posts = existingData ? JSON.parse(existingData) : [];

    // 새 데이터를 배열에 추가합니다.
    posts.push({
      id,
      body: body,
      date: new Date().toISOString()
    });

    // 배열의 크기가 10을 초과하면 가장 오래된 항목(첫 번째 항목)을 제거합니다.
    if (posts.length > 10) {
      posts.shift(); // 배열의 첫 번째 항목을 제거합니다.
    }

    // 변경된 배열을 JSON 문자열로 변환하여 LocalStorage에 저장합니다.
    localStorage.setItem(key, JSON.stringify(posts));
  }

  const Post__save = async () => {
    const newBody = editor.getMarkdown().trim();
    if (oldBody === newBody) {
      return;
    }

    const { data, error } = await rq.apiEndPoints().POST('/api/v1/posts', {
      // url 설정
      body: {
        body: newBody,
        title: title
      }
    });

    oldBody = newBody;

    saveToLocalStorage(parseInt($page.params.id), newBody);

    if (data) {
      rq.msgInfo(data.msg); //msg
      rq.goTo('/board');
    }
  };
</script>

<!--
// v0 by Vercel.
// https://v0.dev/t/BlOM8H6hEQ8
-->
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
        for="post-body">Body</label
      >
      <div bind:this={div} id="post-body"></div>
    </div>
    <button
      on:click={Post__save}
      class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 h-10 px-4 py-2"
      >Save</button
    >
  </div>
</div>
