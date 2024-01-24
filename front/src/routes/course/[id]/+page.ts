import type { PageLoad } from './$types';
import rq from '$lib/rq/rq.svelte';

export const load: PageLoad = async ({ params, fetch }) => {
  const { data, error } = await rq.apiEndPointsWithFetch(fetch).GET(`/api/v1/courses/{course-id}`, {
    params: {
      path: {
        'course-id': parseInt(params.id)
      }
    }
  });
  return {
    course: data!.data
  };
};
