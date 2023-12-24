package org.domain.events.application.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserCreatedMessage extends EventMessage {
    private String userId;
    private String name;

    @Builder
    public UserCreatedMessage(String messageId, String userId, String name) {
        super(messageId);
        this.userId = userId;
        this.name = name;
    }
}
