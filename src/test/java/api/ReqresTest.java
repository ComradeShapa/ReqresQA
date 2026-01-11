package api;

import api.POJO.*;
import api.config.ServerConfig;
import api.config.Specifications;
import io.restassured.http.ContentType;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Test;

import java.time.Clock;


import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class ReqresTest { // POST, PUT, один GET

    ServerConfig cfg = ConfigFactory.create(ServerConfig.class);

    @Test // GET Single User
    public void checkUserEmailTest() {
        Specifications.installSpecification(Specifications.requestSpec(cfg.urlReqres()), Specifications.responseSpec(200));

        UserData user = given()
                .auth().oauth2("x-api-key")
                .when()
                .contentType(ContentType.JSON)
                .get(cfg.urlReqresApiUsers2())
                .then().log().all()
                .extract().jsonPath().getObject("data", UserData.class);

        assertTrue(user.getEmail().endsWith("@reqres.in"));
    }

    @Test // POST Create user (returns token)
    public void successUserRegistrationTest() {
        Specifications.installSpecification(Specifications.requestSpec(cfg.urlReqres()), Specifications.responseSpec(200));
        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        Register regData = new Register("eve.holt@reqres.in", "pistol");

        SuccessReg successReg = given()
                .auth().oauth2("x-api-key")
                .body(regData)
                .when()
                .post(cfg.urlReqresApiRegister())
                .then().log().all()
                .extract().as(SuccessReg.class);

        assertNotNull(successReg);
        assertEquals(id, successReg.getId());
        assertEquals(token, successReg.getToken());
    }

    @Test // POST Register unsuccessful
    public void failUserRegistrationTest() {
        Specifications.installSpecification(Specifications.requestSpec(cfg.urlReqres()), Specifications.responseSpec(400));
        Register regData = new Register("sydney@fife", "");

        FailReg failReg = given()
                .auth().oauth2("x-api-key")
                .body(regData)
                .when()
                .post(cfg.urlReqresApiRegister())
                .then().log().all()
                .extract().as(FailReg.class);

        assertEquals("Missing password", failReg.getError());
    }

    @Test // POST Verify session
    public void verifySessionTest() {
        Specifications.installSpecification(Specifications.requestSpec(cfg.urlReqres()), Specifications.responseSpec(200));
        Register regData = new Register("eve.holt@reqres.in", "cityslicka");
        String expectedToken = "QpwL5tke4Pnpja7X4";

        Token actualToken = given()
                .auth().oauth2("x-api-key")
                .body(regData)
                .when()
                .post("api/login")
                .then().log().all()
                .extract().as(Token.class);

        assertEquals(expectedToken, actualToken.getToken());
    }

    @Test // POST Create record
    public void createRecordTest() {
        Specifications.installSpecification(Specifications.requestSpec(cfg.urlReqres()), Specifications.responseSpec(201));

        EmployeeData sentEmployeeData = new EmployeeData("morpheus", "leader", "", "", "");
        EmployeeData expectedEmployeeData = new EmployeeData("morpheus", "leader", "496", "2024-07-01T10:00:00.000Z", "");

        EmployeeData receivedEmployeeData = given()
                .auth().oauth2("x-api-key")
                .body(sentEmployeeData)
                .when()
                .post(cfg.urlReqresApiUsers())
                .then().log().all()
                .extract().as(EmployeeData.class);

        assertEquals(expectedEmployeeData.getName(), receivedEmployeeData.getName());
        assertEquals(expectedEmployeeData.getJob(), receivedEmployeeData.getJob());
        assertNotNull(receivedEmployeeData.getId());
    }

    @Test // POST Login unsuccessful
    public void failLoginTest() {
        Specifications.installSpecification(Specifications.requestSpec(cfg.urlReqres()), Specifications.responseSpec(400));
        Register regData = new Register("peter@klaven", "");

        FailReg failReg = given()
                .auth().oauth2("x-api-key")
                .body(regData)
                .when()
                .post(cfg.urlReqresApiLogin())
                .then().log().all()
                .extract().as(FailReg.class);
        assertEquals("Missing password", failReg.getError());
    }

    @Test // PUT Update
    public void updateUserDataTest() {
        Specifications.installSpecification(Specifications.requestSpec(cfg.urlReqres()), Specifications.responseSpec(200));

        EmployeeData sentEmployeeData = new EmployeeData("morpheus", "zion resident", "", "", "");

        EmployeeData receivedEmployeeData = given()
                .auth().oauth2("x-api-key")
                .body(sentEmployeeData)
                .when()
                .put(cfg.urlReqresApiUsers2())
                .then().log().all()
                .extract().as(EmployeeData.class);

        String regexExp = "(.{11})$";
        String currentTime = Clock.systemUTC().instant().toString().replaceAll(regexExp, "");

        String regexAct = "(.{5})$";
        assertEquals(currentTime, receivedEmployeeData.getUpdatedAt().replaceAll(regexAct, ""));
    }
}
