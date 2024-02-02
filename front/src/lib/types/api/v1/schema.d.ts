/**
 * This file was auto-generated by openapi-typescript.
 * Do not make direct changes to the file.
 */


export interface paths {
  "/api/v1/posts/{id}": {
    /** 글 상세 정보 */
    get: operations["getDetail"];
    /** 글 수정 */
    put: operations["modify"];
    /** 글 삭제 */
    delete: operations["delete"];
  };
  "/api/v1/courses/{videoid}/note/{noteId}": {
    /** 강의 요약 노트 수정 */
    put: operations["modify_1"];
    /** 강의 요약 노트 삭제 */
    delete: operations["delete_1"];
  };
  "/api/v1/courses/{id}": {
    /** 강좌 수정 */
    put: operations["modify_2"];
    /** 강좌 삭제 */
    delete: operations["delete_2"];
  };
  "/api/v1/comments/{postId}/{commentId}": {
    /** 댓글 수정 */
    put: operations["modifyComment"];
    /** 댓글 삭제 */
    delete: operations["deleteComment"];
  };
  "/api/v1/admin/{courseId}/videos/{id}": {
    /** 강의 수정 */
    put: operations["modify_3"];
    /** 강의 삭제 */
    delete: operations["delete_3"];
  };
  "/api/v1/posts": {
    /** 글 다건조회 */
    get: operations["getPosts_1"];
    /** 글 등록 */
    post: operations["createPost"];
  };
  "/api/v1/posts/{id}/like": {
    /** 글 추천 */
    post: operations["vote"];
    /** 글 추천 취소 */
    delete: operations["deleteVote"];
  };
  "/api/v1/posts/qna": {
    /** 1대1 문의 목록 */
    get: operations["getMyQna"];
    /** 1대1 문의 등록 */
    post: operations["createQna"];
  };
  "/api/v1/members/logout": {
    post: operations["logout"];
  };
  "/api/v1/members/login": {
    /** 로그인, accessToken, refreshToken 쿠키 생성됨 */
    post: operations["login"];
  };
  "/api/v1/enroll/{courseId}": {
    /** 수강 등록 */
    post: operations["create"];
  };
  "/api/v1/courses": {
    /** 강좌 다건 조회 */
    get: operations["getCourses"];
    /** 강좌 등록 */
    post: operations["createCourse"];
  };
  "/api/v1/courses/{videoid}/note": {
    /** 강의 요약 노트 등록 */
    post: operations["create_1"];
  };
  "/api/v1/comments": {
    /** 댓글 등록 */
    post: operations["createComment"];
  };
  "/api/v1/comments/{postId}/{commentId}/like": {
    /** 댓글 추천 */
    post: operations["voteComment"];
    /** 댓글 추천 취소 */
    delete: operations["deleteVoteComment"];
  };
  "/api/v1/admin/{courseId}/videos": {
    /** 강의 등록 */
    post: operations["createVideo"];
  };
  "/api/v1": {
    /** 홈화면 최신 강좌 N개조회 */
    get: operations["getPosts"];
  };
  "/api/v1/posts/qna/{id}": {
    /** 1대1 문의 상세 정보 */
    get: operations["getQnaDetail"];
    /** 1대1 문의 삭제 */
    delete: operations["deleteQna"];
  };
  "/api/v1/members/me": {
    get: operations["getMe"];
  };
  "/api/v1/enroll": {
    /** 수업 목록 조회 */
    get: operations["getSummaryNote"];
  };
  "/api/v1/courses/{videoid}/note/{note-id}": {
    /** 강의 요약 노트 상세 보기 */
    get: operations["getSummaryNote_1"];
  };
  "/api/v1/courses/{videoid}/note/admin": {
    /** 비디오별 강의노트 조회(관리자 기능) */
    get: operations["getSummaryNoteAdmin"];
  };
  "/api/v1/courses/{courseId}/videos": {
    /** 강의 리스트 */
    get: operations["getVideos"];
  };
  "/api/v1/courses/{courseId}/videos/{id}": {
    /** 특정 강의 */
    get: operations["getVideos_1"];
  };
  "/api/v1/courses/{course-id}": {
    /** 강좌 상세 조회 */
    get: operations["getCourse"];
  };
  "/api/v1/comments/{postId}": {
    /** 댓글 목록 */
    get: operations["getComments"];
  };
  "/api/v1/admin/summaryNotes": {
    /** 최신 요약노트 */
    get: operations["getSummeryNotes"];
  };
  "/api/v1/admin/summaryNotes/list": {
    /** 요약노트 목록 */
    get: operations["getAllSummeryNotes"];
  };
  "/api/v1/admin/reports": {
    /** 신고 게시물 최신순 */
    get: operations["getReportedPosts"];
  };
  "/api/v1/admin/reports/list": {
    /** 신고 게시물 목록 */
    get: operations["getAllReportedPosts"];
  };
  "/api/v1/admin/members": {
    /** 회원 최신순 */
    get: operations["getMembers"];
  };
  "/api/v1/admin/members/list": {
    /** 회원 목록 */
    get: operations["getAllMembers"];
  };
  "/api/v1/admin/courses": {
    /** 강좌 최신순 */
    get: operations["getRecentCourses"];
  };
}

export type webhooks = Record<string, never>;

export interface components {
  schemas: {
    PostDto: {
      /** Format: int64 */
      id: number;
      /** Format: date-time */
      createDate: string;
      /** Format: int64 */
      authorId: number;
      authorName: string;
      title: string;
      body: string;
      /** Format: int32 */
      voteCount?: number;
      likedByCurrentUser?: boolean;
    };
    RsDataPostDto: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["PostDto"];
      success: boolean;
      fail: boolean;
    };
    CreateSummaryNoteDto: {
      content?: string;
    };
    Course: {
      /** Format: int64 */
      id?: number;
      /** Format: date-time */
      createDate?: string;
      title?: string;
      notice?: string;
      imgUrl?: string;
      overView?: string;
      /** Format: int32 */
      price?: number;
      videoList?: components["schemas"]["Video"][];
      voter?: components["schemas"]["Member"][];
    };
    CourseEnroll: {
      /** Format: int64 */
      id?: number;
      /** Format: date-time */
      createDate?: string;
      course?: components["schemas"]["Course"];
      member?: components["schemas"]["Member"];
    };
    GrantedAuthority: {
      authority?: string;
    };
    Member: {
      /** Format: int64 */
      id?: number;
      /** Format: date-time */
      createDate?: string;
      username?: string;
      password?: string;
      nickname?: string;
      /** Format: int32 */
      point?: number;
      report?: boolean;
      refreshToken?: string;
      profileImgUrl?: string;
      visitedToday?: boolean;
      courseEnrollList?: components["schemas"]["CourseEnroll"][];
      name?: string;
      authoritiesAsStringList?: string[];
      profileImgUrlOrDefault?: string;
      authorities?: components["schemas"]["GrantedAuthority"][];
    };
    RsDataSummaryNoteDto: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["SummaryNoteDto"];
      success: boolean;
      fail: boolean;
    };
    SummaryNote: {
      /** Format: int64 */
      id?: number;
      /** Format: date-time */
      createDate?: string;
      content?: string;
      writer?: components["schemas"]["Member"];
      video?: components["schemas"]["Video"];
      /** Format: int64 */
      score?: number;
    };
    SummaryNoteDto: {
      /** Format: int64 */
      id?: number;
      member?: components["schemas"]["Member"];
      content?: string;
    };
    Video: {
      /** Format: int64 */
      id?: number;
      /** Format: date-time */
      createDate?: string;
      url?: string;
      imgUrl?: string;
      overView?: string;
      course?: components["schemas"]["Course"];
      summaryNotes?: components["schemas"]["SummaryNote"][];
      keywords?: string;
    };
    CourseDto: {
      /** Format: int64 */
      id?: number;
      title?: string;
      notice?: string;
      imgUrl?: string;
      overView?: string;
      /** Format: int32 */
      price?: number;
    };
    RsDataCourseDto: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["CourseDto"];
      success: boolean;
      fail: boolean;
    };
    CreateCommentDto: {
      body: string;
      /** Format: int64 */
      postId: number;
    };
    CommentDto: {
      /** Format: int64 */
      id: number;
      /** Format: date-time */
      createDate: string;
      /** Format: int64 */
      authorId: number;
      authorName: string;
      body: string;
      likedByCurrentUser?: boolean;
      /** Format: int64 */
      postId: number;
    };
    RsDataCommentDto: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["CommentDto"];
      success: boolean;
      fail: boolean;
    };
    VideoDto: {
      /** Format: int64 */
      id: number;
      url: string;
      overView?: string;
      imgUrl: string;
      /** Format: int64 */
      courseId: number;
      summaryNotes: components["schemas"]["SummaryNoteDto"][];
      keywords: string;
    };
    RsDataVideoDto: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["VideoDto"];
      success: boolean;
      fail: boolean;
    };
    CreatePostDto: {
      title: string;
      body: string;
    };
    RsDataCreatePostDto: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["CreatePostDto"];
      success: boolean;
      fail: boolean;
    };
    RsDataVoid: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: Record<string, never>;
      success: boolean;
      fail: boolean;
    };
    QnaDto: {
      /** Format: int64 */
      id: number;
      /** Format: date-time */
      createDate: string;
      /** Format: int64 */
      authorId: number;
      authorName: string;
      title: string;
      body: string;
    };
    RsDataQnaDto: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["QnaDto"];
      success: boolean;
      fail: boolean;
    };
    Empty: Record<string, never>;
    RsDataEmpty: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["Empty"];
      success: boolean;
      fail: boolean;
    };
    LoginRequestBody: {
      username: string;
      password: string;
    };
    LoginResponseBody: {
      item: components["schemas"]["MemberDto"];
    };
    MemberDto: {
      /** Format: int64 */
      id: number;
      /** Format: date-time */
      createDate: string;
      name: string;
      profileImgUrl: string;
      authorities: string[];
      visitedToday: boolean;
    };
    RsDataLoginResponseBody: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["LoginResponseBody"];
      success: boolean;
      fail: boolean;
    };
    CourseEnrollDto: {
      /** Format: int64 */
      id?: number;
    };
    RsDataCourseEnrollDto: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["CourseEnrollDto"];
      success: boolean;
      fail: boolean;
    };
    CreateCourseDto: {
      title?: string;
      notice?: string;
      imgUrl?: string;
      overView?: string;
      /** Format: int32 */
      price?: number;
    };
    RsDataCreateCourseDto: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["CreateCourseDto"];
      success: boolean;
      fail: boolean;
    };
    RsDataCreateCommentDto: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["CreateCommentDto"];
      success: boolean;
      fail: boolean;
    };
    CreateVideoDto: {
      url: string;
      overView?: string;
      /** Format: int64 */
      courseId: number;
      imgUrl: string;
      keywords: string;
    };
    RsDataCreateVideoDto: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["CreateVideoDto"];
      success: boolean;
      fail: boolean;
    };
    GetPostsResponseBody: {
      items: components["schemas"]["CourseDto"][];
    };
    RsDataGetPostsResponseBody: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["GetPostsResponseBody"];
      success: boolean;
      fail: boolean;
    };
    RsDataListQnaDto: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["QnaDto"][];
      success: boolean;
      fail: boolean;
    };
    MeResponseBody: {
      item: components["schemas"]["MemberDto"];
    };
    RsDataMeResponseBody: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["MeResponseBody"];
      success: boolean;
      fail: boolean;
    };
    GetCourseEnrollResponsebody: {
      items: components["schemas"]["CourseEnrollDto"][];
    };
    RsDataGetCourseEnrollResponsebody: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["GetCourseEnrollResponsebody"];
      success: boolean;
      fail: boolean;
    };
    GetCoursesResponsebody: {
      items: components["schemas"]["CourseDto"][];
    };
    RsDataGetCoursesResponsebody: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["GetCoursesResponsebody"];
      success: boolean;
      fail: boolean;
    };
    GetSummaryNoteResponsebody: {
      items: components["schemas"]["SummaryNoteDto"][];
    };
    RsDataGetSummaryNoteResponsebody: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["GetSummaryNoteResponsebody"];
      success: boolean;
      fail: boolean;
    };
    RsDataListVideoDto: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["VideoDto"][];
      success: boolean;
      fail: boolean;
    };
    RsDataListCommentDto: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["CommentDto"][];
      success: boolean;
      fail: boolean;
    };
    RecentSummaryNoteDto: {
      /** Format: int64 */
      id: number;
      name: string;
      courseName: string;
      /** Format: int64 */
      videoId: number;
      /** Format: int64 */
      courseId: number;
      /** Format: date-time */
      createDate: string;
    };
    RsDataListRecentSummaryNoteDto: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["RecentSummaryNoteDto"][];
      success: boolean;
      fail: boolean;
    };
    GetNotesResponseBody: {
      itemPage?: components["schemas"]["PageDtoRecentSummaryNoteDto"];
    };
    PageDtoRecentSummaryNoteDto: {
      /** Format: int64 */
      totalElementsCount: number;
      /** Format: int64 */
      pageElementsCount: number;
      /** Format: int64 */
      totalPagesCount: number;
      /** Format: int32 */
      number: number;
      content: components["schemas"]["RecentSummaryNoteDto"][];
    };
    RsDataGetNotesResponseBody: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["GetNotesResponseBody"];
      success: boolean;
      fail: boolean;
    };
    ReportedPostDto: {
      /** Format: int64 */
      id: number;
      /** Format: date-time */
      createDate: string;
      /** Format: int64 */
      authorId: number;
      authorName: string;
      title: string;
    };
    RsDataListReportedPostDto: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["ReportedPostDto"][];
      success: boolean;
      fail: boolean;
    };
    RecentMemberDto: {
      /** Format: int64 */
      id: number;
      /** Format: date-time */
      createDate: string;
      name: string;
      report: boolean;
    };
    RsDataListRecentMemberDto: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["RecentMemberDto"][];
      success: boolean;
      fail: boolean;
    };
    PageDtoRecentMemberDto: {
      /** Format: int64 */
      totalElementsCount: number;
      /** Format: int64 */
      pageElementsCount: number;
      /** Format: int64 */
      totalPagesCount: number;
      /** Format: int32 */
      number: number;
      content: components["schemas"]["RecentMemberDto"][];
    };
    RsDataPageDtoRecentMemberDto: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["PageDtoRecentMemberDto"];
      success: boolean;
      fail: boolean;
    };
    RecentCourseDto: {
      /** Format: int64 */
      id: number;
      title: string;
    };
    RsDataListRecentCourseDto: {
      resultCode: string;
      /** Format: int32 */
      statusCode: number;
      msg: string;
      data: components["schemas"]["RecentCourseDto"][];
      success: boolean;
      fail: boolean;
    };
  };
  responses: never;
  parameters: never;
  requestBodies: never;
  headers: never;
  pathItems: never;
}

export type $defs = Record<string, never>;

export type external = Record<string, never>;

export interface operations {

  /** 글 상세 정보 */
  getDetail: {
    parameters: {
      path: {
        id: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataPostDto"];
        };
      };
    };
  };
  /** 글 수정 */
  modify: {
    parameters: {
      path: {
        id: number;
      };
    };
    requestBody: {
      content: {
        "application/json": components["schemas"]["PostDto"];
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataPostDto"];
        };
      };
    };
  };
  /** 글 삭제 */
  delete: {
    parameters: {
      path: {
        id: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataEmpty"];
        };
      };
    };
  };
  /** 강의 요약 노트 수정 */
  modify_1: {
    parameters: {
      path: {
        noteId: number;
      };
    };
    requestBody: {
      content: {
        "application/json": components["schemas"]["CreateSummaryNoteDto"];
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataSummaryNoteDto"];
        };
      };
    };
  };
  /** 강의 요약 노트 삭제 */
  delete_1: {
    parameters: {
      path: {
        noteId: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataEmpty"];
        };
      };
    };
  };
  /** 강좌 수정 */
  modify_2: {
    parameters: {
      path: {
        id: number;
      };
    };
    requestBody: {
      content: {
        "application/json": components["schemas"]["CourseDto"];
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataCourseDto"];
        };
      };
    };
  };
  /** 강좌 삭제 */
  delete_2: {
    parameters: {
      path: {
        id: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataEmpty"];
        };
      };
    };
  };
  /** 댓글 수정 */
  modifyComment: {
    parameters: {
      path: {
        commentId: number;
      };
    };
    requestBody: {
      content: {
        "application/json": components["schemas"]["CreateCommentDto"];
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataCommentDto"];
        };
      };
    };
  };
  /** 댓글 삭제 */
  deleteComment: {
    parameters: {
      path: {
        commentId: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataEmpty"];
        };
      };
    };
  };
  /** 강의 수정 */
  modify_3: {
    parameters: {
      path: {
        courseId: number;
        id: number;
      };
    };
    requestBody: {
      content: {
        "application/json": components["schemas"]["VideoDto"];
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataVideoDto"];
        };
      };
    };
  };
  /** 강의 삭제 */
  delete_3: {
    parameters: {
      path: {
        courseId: number;
        id: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataEmpty"];
        };
      };
    };
  };
  /** 글 다건조회 */
  getPosts_1: {
    parameters: {
      query?: {
        page?: number;
        kw?: string;
        kwType?: "ALL" | "TITLE" | "BODY" | "NAME";
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataGetPostsResponseBody"];
        };
      };
    };
  };
  /** 글 등록 */
  createPost: {
    requestBody: {
      content: {
        "application/json": components["schemas"]["CreatePostDto"];
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataCreatePostDto"];
        };
      };
    };
  };
  /** 글 추천 */
  vote: {
    parameters: {
      path: {
        id: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataVoid"];
        };
      };
    };
  };
  /** 글 추천 취소 */
  deleteVote: {
    parameters: {
      path: {
        id: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataVoid"];
        };
      };
    };
  };
  /** 1대1 문의 목록 */
  getMyQna: {
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataListQnaDto"];
        };
      };
    };
  };
  /** 1대1 문의 등록 */
  createQna: {
    requestBody: {
      content: {
        "application/json": components["schemas"]["QnaDto"];
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataQnaDto"];
        };
      };
    };
  };
  logout: {
    responses: {
      /** @description OK */
      200: {
        content: {
          "*/*": components["schemas"]["RsDataEmpty"];
        };
      };
    };
  };
  /** 로그인, accessToken, refreshToken 쿠키 생성됨 */
  login: {
    requestBody: {
      content: {
        "application/json": components["schemas"]["LoginRequestBody"];
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "*/*": components["schemas"]["RsDataLoginResponseBody"];
        };
      };
    };
  };
  /** 수강 등록 */
  create: {
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataCourseEnrollDto"];
        };
      };
    };
  };
  /** 강좌 다건 조회 */
  getCourses: {
    parameters: {
      query?: {
        page?: number;
        kw?: string;
        kwType?: "ALL" | "TITLE" | "NAME";
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataGetCoursesResponsebody"];
        };
      };
    };
  };
  /** 강좌 등록 */
  createCourse: {
    requestBody: {
      content: {
        "application/json": components["schemas"]["CreateCourseDto"];
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataCreateCourseDto"];
        };
      };
    };
  };
  /** 강의 요약 노트 등록 */
  create_1: {
    parameters: {
      path: {
        videoid: number;
      };
    };
    requestBody: {
      content: {
        "application/json": components["schemas"]["CreateSummaryNoteDto"];
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataSummaryNoteDto"];
        };
      };
    };
  };
  /** 댓글 등록 */
  createComment: {
    requestBody: {
      content: {
        "application/json": components["schemas"]["CreateCommentDto"];
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataCreateCommentDto"];
        };
      };
    };
  };
  /** 댓글 추천 */
  voteComment: {
    parameters: {
      path: {
        commentId: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataVoid"];
        };
      };
    };
  };
  /** 댓글 추천 취소 */
  deleteVoteComment: {
    parameters: {
      path: {
        commentId: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataVoid"];
        };
      };
    };
  };
  /** 강의 등록 */
  createVideo: {
    parameters: {
      path: {
        courseId: number;
      };
    };
    requestBody: {
      content: {
        "application/json": components["schemas"]["CreateVideoDto"];
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataCreateVideoDto"];
        };
      };
    };
  };
  /** 홈화면 최신 강좌 N개조회 */
  getPosts: {
    responses: {
      /** @description OK */
      200: {
        content: {
          "*/*": components["schemas"]["RsDataGetPostsResponseBody"];
        };
      };
    };
  };
  /** 1대1 문의 상세 정보 */
  getQnaDetail: {
    parameters: {
      path: {
        id: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataQnaDto"];
        };
      };
    };
  };
  /** 1대1 문의 삭제 */
  deleteQna: {
    parameters: {
      path: {
        id: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataEmpty"];
        };
      };
    };
  };
  getMe: {
    responses: {
      /** @description OK */
      200: {
        content: {
          "*/*": components["schemas"]["RsDataMeResponseBody"];
        };
      };
    };
  };
  /** 수업 목록 조회 */
  getSummaryNote: {
    parameters: {
      query?: {
        page?: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataGetCourseEnrollResponsebody"];
        };
      };
    };
  };
  /** 강의 요약 노트 상세 보기 */
  getSummaryNote_1: {
    parameters: {
      path: {
        "note-id": number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataSummaryNoteDto"];
        };
      };
    };
  };
  /** 비디오별 강의노트 조회(관리자 기능) */
  getSummaryNoteAdmin: {
    parameters: {
      query?: {
        page?: number;
      };
      path: {
        videoid: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataGetSummaryNoteResponsebody"];
        };
      };
    };
  };
  /** 강의 리스트 */
  getVideos: {
    parameters: {
      path: {
        courseId: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataListVideoDto"];
        };
      };
    };
  };
  /** 특정 강의 */
  getVideos_1: {
    parameters: {
      path: {
        courseId: number;
        id: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataVideoDto"];
        };
      };
    };
  };
  /** 강좌 상세 조회 */
  getCourse: {
    parameters: {
      path: {
        "course-id": number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataCourseDto"];
        };
      };
    };
  };
  /** 댓글 목록 */
  getComments: {
    parameters: {
      path: {
        postId: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataListCommentDto"];
        };
      };
    };
  };
  /** 최신 요약노트 */
  getSummeryNotes: {
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataListRecentSummaryNoteDto"];
        };
      };
    };
  };
  /** 요약노트 목록 */
  getAllSummeryNotes: {
    parameters: {
      query?: {
        page?: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataGetNotesResponseBody"];
        };
      };
    };
  };
  /** 신고 게시물 최신순 */
  getReportedPosts: {
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataListReportedPostDto"];
        };
      };
    };
  };
  /** 신고 게시물 목록 */
  getAllReportedPosts: {
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataListReportedPostDto"];
        };
      };
    };
  };
  /** 회원 최신순 */
  getMembers: {
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataListRecentMemberDto"];
        };
      };
    };
  };
  /** 회원 목록 */
  getAllMembers: {
    parameters: {
      query?: {
        page?: number;
      };
    };
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataPageDtoRecentMemberDto"];
        };
      };
    };
  };
  /** 강좌 최신순 */
  getRecentCourses: {
    responses: {
      /** @description OK */
      200: {
        content: {
          "application/json": components["schemas"]["RsDataListRecentCourseDto"];
        };
      };
    };
  };
}
