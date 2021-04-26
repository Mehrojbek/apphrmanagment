package uz.pdp.apphrmanagment.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class MonthlySalaryDto {
    @NotNull
    private Double amount;

    @NotNull
    private String monthName;

    @NotNull
    private UUID userId;
}
