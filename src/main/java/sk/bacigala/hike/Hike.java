package sk.bacigala.hike;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hike {

    @Id
    private Integer id;
    private long peak_id, difficulty, author_id;
    private String name;
    private Date date;

}
