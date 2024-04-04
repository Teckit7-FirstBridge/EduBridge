package com.ll.edubridge.domain.member.member.controller;


import com.ll.edubridge.domain.course.courseVoter.service.CourseVoterService;
import com.ll.edubridge.domain.course.course.dto.CourseDto;
import com.ll.edubridge.domain.course.course.service.CourseService;
import com.ll.edubridge.domain.member.member.dto.MemberDto;
import com.ll.edubridge.domain.member.member.dto.MyPageDto;
import com.ll.edubridge.domain.member.member.dto.NickNameDto;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.member.member.service.MemberService;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.msg.Msg;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
import com.ll.edubridge.standard.base.Empty;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.ll.edubridge.global.exceptions.CodeMsg.E400_11_AlREADY_VISITED;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class ApiV1MemberController {
    private final MemberService memberService;
    private final Rq rq;
    private final CourseService courseService;
    private final CourseVoterService courseVoterService;

    public record LoginRequestBody(@NotBlank String username, @NotBlank String password) {
    }

    public record LoginResponseBody(@NonNull MemberDto item) {
    }

    public record isLoginResponseBody(@NonNull Boolean isLogin){
    }

    public record isAdminResponseBody(@NonNull Boolean isAdmin){
    }

    @GetMapping("/isAdmin")
    @Operation(summary = "관리자 여부 확인")
    public RsData<isAdminResponseBody> isAdmin(){

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(),Msg.E200_1_INQUIRY_SUCCEED.getMsg(),new isAdminResponseBody(rq.isAdmin()));
    }

    @GetMapping("/isLogin")
    @Operation(summary = "로그인 여부 확인")
    public RsData<isLoginResponseBody> isLogin(){

        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(),Msg.E200_1_INQUIRY_SUCCEED.getMsg(),new isLoginResponseBody(rq.isLogin()));
    }

    @PostMapping(value = "/login")
    @Operation(summary = "로그인, accessToken, refreshToken 쿠키 생성됨")
    public RsData<LoginResponseBody> login(@Valid @RequestBody LoginRequestBody body) {
        RsData<MemberService.AuthAndMakeTokensResponseBody> authAndMakeTokensRs = memberService.authAndMakeTokens(
                body.username,
                body.password
        );

        rq.setCrossDomainCookie("refreshToken", authAndMakeTokensRs.getData().refreshToken());
        rq.setCrossDomainCookie("accessToken", authAndMakeTokensRs.getData().accessToken());

        return authAndMakeTokensRs.newDataOf(
                new LoginResponseBody(
                        new MemberDto(authAndMakeTokensRs.getData().member())
                )
        );
    }

    public record MeResponseBody(@NonNull MemberDto item) {
    }

    @GetMapping("/me")
    public RsData<MeResponseBody> getMe() {
        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(),
                Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                new MeResponseBody(
                        new MemberDto(rq.getMember())
                )
        );
    }

    @GetMapping("/uuid/{uuid}")
    public RsData<MemberDto> getMember(@PathVariable String uuid){
        return RsData.of(Msg.E200_1_INQUIRY_SUCCEED.getCode(),Msg.E200_1_INQUIRY_SUCCEED.getMsg(),
                new MemberDto(
                        memberService.findByUUID(uuid).get()
                ));
    }

    @PostMapping("/logout")
    public RsData<Empty> logout() {
        rq.setLogout();

        return RsData.of(Msg.E200_6_LOGOUT_SUCCEED.getCode(),
                Msg.E200_6_LOGOUT_SUCCEED.getMsg());
    }

    public record MyPageResponseBody(@NonNull MyPageDto item){
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "마이 페이지 데이터 요청")
    public RsData<MyPageResponseBody> myPage(@PathVariable("uuid") String uuid){
        Member member = memberService.GetMemberByUUID(uuid);

        MemberDto memberDto = new MemberDto(member);

        List<CourseDto> learningCourses = member
                .getCourseEnrollList()
                .stream()
                .map(courseEnroll -> new CourseDto(courseEnroll.getCourse(), member))
                .collect(Collectors.toList());

        List<CourseDto> likeCourses = member.getCourseVoters().stream().map(courseVoter -> new CourseDto(courseVoter.getCourse(), member)).toList();

        MyPageDto myPageDto = new MyPageDto(learningCourses,likeCourses,memberDto);
        return RsData.of("200","성공",
                new MyPageResponseBody(
                        myPageDto
                ));
    }

    @PutMapping("/modifyNickName")
    @Operation(summary = "회원 닉네임 변경")
    public RsData<NickNameDto> modifyNickName(@RequestBody NickNameDto nickNameDto) {

        Member member = memberService.modifyNickname(nickNameDto);

        NickNameDto modifyNickNameDto = new NickNameDto(member.getNickname());

        return RsData.of(Msg.E200_2_MODIFY_SUCCEED.getCode(),
                Msg.E200_2_MODIFY_SUCCEED.getMsg(),
                modifyNickNameDto);
    }

    // 회원 탈퇴지만 DB에서 삭제하지 않기 때문에 drop으로 명시
    @PutMapping("/drop")
    @Operation(summary = "회원 탈퇴")
    public RsData<Empty> drop() {
        memberService.dropMember();

        return RsData.of(Msg.E200_3_DELETE_SUCCEED.getCode(),
                Msg.E200_3_DELETE_SUCCEED.getMsg());
    }

    @PutMapping("visit")
    @Operation(summary = "수동 출석체크")
    public RsData<Empty> visit(){
        Member member = rq.getMember();
        if(!member.isVisitedToday()){
            memberService.visit(member);
        } else {
            throw new GlobalException(E400_11_AlREADY_VISITED.getCode(), E400_11_AlREADY_VISITED.getMessage());
        }

        return RsData.of(Msg.E200_2_MODIFY_SUCCEED.getCode(), Msg.E200_2_MODIFY_SUCCEED.getMsg());
    }
}