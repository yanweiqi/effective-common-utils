package com.effective.common.disruptor.one;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LongEvent {

    private Long value;

}
