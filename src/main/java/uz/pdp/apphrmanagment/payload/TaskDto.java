package uz.pdp.apphrmanagment.payload;

import lombok.Data;



import java.sql.Date;
import java.util.*;
import java.util.UUID;

@Data
public class TaskDto {

    private String name;

    private String description;

    private Date deadline;

    private List<UUID> users;
}
