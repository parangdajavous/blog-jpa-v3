package shop.mtcoding.blog;

import lombok.Getter;

@Getter
public class Apply {
    private Integer id;  // 지원번호
    private String name; // 지원자명
    private Integer comId;  // 회사아이디
    private String status;

    public Apply(Integer id, String name, Integer comId, ApplyEnum status) {
        this.id = id;
        this.name = name;
        this.comId = comId;
        this.status = status.value;
    }
}
