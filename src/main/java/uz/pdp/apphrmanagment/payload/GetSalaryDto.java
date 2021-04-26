package uz.pdp.apphrmanagment.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class GetSalaryDto {

    @NotNull
    private String monthName;

    private UUID workerId;
}
