package com.epam.collections;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Month;

@Data
@AllArgsConstructor
@Builder
public class MonthData {
    private Month month;
    private int trainingDurationSummary;
}




