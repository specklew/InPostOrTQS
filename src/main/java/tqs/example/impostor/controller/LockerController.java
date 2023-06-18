package tqs.example.impostor.controller;

//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(description = "Get all lockers")
    @GetMapping
    public ResponseEntity<List<Locker>> getAllLockers() {
        List<Locker> lockers = lockerService.getAllLockers();
        return ResponseEntity.ok(lockers);
    }

    @Operation(description = "Get locker by ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "404", description = "Locker not found")})
    @GetMapping("/{id}")
    public ResponseEntity<Locker> getLockerById(@PathVariable("id") Long id) {
        Optional<Locker> locker = lockerService.getLockerById(id);
        return locker.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(description = "Get locker by address")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "404", description = "Locker not found")})
    @GetMapping("/address/{address}")
    public ResponseEntity<Locker> getLockerByAddress(@PathVariable String address) {
        Optional<Locker> locker = lockerService.getLockerByAddress(address);
        return locker.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(description = "Create a new locker")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Locker created"), @ApiResponse(responseCode = "400", description = "Bad request")})
    @PostMapping("/create")
    public ResponseEntity<Void> createLocker(@RequestParam Long id, @RequestParam String address, @RequestParam Float capacity) {
        boolean createdLocker = lockerService.createLocker(id, address, capacity);
        if (createdLocker) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(description = "Update a locker")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "404", description = "Locker not found")})
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

    @Operation(description = "Delete a locker")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Locker deleted"), @ApiResponse(responseCode = "404", description = "Locker not found")})
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
