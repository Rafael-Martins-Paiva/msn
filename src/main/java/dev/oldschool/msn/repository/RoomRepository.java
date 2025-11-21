package dev.oldschool.msn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.oldschool.msn.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
}