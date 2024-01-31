<script lang="ts">
  //@ts-ignore
  import Editor from '@toast-ui/editor';
  import '@toast-ui/editor/dist/toastui-editor.css';
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import axios from 'axios';

  let divNoti: HTMLDivElement | undefined = $state();
  let divOverview: HTMLDivElement | undefined = $state();
  let bodyeditor: Editor;
  let notieditor: Editor;
  let overvieweditor: Editor;
  let title: string | undefined = $state();
  let imgUrl: string | undefined = $state();
  let price: number | undefined = $state();
  $effect(() => {
    notieditor = new Editor({
      el: divNoti,
      height: 'calc(30dvh - 60px)',
      initialEditType: 'markdown',
      previewStyle: 'vertical'
    });
    overvieweditor = new Editor({
      el: divOverview,
      height: 'calc(50dvh - 60px)',
      initialEditType: 'markdown',
      previewStyle: 'vertical'
    });
    return () => {
      notieditor.destroy();
      overvieweditor.destroy();
    };
  });

  const Post__save = async () => {
    const newNoti = notieditor.getMarkdown().trim();
    const newOverview = overvieweditor.getMarkdown().trim();

    const { data, error } = await rq.apiEndPointsWithFetch(fetch).POST('/api/v1/courses', {
      // url 설정
      body: {
        title: title,
        notice: newNoti,
        overView: newOverview,
        imgUrl: imgUrl,
        price: price
      }
    });

    if (data) {
      rq.msgInfo(data.msg); //msg
      rq.goTo('/course');
    }
  };
</script>

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
      <label
        class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
        for="post-overview">OverView</label
      >
      <div bind:this={divOverview} id="post-overview"></div>
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
        for="post-price">price</label
      ><input
        class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
        id="post-price"
        placeholder="Enter ImgUrl"
        bind:value={price}
      />
    </div>

    <button
      on:click={Post__save}
      class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 h-10 px-4 py-2"
      >Save</button
    >
  </div>
</div>
