package shop.mtcoding.blog.reply;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.error.ex.ExceptionApi403;
import shop.mtcoding.blog._core.error.ex.ExceptionApi404;
import shop.mtcoding.blog.user.User;

@RequiredArgsConstructor
@Service
public class ReplyService {
    private final ReplyRepository replyRepository;

    @Transactional
    public ReplyResponse.DTO 댓글쓰기(ReplyRequest.SaveDTO reqDTO, User sessionUser) {
        Reply replyPS = replyRepository.save(reqDTO.toEntity(sessionUser));
        return new ReplyResponse.DTO(replyPS);

    }

    // TODO : 2단계 RestAPI 주소 변경 json돌려주기 할때 void 변경하기
    // 규칙5: ResiAPI에서 삭제는 void - delete는 응답할 데이터가 없기 때문
    @Transactional
    public void 댓글삭제(Integer id, Integer sessionUserId) {
        Reply replyPS = replyRepository.findById(id)
                .orElseThrow(() -> new ExceptionApi404("자원을 찾을 수 없습니다"));

        if (!replyPS.getUser().getId().equals(sessionUserId)) {
            throw new ExceptionApi403("권한이 없습니다");
        }

        replyRepository.deleteById(id);

    }
}
