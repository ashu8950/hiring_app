package com.example.onboarding.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // One-to-One with Manager
    @OneToOne(mappedBy = "branch", cascade = CascadeType.ALL)
    private Manager manager;

    // One-to-Many with HRs
    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    private List<HR> hrs;

    // One-to-Many with Candidates
    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    private List<Candidate> candidates;
}
