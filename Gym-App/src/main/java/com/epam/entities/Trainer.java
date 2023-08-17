package com.epam.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainers")
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long trainerId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "specialization_id", referencedColumnName = "trainingTypeId", nullable = false)
    private TrainingType specialization;

    @ManyToMany
    @JoinTable(
            name = "Trainee2Trainer",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "trainee_id")
    )
    private Set<Trainee> trainees = new HashSet<>();

    @OneToMany(mappedBy = "trainer",cascade = CascadeType.ALL)
    private Set<Training> trainings = new HashSet<>();

    public void removeTrainee(Trainee trainee) {
        trainees.remove(trainee);
        trainee.getTrainers().remove(this);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trainer trainer = (Trainer) o;
        return trainerId == trainer.trainerId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainerId);
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "trainerId=" + trainerId +
                ", user=" + user +
                ", specialization=" + specialization.getTrainingTypeName() +
                '}';
    }
}
