package api;

import api.DTO.*;
import api.config.Configuration;
import api.config.Specifications;
import api.dataFactory.EmployeeDataFactory;
import api.dataFactory.RegisterDataFactory;
import io.restassured.response.ValidatableResponse;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Clock;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class ReqresTest {

    private static final Configuration config = ConfigFactory.create(Configuration.class);

    @Test // GET Single User
    public void checkUserEmailTest() {
        Specifications.installSpecification(
                Specifications.requestSpec(),
                Specifications.responseSpec(config.status200()));

        UserData user = given()
                .baseUri(config.urlReqresApi())
                .header("x-api-key", config.apiToken())
                .when()
                .get(config.users2())
                .then().log().all()
                .extract().jsonPath().getObject("data", UserData.class);

        assertTrue(user.getEmail().endsWith(config.emailEnding()));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testdata/create_user_case.csv") // POST Create user (returns token)
    public void successUserRegistrationTest(String userEmail,
                                            String userPassword,
                                            String expectedToken,
                                            Integer id) {
        Specifications.installSpecification(
                Specifications.requestSpec(),
                Specifications.responseSpec(config.status200()));

        Register regData = new Register(userEmail, userPassword);

        SuccessReg successReg = given()
                .baseUri(config.urlReqresApi())
                .header("x-api-key", config.apiToken())
                .body(regData)
                .when()
                .post(config.register())
                .then().log().all()
                .extract().as(SuccessReg.class);

        assertNotNull(successReg);
        assertEquals(id, successReg.getId());
        assertEquals(expectedToken, successReg.getToken());
    }

    @ParameterizedTest
    @MethodSource("registerDataStream") // POST Register unsuccessful
    public void failUserRegistrationTest(Register sentRegisterData) {
        Specifications.installSpecification(
                Specifications.requestSpec(),
                Specifications.responseSpec(config.status400()));

        FailReg failReg = given()
                .baseUri(config.urlReqresApi())
                .header("x-api-key", config.apiToken())
                .body(sentRegisterData)
                .when()
                .post(config.register())
                .then().log().all()
                .extract().as(FailReg.class);

        assertEquals(config.missingPasswordError(), failReg.getError());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testdata/login_positive_cases_eveholt.csv")
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
                .header("x-api-key", config.apiToken())
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

    @ParameterizedTest
    @MethodSource("employeeDataStream")// POST Create record
    public void createRecordTest(EmployeeData sentEmployeeData) {
        Specifications.installSpecification(
                Specifications.requestSpec(),
                Specifications.responseSpec(config.status201()));

        EmployeeData expectedEmployeeData = EmployeeDataFactory.nameJobEmployeeData();

        EmployeeData receivedEmployeeData = given()
                .baseUri(config.urlReqresApi())
                .header("x-api-key", config.apiToken())
                .body(sentEmployeeData)
                .when()
                .post(config.users())
                .then().log().all()
                .extract().as(EmployeeData.class);

        assertEquals(expectedEmployeeData.getName(), receivedEmployeeData.getName());
        assertEquals(expectedEmployeeData.getJob(), receivedEmployeeData.getJob());
        assertNotNull(receivedEmployeeData.getId());
    }

    @ParameterizedTest
    @MethodSource("registerDataStream") // POST Login unsuccessful
    public void failLoginTest(Register sentRegisterData) {
        Specifications.installSpecification(
                Specifications.requestSpec(),
                Specifications.responseSpec(config.status400()));

        FailReg failReg = given()
                .baseUri(config.urlReqresApi())
                .header("x-api-key", config.apiToken())
                .body(sentRegisterData)
                .when()
                .post(config.login())
                .then().log().all()
                .extract().as(FailReg.class);
        assertEquals(config.missingPasswordError(), failReg.getError());
    }

    @ParameterizedTest
    @MethodSource("employeeDataStream") // PUT Update
    public void updateUserDataTest(EmployeeData sentEmployeeData) {
        Specifications.installSpecification(
                Specifications.requestSpec(),
                Specifications.responseSpec(config.status200()));

        EmployeeData receivedEmployeeData = given()
                .baseUri(config.urlReqresApi())
                .header("x-api-key", config.apiToken())
                .body(sentEmployeeData)
                .when()
                .put(config.users2())
                .then().log().all()
                .extract().as(EmployeeData.class);

        String regexExp = "(.{17})$";
        String currentTime = Clock.systemUTC().instant().toString().replaceAll(regexExp, "");

        String regexAct = "(.{11})$";
        assertEquals(currentTime, receivedEmployeeData.getUpdatedAt().replaceAll(regexAct, ""));
    }

    static Stream<EmployeeData> employeeDataStream() {
        return Stream.of(
                EmployeeDataFactory.nameJobEmployeeData(),
                EmployeeDataFactory.fullEmployeeData()
        );
    }

    static Stream<Register> registerDataStream() {
        return Stream.of(
                RegisterDataFactory.firstEmailData(),
                RegisterDataFactory.secondEmailData()
        );
    }

}
