package com.epam.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainings")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long trainingId;

    @ManyToOne
    @JoinColumn(name = "trainee_id", referencedColumnName = "traineeId", nullable = false)
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "trainer_id", referencedColumnName = "trainerId", nullable = false)
    private Trainer trainer;

    @Column(name = "training_name", nullable = false)
    private String trainingName;

    @ManyToOne
    @JoinColumn(name = "training_type_id", referencedColumnName = "trainingTypeId", nullable = false)
    private TrainingType trainingType;

    @Column(name = "training_date", nullable = false)
    private LocalDate trainingDate;

    @Column(name = "training_duration", nullable = false)
    private int trainingDuration; // Assuming the duration is measured in minutes

    @Override
    public String toString() {
        return "Training{" +
                "trainingId=" + trainingId +
                ", trainee=" + trainee.getUser().getUsername() +
                ", trainer=" + trainer.getUser().getUsername() +
                ", trainingName='" + trainingName + '\'' +
                ", trainingType=" + trainingType.getTrainingTypeName() +
                ", trainingDate=" + trainingDate +
                ", trainingDuration=" + trainingDuration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Training training = (Training) o;
        return trainingId == training.trainingId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainingId);
    }
}
