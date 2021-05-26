package com.chillpill.zhospar.repository.dto;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "project_membership",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"account_id", "project_id"})
        })
public class ProjectMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long membershipId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "project_role_id")
    private ProjectRole projectRole;
}
