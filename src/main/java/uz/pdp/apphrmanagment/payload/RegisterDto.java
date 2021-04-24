package uz.pdp.apphrmanagment.payload;

import lombok.Data;
import uz.pdp.apphrmanagment.entity.enums.RoleName;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RegisterDto {
    @NotNull
    @Size(min = 3,max = 50)
    private String firstName;

    @NotNull
    @Size(min = 3,max = 50)
    private String lastName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 8)
    private String password;

    @NotNull
    private RoleName roleName;
}
