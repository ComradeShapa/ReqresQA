package api;

import api.DTO.*;
import api.config.Configuration;
import api.config.Specifications;
import io.restassured.response.ValidatableResponse;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.time.Clock;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

// ОК - плюс статус коды в конфиг
// ОК - токен в конфиг
// ОК - класс application, его экземпляр над тестами в классе тестов, через который идёт обращение к этому файлу
// ОК - .header("Authorization", "Bearer " + token) - убрать из спецификаций, вынести в тесты. В спец-ях оставить только контент жсон
// OK - параметризация каждого теста, csv-файлы, вариантивность данных для передачи (2-3 файла)
// OK - обновить компилятор и проверить кириллицу внутри ConfigReader
// OK - lombok - из курса видео, настройка в идее + переписать аннотации DTO
// OK - копипаст build.gradle от Давида к себе (джексон вместо gson)
// ОК - исправить кодировку установкой зависимости (?)
// DTO классы в пакет main  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


public class ReqresTest {

    private static final Configuration config = ConfigFactory.create(Configuration.class);

    @Test // GET Single User
    public void checkUserEmailTest() {
        Specifications.installSpecification(
                Specifications.requestSpec(),
                Specifications.responseSpec(config.status200()));

        UserData user = given()
                .baseUri(config.urlReqresApi())
                .auth().oauth2(config.apiToken())
                .when()
                .get(config.users2())
                .then().log().all()
                .extract().jsonPath().getObject("data", UserData.class);

        assertTrue(user.getEmail().endsWith("@reqres.in"));
    }

    @Test
    // POST Create user (returns token)
    public void successUserRegistrationTest() {
        Specifications.installSpecification(
                Specifications.requestSpec(),
                Specifications.responseSpec(config.status200()));

        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        Register regData = new Register("eve.holt@reqres.in", "pistol");

        SuccessReg successReg = given()
                .baseUri(config.urlReqresApi())
                .auth().oauth2(config.apiToken())
                .body(regData)
                .when()
                .post(config.register())
                .then().log().all()
                .extract().as(SuccessReg.class);

        assertNotNull(successReg);
        assertEquals(id, successReg.getId());
        assertEquals(token, successReg.getToken());
    }

    @Test // POST Register unsuccessful
    public void failUserRegistrationTest() {
        Specifications.installSpecification(
                Specifications.requestSpec(),
                Specifications.responseSpec(config.status400()));

        Register regData = new Register("sydney@fife", "");

        FailReg failReg = given()
                .baseUri(config.urlReqresApi())
                .auth().oauth2(config.apiToken())
                .body(regData)
                .when()
                .post(config.register())
                .then().log().all()
                .extract().as(FailReg.class);

        assertEquals("Missing password", failReg.getError());
    }

    @ParameterizedTest(name = "[{index}] email={0}, password={1}")
    @CsvFileSource(resources = "/testdata/login_positive_cases_eveholt.csv", numLinesToSkip = 1)
    // POST Verify session
    public void happyVerifySessionTest(String email,
                                  String password,
                                  int expectedStatus,
                                  String expectedToken,
                                  String expectedErrorMessage) {
        Specifications.installSpecification(
                Specifications.requestSpec(),
                Specifications.responseSpec(config.status200()));

        Register regData = new Register(email, password);

        ValidatableResponse response = given()
                .baseUri(config.urlReqresApi())
                .auth().oauth2(config.apiToken())
                .body(regData)
                .when()
                .post(config.login())
                .then()
                .log().all();

        if (expectedStatus == config.status200()) {
            Token actualToken = response.extract().as(Token.class);
            assertEquals(expectedToken, actualToken.getToken());
        } else {
            FailReg error = response.extract().as(FailReg.class);
            assertEquals(expectedErrorMessage, error.getError());
        }
    }

    @Test // POST Create record
    public void createRecordTest() {
        Specifications.installSpecification(
                Specifications.requestSpec(),
                Specifications.responseSpec(config.status201()));

        EmployeeData sentEmployeeData = new EmployeeData("morpheus", "leader", "", "", "");
        EmployeeData expectedEmployeeData = new EmployeeData("morpheus", "leader", "496", "2024-07-01T10:00:00.000Z", "");

        EmployeeData receivedEmployeeData = given()
                .baseUri(config.urlReqresApi())
                .auth().oauth2(config.apiToken())
                .body(sentEmployeeData)
                .when()
                .post(config.users())
                .then().log().all()
                .extract().as(EmployeeData.class);

        assertEquals(expectedEmployeeData.getName(), receivedEmployeeData.getName());
        assertEquals(expectedEmployeeData.getJob(), receivedEmployeeData.getJob());
        assertNotNull(receivedEmployeeData.getId());
    }

    @Test // POST Login unsuccessful
    public void failLoginTest() {
        Specifications.installSpecification(
                Specifications.requestSpec(),
                Specifications.responseSpec(config.status400()));

        Register regData = new Register("peter@klaven", "");

        FailReg failReg = given()
                .baseUri(config.urlReqresApi())
                .auth().oauth2(config.apiToken())
                .body(regData)
                .when()
                .post(config.login())
                .then().log().all()
                .extract().as(FailReg.class);
        assertEquals("Missing password", failReg.getError());
    }

    @Test // PUT Update
    public void updateUserDataTest() {
        Specifications.installSpecification(
                Specifications.requestSpec(),
                Specifications.responseSpec(config.status200()));

        EmployeeData sentEmployeeData = new EmployeeData("morpheus", "zion resident", "", "", "");

        EmployeeData receivedEmployeeData = given()
                .baseUri(config.urlReqresApi())
                .auth().oauth2(config.apiToken())
                .body(sentEmployeeData)
                .when()
                .put(config.users2())
                .then().log().all()
                .extract().as(EmployeeData.class);

        String regexExp = "(.{14})$";
        String currentTime = Clock.systemUTC().instant().toString().replaceAll(regexExp, "");

        String regexAct = "(.{8})$";
        assertEquals(currentTime, receivedEmployeeData.getUpdatedAt().replaceAll(regexAct, ""));
    }
}
