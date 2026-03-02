package api;

import api.DTO.Register;
import api.dataFactory.FakerDataGenerator;
import api.database.DBUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static api.dataFactory.RegisterDataFactory.*;
import static org.junit.jupiter.api.Assertions.*;

public class DatabasePositiveTest {

    private static final int numberOfAccounts = 10;
    public static List<Register> generatedAccountsList = FakerDataGenerator.generateRegistersList(numberOfAccounts);

    @BeforeEach
    public void databaseSetup() {
        DBUtilities.dropRegisterTable();
        DBUtilities.createRegisterTable();
        DBUtilities.postRegister(generatedAccountsList);
    }

    @ParameterizedTest
    @MethodSource("positiveRegisterDataStream")
    public void postRegisterTest(Register registerAccount) {
        DBUtilities.postRegister(registerAccount.getEmail(), registerAccount.getPassword());
        Register actualRegister = DBUtilities.selectRegisterByEmail(registerAccount.getEmail());

        assertEquals(registerAccount, actualRegister);
    }

    @ParameterizedTest
    @MethodSource("positiveRegisterDataStream")
    public void putRegisterTest(Register registerAccount) {
        int actualDBSize = DBUtilities.selectCountRegisterTable();

        int randomId = FakerDataGenerator.rndIntFromOneToIncluding(actualDBSize);
        String newEmail = registerAccount.getEmail();
        String newPassword = registerAccount.getPassword();

        DBUtilities.putRegisterById(randomId, newEmail, newPassword);
        Register actualPatchedRegister = DBUtilities.selectRegisterById(randomId);

        assertEquals(actualPatchedRegister, registerAccount);
    }

    @ParameterizedTest
    @MethodSource("positiveRegisterDataStream")
    public void patchRegisterTest(Register registerAccount) {
        int actualDBSize = DBUtilities.selectCountRegisterTable();
        int randomId = FakerDataGenerator.rndIntFromOneToIncluding(actualDBSize);
        DBUtilities.patchRegisterPasswordById(randomId, registerAccount.getPassword());
        Register patchedRegister = DBUtilities.selectRegisterById(randomId);

        assertEquals(registerAccount.getPassword(), patchedRegister.getPassword());
    }

    @Test
    public void deleteRegisterTest() {
        int actualDBSize = DBUtilities.selectCountRegisterTable();
        int randomId = FakerDataGenerator.rndIntFromOneToIncluding(actualDBSize);
        DBUtilities.deleteRegisterById(randomId);
        int expectedDBSize = DBUtilities.selectCountRegisterTable();

        assertNotEquals(expectedDBSize, actualDBSize);
    }

    @Test
    public void compareRegisterByIdTest() {
        int actualDBSize = DBUtilities.selectCountRegisterTable();
        int randomId = FakerDataGenerator.rndIntFromOneToIncluding(actualDBSize);
        int randomIndex = randomId - 1;

        Register expectedListRegister = generatedAccountsList.get(randomIndex);
        Register actualDatabaseRegister = DBUtilities.selectRegisterById(randomId);

        assertEquals(expectedListRegister, actualDatabaseRegister);
    }
    static Stream<Register> positiveRegisterDataStream() {
        return Stream.of(
                positiveRegisterData(),
                positiveEmailOf50SymbolsRegisterData(),
                positivePasswordOf50SymbolsRegisterData()
        );
    }
}