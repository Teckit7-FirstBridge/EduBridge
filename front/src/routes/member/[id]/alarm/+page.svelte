<script lang="ts">
  import rq from '$lib/rq/rq.svelte';

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');
    console.log('hi');
    const { data } = await rq.apiEndPoints().GET('/api/v1/notification/get', {});
    console.log(data);
    const list = data?.data;
    return { list };
  }
</script>

{#await load()}
  <h2>loading...</h2>
{:then { list }}
  {#each list as li}
    <p>{li}</p>
  {/each}
{/await}
