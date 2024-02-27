<script>
  import rq from '$lib/rq/rq.svelte';

  let modal;

  let username = '';
  let password = '';

  function openModal() {
    modal.showModal();
  }

  const login = async (event) => {
    event.preventDefault();
    const { data, error } = await rq.apiEndPointsWithFetch(fetch).POST('/api/v1/members/login', {
      // url 설정
      body: {
        username: username,
        password: password
      }
    });

    if (data) {
      rq.msgInfo(data.msg); //msg
      rq.goTo('/');
      location.reload();
    }
  };
</script>

<!--
// v0 by Vercel.
// https://v0.dev/t/o26Zp0tqtWn
-->
<div class="flex flex-col gap-20 items-center justify-center p-4">
  <h1 class="mt-8 text-6xl font-bold">EduBridge</h1>
  <div class="space-y-4">
    <div class="flex justify-center">
      <a href={rq.getKakaoLoginUrl()}>
        <img src="/kakao_login_medium_narrow.png" alt="" />
      </a>
    </div>
    <div class="flex justify-center">
      <a href={rq.getGoogleLoginUrl()}>
        <img src="/web_light_sq_ctn@1x.png" alt="" />
      </a>
    </div>
    <div class="flex justify-center">
      <button
        class="whitespace-nowrap text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50h-10 px-4 bg-white text-black w-[190px] flex items-center justify-center py-3 rounded-md shadow-md"
        onclick={openModal}>관리자 로그인</button
      >
    </div>
    <dialog id="my_modal_3" class="modal" bind:this={modal}>
      <div class="modal-box">
        <form on:submit={login} class="flex flex-col p-6">
          <!-- 폼에 on:submit 이벤트 추가 -->
          <button
            type="button"
            class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2"
            onclick={() => modal.close()}>✕</button
          >
          <label for="username">아이디</label>
          <input type="text" placeholder="username" class="w-2/3 mb-2" bind:value={username} />
          <label for="password">비밀번호</label>
          <input type="password" placeholder="password" class="max-w-xs" bind:value={password} />
          <div class="flex justify-center">
            <button
              type="submit"
              class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 h-10 px-4 py-2"
              >관리자 로그인</button
            >
          </div>
        </form>
      </div>
    </dialog>
  </div>
</div>
