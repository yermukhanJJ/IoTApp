package kz.car.iotapp.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "IOT_TAB")
@Getter
@Setter
@NoArgsConstructor
public class IoT {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "THING_NAME", length = 100)
    private String thingName;

    @Column(name = "THING_ATTRIBUTE", length = 100)
    private String thingAttribute;

    @ManyToOne
    private Vehicle vehicle;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        IoT ioT = (IoT) o;
        return id != null && Objects.equals(id, ioT.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
