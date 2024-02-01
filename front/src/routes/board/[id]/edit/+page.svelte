<script lang="ts">
  //@ts-ignore
  import Editor from '@toast-ui/editor';
  import '@toast-ui/editor/dist/toastui-editor.css';
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import { post } from 'jquery';
  import ToastUiEditor from '$lib/components/ToastUiEditor.svelte';

  let oldBody: string = '';
  let initialData: components['schemas']['PostDto'] | undefined = $state();
  let dto: components['schemas']['PostDto'][] = $state([]);
  let toastUiEditor: any | undefined = $state();
  async function load() {
    const { data } = await rq.apiEndPoints().GET('/api/v1/posts/{id}', {
      params: { path: { id: parseInt($page.params.id) } }
    });
    initialData = data?.data!;

    return { initialData };
  }

  const Post__save = async () => {
    const newBody = toastUiEditor.editor.getMarkdown().trim();
    if (oldBody === newBody) {
      return;
    }

    const { data, error } = await rq.apiEndPoints().PUT('/api/v1/posts/{id}', {
      params: { path: { id: initialData?.id! } },
      // url 설정
      body: {
        id: initialData?.id!,
        body: newBody,
        title: initialData!.title,
        likedByCurrentUser: initialData?.likedByCurrentUser,
        createDate: initialData?.createDate!,
        authorId: initialData?.authorId!,
        authorName: initialData?.authorName!,
        voteCount: initialData?.voteCount
      }
    });

    oldBody = newBody;

    if (data) {
      rq.msgInfo(data.msg); //msg
      rq.goTo('/board/' + initialData?.id);
    }
  };
</script>

<!--
  // v0 by Vercel.
  // https://v0.dev/t/BlOM8H6hEQ8
  -->
{#await load() then { initialData }}
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
          bind:value={initialData.title}
        />
      </div>
      <div class="space-y-2">
        <label
          class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
          for="post-body">Body</label
        >
        <!-- <div bind:this={div} id="post-body"></div> -->
        <ToastUiEditor bind:this={toastUiEditor} body={initialData?.body}></ToastUiEditor>
      </div>
      <button
        on:click={Post__save}
        class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 h-10 px-4 py-2"
        >Save</button
      >
    </div>
  </div>
{/await}
