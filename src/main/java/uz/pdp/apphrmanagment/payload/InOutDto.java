package uz.pdp.apphrmanagment.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class InOutDto {
    @NotNull
    private String inOutStatus;

    @NotNull
    private UUID userId;
}
