<script lang="ts">
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import { page } from '$app/stores';

  let comments: components['schemas']['CommentDto'][] = $state();

  let postId = parseInt($page.params.id);

  let body: string | undefined = $state();

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');
    const responseVideos = await rq.apiEndPoints().GET(`/api/v1/comments/{postId}`, {
      params: {
        path: {
          postId: parseInt($page.params.id)
        }
      }
    });
    comments = responseVideos.data?.data!;

    return { comments };
  }

  const { data } = $props<{ data: { post: components['schemas']['PostDto'] } }>();
  const { post } = data;
  console.log(post);

  async function deletePost() {
    const { data, error } = await rq.apiEndPoints().DELETE(`/api/v1/posts/{id}`, {
      params: { path: { id: parseInt($page.params.id) } }
    });
    if (data) {
      rq.msgInfo('게시글이 삭제되었습니다');
      rq.goTo('/board');
    } else if (error) {
      rq.msgError(error.msg);
    }
  }

  async function deleteComment(commentId: number) {
    const { data, error } = await rq
      .apiEndPoints()
      .DELETE(`/api/v1/comments/{postId}/{commentId}`, {
        params: { path: { postId: parseInt($page.params.id), commentId: commentId } }
      });
    if (data) {
      rq.msgInfo('댓글이 삭제되었습니다');
      location.reload();
    } else if (error) {
      rq.msgError(error.msg);
    }
  }

  async function clickLiked() {
    if (post.likedByCurrentUser) {
      const { data, error } = await rq.apiEndPoints().DELETE(`/api/v1/posts/{id}/like`, {
        params: { path: { id: parseInt($page.params.id) } }
      });
      if (data) {
        rq.msgInfo('좋아요 취소');
        window.location.reload();
      } else if (error) {
        rq.msgError(error.msg);
      }
    } else {
      const { data, error } = await rq.apiEndPoints().POST(`/api/v1/posts/{id}/like`, {
        params: { path: { id: parseInt($page.params.id) } }
      });
      if (data) {
        rq.msgInfo('좋아요!!');
        window.location.reload();
      } else if (error) {
        rq.msgError(error.msg);
      }
    }
  }

  const Comment__save = async () => {
    console.log(body);

    const { data, error } = await rq.apiEndPointsWithFetch(fetch).POST('/api/v1/comments', {
      // url 설정
      body: {
        body: body,
        postId: postId
      }
    });

    if (data) {
      rq.msgInfo(data.msg); //msg
      location.reload();
      body = '';
    }
  };
</script>

<div class="max-w-4xl mx-auto my-8">
  <div class="flex justify-between">
    <div>
      <h1 class="text-3xl font-bold mb-4">{post.title}</h1>
    </div>
    <div>
      <p class="text-nm space-y-1.5 p-6">
        {(() => {
          const now = new Date();
          const postDate = new Date(post.createDate);
          const seconds = Math.floor((now - postDate) / 1000);

          let interval = seconds / 31536000;
          if (interval > 1) {
            return Math.floor(interval) + '년 전';
          }
          interval = seconds / 2592000;
          if (interval > 1) {
            return Math.floor(interval) + '개월 전';
          }
          interval = seconds / 86400;
          if (interval > 1) {
            return Math.floor(interval) + '일 전';
          }
          interval = seconds / 3600;
          if (interval > 1) {
            return Math.floor(interval) + '시간 전';
          }
          interval = seconds / 60;
          if (interval > 1) {
            return Math.floor(interval) + '분 전';
          }
          return Math.floor(seconds) + '초 전';
        })()}
      </p>
    </div>
  </div>
  <div class="justify-between flex items-center mt-3 mb-20">
    <p class="text-gray-600 mb-2">작성자: {post.authorName}</p>
    <!-- 글 작성자인경우 -->
    {#if rq.member.id == post.authorId}
      <div class="mb-5 mx-2">
        <a href="/board/{post.id}/edit" class="btn btn-sm">글 수정</a>
        <a class="btn btn-sm" on:click={deletePost}>글 삭제</a>
      </div>
    {/if}
    <!-- 글 수정, 삭제 -->
  </div>
  <p class="w-auto break-all">{post.body}</p>
  <div class="flex justify-center mt-20">
    <!-- 버튼에 좋아요 기능 추가 -->
    <button
      class="btn btn-outline hover:bg-gray-100 hover:text-black flex-col h-14"
      on:click={clickLiked}
    >
      {#if post.likedByCurrentUser}
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
      {:else}
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke-width="1.5"
          stroke="currentColor"
          class="w-6 h-6"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"
          />
        </svg>
      {/if}
      <span>{post.voteCount}</span>
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
        bind:value={body}
      ></textarea>
      <!-- 댓글 등록 기능 추가 -->
      <button class="mt-2 btn" on:click={Comment__save}>댓글 등록</button>
    </div>
  </div>

  {#await load()}
    <div>loading...</div>
  {:then { comments }}
    <div class="border-t my-8"></div>
    <div>댓글</div>
    {#each comments as comment}
      <div class="mt-8">
        <div class="border rounded-md">
          <div>
            <div class="flex items-center">
              <div class="ml-5">
                <span class="font-bold mr-2">{comment.authorName}</span>
              </div>
              <div>
                <p class="text-nm space-y-1.5 p-6">
                  {(() => {
                    const now = new Date();
                    const commentDate = new Date(comment.createDate);
                    const seconds = Math.floor((now - commentDate) / 1000);

                    let interval = seconds / 31536000;
                    if (interval > 1) {
                      return Math.floor(interval) + '년 전';
                    }
                    interval = seconds / 2592000;
                    if (interval > 1) {
                      return Math.floor(interval) + '개월 전';
                    }
                    interval = seconds / 86400;
                    if (interval > 1) {
                      return Math.floor(interval) + '일 전';
                    }
                    interval = seconds / 3600;
                    if (interval > 1) {
                      return Math.floor(interval) + '시간 전';
                    }
                    interval = seconds / 60;
                    if (interval > 1) {
                      return Math.floor(interval) + '분 전';
                    }
                    return Math.floor(seconds) + '초 전';
                  })()}
                </p>
              </div>
              <div class="flex justify-end">
                {#if rq.member.id === comment.authorId}
                  <div class="flex gap-2 text-gray-400">
                    <!-- 수정 기능 추가 -->
                    <button class="text-xs">수정</button>
                    <p>/</p>
                    <!-- 삭제 기능 추가 -->
                    <button class="text-xs" on:click={() => deleteComment(comment.id)}>삭제</button>
                  </div>
                {/if}
              </div>
            </div>
            <div class="flex items-center mx-5 mb-5">
              <span class="text-gray-600">{comment.body}</span>
            </div>
          </div>
        </div>
      </div>
    {/each}
  {/await}

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
</div>
<!-- {/if} -->
