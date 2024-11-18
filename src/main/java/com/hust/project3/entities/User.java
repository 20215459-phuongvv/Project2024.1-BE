package com.hust.project3.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "status")
    private Integer status;

    @Column(name = "verificationToken")
    private String verificationToken;

    @Column(name = "role")
    private String role;

    @UpdateTimestamp
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "updatedBy")
    private String updatedBy;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("user")
    private List<ReadingCard> readingCardList;
}
