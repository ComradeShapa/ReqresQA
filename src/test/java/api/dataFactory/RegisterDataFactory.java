package api.dataFactory;

import api.DTO.Register;

public class RegisterDataFactory {

    public static Register positiveRegisterData() {
        return new Register(
                "positive@somewhere.com",
                "my.passw0rd"
        );
    }

    public static Register negativeTooLongEmailRegisterData() {
        return new Register(
                "wayTooLongEmailDataWithOver50SymbolsHere@somewhere.com",
                "my.passw0rd"
        );
    }

    public static Register positiveEmailOf50SymbolsRegisterData() {
        return new Register(
                "emailMadeOf50SymbolsRegistersData@notsomewhere.com",
                "my.passw0rd"
        );
    }

    public static Register negativeEmailAtSymbolRegisterData() {
        return new Register(
                "@",
                "my.passw0rd"
        );
    }

    public static Register negativeEmptyEmailRegisterData() {
        return new Register(
                "   ",
                "my.passw0rd"
        );
    }

    public static Register negativeNullEmailRegisterData() {
        return new Register(
                "",
                "my.passw0rd"
        );
    }

    public static Register negativeCyrillicEmailRegisterData() {
        return new Register(
                "позитив@somewhere.com",
                "my.passw0rd"
        );
    }

    public static Register negativeEmailNoAtSymbolRegisterData() {
        return new Register(
                "positivesomewhere.com",
                "my.passw0rd"
        );
    }

    public static Register negativeWrongSymbolEmailRegisterData() {
        return new Register(
                "pos&tive@somewhere.com",
                "my.passw0rd"
        );
    }


    public static Register negativeTooLongPasswordRegisterData() {
        return new Register(
                "positive@somewhere.com",
                "wayTooLongPasswordDataWithOver50SymbolsHereNotGonnaWorkNotGonnaWork"
        );
    }

    public static Register positivePasswordOf50SymbolsRegisterData() {
        return new Register(
                "positive@somewhere.com",
                "wayTo_LongPasswordDataWithOver50SymbolHereNotGonna"
        );
    }

    public static Register negativePasswordOfOneSymbolRegisterData() {
        return new Register(
                "positive@somewhere.com",
                "s"
        );
    }

    public static Register negativeEmptyPasswordRegisterData() {
        return new Register(
                "positive@somewhere.com",
                "   "
        );
    }

    public static Register negativeNullPasswordRegisterData() {
        return new Register(
                "positive@somewhere.com",
                ""
        );
    }

    public static Register negativeCyrillicPasswordRegisterData() {
        return new Register(
                "positive@somewhere.com",
                "мой_пар0ль"
        );
    }
}
