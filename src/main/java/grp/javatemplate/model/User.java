package grp.javatemplate.model;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import grp.javatemplate.model.enums.UserRole.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.Instant;

import static grp.javatemplate.exception.UserException.INVALID_USER_EMAIL;
import static grp.javatemplate.exception.UserException.INVALID_USER_PHONE_NUMBER;
import static jakarta.persistence.EnumType.STRING;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"user\"")
public class User extends AbstractAuditingEntity<Long> {
    public static final int MAX_EMAIL_LEN = 35;
    public static final String EMAIL_REGEX = "^[\\w!#$%&’*+\\/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+\\/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    public static final String PHONE_NR_REGEX = "\\+[0-9]{1,3} [0-9]{7,10}";

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

    @Column(columnDefinition = "user_role")
    @Enumerated(STRING)
    @Type(PostgreSQLEnumType.class)
    private Roles role;

    @Column(unique = true)
    @Size(max = MAX_EMAIL_LEN, message = "Email is too long")
    @Email(regexp = EMAIL_REGEX, message = INVALID_USER_EMAIL)
    private String email;

    @Column(name = "phone_number", unique = true)
    @Pattern(regexp = PHONE_NR_REGEX, message = INVALID_USER_PHONE_NUMBER)
    private String phoneNumber;
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
