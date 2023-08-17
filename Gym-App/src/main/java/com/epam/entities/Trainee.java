package com.epam.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainees")
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long traineeId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false, unique = true)
    private User user;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "address")
    private String address;

    @ManyToMany(mappedBy = "trainees")
    private Set<Trainer> trainers = new HashSet<>();

    @OneToMany(mappedBy = "trainee",cascade = CascadeType.ALL)
    private Set<Training> trainings = new HashSet<>();


    public void removeTrainer(Trainer trainer) {
        trainers.remove(trainer);
        trainer.getTrainees().remove(this);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trainee trainee = (Trainee) o;
        return traineeId == trainee.traineeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(traineeId);
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "traineeId=" + traineeId +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                '}';
    }
}