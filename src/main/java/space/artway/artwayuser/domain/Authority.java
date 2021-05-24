package space.artway.artwayuser.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "Authorities")
public class Authority {
    @Id
    @Column(name = "name")
    private String name;
}
