<script lang="ts">
  import { onMount } from 'svelte';
  import rq from '$lib/rq/rq.svelte';

  let tags = [];
  let newTag = '';

  function addTag() {
    if (tags.length >= 5 || newTag.trim() === '') {
      rq.msgWarning('태그는 최대 5개까지 등록할 수 있습니다');
      return;
    } // 최대 태그 개수를 초과하거나 빈 태그인 경우 추가하지 않음

    if (tags.includes(newTag.trim())) {
      rq.msgWarning('이미 등록된 태그입니다');
      return;
    }
    tags = [...tags, newTag.trim()];
    newTag = '';
  }

  function removeTag(tag) {
    tags = tags.filter((t) => t !== tag);
  }

  function handleSubmit() {
    // 여기서 태그(tags)를 서버로 보낼 수 있습니다.
    console.log(tags);
  }
</script>

<div class="my-4">
  <input
    type="text"
    bind:value={newTag}
    placeholder="태그를 입력하세요"
    class="px-4 py-2 border rounded-lg mr-2 focus:outline-none focus:border-blue-500"
    on:keydown={(e) => e.key === 'Enter' && addTag()}
  />
  <button on:click={addTag} class="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600"
    >추가</button
  >
</div>

<div class="my-4">
  {#each tags as tag}
    <span
      class="inline-flex items-center bg-gray-200 text-gray-800 px-2 py-1 rounded-full mr-2 mb-2"
    >
      <span>{tag}</span>
      <button on:click={() => removeTag(tag)} class="ml-2">&times;</button>
    </span>
  {/each}
</div>

<button
  on:click={handleSubmit}
  class="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600">태그 저장하기</button
>
