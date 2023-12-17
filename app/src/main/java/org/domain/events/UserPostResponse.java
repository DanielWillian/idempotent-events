package org.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.domain.events.domain.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "Builder")
public class UserPostResponse {
    private String id;
    private String name;

    public static UserPostResponse fromDomain(User user) {
        return builder()
                .id(user.getUserId().getId())
                .name(user.getName())
                .build();
    }
}
