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
  let title: string | undefined = $state();
  let imgUrl: string | undefined = $state();
  let initialData: components['schemas']['CourseDto'] | undefined = $state();

  async function load() {
    const { data } = await rq.apiEndPoints().GET('/api/v1/courses/{course-id}', {
      params: { path: { 'course-id': parseInt($page.params.id) } }
    });

    initialData = data!.data;

    return { initialData };
  }

  const Post__save = async () => {
    const newNoti = notieditor.editor.getMarkdown().trim();
    const newOverview = overvieweditor.editor.getMarkdown().trim();

    const { data, error } = await rq
      .apiEndPointsWithFetch(fetch)
      .PUT('/api/v1/admin/courses/{id}', {
        params: { path: { id: parseInt($page.params.id) } },
        // url 설정
        body: {
          id: parseInt($page.params.id),
          title: initialData?.title,
          notice: newNoti,
          overView: newOverview,
          imgUrl: initialData?.imgUrl
        }
      });

    if (data) {
      rq.msgInfo(data.msg); //msg
      rq.goTo('/course/' + $page.params.id);
    }
  };
</script>

{#await load()}
  <h1>loading...</h1>
{:then { initialData }}
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
          for="post-noti">Notification</label
        >
        <ToastUiEditor
          id="post-noti"
          bind:this={notieditor}
          body={initialData.notice}
          height={'calc(60dvh - 64px)'}
        ></ToastUiEditor>
        <label
          class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
          for="post-overview">OverView</label
        >
        <ToastUiEditor
          id="post-overview"
          bind:this={overvieweditor}
          body={initialData.overView}
          height={'calc(60dvh - 64px)'}
        ></ToastUiEditor>
        <label
          class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
          for="post-title">ImgUrl</label
        ><input
          class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
          id="post-title"
          placeholder="Enter ImgUrl"
          bind:value={initialData.imgUrl}
        />
      </div>

      <button
        on:click={Post__save}
        class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 h-10 px-4 py-2"
        >Save</button
      >
    </div>
  </div>
{/await}
