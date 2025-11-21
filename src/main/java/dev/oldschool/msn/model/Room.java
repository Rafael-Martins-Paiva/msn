package dev.oldschool.msn.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private RoomType type;

    // For simplicity, creatorId for now. Can be linked to a User entity later.
    private Long creatorId;
}
