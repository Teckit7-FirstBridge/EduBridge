<script lang="ts">
  import rq from '$lib/rq/rq.svelte';
  import { page } from '$app/stores';
  import type { components } from '$lib/types/api/v1/schema';
  let url: string | undefined = $state('');
  let imgUrl: string | undefined = $state('');
  let overview: string | undefined = $state('');
  let video: components['schemas']['VideoDto'] | undefined = $state();
  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');
    const { data, error } = await rq.apiEndPoints().GET(`/api/v1/courses/{courseId}/videos/{id}`, {
      params: {
        path: {
          courseId: parseInt($page.params.id),
          id: parseInt($page.params.videoid)
        }
      }
    });
    video = data?.data;
    url = video?.url;
    imgUrl = video?.imgUrl;
    overview = video?.overView;
    return video;
  }

  const submitForm = async () => {
    const { data, error } = await rq.apiEndPoints().PUT(`/api/v1/admin/{courseId}/videos/{id}`, {
      params: { path: { courseId: parseInt($page.params.id), id: parseInt($page.params.videoid) } },
      body: {
        id: parseInt($page.params.videoid),
        url: url!,
        imgUrl: imgUrl,
        overView: overview,
        courseId: parseInt($page.params.id)
      }
    });
    if (data) {
      rq.goTo(`/course/${$page.params.id}`);
    }
  };
</script>

{#await load()}
  <div>loading...</div>
{:then video}
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
          <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="video-url">
              Img URL
            </label>
            <input
              class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              id="video-imgUrl"
              type="text"
              placeholder="Enter video URL"
              bind:value={imgUrl}
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
{/await}
