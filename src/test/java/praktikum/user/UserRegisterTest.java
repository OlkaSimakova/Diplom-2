package praktikum.user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import praktikum.user.User;
import praktikum.user.UserClient;
import praktikum.user.UserCredentials;

public class UserRegisterTest {

    UserClient userClient = new UserClient();
    User credentials;
    User user;

    @After
    public void tearDown() {
        UserCredentials credentials = UserCredentials.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        String token = userClient.loginWithCorrectCredentials(credentials);
        userClient.delete(token);
    }

    @Test
    @DisplayName("Создание уникального пользователя с правильными данными возвращает 200 OK")
    @Description("Регистрация нового уникального пользователя возвращает код статуса 200 OK")
    public void createUniqueUserReturnsOk() {
        credentials = userClient.getRandomUserTestData();
        user = User.builder()
                .email(credentials.getEmail())
                .password(credentials.getPassword())
                .name(credentials.getName())
                .build();

        userClient.registerWithCorrectData(user);
    }

    @Test
    @DisplayName("Создание существующего пользователя возвращает значение 403")
    @Description("Регистрация с уже существующими данными возвращает код статуса 403. Сообщение: Пользователь уже существует")
    public void createExistingUserReturnsForbidden() {
        credentials = userClient.getRandomUserTestData();
        user = User.builder()
                .email(credentials.getEmail())
                .password(credentials.getPassword())
                .name(credentials.getName())
                .build();

        userClient.registerWithCorrectData(user);
        userClient.registerWithExistingData(user);
    }

}