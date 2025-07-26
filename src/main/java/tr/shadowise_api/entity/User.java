package tr.shadowise_api.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "users")
public class User extends SoftDeleteEntity {
    @NotNull
    private String email;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String[] roles;
    @NotNull
    private Date lastLogin;
}
