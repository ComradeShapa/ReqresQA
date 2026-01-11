package api.POJO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EmployeeData {
    private String name;
    private String job;
    private String id;
    private String createdAt;
    private String updatedAt;
}
