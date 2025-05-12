package shop.mtcoding.blog.user;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.util.Resp;

import java.util.Map;

// TODO: 미완 / JWT 배우고 응담 완료하기
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final HttpSession session;


    @PutMapping("/s/api/user")  // 주소에 id를 적을 수 없다 - 신뢰할 수 없기 때문 / session에서 id 꺼낸다
    public String update(@Valid @RequestBody UserRequest.UpdateDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        // TODO: JWT 인증 후에 하기
        User userPS = userService.회원정보수정(reqDTO, sessionUser.getId());
        session.setAttribute("sessionUser", userPS);
        return "redirect:/";
    }

    @GetMapping("/api/check-username-available/{username}")
    public ResponseEntity<?> checkUsernameAvailable(@PathVariable("username") String username) {
        Map<String, Object> dto = userService.유저네임중복체크(username);
        return Resp.ok(dto);
    }


    @PostMapping("/join")
    // DTO는 reqDTO와 respDTO로 구분하기
    public ResponseEntity<?> join(@Valid @RequestBody UserRequest.JoinDTO reqDTO, Errors errors) {
        UserResponse.DTO respDTO = userService.회원가입(reqDTO);
        return Resp.ok(respDTO);
    }

    @PostMapping("/login")  // password가 노출되면 안되기 때문에 예외로 post방식
    public ResponseEntity<?> login(@Valid @RequestBody UserRequest.LoginDTO reqDTO, Errors errors) {
        UserResponse.TokenDTO respDTO = userService.로그인(reqDTO);
        return Resp.ok(respDTO);
    }

    // AccessToken 만으로는 로그아웃을 구현할 수 없다
//    // TODO: JWT 인증 후에 하기
//    @GetMapping("/logout")
//    public String logout() {
//        session.invalidate();
//        return "redirect:/login-form";
//    }  로그아웃이라는 개념이 없다
}
