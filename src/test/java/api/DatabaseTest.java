package api;

import api.DTO.Register;
import api.config.Configuration;
import api.dataFactory.UserDataGenerator;
import api.database.DBUtilities;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {

    private static final Configuration config = ConfigFactory.create(Configuration.class);

    public static Register generatedAccount = UserDataGenerator.generateRegister();
    public static List<Register> generatedAccountsList = UserDataGenerator.generateRegistersList(10);

    @BeforeEach
    public void databaseSetup() {
        DBUtilities.dropRegisterTable();
        DBUtilities.createRegisterTable();
        DBUtilities.postRegister(generatedAccountsList);
        DBUtilities.postRegister(generatedAccount);
    }

    @Test
    public void postRegisterTest() {
        Register actualRegister = DBUtilities.selectRegisterByEmail(generatedAccount.getEmail());
        assertEquals(actualRegister, generatedAccount);
    }

    @Test
    public void patchRegisterTest() {
        String expectedPassword = config.passwordDB();
        DBUtilities.patchRegister(generatedAccount.getEmail(), expectedPassword);
        Register patchedRegister = DBUtilities.selectRegisterByEmail(generatedAccount.getEmail());

        assertNotNull(patchedRegister);
        assertEquals(expectedPassword, patchedRegister.getPassword());
    }

    @Test
    public void deleteRegisterTest() {
        DBUtilities.deleteRegisterByEmail(generatedAccount.getEmail());
        assertNull(DBUtilities.selectRegisterByEmail(generatedAccount.getEmail()));
    }

    @Test
    public void compareRegisterByIdTest() {
        Random random = new Random();
        int arrayIndex = random.nextInt(generatedAccountsList.size());

        Register expectedListRegister = generatedAccountsList.get(arrayIndex);
        Register actualDatabaseRegister = DBUtilities.selectRegisterById(arrayIndex);

        assertEquals(expectedListRegister, actualDatabaseRegister);
    }
}