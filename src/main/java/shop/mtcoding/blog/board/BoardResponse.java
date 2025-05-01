package shop.mtcoding.blog.board;

import lombok.Data;
import shop.mtcoding.blog.reply.Reply;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BoardResponse {

    // 변경된 row를 돌려주는 DTO
    @Data
    public static class DTO {
        private Integer id;
        private String title;
        private String content;
        private Boolean isPublic;
        private Integer userId;
        private String createdAt;

        public DTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.isPublic = board.getIsPublic();
            this.userId = board.getUser().getId();
            this.createdAt = board.getCreatedAt().toString();
        }
    }

    @Data
    public static class UpdateFormDTO {
        private Integer id;  // 화면에는 없지만, PK는 반드시 있어야한다
        private String title;
        private String content;
        private Boolean isPublic;
//        private Integer userId;  // 개인 식별키는 들고가봤자 의미가 없음 (식별불가) - 화면에 안보임 화면에 보여줄땐 신뢰가능하나 서버로 다시 들고올때는 바뀔수도 있기 때문에 신뢰할 수 없으므로 없애야함
//        private String createdAt;

        public UpdateFormDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.isPublic = board.getIsPublic();

        }
    }


    @Data
    public static class ListDTO {
        private List<Board> boards;
        private Integer prev;
        private Integer next;
        private Integer current;
        private Integer size;
        private Integer totalCount;
        private Integer totalPage;
        private Boolean isFirst; // current == 0
        private Boolean isLast; // totalCount, size=3, totalPage == current
        private List<Integer> numbers; // 20개 [1,2,3,4,5,6,7] -> model.numbers -> {{.}}
        private String keyword;

        public ListDTO(List<Board> boards, Integer current, Integer totalCount, String keyword) {
            this.boards = boards;
            this.prev = current - 1;
            this.next = current + 1;
            this.size = 3;
            this.totalCount = totalCount; // given
            this.totalPage = makeTotalPage(totalCount, size); // 2
            this.isFirst = current == 0;
            this.isLast = (totalPage - 1) == current;
            this.numbers = makeNumbers(current, totalPage);
            this.keyword = keyword; // 경우의 수 : keyword 값이 있거나, keyword null이거나
        }

        private Integer makeTotalPage(int totalCount, int size) {
            int rest = totalCount % size > 0 ? 1 : 0; // 6 -> 0, 7 -> 1, 8 -> 2
            return totalCount / size + rest;
        }

        private List<Integer> makeNumbers(int current, int totalPage) {
            List<Integer> numbers = new ArrayList<>();

            int start = (current / 5) * 5;
            int end = Math.min(start + 5, totalPage);

            for (int i = start; i < end; i++) {
                numbers.add(i);
            }

            return numbers;
        }

    }


    // 상세보기 화면에 필요한 데이터
    @Data
    public static class DetailDTO {
        private Integer id;
        private String title;
        private String content;
        private Boolean isPublic;
        private Boolean isOwner;
        private Boolean isLove;
        private Integer loveCount;
        private String username;
        private Timestamp createdAt;
        private Integer loveId;

        private List<ReplyDTO> replies;

        @Data
        public class ReplyDTO {
            private Integer id;
            private String content;
            private String username;
            private Boolean isOwner;

            public ReplyDTO(Reply reply, Integer sessionUserId) {
                this.id = reply.getId();
                this.content = reply.getContent();
                this.username = reply.getUser().getUsername();
                this.isOwner = reply.getUser().getId().equals(sessionUserId);
            }
        }

        public DetailDTO(Board board, Integer sessionUserId, Boolean isLove, Integer loveCount, Integer loveId) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.isPublic = board.getIsPublic();
            this.isOwner = sessionUserId == board.getUser().getId();
            this.username = board.getUser().getUsername();
            this.createdAt = board.getCreatedAt();
            this.isLove = isLove;
            this.loveCount = loveCount;
            this.loveId = loveId;

            List<ReplyDTO> repliesDTO = new ArrayList<>();

            for (Reply reply : board.getReplies()) {
                ReplyDTO replyDTO = new ReplyDTO(reply, sessionUserId);
                repliesDTO.add(replyDTO);
            }

            this.replies = repliesDTO;
        }
    }

}
