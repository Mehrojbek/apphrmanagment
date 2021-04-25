package uz.pdp.apphrmanagment.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class ReseiveTaskDto {
    @NotNull
    private UUID taskId;

    @NotNull
    private UUID performerId;

    @NotNull
    private boolean acceptedByPerformer;
}
