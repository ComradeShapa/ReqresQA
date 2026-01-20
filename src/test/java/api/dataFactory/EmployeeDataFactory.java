package api.dataFactory;

import api.DTO.EmployeeData;

public class EmployeeDataFactory {

    public static EmployeeData nameJobEmployeeData() {
        return new EmployeeData(
                "morpheus",
                "leader",
                null,
                null,
                null
        );

    }
    public static EmployeeData fullEmployeeData() {
        return new EmployeeData(
                "morpheus",
                "leader",
                "496",
                "2024-07-01T10:00:00.000Z",
                null
        );
    }

}