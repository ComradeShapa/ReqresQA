package api;

import api.DTO.Register;
import api.dataFactory.FakerDataGenerator;
import api.database.DBUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static api.dataFactory.RegisterDataFactory.*;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseNegativeTest {

    private static final int numberOfAccounts = 10;
    public static List<Register> generatedAccountsList = FakerDataGenerator.generateRegistersList(numberOfAccounts);

    @BeforeEach
    public void databaseSetup() {
        DBUtilities.dropRegisterTable();
        DBUtilities.createRegisterTable();
        DBUtilities.postRegister(generatedAccountsList);
    }

    @ParameterizedTest
    @MethodSource("invalidEmailRegisterDataStream")
    public void postInvalidEmailRegisterNegativeTest(Register invalidAccount) {
        assertThrows(RuntimeException.class, () ->
                DBUtilities.postRegister(invalidAccount.getEmail(), invalidAccount.getPassword())
        );
    }

    @ParameterizedTest
    @MethodSource("invalidPasswordRegisterDataStream")
    public void postInvalidPasswordRegisterNegativeTest(Register invalidAccount) {
        assertThrows(RuntimeException.class, () ->
                DBUtilities.postRegister(invalidAccount.getEmail(), invalidAccount.getPassword())
        );
    }

    @ParameterizedTest
    @MethodSource("invalidEmailRegisterDataStream")
    public void putInvalidEmailRegisterNegativeTest(Register registerAccount) {
        int actualDBSize = DBUtilities.selectCountRegisterTable();

        int randomId = FakerDataGenerator.rndIntFromOneToIncluding(actualDBSize);
        String newEmail = registerAccount.getEmail();
        String newPassword = registerAccount.getPassword();

        assertThrows(RuntimeException.class, () ->
                DBUtilities.putRegisterById(randomId, newEmail, newPassword)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidPasswordRegisterDataStream")
    public void putInvalidPasswordRegisterNegativeTest(Register registerAccount) {
        int actualDBSize = DBUtilities.selectCountRegisterTable();

        int randomId = FakerDataGenerator.rndIntFromOneToIncluding(actualDBSize);
        String newEmail = registerAccount.getEmail();
        String newPassword = registerAccount.getPassword();

        assertThrows(RuntimeException.class, () ->
                DBUtilities.putRegisterById(randomId, newEmail, newPassword)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidPasswordRegisterDataStream")
    public void patchRegisterPasswordNegativeTest(Register registerAccount) {
        int actualDBSize = DBUtilities.selectCountRegisterTable();
        int randomId = FakerDataGenerator.rndIntFromOneToIncluding(actualDBSize);
        String expectedPassword = registerAccount.getPassword();

        assertThrows(RuntimeException.class, () ->
                DBUtilities.patchRegisterPasswordById(randomId, expectedPassword)
        );
    }

    static Stream<Register> invalidEmailRegisterDataStream() {
        return Stream.of(
                negativeCyrillicEmailRegisterData(),
                negativeTooLongEmailRegisterData(),
                negativeEmailAtSymbolRegisterData(),
                negativeEmptyEmailRegisterData(),
                negativeNullEmailRegisterData(),
                negativeEmailNoAtSymbolRegisterData(),
                negativeWrongSymbolEmailRegisterData()
        );
    }

    static Stream<Register> invalidPasswordRegisterDataStream() {
        return Stream.of(
                negativeTooLongPasswordRegisterData(),
                negativeEmptyPasswordRegisterData(),
                negativeNullPasswordRegisterData(),
                negativePasswordOfOneSymbolRegisterData(),
                negativeCyrillicPasswordRegisterData()
        );
    }
}