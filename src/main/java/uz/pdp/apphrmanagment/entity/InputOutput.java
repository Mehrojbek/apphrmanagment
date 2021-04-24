package uz.pdp.apphrmanagment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.apphrmanagment.entity.enums.InOutStatus;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class InputOutput {
    @Id
    @GeneratedValue
    private UUID id;

    private InOutStatus status;

    private Timestamp time;

    @ManyToOne
    private Turniket turniket;
}
