import type { PageLoad } from './$types';
import rq from '$lib/rq/rq.svelte';

export const load: PageLoad = async ({ params, fetch, url }) => {
  const page = url.searchParams.get('page');

  const { data } = await rq.apiEndPointsWithFetch(fetch).GET(`/api/v1/courses`, {
    params: {
      query: {
        page: page
      }
    }
  });
  console.log(data!);
  return {
    course: data!.data.items
  };
};
