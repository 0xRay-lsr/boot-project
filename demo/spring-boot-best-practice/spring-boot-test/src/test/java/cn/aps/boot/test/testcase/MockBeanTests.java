/**n * @author lishiruin * @date 2025-10-27n */
package cn.aps.boot.test.testcase;

import cn.aps.boot.test.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Author : lishirui
 */
@SpringBootTest
class MockBeanTests {

//    @Autowired
//    private UserService userService;

    @MockBean
    private UserService userService;

    @Test
    public void countAllUsers() {
        BDDMockito.given(this.userService.countAllUsers()).willReturn(88);
        assertThat(this.userService.countAllUsers()).isEqualTo(88);
    }

}

