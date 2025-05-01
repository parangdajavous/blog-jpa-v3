package shop.mtcoding.blog.reply;

import java.sql.Timestamp;

public class ReplyResponse {

    public static class DTO {
        private Integer id;
        private String content; // 댓글 내용
        private Integer userId;
        private Integer boardId;
        private Timestamp createdAt;


        public DTO(Reply reply) {
            this.id = reply.getId();
            this.content = reply.getContent();
            this.userId = reply.getUser().getId();
            this.boardId = reply.getBoard().getId();
            this.createdAt = reply.getCreatedAt();
        }
    }
}
