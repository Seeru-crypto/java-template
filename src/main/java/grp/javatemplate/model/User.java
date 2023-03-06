package grp.javatemplate.model;

import grp.javatemplate.model.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"user\"")
public class User extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    @NotNull
    @Column(nullable = false)
    @Size(min = 2, message = "{validation.name.size.too_short}")
    @Size(max = 200, message = "{validation.name.size.too_long}")
    private String name;

    @Column
    private Instant dob;

    @Column
    private UserRole role;

    @Column
    private String password;

//    @OneToMany(
//            mappedBy = "flowStepId",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
//    private List<FlowStepStation> flowStepStations  = new ArrayList<>();
//
//    public FlowStep addFlowStation(FlowStepStation flowStepStation) {
//        flowStepStations.add(flowStepStation);
//        flowStepStation.setFlowStepId(this.getId());
//        return this;
//    }

//    @ManyToMany(
//            fetch = FetchType.EAGER
//    )
//    @JoinTable(name = "order_tag",
//            joinColumns = @JoinColumn(name = "order_id"),
//            inverseJoinColumns = @JoinColumn(name = "tag_id")
//    )
//    private Set<Tag> tags = new HashSet<>();

//    @NotNull
//    @OneToOne
//    @JoinColumn(name = "type_id", referencedColumnName = "id")
//    private StationType stationType;


}
