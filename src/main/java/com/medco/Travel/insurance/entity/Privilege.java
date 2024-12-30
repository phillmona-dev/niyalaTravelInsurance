package com.medco.Travel.insurance.entity;

import com.medco.Travel.insurance.shared.audit.Audit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "privileges", uniqueConstraints = {
        @UniqueConstraint(columnNames = "privilegeName"),
        @UniqueConstraint(columnNames = "privilege_uuid")
})
public class Privilege extends Audit {

    @Serial
    private static final long serialVersionUID = 2369844719759914085L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrivilegeUuid() {
        return privilegeUuid;
    }

    public void setPrivilegeUuid(String privilegeUuid) {
        this.privilegeUuid = privilegeUuid;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    public String getPrivilegeDescription() {
        return privilegeDescription;
    }

    public void setPrivilegeDescription(String privilegeDescription) {
        this.privilegeDescription = privilegeDescription;
    }

    public String getPrivilegeCategory() {
        return privilegeCategory;
    }

    public void setPrivilegeCategory(String privilegeCategory) {
        this.privilegeCategory = privilegeCategory;
    }

    public List<RolePrivilege> getRolePrivileges() {
        return rolePrivileges;
    }

    public void setRolePrivileges(List<RolePrivilege> rolePrivileges) {
        this.rolePrivileges = rolePrivileges;
    }

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
