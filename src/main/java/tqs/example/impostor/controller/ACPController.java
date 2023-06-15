package tqs.example.impostor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tqs.example.impostor.models.ACP;
import tqs.example.impostor.service.ACPService;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{id}")
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
    public ResponseEntity<Void> createACP(@RequestParam Long id, @RequestParam String address, @RequestParam Float capacity) {
        boolean created = acpService.createACP(id, address, capacity);
        if (created) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Boolean> updateACP(@PathVariable Long id, @RequestParam(required = false) String address, @RequestParam(required = false) Float capacity) {
        Optional<ACP> optionalACP = acpService.getACPById(id);
        if (optionalACP.isPresent()) {
            boolean updatedACP = acpService.updateACP(id, address, capacity);
            return ResponseEntity.ok(updatedACP);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteACP(@PathVariable("id") long id) {
        return ResponseEntity.ok(acpService.deleteACP(id));
    }
}




