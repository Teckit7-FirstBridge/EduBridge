<div class="max-w-4xl mx-auto my-8">
  <h1 class="text-3xl font-bold mb-4">title</h1>
  <div class="justify-between flex items-center mt-10 mb-20">
    <p class="text-gray-600 mb-2">작성자: author</p>
    <!-- 글 작성자인경우 -->
    <div class="mb-5 mx-2">
      <a href="#" class="btn btn-sm">글 수정</a>
      <a class="btn btn-sm">글 삭제</a>
    </div>
    <!-- 글 수정, 삭제 -->
  </div>
  <p class="w-auto break-all">body</p>
  <div class="flex justify-center mt-20">
    <!-- 버튼에 좋아요 기능 추가 -->
    <button class="btn btn-outline hover:bg-gray-100 hover:text-black flex-col">
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
          d="M6.633 10.5c.806 0 1.533-.446 2.031-1.08a9.041 9.041 0 012.861-2.4c.723-.384 1.35-.956 1.653-1.715a4.498 4.498 0 00.322-1.672V3a.75.75 0 01.75-.75A2.25 2.25 0 0116.5 4.5c0 1.152-.26 2.243-.723 3.218-.266.558.107 1.282.725 1.282h3.126c1.026 0 1.945.694 2.054 1.715.045.422.068.85.068 1.285a11.95 11.95 0 01-2.649 7.521c-.388.482-.987.729-1.605.729H13.48c-.483 0-.964-.078-1.423-.23l-3.114-1.04a4.501 4.501 0 00-1.423-.23H5.904M14.25 9h2.25M5.904 18.75c.083.205.173.405.27.602.197.4-.078.898-.523.898h-.908c-.889 0-1.713-.518-1.972-1.368a12 12 0 01-.521-3.507c0-1.553.295-3.036.831-4.398C3.387 10.203 4.167 9.75 5 9.75h1.053c.472 0 .745.556.5.96a8.958 8.958 0 00-1.302 4.665c0 1.194.232 2.333.654 3.375z"
        />
      </svg>
      <span>1</span>
    </button>
  </div>
  <div class="border-t mb-8 mt-10"></div>
  <div>
    <h1>Comment</h1>

    <div class="mt-8 flex gap-2 items-center">
      <!-- textarea         bind:value={commentbody} 추가 -->
      <textarea
        id="commentbody"
        class="w-full px-3 py-2 border rounded-md focus:outline-none focus:border-blue-500"
        rows="4"
        placeholder="댓글을 입력하세요..."
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
