package praktikum.user;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import praktikum.StellarBurgersRestClient;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class UserClient extends StellarBurgersRestClient {

    private final String USER_AUTH_REGISTER_PATH = "/auth/register";
    private final String USER_AUTH_LOGIN_PATH = "/auth/login";
    private final String USER_AUTH_LOGOUT_PATH = "/auth/logout";
    private final String USER_AUTH_USER = "/auth/user";

    @Step("Создаем учетные данные")
    public User getRandomUserTestData() {
        final String email = RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@mail.com";
        final String password = RandomStringUtils.randomAlphabetic(5);
        final String name = RandomStringUtils.randomAlphabetic(5);

        Allure.addAttachment("Email: ", email);
        Allure.addAttachment("Password: ", password);
        Allure.addAttachment("Username: ", name);

        return new User(email, password, name);
    }

    @Step("Регистрируем нового пользователя с правильными данными")
    public void registerWithCorrectData(User user) {
        RestAssured.given()
                .spec(requestSpecification())
                .body(user)
                .when()
                .post(USER_AUTH_REGISTER_PATH)
                .then().log().ifError()
                .assertThat()
                .body("success", equalTo(true))
                .body("user", notNullValue())
                .statusCode(SC_OK);
    }

    @Step("Регистрируем пользователя с существующими данными")
    public void registerWithExistingData(User user) {
        RestAssured.given()
                .spec(requestSpecification())
                .body(user)
                .when()
                .post(USER_AUTH_REGISTER_PATH)
                .then().log().ifError()
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"))
                .statusCode(SC_FORBIDDEN);
    }


    @Step("Регистрируем пользователя с существующими электронной почтой, паролем и именем")
    public void registerWithMissingDataField(User user) {
        RestAssured.given()
                .spec(requestSpecification())
                .body(user)
                .when()
                .post(USER_AUTH_REGISTER_PATH)
                .then().log().ifError()
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"))
                .statusCode(SC_FORBIDDEN);
    }

    @Step("Войти в систему с правильными учетными данными, код статуса = 200")
    public String loginWithCorrectCredentials(UserCredentials credentials) {
        return RestAssured.given()
                .spec(requestSpecification())
                .body(credentials)
                .when()
                .post(USER_AUTH_LOGIN_PATH)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .extract()
                .path("accessToken")
                .toString()
                .substring(7);
    }

    @Step("Войти в систему с неправильными учетными данными, код статуса = 401")
    public void loginWithIncorrectCredentials(UserCredentials credentials) {
        RestAssured.given()
                .spec(requestSpecification())
                .body(credentials)
                .when()
                .post(USER_AUTH_LOGIN_PATH)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Step("Выйти из системы")
    public void logout(UserCredentials credentials) {
        Token refreshToken = new Token(getRefreshToken(credentials));

        RestAssured.given()
                .spec(requestSpecification())
                .body(refreshToken)
                .when()
                .post(USER_AUTH_LOGOUT_PATH)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("message", equalTo("Successful logout"));
    }

    public String getRefreshToken(UserCredentials credentials) {
        return RestAssured.given()
                .spec(requestSpecification())
                .body(credentials)
                .when()
                .post(USER_AUTH_LOGIN_PATH)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .extract()
                .path("refreshToken");
    }

    @Step("Изменить имя пользователя, используя авторизацию")
    public void changeUserNameWithAuthorization(String token, User user) {
        RestAssured.given()
                .spec(requestSpecification())
                .auth().oauth2(token)
                .and()
                .body(user)
                .when()
                .patch(USER_AUTH_USER)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user", notNullValue())
                .body("user.name", equalTo(user.getName()));
    }

    @Step("Изменить электронную почту, используя авторизацию")
    public void changeUserEmailWithAuthorization(String token, User user) {
        RestAssured.given()
                .spec(requestSpecification())
                .auth().oauth2(token)
                .and()
                .body(user)
                .when()
                .patch(USER_AUTH_USER)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user", notNullValue())
                .body("user.email", equalTo(user.getEmail()))
                ;
    }

    @Step("Изменить адрес электронной почты пользователя на существующий адрес электронной почты")
    public void changeUserEmailWithAuthWithExistingEmail(String token, User user) {
        RestAssured.given()
                .spec(requestSpecification())
                .auth().oauth2(token)
                .and()
                .body(user)
                .when()
                .patch(USER_AUTH_USER)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User with such email already exists"));
    }

    @Step("Изменить имя пользователя без авторизации")
    public void changeUserNameWithoutAuthorization(User user) {
        RestAssured.given()
                .spec(requestSpecification())
                .body(user)
                .when()
                .patch(USER_AUTH_USER)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Step("Изменить электронную почту пользователя без авторизации")
    public void changeUserEmailWithoutAuthorization(User user) {
        RestAssured.given()
                .spec(requestSpecification())
                .body(user)
                .when()
                .patch(USER_AUTH_USER)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Step("Удалить пользователя")
    public void delete(String token) {
        RestAssured.given()
                .spec(requestSpecification())
                .auth().oauth2(token)
                .when()
                .delete(USER_AUTH_USER)
                .then().log().ifError()
                .assertThat()
                .statusCode(SC_ACCEPTED)
                .body("success", equalTo(true))
                .body("message", equalTo("User successfully removed"));
    }

}