package org.gentar.biology.targ_rep.centre;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class TargRepCentre extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "targRepCentreSeq", sequenceName = "TARG_REP_CENTRE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "targRepCentreSeq")
    private Long id;

    @Column(unique = true)
    @NotNull
    private String name;

    private String fullName;

    private String ilarCode;

    private String contactName;

    private String contactEmail;

    private String superscript;
}
