package com.chillpill.zhospar.repository.dto;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Invite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long inviteId;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "invited_account_id", nullable = false)
    private Account invitedAccount;

    @ManyToOne
    @JoinColumn(name = "inviter_account_id", nullable = false)
    private Account inviterAccount;

    @Column(name = "accepted", columnDefinition = "boolean default false")
    private boolean accepted;

    @Column(name = "expired", columnDefinition = "boolean default false")
    private boolean expired;
}
