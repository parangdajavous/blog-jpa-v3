package shop.mtcoding.blog.user;


import lombok.Data;


public class UserResponse {

    // RestAPI 규칙 2: DTO에 민감한 정보는 빼기(ex.password), 날짜는 String (날짜 공부하기 전까지)
    // 깊은 복사
    @Data
    public static class DTO {
        private Integer id;
        private String username;
        private String email;
        private String createdAt;

        public DTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.createdAt = user.getCreatedAt().toString();
        }
    }

}
