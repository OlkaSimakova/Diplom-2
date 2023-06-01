package praktikum.user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.user.User;
import praktikum.user.UserClient;
import praktikum.user.UserCredentials;

public class UserLoginTest {

    UserClient userClient = new UserClient();
    UserCredentials credentials;

    @Before
    public void setUp() {
        User user = userClient.getRandomUserTestData();
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
    }

    @After
    public void tearDown() {
        String token = userClient.loginWithCorrectCredentials(credentials);
        userClient.delete(token);
    }

    @Test
    @DisplayName("Вход с правильными данными существующего пользователя возвращает 200 OK")
    @Description("Регистрируем нового пользователя. Входим в систему с правильными данными. Ожидаемые результаты:Код статуса - 200. Токены доступа и обновления не являются нулевыми значениями.")
    public void loginWithCorrectDataExistingUserReturnsOK() {
        UserCredentials credentialsForLogin = UserCredentials.builder()
                .email(credentials.getEmail())
                .password(credentials.getPassword())
                .build();

        userClient.loginWithCorrectCredentials(credentialsForLogin);
    }

    @Test
    @DisplayName("Вход с неправильным именем пользователя возвращает 401 Неавторизованный")
    @Description("Регистрируем нового пользователя. Входим в систему с неправильным адресом электронной почты. Ожидаемые результаты: Код статуса - 401. Сообщение - неверный адрес электронной почты или пароль.")
    public void loginWithIncorrectLoginReturnsUnauthorized() {
        UserCredentials credentialsForLogin = UserCredentials.builder()
                .email(credentials.getEmail() + "1")
                .password(credentials.getPassword())
                .build();

        userClient.loginWithIncorrectCredentials(credentialsForLogin);
    }

    @Test
    @DisplayName("Вход с неверным паролем возвращает 401 неавторизованный")
    @Description("Регистрируем нового пользователя. Входим в систему с неправильным паролем.Ожидаемые результаты: Код статуса - 401. Сообщение - неверны адрес электронной почты или пароль.")
    public void loginWithIncorrectPasswordReturnsUnauthorized() {
        UserCredentials credentialsForLogin = UserCredentials.builder()
                .email(credentials.getEmail())
                .password(credentials.getPassword() + "1")
                .build();

        userClient.loginWithIncorrectCredentials(credentialsForLogin);
    }

}