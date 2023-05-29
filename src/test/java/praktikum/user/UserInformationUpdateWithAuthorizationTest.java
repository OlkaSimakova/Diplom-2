package praktikum.user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.user.User;
import praktikum.user.UserClient;
import praktikum.user.UserCredentials;

public class UserInformationUpdateWithAuthorizationTest {

    UserClient userClient = new UserClient();
    UserCredentials credentials;
    User user;
    String token;

    @Before
    public void setUp() {
        user = userClient.getRandomUserTestData();
        user = User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .build();

        userClient.registerWithCorrectData(user);

        credentials = UserCredentials.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        token = userClient.loginWithCorrectCredentials(credentials);
    }

    @After
    public void tearDown() {
        userClient.delete(token);
    }

    @Test
    @DisplayName("Обновление имени пользователя во время его авторизации возвращает 200 OK")
    @Description("Имя пользователя должно быть равно заданному значению. Ответ должен иметь код состояния 200 ")
    public void updateUserNameFieldWithAuthorizationReturnsOK() {
        user.setName(RandomStringUtils.randomAlphabetic(5));
        userClient.changeUserNameWithAuthorization(token, user);
    }

    @Test
    @DisplayName("Обновление электронной почты пользователя во время его авторизации возвращает 200 OK")
    @Description("Адрес электронной почты пользователя должен быть равен заданному значению. Ответ должен иметь код состояния 200.")
    public void updateUserEmailFieldWithAuthorizationReturnsOK() {
        user.setEmail(RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@mail.ru");
        userClient.changeUserEmailWithAuthorization(token, user);
    }

}