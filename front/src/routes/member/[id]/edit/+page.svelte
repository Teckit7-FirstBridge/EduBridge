<script lang="ts">
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  import type { components } from '$lib/types/api/v1/schema';

  let profileImageURL = $state(''); // 초기 프로필 이미지 경로
  let nickname = $state(''); // 사용자 닉네임

  async function load() {
    profileImageURL = rq.member.profileImgUrl;
    nickname = rq.member.name;
    return { profileImageURL, nickname };
  }
  // 사용자가 이미지를 업로드하면 호출될 함수
  function handleImageUpload(event) {
    const file = event.target.files[0];
    if (file) {
      console.log(file);
      // FileReader API를 사용하여 파일을 읽고, 결과를 profileImageURL에 할당
      const reader = new FileReader();
      reader.onloadend = () => {
        profileImageURL = reader.result;
      };
      reader.readAsDataURL(file);
    }
  }
</script>

{#await load()}
  <h2>loading...</h2>
{:then { profileImageURL, nickname }}
  <div class="container mx-auto p-4">
    <div class="profile-section flex flex-col items-center justify-center">
      <!-- 프로필 사진 -->
      <img src={profileImageURL} alt="프로필 사진" class="profile-picture w-64 h-64 rounded-full" />

      <!-- 이미지 업로드 입력란 -->
      <input
        type="file"
        class="file-upload mt-3"
        id="profileImageUpload"
        on:change={handleImageUpload}
      />

      <!-- 닉네임 변경 입력란 -->
      <input type="text" placeholder="닉네임" class="nickname-input mt-3" id="nicknameInput" />

      <!-- 변경 사항 적용 버튼 -->
      <button class="submit-button mt-3">수 정</button>
    </div>
  </div>
{/await}
