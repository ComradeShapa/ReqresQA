package api.dataFactory;

import api.DTO.Register;
import net.datafaker.Faker;

import java.util.List;

public class FakerDataGenerator {

    private static final Faker faker = new Faker();

    public static List<Register> generateRegistersList(Integer numberOfAccounts) {

        return faker.collection(
                () -> new Register(
                        faker.internet().emailAddress(),
                        faker.credentials().password())
        )
                .len(numberOfAccounts)
                .generate();
    }

    public static int rndIntFromOneToIncluding(int max) {
        return faker.number().numberBetween(1, ++max);
    }
}