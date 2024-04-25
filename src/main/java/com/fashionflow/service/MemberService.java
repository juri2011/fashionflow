import com.fashionflow.constant.Gender;
import com.fashionflow.dto.MemberFormDTO;
import com.fashionflow.entity.Member;
import com.fashionflow.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member createMember(MemberFormDTO memberFormDTO) {
        // DTO에서 Member 엔티티로 매핑
        Member member = Member.builder()
                .name(memberFormDTO.getName())
                .email(memberFormDTO.getEmail())
                .pwd(passwordEncoder.encode(memberFormDTO.getPwd())) // 비밀번호 인코딩
                .nickname(memberFormDTO.getNickname())
                .phone(memberFormDTO.getPhone())
                .birth(memberFormDTO.getBirth())
                .gender(Gender.valueOf(memberFormDTO.getGender().toUpperCase())) // 성별 enum 매핑
                .userAddr(memberFormDTO.getUserAddr())
                .userDaddr(memberFormDTO.getUserDaddr())
                .userStnum(memberFormDTO.getUserStnum())
                .regdate(LocalDateTime.now()) // 가입일은 현재 시간으로 설정
                .build();

        // 엔티티 저장
        return memberRepository.save(member);
    }
}