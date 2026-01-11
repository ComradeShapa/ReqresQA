package api.POJO;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserData {
    private Integer id;
    private String email;
    private String first_name;
    private String last_name;
    private String avatar;
}


