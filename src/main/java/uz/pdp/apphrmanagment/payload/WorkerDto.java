package uz.pdp.apphrmanagment.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;

@Data
public class WorkerDto {

    @NotNull
    private Timestamp from;

    @NotNull
    private Timestamp to;

}
