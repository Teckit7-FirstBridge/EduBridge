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
<div class="flex flex-col gap-20 items-center justify-center min-h-screen bg-[#f5f5f5] p-4">
  <h1 class="text-6xl font-bold mt-8">EduBridge</h1>
  <div class="space-y-4">
    <a
      href={rq.getKakaoLoginUrl()}
      class="whitespace-nowrap text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 h-10 px-4 bg-[#ffeb3b] text-black w-[300px] flex items-center justify-center py-3 rounded-md shadow-md"
    >
      <img src="/kakao_login_medium_wide (1).png" alt="" />
    </a>
    <a
      href={rq.getGoogleLoginUrl()}
      class="whitespace-nowrap text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50h-10 px-4 bg-white text-black w-[300px] flex items-center justify-center py-3 rounded-md shadow-md"
    >
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="24"
        height="24"
        viewBox="0 0 24 24"
        fill="none"
        stroke="currentColor"
        stroke-width="2"
        stroke-linecap="round"
        stroke-linejoin="round"
        class="text-[#4285F4] mr-2"
      >
        <circle cx="12" cy="12" r="10"></circle>
        <circle cx="12" cy="12" r="4"></circle>
        <line x1="21.17" x2="12" y1="8" y2="8"></line>
        <line x1="3.95" x2="8.54" y1="6.06" y2="14"></line>
        <line x1="10.88" x2="15.46" y1="21.94" y2="14"></line>
      </svg>
      구글로 계속하기
    </a>
    <button
      class="whitespace-nowrap text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50h-10 px-4 bg-white text-black w-[300px] flex items-center justify-center py-3 rounded-md shadow-md"
      onclick={openModal}>관리자 로그인</button
    >
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
