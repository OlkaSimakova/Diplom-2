package praktikum.user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import praktikum.user.User;
import praktikum.user.UserClient;

public class UserRegisterNegativeTest {

    UserClient userClient = new UserClient();
    User credentials;
    User user;

    @Test
    @DisplayName("Создание нового пользователя с отсутствующим полем имени возвращает код 403 ")
    @Description("Регистрация с отсутствующим полем имени возвращает код статуса 403. Сообщение: поля электронной почты, пароля и имени являются обязательными для заполнения.")
    public void createUserWithMissingNameReturnsForbidden() {
        credentials = userClient.getRandomUserTestData();
        user = User.builder()
                .email(credentials.getEmail())
                .password(credentials.getPassword())
                .build();

        userClient.registerWithMissingDataField(user);
    }

    @Test
    @DisplayName("Создание нового пользователя с отсутствующим полем электронной почты возвращает код 403 ")
    @Description("Регистрация с отсутствующим полем электронной почты возвращает код статуса 403. Сообщение: Поля электронной почты, пароля и имени являются обязательными для заполнения.")
    public void createUserWithMissingEmailReturnsForbidden() {
        credentials = userClient.getRandomUserTestData();
        user = User.builder()
                .password(credentials.getPassword())
                .name(credentials.getName())
                .build();

        userClient.registerWithMissingDataField(user);
    }

    @Test
    @DisplayName("Создание нового пользователя с отсутствующим полем пароля возвращает значение 403")
    @Description("Регистрация с отсутствующим полем пароля возвращает код статуса 403. Сообщение: Поля электронной почты, пароля и имени являются обязательными для заполнения")
    public void createUserWithMissingPasswordReturnsForbidden() {
        credentials = userClient.getRandomUserTestData();
        user = User.builder()
                .email(credentials.getEmail())
                .name(credentials.getName())
                .build();

        userClient.registerWithMissingDataField(user);
    }

}