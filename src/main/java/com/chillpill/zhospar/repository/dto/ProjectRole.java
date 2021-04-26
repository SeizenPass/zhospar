package com.chillpill.zhospar.repository.dto;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class ProjectRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roleId;

    @Column(nullable = false)
    private String roleName;

    private String description;

    @OneToMany(mappedBy = "projectRole")
    private List<ProjectMembership> memberships;
}
