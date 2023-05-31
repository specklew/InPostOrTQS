package tqs.example.impostor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tqs.example.impostor.models.ACP;
import tqs.example.impostor.models.Locker;
import tqs.example.impostor.models.Order;
import tqs.example.impostor.service.ACPService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/acp")
public class ACPController {

    private final ACPService acpService;

    public ACPController(ACPService acpService) {
        this.acpService = acpService;
    }

    @GetMapping
    public ResponseEntity<List<ACP>> getAllACPs() {
        List<ACP> acps = acpService.getAllACPs();
        return ResponseEntity.ok(acps);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<ACP> getACPById(@PathVariable("id") Long id) {
        Optional<ACP> acp = acpService.getACPById(id);
        return acp.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/address/{address}")
    public ResponseEntity<ACP> getACPByAddress(@PathVariable String address) {
        Optional<ACP> acp = acpService.getACPByAddress(address);
        return acp.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createACP(@RequestParam Long id, @RequestParam String address, @RequestParam float capacity) {
        boolean created = acpService.createACP(id, address, capacity);
        if (created) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateACP(@PathVariable Long id, @RequestParam(required = false) String address, @RequestParam(required = false) Float capacity, @RequestParam(required = false) Set<Order> orders) {
        boolean updated = acpService.updateACP(id, address, capacity, orders);
        if (updated) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteACP(@PathVariable Long id) {
        boolean deleted = acpService.deleteACP(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}




