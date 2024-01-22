<script lang="ts">
  //@ts-ignore
  import Editor from '@toast-ui/editor';
  import '@toast-ui/editor/dist/toastui-editor.css';
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';
  let div: HTMLDivElement;
  let editor: Editor;
  let oldBody: string = '';

  $effect(() => {
    editor = new Editor({
      el: div,
      height: 'calc(100dvh - 60px)',
      initialEditType: 'markdown',
      previewStyle: 'vertical'
    });
    return () => {
      editor.destroy();
    };
  });

  function saveToLocalStorage(id: number, body: string) {
    const key = 'posts_' + id;
    // LocalStorage에서 기존 데이터를 가져옵니다.
    const existingData = localStorage.getItem(key);

    // 기존 데이터가 있으면 JSON으로 파싱하고, 없으면 빈 배열을 사용합니다.
    const posts = existingData ? JSON.parse(existingData) : [];

    // 새 데이터를 배열에 추가합니다.
    posts.push({
      id,
      body: body,
      date: new Date().toISOString()
    });

    // 배열의 크기가 10을 초과하면 가장 오래된 항목(첫 번째 항목)을 제거합니다.
    if (posts.length > 10) {
      posts.shift(); // 배열의 첫 번째 항목을 제거합니다.
    }

    // 변경된 배열을 JSON 문자열로 변환하여 LocalStorage에 저장합니다.
    localStorage.setItem(key, JSON.stringify(posts));
  }

  const Post__save = async () => {
    const newBody = editor.getMarkdown().trim();
    console.log(newBody);
    if (oldBody === newBody) {
      return;
    }

    const { data, error } = await rq.apiEndPoints().PUT('/api/v1/posts/{id}', {
      params: { path: { id: parseInt($page.params.id) } },
      body: { body: newBody }
    });

    oldBody = newBody;

    saveToLocalStorage(parseInt($page.params.id), newBody);

    if (data) {
      rq.msgInfo(data.msg);
    }
  };
</script>

<div class="grid min-h-screen w-full lg:grid-cols-[280px_1fr]">
  <div class="hidden w-64 border-r bg-gray-100/40 lg:block dark:bg-gray-800/40">
    <div class="flex h-full max-h-screen flex-col gap-2">
      <div class="flex items-center h-16 px-4 border-b border-gray-200 dark:border-gray-800">
        <a class="flex items-center gap-2 font-semibold" href="#"
          ><svg
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            class="h-6 w-6"
          >
            <path d="m8 3 4 8 5-5 5 15H2L8 3z"></path></svg
          ><span class="">EduBridge</span></a
        >
      </div>
      <ul class="menu w-56 rounded-box">
        <li>
          <a
            class="flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors hover:bg-gray-100 hover:text-gray-900 dark:hover:bg-gray-800 dark:hover:text-gray-50"
            href="#"
          >
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
                d="m2.25 12 8.954-8.955c.44-.439 1.152-.439 1.591 0L21.75 12M4.5 9.75v10.125c0 .621.504 1.125 1.125 1.125H9.75v-4.875c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125V21h4.125c.621 0 1.125-.504 1.125-1.125V9.75M8.25 21h8.25"
              />
            </svg>

            DashBoard
          </a>
        </li>

        <li>
          <a
            class="flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors hover:bg-gray-100 hover:text-gray-900 dark:hover:bg-gray-800 dark:hover:text-gray-50"
            href="#"
          >
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
                d="M14.857 17.082a23.848 23.848 0 0 0 5.454-1.31A8.967 8.967 0 0 1 18 9.75V9A6 6 0 0 0 6 9v.75a8.967 8.967 0 0 1-2.312 6.022c1.733.64 3.56 1.085 5.455 1.31m5.714 0a24.255 24.255 0 0 1-5.714 0m5.714 0a3 3 0 1 1-5.714 0"
              />
            </svg>

            공지사항
          </a>
        </li>
        <li>
          <a
            class="flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors hover:bg-gray-100 hover:text-gray-900 dark:hover:bg-gray-800 dark:hover:text-gray-50"
            href="#"
          >
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
                d="M12 6.042A8.967 8.967 0 0 0 6 3.75c-1.052 0-2.062.18-3 .512v14.25A8.987 8.987 0 0 1 6 18c2.305 0 4.408.867 6 2.292m0-14.25a8.966 8.966 0 0 1 6-2.292c1.052 0 2.062.18 3 .512v14.25A8.987 8.987 0 0 0 18 18a8.967 8.967 0 0 0-6 2.292m0-14.25v14.25"
              />
            </svg>

            요약 노트
          </a>
        </li>
        <li>
          <details open>
            <summary
              ><svg
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
                  d="M8.25 6.75h12M8.25 12h12m-12 5.25h12M3.75 6.75h.007v.008H3.75V6.75Zm.375 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0ZM3.75 12h.007v.008H3.75V12Zm.375 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm-.375 5.25h.007v.008H3.75v-.008Zm.375 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Z"
                />
              </svg>
              강의 목록</summary
            >
            <ul>
              <li><a>강의 1</a></li>
              <li><a>강의 2</a></li>
            </ul>
          </details>
        </li>
      </ul>
    </div>
  </div>
  <div bind:this={div}></div>
  <button on:click={Post__save}>저장</button>
</div>
