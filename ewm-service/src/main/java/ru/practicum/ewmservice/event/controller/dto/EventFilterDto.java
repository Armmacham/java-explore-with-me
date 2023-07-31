package ru.practicum.ewmservice.event.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmservice.state.State;

import javax.validation.constraints.Min;
import java.util.List;

@Data
@NoArgsConstructor
public class EventFilterDto {
    private List<Long> users;
    private List<State> states;
    private List<Long> categories;
    private String rangeStart;
    private String rangeEnd;
    @Min(0)
    private int from = 0;
    @Min(1)
    private int size = 10;
}
