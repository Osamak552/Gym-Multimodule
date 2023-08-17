package com.epam.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "training_types")
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long trainingTypeId;
    @Column(name = "training_type_name", nullable = false,unique = true)
    private String trainingTypeName;
    @OneToMany(mappedBy = "specialization")
    private Set<Trainer> trainers = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainingType that = (TrainingType) o;
        return trainingTypeId == that.trainingTypeId && Objects.equals(trainingTypeName, that.trainingTypeName);
    }
    @Override
    public int hashCode() {
        return Objects.hash(trainingTypeId, trainingTypeName);
    }

}