package shop.mtcoding.blog.temp;

import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

public class HashTest {

    @Test
    public void encode_test() {
        String password = "1234";

        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println(encPassword);
        // $2a$10$LACXclDpbAvdikOzSEnC4uq230Llj0bawhtZr4Sd596P5ZUwHB06S
        // $2a$10$/RVbwRlD2uoOGHWQn8yMke/WcM4pGj/8U8zMo1Mm0o9.bALcIN7Gy


    }

    @Test
    public void decode_test() {
        String dbPassword = "$2a$10$LACXclDpbAvdikOzSEnC4uq230Llj0bawhtZr4Sd596P5ZUwHB06S";
        String password = "1234";

        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        if (encPassword.equals(dbPassword)) {
            System.out.println("비밀번호가 같아요");
        } else {
            System.out.println("비밀번호가 달라요");
        }


    }

    @Test
    public void decodeV2_test() {
        String dbPassword = "$2a$10$LACXclDpbAvdikOzSEnC4uq230Llj0bawhtZr4Sd596P5ZUwHB06S";
        String password = "1234";

        Boolean isSame = BCrypt.checkpw(password, dbPassword);
        System.out.println(isSame);


    }

}
