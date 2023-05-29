package praktikum.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import praktikum.Order;
import praktikum.Ingredients;
import praktikum.OrderClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreatingOrderWithoutAuthorizationTest {

    OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("Создаем заказ без ингридиентов для неавторизованного пользователя. Возвращает 400")
    public void createOrderWithoutIngredientUserIsNotAuthorizedReturns400() {
        Order order = Order.builder().build();
        orderClient.createOrderWithoutIngredientsWithoutAuthorization(order);
    }

    @Test
    @DisplayName("Создаем заказ с одним доступным ингридиентом для неавторизованного пользователя. Возвращает 200 OK")
    @Description("Создаем заказ с одним доступным ингридиентом. Должен вернуть 200 OK и номер заказа")

    public void createOrderWithOneValidIngredientUserIsNotAuthorizedReturns200() {
        int index = new Random().nextInt(14);
        String ingredientHash = orderClient.getIngredient(index);

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredients.builder()._id(ingredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.createOrderWithIngredientsWithoutAuthorization(order);
    }

    @Test
    @DisplayName("Создаем заказ с одним недоступным ингридиентом для неавторизованного пользователя. Возвращает 500 Internal Server Error")
    @Description("Должен вернуть  500.")
    public void createOrderWithOneInvalidIngredientUserIsNotAuthorizedReturns500() {
        String invalidIngredientHash = "61c0c5a71d1f82001bdaaa6r";

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredients.builder()._id(invalidIngredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.createOrderWithInvalidIngredientHashWithoutAuthorization(order);
    }

    @Test
    @DisplayName("Создаем заказ с более чем одним доступным ингридиентом для неавторизованного пользователя. Возвращает 200 OK")
    @Description("Создаем заказ с более чем одним доступным ингридиентом. Должен вернуть 200 OK и номер заказа")
    public void createOrderWithValidIngredientsUserIsNotAuthorizedReturns200() {
        int firstIndex = new Random().nextInt(14);
        String firstIngredientHash = orderClient.getIngredient(firstIndex);

        int secondIndex = new Random().nextInt(14);
        String secondIngredientHash = orderClient.getIngredient(secondIndex);

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredients.builder()._id(firstIngredientHash).build());
        ingredientsList.add(Ingredients.builder()._id(secondIngredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.createOrderWithIngredientsWithoutAuthorization(order);
    }

    @Test
    @DisplayName("Создем заказ с двумя ингредиентами, один из которых с недопустимым для неавторизованного пользователя. Возвращает 500 Internal Server Error")
    @Description("Создем заказ без авторизации с двумя ингредиентами, один из которых с недопустимым. Возвращает 500 Internal Server Error")
    public void createOrderWithInvalidIngredientsUserIsNotAuthorizedReturns500() {
        int index = new Random().nextInt(14);
        String validIngredientHash = orderClient.getIngredient(index);
        String invalidIngredientHash = orderClient.getIngredient(index) + "q";

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsList.add(Ingredients.builder()._id(invalidIngredientHash).build());
        ingredientsList.add(Ingredients.builder()._id(validIngredientHash).build());

        Order order = Order.builder().ingredients(ingredientsList).build();
        orderClient.createOrderWithInvalidIngredientHashWithoutAuthorization(order);
    }

}