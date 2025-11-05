package dev.oldschool.msn.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import dev.oldschool.msn.model.Room;
import dev.oldschool.msn.model.RoomType;
import dev.oldschool.msn.repository.RoomRepository;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public List<Room> listPublicRooms() {
        return roomRepository.findAll().stream()
                .filter(room -> room.getType() == RoomType.PUBLIC)
                .collect(Collectors.toList());
    }

    public Optional<Room> getRoomById(Long roomId) {
        return roomRepository.findById(roomId);
    }

    public List<Room> listRoomsByCreatorId(Long creatorId) {
        return roomRepository.findAll().stream()
                .filter(room -> room.getCreatorId().equals(creatorId))
                .collect(Collectors.toList());
    }

    // TODO: Add methods for joining/leaving private rooms, managing members, etc.
}
