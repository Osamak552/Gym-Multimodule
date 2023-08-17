package com.epam.collections;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;


@Data
@AllArgsConstructor
public class YearData {
    private int year;
    private List<MonthData> monthsList;
}
