package praktikum.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.Order;
import praktikum.Ingredients;
import praktikum.OrderClient;
import praktikum.user.User;
import praktikum.user.UserClient;
import praktikum.user.UserCredentials;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreatingOrderWithAuthorizationTest {

    OrderClient orderClient = new OrderClient();
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
    @DisplayName("Создание заказа без ингредиентов, в то время как пользователь авторизован, возвращает 400 ")
    @Description("Регистрируем нового пользователя.Авторизуемся. Создаем заказ без ингридиентов. Получаем код статуса 400")
    public void createOrderWithoutIngredientWhileUserIsAuthorizedReturns400() {
        Order order = Order.builder().build();
        orderClient.createOrderWithoutIngredientsWithAuthorization(order, token);
    }

    @Test
    @DisplayName("Создание заказа с одним допустимым ингредиентом для авторизованного пользователя, возвращает 200 OK")
    @Description("Регистрируем нового пользователя.Авторизуемся. Создаем заказ одним допустимым ингредиентом. Должен вернуть 200")
    public void createOrderWithOneValidIngredientWhileUserIsAuthorizedReturns200() {
        int index = new Random().nextInt(14);
        String ingredientHash = orderClient.getIngredient(index);

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredients.builder().id(ingredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.createOrderWithIngredientsWithAuthorization(order, token);
    }

    @Test
    @DisplayName("Создание заказа с одним допустимым ингредиентом для авторизованного пользователя, возвращает 500  Internal Server Error")
    @Description("Регистрируем нового пользователя.Авторизуемся. Создаем заказ одним недопустимым ингредиентом. Должен вернуть 500")
    public void createOrderWithOneInvalidIngredientWhileUserIsAuthorizedReturns500() {
        String invalidIngredientHash = "61c0c5a71d1f82001bdaaa6r";

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredients.builder().id(invalidIngredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.createOrderWithInvalidIngredientHashWithAuthorization(order, token);
    }

    @Test
    @DisplayName("Создание заказа с более чем одним допустимым ингредиентом для авторизованного пользователя, возвращает 200 OK")
    @Description("Регистрируем нового пользователя.Авторизуемся. Создаем заказ с более чем одним допустимым ингредиентом. Должен вернуть 200 и номер заказа")
    public void createOrderWithValidIngredientsWhileUserIsAuthorizedReturns200() {
        int firstIndex = new Random().nextInt(14);
        String firstIngredientHash = orderClient.getIngredient(firstIndex);

        int secondIndex = new Random().nextInt(14);
        String secondIngredientHash = orderClient.getIngredient(secondIndex);

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredients.builder().id(firstIngredientHash).build());
        ingredientsList.add(Ingredients.builder().id(secondIngredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.createOrderWithIngredientsWithAuthorization(order, token);
    }

    @Test
    @DisplayName("Создем заказ с двумя ингредиентами, один из которых с недопустимым для авторизованного пользователя. Возвращает 500 Internal Server Error")
    @Description("Регистрируем нового пользователя.Авторизуемся. Создем заказ с двумя ингредиентами, один из которых с недопустимым. Возвращает 500 Internal Server Error")
    public void createOrderWithInvalidIngredientsWhileUserIsAuthorizedReturns500() {
        int index = new Random().nextInt(14);
        String validIngredientHash = orderClient.getIngredient(index);
        String invalidIngredientHash = orderClient.getIngredient(index) + "q";

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredients.builder().id(invalidIngredientHash).build());
        ingredientsList.add(Ingredients.builder().id(validIngredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.createOrderWithInvalidIngredientHashWithAuthorization(order, token);
    }

}