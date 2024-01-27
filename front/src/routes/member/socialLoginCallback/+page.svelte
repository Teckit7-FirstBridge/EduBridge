<script lang="ts">
  import { onMount } from 'svelte';
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';

  onMount(async () => {
    await rq.initAuth();
    const providerTypeCode = $page.url.searchParams.get('providerTypeCode');
    const username = rq.member.name;
    console.log(providerTypeCode);
    if (username) {
      rq.msgInfo(`${username}님 환영합니다.`);
    } else {
      rq.msgInfo('환영합니다.');
    }
    rq.replace('/');
  });
</script>
