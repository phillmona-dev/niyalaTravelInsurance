package com.medco.Travel.insurance.entity;

import com.medco.Travel.insurance.shared.audit.Audit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Table(name = "privileges", uniqueConstraints = {
//        @UniqueConstraint(columnNames = "privilege_Name"),
//        @UniqueConstraint(columnNames = "privilege_uuid")
//})
public class Privilege extends Audit {

    @Serial
    private static final long serialVersionUID = 2369844719759914085L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "privilege_uuid", unique = true, nullable = false)
    private String privilegeUuid = UUID.randomUUID().toString();

    @Column(length = 50, nullable = false)
    private String privilegeName;

    @NotBlank
    @Size(max = 100)
    private String privilegeDescription;

    @NotBlank
    @Column(length = 50, nullable = false)
    private String privilegeCategory;

    @OneToMany(mappedBy = "privilege", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RolePrivilege> rolePrivileges = new ArrayList<>();
}
