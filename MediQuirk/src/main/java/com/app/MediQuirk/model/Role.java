package com.app.MediQuirk.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @NotBlank
    private String roleName;

    private String roleDescription;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<Users> users;
}
