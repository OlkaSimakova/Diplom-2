package praktikum.user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import praktikum.user.User;
import praktikum.user.UserClient;

public class UserInformationUpdateWithoutAuthorizationTest {

    UserClient userClient = new UserClient();

    @Test
    @DisplayName("Обновление имени пользователя во время его авторизации возвращает 401 неавторизованный")
    @Description("Ответ должен иметь код 401. Сообщение: 'Вы должны быть авторизованы'")
    public void updateUserNameFieldWithAuthorizationReturnsUnauthorized() {
        User user = User.builder().name(RandomStringUtils.randomAlphabetic(5)).build();
        userClient.changeUserNameWithoutAuthorization(user);
    }

    @Test
    @DisplayName("Обновление электронной почты пользователя, пока он авторизован, возвращает 401 неавторизованный")
    @Description("Ответ должен иметь код 401. Сообщение:'Вы должны быть авторизованы'")
    public void updateUserEmailFieldWithAuthorizationReturnsUnauthorized() {
        User user = User.builder().email(RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@mail.ru").build();
        userClient.changeUserEmailWithoutAuthorization(user);
    }

}