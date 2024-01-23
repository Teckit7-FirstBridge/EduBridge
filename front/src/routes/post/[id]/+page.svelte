<script lang="ts">
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';

  const { data } = $props<{ data: { post: components['schemas']['PostDto'] } }>();
  const { post } = data;
</script>

<div class="max-w-4xl mx-auto my-8">
  <h1 class="text-3xl font-bold mb-4">{post.title}</h1>
  <div class="justify-between flex items-center mt-10 mb-20">
    <p class="text-gray-600 mb-2">작성자: {post.authorName}</p>
    <!-- 글 작성자인경우 -->
    {#if rq.member == post.authorId}
      <div class="mb-5 mx-2">
        <a href="#" class="btn btn-sm">글 수정</a>
        <a class="btn btn-sm">글 삭제</a>
      </div>
    {/if}
    <!-- 글 수정, 삭제 -->
  </div>
  <p class="w-auto break-all">{post.body}</p>
  <div class="flex justify-center mt-20">
    <!-- 버튼에 좋아요 기능 추가 -->
    <button class="btn btn-outline hover:bg-gray-100 hover:text-black flex-col h-14">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        fill="red"
        viewBox="0 0 24 24"
        stroke-width="1.5"
        stroke="red"
        class="w-6 h-6"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"
        />
      </svg>
      <span>1</span>
    </button>
  </div>
  <div class="border-t mb-8 mt-10"></div>
  <div>
    <h1>Comment</h1>

    <div class="mt-8 flex gap-2 items-center">
      <textarea
        id="commentbody"
        class="w-full px-3 py-2 border rounded-md focus:outline-none focus:border-blue-500"
        rows="4"
        placeholder="댓글을 입력하세요..."
        bind:value={post.body}
      ></textarea>
      <!-- 댓글 등록 기능 추가 -->
      <button class="mt-2 btn">댓글 등록</button>
    </div>
  </div>

  <div class="border-t my-8"></div>

  <div>댓글</div>
  <!-- {#each commentlist as comment} -->
  <div class="mt-8">
    <div class="border rounded-md p-4">
      <div class="flex items-center justify-between mb-2">
        <div class="flex items-center">
          <span class="font-bold mr-2">comment.author.username:</span>
          <span class="text-gray-600">comment.body</span>
        </div>
        <!--    만약 댓글 작성자 라면 -->
        <div class="flex gap-2 text-gray-400">
          <!-- 수정 기능 추가 -->
          <button class="text-xs">수정</button>
          <p>/</p>
          <!-- 삭제 기능 추가 -->
          <button class="text-xs">삭제</button>
        </div>
        <!-- 댓글 수정, 삭제 -->
      </div>
      <!-- 댓글 내용이 들어가는 부분 -->
    </div>

    <!-- {#if editingCommentId === comment.id} -->
    <div class="flex justify-center items-center">
      <!--bind:value = {comment.body}추가 -->
      <textarea
        class="w-full px-3 py-2 h-20 border rounded-md focus:outline-none focus:border-blue-500 mt-4 mx-2"
        rows="4"
      ></textarea>
      <!-- on:click={() => fetchModiComment(comment.id, comment.body)} 추가 -->
      <button class="btn">저장</button>
    </div>
    <!-- {/if} -->
    <!-- 다른 댓글들도 유사한 방식으로 표시 -->
  </div>
  <!-- {/each} -->
</div>
