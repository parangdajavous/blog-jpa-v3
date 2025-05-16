package shop.mtcoding.blog._core.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class CorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String origin = request.getHeader("Origin");
        log.debug("Origin : " + origin);

        // Ajax fetch 요청
        response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");  // origin - 다 허용 / 특정ip - 특정 ip만 허용 -> 프로토콜
        //response.setHeader("Access-Control-Expose-Headers", "Authorization");  // 이 헤더 응답을 JS로 접근하게 허용할지
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, DELETE, OPTIONS");  // JS 요청들  OPTIONS -> pre-flight 요청
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Key, Content-Type, Accept, Authorization");  // 클라이언트가 요청할 때 특정 header 허용해줘 / X-Key - 프로토콜에 없는 임의 값 (커스터마이징 - 약속임 X 붙는거)
        response.setHeader("Access-Control-Allow-Credentials", "true");  // cookie 의 session 값 허용

        // Preflight 요청을 허용하고 바로 응답하는 코드 - filter 두 번 동작함
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);  // 200 - preflight 허용 - header 값 브라우저에 응답 - 브라우저는 origin 확인 / 다르면 터뜨린다
        } else {
            chain.doFilter(req, res);
        }
    }
}