package com.github.avvero.herald.dto.bf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fxdev-belyaev-ay
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextMessage {
    private String type;
    private String text;
}
