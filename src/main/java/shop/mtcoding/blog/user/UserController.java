package shop.mtcoding.blog.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.util.Resp;

import java.util.Map;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;
    private final HttpSession session;


    // TODO: JWT 인증 후에 하기
    @PostMapping("/user/update")
    public String update(@Valid UserRequest.UpdateDTO reqDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        // update user_tb set password = ?, email = ? where id = ?
        User userPS = userService.회원정보수정(reqDTO, sessionUser.getId());
        // 세션 동기화
        session.setAttribute("sessionUser", userPS);
        return "redirect:/";
    }

    @GetMapping("/api/check-username-available/{username}")
    public @ResponseBody Resp<?> checkUsernameAvailable(@PathVariable("username") String username) {
        Map<String, Object> dto = userService.유저네임중복체크(username);
        return Resp.ok(dto);
    }


    @PostMapping("/join")
    // DTO는 reqDTO와 respDTO로 구분하기
    public @ResponseBody Resp<?> join(@Valid @RequestBody UserRequest.JoinDTO reqDTO, Errors errors) {
        UserResponse.DTO respDTO = userService.회원가입(reqDTO);
        return Resp.ok(respDTO);
    }

    // TODO: JWT 인증 후에 하기
    @PostMapping("/login")
    public String login(@Valid UserRequest.LoginDTO reqDTO, Errors errors, HttpServletResponse response) {
        User sessionUser = userService.로그인(reqDTO);
        session.setAttribute("sessionUser", sessionUser);

        if (reqDTO.getRememberMe() == null) {
            Cookie cookie = new Cookie("username", null);
            cookie.setMaxAge(0); // 즉시 만료
            response.addCookie(cookie);
        } else {
            Cookie cookie = new Cookie("username", reqDTO.getUsername());
            cookie.setMaxAge(60 * 60 * 24 * 7);
            response.addCookie(cookie);
        }

        return "redirect:/";
    }

    // TODO: JWT 인증 후에 하기
    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/login-form";
    }
}
