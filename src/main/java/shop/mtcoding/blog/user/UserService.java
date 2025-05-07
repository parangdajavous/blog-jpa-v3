package shop.mtcoding.blog.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.error.ex.Exception401;
import shop.mtcoding.blog._core.error.ex.Exception404;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// 비지니스로직, 트랜잭션처리, DTO 완료
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    // RestAPI 규칙1: 모든 insert 요청은 insert 된 데이터의 row를 조회해서 DTO에 담아서 돌려줘야한다
    @Transactional
    public UserResponse.DTO 회원가입(UserRequest.JoinDTO reqDTO) {
        User userPS = userRepository.save(reqDTO.toEntity());
        return new UserResponse.DTO(userPS);
    }

    // TODO: A4용지에다가 id,username 적어, A4용지에 서명, A4용지에 서명해서 돌려주니까 A4용지에 user 정보가 적혀있다 - 근데 password는 있으면 안됨 보안에 민감한 정보니까 ~
    public User 로그인(UserRequest.LoginDTO loginDTO) {
        User userPS = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new Exception401("유저네임 혹은 비밀번호가 틀렸습니다"));

        if (!userPS.getPassword().equals(loginDTO.getPassword())) {
            throw new Exception401("유저네임 혹은 비밀번호가 틀렸습니다");
        }
        return userPS;
    }

    public Map<String, Object> 유저네임중복체크(String username) {
        Optional<User> userOP = userRepository.findByUsername(username);  // 터트릴게 아니기 때문에 orElseThrow() 을 쓸 수가 없음

        Map<String, Object> dto = new HashMap<>();

        if (userOP.isPresent()) {  // 부정으로 물어보는 것 보다 긍정으로 물어보는 게 낫다
            dto.put("available", false);
        } else {
            dto.put("available", true);
        }
        return dto;
    }


    // TODO: RestAPI 규칙3: update된 데이터를 돌려줘야 한다 -> 이 역시 보안에 민감한 정보는 빼고
    @Transactional
    public User 회원정보수정(UserRequest.UpdateDTO reqDTO, Integer userId) {

        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new Exception404("자원을 찾을 수 없습니다"));

        userPS.update(reqDTO.getPassword(), reqDTO.getEmail()); // 영속화된 객체의 상태변경
        return userPS; // 리턴한 이유는 세션을 동기화해야해서!!
    } // 더티체킹 -> 상태가 변경되면 update을 날려요!!
}
