package com.ll.edubridge.domain.member.member.service;

import com.ll.edubridge.domain.course.courseEnroll.service.CourseEnrollService;
import com.ll.edubridge.domain.member.member.dto.NickNameDto;
import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.member.member.repository.MemberRepository;
import com.ll.edubridge.domain.notification.entity.NotificationType;
import com.ll.edubridge.domain.notification.service.NotificationService;
import com.ll.edubridge.domain.point.point.entity.PointType;
import com.ll.edubridge.domain.point.point.service.PointService;
import com.ll.edubridge.global.exceptions.CodeMsg;
import com.ll.edubridge.global.exceptions.GlobalException;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.rsData.RsData;
import com.ll.edubridge.global.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static com.ll.edubridge.global.app.AppConfig.ALPHANUMERIC;
import static com.ll.edubridge.global.app.AppConfig.ALPHANUMERIC_LENGTH;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenService authTokenService;
    private final PointService pointService;
    private final CourseEnrollService courseEnrollService;
    private final NotificationService notificationService;
    private final Rq rq;

    @Transactional
    public void addPoint(PointType pointType){
        Member member = rq.getMember();

        member.setPoint(member.getPoint()+pointType.getAmount());

        memberRepository.save(member);
    }

    @Transactional
    public void subPoint(PointType pointType){
        Member member = rq.getMember();

        member.setPoint(member.getPoint()-pointType.getAmount());

        memberRepository.save(member);
    }

    @Transactional
    public RsData<Member> join(String username, String password) {
        return join(username, password, username, "");
    }

    @Transactional
    public RsData<Member> join(String username, String password, String nickname, String profileImgUrl) {
        if (findByUsername(username).isPresent()) {
            return RsData.of("400-2", "이미 존재하는 회원입니다.");
        }
        String uuid = generateRandomString();
        while(this.checkUUID(uuid)){
            uuid = generateRandomString();
        }

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .refreshToken(authTokenService.genRefreshToken())
                .nickname(nickname)
                .profileImgUrl(profileImgUrl)
                .point(PointType.Welcome.getAmount())
                .uuid(uuid)
                .build();

        memberRepository.save(member);
        pointService.addPoint(PointType.Welcome, member); // 포인트 내역 추가

        return RsData.of("200", "%s님 환영합니다. 회원가입이 완료되었습니다. 로그인 후 이용해주세요.".formatted(member.getUsername()), member);
    }

    public static String generateRandomString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(ALPHANUMERIC_LENGTH);

        for (int i = 0; i < ALPHANUMERIC_LENGTH; i++) {
            int randomIndex = random.nextInt(ALPHANUMERIC.length());
            char randomChar = ALPHANUMERIC.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    public Boolean checkUUID(String uuid){
        return memberRepository.existsMemberByUuid(uuid);
    }

    @Transactional
    public RsData<Member> modifyOrJoin(String username, String providerTypeCode, String nickname, String profileImgUrl) {
        Member member = findByUsername(username).orElse(null);

        if (member == null) {
            return join(
                    username, "", nickname, profileImgUrl
            );
        }
        return modify(member, profileImgUrl);
    }

    @Transactional
    public RsData<Member> modify(Member member, String profileImgUrl) {
        member.setNickname(member.getNickname());
        member.setProfileImgUrl(profileImgUrl);

        return RsData.of("200-2","회원정보가 수정되었습니다.".formatted(member.getUsername()), member);
    }

    @Transactional
    public Member modifyNickname(NickNameDto nicknameDto) {
        Member member = rq.getMember();
        member.setNickname(nicknameDto.getNickName());
        System.out.println(member.getNickname()+"바뀜?");

        memberRepository.save(member);
        Member member1 = rq.getMember();
        System.out.println(member1.getNickname()+"??");

        return member;
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public long count() {
        return memberRepository.count();
    }

    public RsData<Member> whenSocialLogin(String providerTypeCode, String username, String nickname, String profileImgUrl) {
        Optional<Member> opMember = findByUsername(username);

        if (opMember.isPresent()) return RsData.of("200", "이미 존재합니다.", opMember.get());

        return join(username, "");
    }

    public void isReported(Member member) {
        member.setReport(true);
    }

    public Page<Member> findAll(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public Optional<Member> findByUUID(String uuid){return memberRepository.findByUuid(uuid);}

    public Member GetMemberByUUID(String uuid){
        Optional<Member> member = this.findByUUID(uuid);

        if(member.isPresent()){
            return member.get();
        } else {
            throw new GlobalException(CodeMsg.E404_1_DATA_NOT_FIND.getCode(), CodeMsg.E404_1_DATA_NOT_FIND.getMessage());
        }
    }

    public Member getMember(Long id) {
        Optional<Member> member = this.findById(id);

        if (member.isPresent()) {
            return member.get();
        } else {
            throw new GlobalException(CodeMsg.E404_1_DATA_NOT_FIND.getCode(),CodeMsg.E404_1_DATA_NOT_FIND.getMessage());
        }
    }

    public record AuthAndMakeTokensResponseBody(
            @NonNull
            Member member,
            @NonNull
            String accessToken,
            @NonNull
            String refreshToken
    ) {}

    @Transactional
    public RsData<AuthAndMakeTokensResponseBody> authAndMakeTokens(String username, String password) {
        Member member = findByUsername(username)
                .orElseThrow(() -> new GlobalException(CodeMsg.E400_3_NO_EXIST_USER.getCode(),CodeMsg.E400_3_NO_EXIST_USER.getMessage()));

        if (!passwordMatches(member, password))
            throw new GlobalException(CodeMsg.E400_4_NOT_CORRECT_PASSWORD.getCode(),CodeMsg.E400_4_NOT_CORRECT_PASSWORD.getMessage() );

        String refreshToken = member.getRefreshToken();
        String accessToken = authTokenService.genAccessToken(member);

        return RsData.of(
                "200-1",
                "로그인 성공",
                new AuthAndMakeTokensResponseBody(member, accessToken, refreshToken)
        );
    }

    @Transactional
    public String genAccessToken(Member member) {
        return authTokenService.genAccessToken(member);
    }

    public boolean passwordMatches(Member member, String password) {
        return passwordEncoder.matches(password, member.getPassword());
    }

    public SecurityUser getUserFromAccessToken(String accessToken) {
        Map<String, Object> payloadBody = authTokenService.getDataFrom(accessToken);

        long id = (int) payloadBody.get("id");
        String username = (String) payloadBody.get("username");
        List<String> authorities = (List<String>) payloadBody.get("authorities");

        return new SecurityUser(
                id,
                username,
                "",
                authorities.stream().map(SimpleGrantedAuthority::new).toList()
        );
    }

    public boolean validateToken(String token) {
        return authTokenService.validateToken(token);
    }

    public RsData<String> refreshAccessToken(String refreshToken) {
        Member member = memberRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new GlobalException(CodeMsg.E400_5_NOT_EXIST_REFRESHTOKEN.getCode(),CodeMsg.E400_5_NOT_EXIST_REFRESHTOKEN.getMessage()));

        String accessToken = authTokenService.genAccessToken(member);

        return RsData.of("200-1", "토큰 갱신 성공", accessToken);
    }

    @Transactional
    @Scheduled(cron = "0 0 15 * * ?") // 매일 자정
    public void resetVisitedToday() {
        List<Member> allMembers = this.getAllMembers();

        for (Member member : allMembers) {
            member.setVisitedToday(false);
            member.setDailyAchievement(0);
            member.setRegisterCount(0);
        }
        memberRepository.saveAll(allMembers);
    }

    private List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public List<Member> recentMembers() {
        return memberRepository.findTop5ByOrderByIdDesc();
    }

    public void save(Member member) {
        memberRepository.save(member);
    }

    @Transactional
    public void cancelReport(Member member) {
        member.setReport(false);
        memberRepository.save(member);
    }

    public boolean canEnroll(Member member){
        return member.getRegisterCount() < 5;
    }

    @Transactional
    public void increaseRegisterCount(){
        Member member = rq.getMember();

        member.setRegisterCount(member.getRegisterCount() + 1);

        memberRepository.save(member);
    }

    @Transactional
    public void dropMember() {
        Member member = rq.getMember();

        member.setNickname("탈퇴한 회원");
        member.setRefreshToken(member.getUsername() + "_deleted_" + LocalDateTime.now()); // unique
        member.setUsername(member.getUsername() + "_deleted_" + LocalDateTime.now()); // unique
        member.setPassword("");
        member.setUuid("");
        member.setProfileImgUrl("");
        member.setPoint(0);
        member.setRegisterCount(0);

        courseEnrollService.delete(member);

        memberRepository.save(member);
        rq.setLogout();
    }

    @Transactional
    public void visit(Member member){
        member.setVisitedToday(true);
        int point = member.getPoint() + PointType.Attend.getAmount();
        member.setPoint(point); // 실제 포인트 추가
        notificationService.notifyAttendPoint(member.getId()); // 포인트 지급 알림
        notificationService.createByPoint(NotificationType.POINTS, member, PointType.Attend.getAmount()); // 알림 내역 저장
        pointService.addPoint(PointType.Attend, member); // 포인트 내역 추가
    }
}
