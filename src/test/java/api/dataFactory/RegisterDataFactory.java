package api.dataFactory;

import api.DTO.Register;

public class RegisterDataFactory {

    public static Register firstEmailData() {
        return new Register(
                "sydney@fife",
                null
        );
    }

    public static Register secondEmailData() {
        return new Register(
                "peter@klaven",
                null
        );
    }
}
