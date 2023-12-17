package org.domain.events.infrastructure;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "Builder")
public class UserEntity {
    @Id
    private String id;
    private String name;
}
