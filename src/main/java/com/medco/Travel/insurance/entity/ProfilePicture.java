package com.medco.Travel.insurance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProfilePicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    // private String type;
    @Lob @Basic(fetch= FetchType.LAZY)
    @Column(name = "Profile", columnDefinition = "LONGBLOB")
    private byte[] imageData;

    //@OneToOne
    //@JoinColumn(name = "user_id", referencedColumnName = "id")
    private String userUuid;
}
