<script lang="ts">
  import rq from '$lib/rq/rq.svelte';
  import { page } from '$app/stores';
  let url = $state('');
  let overview = $state('');

  const submitForm = async () => {
    const { data, error } = await rq.apiEndPoints().POST(`/api/v1/admin/{courseId}/videos`, {
      params: { path: { courseId: parseInt($page.params.id) } },
      body: {
        url,
        overView: overview
      }
    });
  };
</script>

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
          <label class="block text-gray-700 text-sm font-bold mb-2" for="video-url">
            Video URL
          </label>
          <input
            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            id="video-url"
            type="text"
            placeholder="Enter video URL"
            bind:value={url}
          />
        </div>
        <div class="mb-6">
          <label class="block text-gray-700 text-sm font-bold mb-2" for="video-summary">
            OverView
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
