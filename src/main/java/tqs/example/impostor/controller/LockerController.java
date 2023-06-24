package tqs.example.impostor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tqs.example.impostor.models.Locker;
import tqs.example.impostor.service.LockerService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/locker")
public class LockerController {

    private final LockerService lockerService;

    public LockerController(LockerService lockerService) {
        this.lockerService = lockerService;
    }

    @GetMapping
    public ResponseEntity<List<Locker>> getAllLockers() {
        List<Locker> lockers = lockerService.getAllLockers();
        return ResponseEntity.ok(lockers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Locker> getLockerById(@PathVariable("id") Long id) {
        Optional<Locker> locker = lockerService.getLockerById(id);
        return locker.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/address/{address}")
    public ResponseEntity<Locker> getLockerByAddress(@PathVariable String address) {
        Optional<Locker> locker = lockerService.getLockerByAddress(address);
        return locker.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createLocker(@RequestParam Long id, @RequestParam String address, @RequestParam Float capacity) {
        boolean createdLocker = lockerService.createLocker(id, address, capacity);
        if (createdLocker) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Boolean> updateLocker(@PathVariable("id") Long id, @RequestParam(required = false) String address, @RequestParam(required = false) Float capacity) {
        Optional<Locker> optionalLocker = lockerService.getLockerById(id);
        if (optionalLocker.isPresent()) {
            boolean updatedLocker = lockerService.updateLocker(id, address, capacity);
            return ResponseEntity.ok(updatedLocker);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteLocker(@PathVariable("id") long id) {
        Optional<Locker> lockerOptional = lockerService.getLockerById(id);

        if (lockerOptional.isPresent()) {
            lockerService.deleteLocker(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
