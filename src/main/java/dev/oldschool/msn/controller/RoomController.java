package dev.oldschool.msn.controller;

import dev.oldschool.msn.model.Room;
import dev.oldschool.msn.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        Room createdRoom = roomService.createRoom(room);
        return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
    }

    @GetMapping("/public")
    public ResponseEntity<List<Room>> getPublicRooms() {
        List<Room> publicRooms = roomService.listPublicRooms();
        return ResponseEntity.ok(publicRooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{creatorId}")
    public ResponseEntity<List<Room>> getRoomsByCreatorId(@PathVariable Long creatorId) {
        List<Room> userRooms = roomService.listRoomsByCreatorId(creatorId);
        return ResponseEntity.ok(userRooms);
    }

    // TODO: Add endpoints for joining/leaving rooms, managing members, etc.
}
