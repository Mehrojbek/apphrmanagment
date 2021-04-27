package uz.pdp.apphrmanagment.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class MonthlySalaryDto {
    @NotNull
    private Double amount;

    @NotNull
    private Integer monthId;

    @NotNull
    private UUID userId;
}
