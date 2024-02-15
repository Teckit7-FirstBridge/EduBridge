<script lang="ts">
  //@ts-ignore
  import '@toast-ui/editor/dist/toastui-editor.css';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';
  import { page } from '$app/stores';
  import ToastUiEditor from '$lib/components/ToastUiEditor.svelte';

  let comments: components['schemas']['CommentDto'][] = $state();
  let qna: components['schemas']['QnaDto'] = $state();
  let commentEditOpen: number | null = $state();

  let postId = parseInt($page.params.id);

  let body: string | undefined = $state();

  async function load() {
    if (import.meta.env.SSR) throw new Error('CSR ONLY');
    const responseComment = await rq.apiEndPoints().GET(`/api/v1/comments/{postId}`, {
      params: {
        path: {
          postId: parseInt($page.params.id)
        }
      }
    });
    comments = responseComment.data?.data!;

    const responsePost = await rq.apiEndPointsWithFetch(fetch).GET(`/api/v1/posts/qna/{id}`, {
      params: {
        path: {
          id: parseInt($page.params.id)
        }
      }
    });
    qna = responsePost.data?.data!;

    return { comments, qna };
  }

  async function deletePost() {
    const isConfirmed = confirm('문의를 삭제하시겠습니까?');

    if (isConfirmed) {
      const { data, error } = await rq.apiEndPoints().DELETE(`/api/v1/posts/qna/{id}`, {
        params: { path: { id: parseInt($page.params.id) } }
      });

      if (data) {
        rq.msgInfo(data.msg);
        rq.goTo('/member/qna');
      } else if (error) {
        rq.msgError(error.msg);
      }
    }
  }

  async function deleteComment(commentId: number) {
    const isConfirmed = confirm('댓글을 삭제하시겠습니까?');

    if (isConfirmed) {
      const { data, error } = await rq
        .apiEndPoints()
        .DELETE(`/api/v1/comments/{postId}/{commentId}`, {
          params: { path: { postId: parseInt($page.params.id), commentId: commentId } }
        });

      if (data) {
        rq.msgInfo(data.msg);
        location.reload();
      } else if (error) {
        rq.msgError(error.msg);
      }
    }
  }

  const Comment__save = async () => {
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

  const fetchModiComment = async (commentid: number, commendbody: string) => {
    const { data, error } = await rq
      .apiEndPointsWithFetch(fetch)
      .PUT('/api/v1/comments/{postId}/{commentId}', {
        params: {
          path: {
            postId: parseInt($page.params.id),
            commentId: commentid
          }
        },
        // url 설정
        body: {
          body: commendbody,
          postId: postId
        }
      });

    if (data) {
      rq.msgInfo(data.msg); //msg
      commentEditOpen = null;
    }
  };
</script>

{#await load()}
  <h1>loading...</h1>
{:then { comments, qna }}
  <div class="max-w-4xl mx-auto my-8">
    <div class="flex justify-between my-8">
      <div class="flex">
        <h1 class="text-3xl font-bold">{qna.title}</h1>
        <div
          class={`inline-flex mx-2 my-1 px-2 pt-1 text-sm font-semibold rounded-full ${qna.commentCount == 0 ? 'bg-red-100 text-red-800' : 'bg-blue-100 text-blue-800'}`}
        >
          {qna.commentCount == 0 ? '미완료' : '답변 완료'}
        </div>
      </div>
      <div class="flex">
        <div>
          <p class="text-sm text-gray-500">
            작성일: {`${new Date(qna.createDate).getFullYear()}년 ${new Date(qna.createDate).getMonth() + 1}월 ${new Date(qna.createDate).getDate()}일`}
          </p>
        </div>
      </div>
    </div>

    <div class="justify-between flex items-center mt-3 mb-20">
      <p class="text-gray-600 mb-2">작성자: {qna.authorName}</p>
      <div class="flex">
        {#if rq.member.id == qna.authorId || rq.isAdmin()}
          <div class="mb-5 mx-2 flex">
            <a class="btn btn-sm" on:click={deletePost}>문의 삭제</a>
          </div>
        {/if}
      </div>
    </div>
    <div class=" border-">
      <ToastUiEditor body={qna.body} viewer={true}></ToastUiEditor>
    </div>
    {#if rq.isAdmin()}
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
          <button class="mt-2 btn" on:click={Comment__save}>댓글 등록</button>
        </div>
      </div>
    {/if}

    <div class="border-t my-8"></div>
    <div>답변</div>
    {#if comments.length > 0}
      {#each comments as comment}
        <div class="mt-8">
          <div class="border rounded-md flex py-2 justify-between">
            <div class="mt-2">
              <div class="flex items-center">
                <div class="ml-5">
                  <span class="font-bold mr-2">{comment.authorName}</span>
                </div>
                <div>
                  <p class="text-sm text-gray-500">
                    {`${new Date(comment.createDate).getFullYear()}년 ${new Date(comment.createDate).getMonth() + 1}월 ${new Date(comment.createDate).getDate()}일`}
                  </p>
                </div>
                <div class="flex justify-end flex gap-2 ml-5 text-gray-400">
                  {#if rq.member.id == comment.authorId}
                    <button
                      class="text-xs"
                      on:click={() => {
                        commentEditOpen = comment.id;
                      }}>수정</button
                    >
                    <p>/</p>
                  {/if}
                  {#if rq.member.id == comment.authorId || rq.isAdmin()}
                    <button class="text-xs" on:click={() => deleteComment(comment.id)}>삭제</button>
                  {/if}
                </div>
              </div>
              <div class="flex items-center mx-5 my-5">
                <span class="text-gray-600">{comment.body}</span>
              </div>
            </div>
          </div>
        </div>
        {#if commentEditOpen == comment.id}
          <div class="flex justify-center items-center">
            <textarea
              class="w-full px-3 py-2 h-20 border rounded-md focus:outline-none focus:border-blue-500 mt-4 mx-2"
              rows="4"
              bind:value={comment.body}
            ></textarea>

            <button class="btn" on:click={() => fetchModiComment(comment.id, comment.body)}
              >저장</button
            >
          </div>
        {/if}
      {/each}
    {:else}
      <div class="mt-5">아직 답변이 등록 되지 않았습니다.</div>
    {/if}
  </div>
{/await}
