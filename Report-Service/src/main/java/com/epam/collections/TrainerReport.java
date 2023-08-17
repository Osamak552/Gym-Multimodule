package com.epam.collections;

import lombok.Builder;
import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "trainer-report")
@Data
@Builder
public class TrainerReport{
    @Id
    private String username;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private List<YearData> yearsList;
}
