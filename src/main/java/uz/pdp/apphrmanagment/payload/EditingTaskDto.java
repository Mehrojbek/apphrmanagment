package uz.pdp.apphrmanagment.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.UUID;
@Data
public class EditingTaskDto {
    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Date deadline;

    @NotNull
    private UUID performerId;

    @NotNull
    private String status;
}
