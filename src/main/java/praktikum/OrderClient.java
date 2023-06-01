package praktikum;

import io.qameta.allure.Step;
import io.restassured.RestAssured;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderClient extends StellarBurgersRestClient {

    private final String ORDERS = "/orders";
    private final String INGREDIENTS = "/ingredients";

    @Step("Создаем случайные данные для тестирования")
    public String getIngredient(int index) {
        String path = "data[" + index + "]._id";
        return RestAssured.given()
                .spec(requestSpecification())
                .when()
                .get(INGREDIENTS)
                .then().log().ifError()
                .extract()
                .body()
                .path(path).toString();
    }

    @Step("Создаем заказ с ингредиентами для авторизованного пользователя")
    public void createOrderWithIngredientsWithAuthorization(Order order, String token) {
        RestAssured.given()
                .spec(requestSpecification())
                .auth().oauth2(token)
                .body(order)
                .when()
                .post(ORDERS)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.ingredients", notNullValue())
                .body("order.number", notNullValue());
    }

    @Step("Создаем заказ с ингредиентами без авторизации")
    public void createOrderWithIngredientsWithoutAuthorization(Order order) {
        RestAssured.given()
                .spec(requestSpecification())
                .body(order)
                .when()
                .post(ORDERS)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Step("Создаем заказ без ингредиентов для авторизованного пользователя")
    public void createOrderWithoutIngredientsWithAuthorization(Order order, String token) {
        RestAssured.given()
                .spec(requestSpecification())
                .auth().oauth2(token)
                .body(order)
                .when()
                .post(ORDERS)
                .then().log().ifStatusCodeIsEqualTo(SC_OK)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step("Создаем заказ без ингредиентов без авторизации")
    public void createOrderWithoutIngredientsWithoutAuthorization(Order order) {
        RestAssured.given()
                .spec(requestSpecification())
                .body(order)
                .when()
                .post(ORDERS)
                .then().log().ifStatusCodeIsEqualTo(SC_OK)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step("Создаем заказ с недопустимым ингредиентами для авторизованного пользователя")
    public void createOrderWithInvalidIngredientHashWithAuthorization(Order order, String token) {
        RestAssured.given()
                .spec(requestSpecification())
                .auth().oauth2(token)
                .body(order)
                .when()
                .post(ORDERS)
                .then().log().ifStatusCodeIsEqualTo(SC_OK)
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Step("Создаем заказ с недопустимым ингредиентами без авторизации")
    public void createOrderWithInvalidIngredientHashWithoutAuthorization(Order order) {
        RestAssured.given()
                .spec(requestSpecification())
                .body(order)
                .when()
                .post(ORDERS)
                .then().log().ifStatusCodeIsEqualTo(SC_OK)
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Step("Получать заказы авторизованного пользователя")
    public void getOrdersWithAuthorization(String token, int total) {
        RestAssured.given()
                .spec(requestSpecification())
                .auth().oauth2(token)
                .when()
                .get(ORDERS)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("total", equalTo(total))
                .body("totalToday", equalTo(total));
    }

    @Step("Получать заказы пользователя без авторизации")
    public void getOrdersWithoutAuthorization() {
        RestAssured.given()
                .spec(requestSpecification())
                .when()
                .get(ORDERS)
                .then().log().ifValidationFails()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

}