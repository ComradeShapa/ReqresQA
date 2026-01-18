package api.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeData {
    private String name;
    private String job;
    private String id;
    private String createdAt;
    private String updatedAt;
}
