package redis;

import com.tiheima.reggie.utils.ValidateCodeUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

public class otherTest {

    @Test
    public void randomCode(){
        Integer integer = ValidateCodeUtils.generateValidateCode(4);
        System.out.println(integer);

        String s = ValidateCodeUtils.generateValidateCode4String(4);
        System.out.println(s);
    }
}
