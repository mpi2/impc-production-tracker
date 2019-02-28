package uk.ac.ebi.impc_prod_tracker.data.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Entity
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull
    private Boolean is_current;

    private Long parentPlanId;


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    void createdAt() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}
