package praktikum.order;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.OrderClient;
import praktikum.user.User;
import praktikum.user.UserClient;
import praktikum.user.UserCredentials;

public class OrderListTest {

    UserClient userClient = new UserClient();
    OrderClient orderClient = new OrderClient();
    UserCredentials credentials;
    User user;
    String token;
    int totalOrders;

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
    @DisplayName("Получение списка заказов неавторизованного пользователя возвращает 401 ")
    public void getUserOrderListWithoutAuthorizationReturns401() {
        userClient.logout(credentials);
        orderClient.getOrdersWithoutAuthorization();
    }
}
