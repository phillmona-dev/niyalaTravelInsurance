package com.medco.Travel.insurance.entity;

import com.medco.Travel.insurance.shared.audit.Audit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "role_privileges")
public class RolePrivilege extends Audit {

    @Serial
    private static final long serialVersionUID = -4814904549349101427L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_uuid", referencedColumnName = "role_uuid", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "privilege_uuid", referencedColumnName = "privilege_uuid", nullable = false)
    private Privilege privilege;
}
