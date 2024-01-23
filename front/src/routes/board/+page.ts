import type { PageLoad } from './$types';
import rq from '$lib/rq/rq.svelte';

export const load: PageLoad = async ({ params, fetch, url }) => {
  const page = url.searchParams.get('page');

  const { data } = await rq.apiEndPointsWithFetch(fetch).GET(`/api/v1/posts`, {
    params: {
      query: {
        page: page
      }
    }
  });

  return {
    post: data!.data.items
  };
};
