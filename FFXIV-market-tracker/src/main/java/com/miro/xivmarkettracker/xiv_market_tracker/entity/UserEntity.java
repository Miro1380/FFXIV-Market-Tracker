package com.miro.xivmarkettracker.xiv_market_tracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Unique at the DB level, not just checked in the service: a check-then-insert
    //can be raced by two concurrent registrations, the constraint can't be.
    @Column(nullable = false, unique = true)
    private String username;

    private String email;

    private String homeWorld;          // defaults Universalis queries to their server

    @Column(nullable = false)
    private String passwordHash;       // unless you're using OAuth only


    @CreationTimestamp
    private LocalDateTime createdAt;
}
