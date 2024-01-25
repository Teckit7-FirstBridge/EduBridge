export function filterObjectKeys(obj: Record<string, any>, keysToKeep: string[]) {
  return Object.keys(obj)
    .filter((key) => keysToKeep.includes(key))
    .reduce(
      (newObj, key) => {
        newObj[key] = obj[key];
        return newObj;
      },
      {} as Record<string, any>
    );
}

export function getUrlParams(url: string): Record<string, string> {
  // 공백 제거 및 HTML 엔티티 정리
  url = url.trim().replaceAll('&amp;', '&');

  // URL 객체 생성
  const urlObj = new URL(url, window.location.href);

  const params: Record<string, string> = {};

  // URLSearchParams 객체를 사용하여 쿼리 파라미터 추출
  urlObj.searchParams.forEach((value, key) => {
    params[key] = value;
  });

  return params;
}

export function stripIndent(str: string): string {
  // 첫 줄의 공백을 찾아서 모든 줄에서 해당 공백을 제거
  const match = str.match(/^[ \t]*(?=\S)/gm);
  if (!match) {
    return str;
  }

  // 가장 작은 공백 길이를 찾기
  const indent = Math.min(...match.map((el) => el.length));
  const re = new RegExp(`^[ \\t]{${indent}}`, 'gm');

  // 모든 줄에서 해당 공백 제거
  return str.replace(re, '');
}
