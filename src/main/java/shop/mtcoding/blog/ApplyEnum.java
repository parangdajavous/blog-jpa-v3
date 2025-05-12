package shop.mtcoding.blog;

/*
    리스트 = [1, 2, 3] <- 열거형 (가변)
    튜플 = (1, 2, 3) <- 열거형 (불변) - 한번 만들어지면 고정

 */

public enum ApplyEnum {  // 열거형 (불변) - 한번 만들어지면 고정
    PASS("합격"), FAIL("불합격");

    public String value;

    ApplyEnum(String value) {
        this.value = value;
    }
}
