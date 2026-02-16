package api.dataFactory;

import api.DTO.Register;
import net.datafaker.Faker;

import java.util.List;

public class UserDataGenerator {

    public static List<Register> generateAccountsList(Integer numberOfAccounts) {

        Faker faker = new Faker();

        return faker.collection(
                () -> new Register(
                        faker.internet().emailAddress(),
                        faker.credentials().password())
        )
                .len(numberOfAccounts)
                .generate();
    }

}