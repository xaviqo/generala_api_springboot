package tech.xavi.generalabe.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TimeRule {

    NO_LIMIT(0), // 0
    SEC_30(30), // 1
    SEC_60(60), // 2
    SEC_90(90), // 3
    SEC_120(120); // 4

    private final int time;
}