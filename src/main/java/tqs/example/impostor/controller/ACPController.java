package tqs.example.impostor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tqs.example.impostor.models.ACP;
import tqs.example.impostor.service.ACPService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/acp")
public class ACPController {

    private final ACPService acpService;

    public ACPController(ACPService acpService) {
        this.acpService = acpService;
    }

    @Operation(description = "Show the home page to the user")
    @GetMapping("")
    public ResponseEntity<Resource> getDefaultPage() throws IOException {
        Resource htmlResource = new ClassPathResource("templates/adminPanel/ACP.html");
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(htmlResource);
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "404", description = "No ACPs found")})
    @Operation(description = "Get all ACPs")
    @GetMapping("/get/all")
    public ResponseEntity<List<ACP>> getAllACPs() {
        List<ACP> acps = acpService.getAllACPs();
        return ResponseEntity.ok(acps);
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "404", description = "ACP not found")})
    @Operation(description = "Get an ACP by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ACP> getACPById(@PathVariable("id") Long id) {
        Optional<ACP> acp = acpService.getACPById(id);
        return acp.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "404", description = "ACP not found")})
    @Operation(description = "Get an ACP by address")
    @GetMapping("/address/{address}")
    public ResponseEntity<ACP> getACPByAddress(@PathVariable String address) {
        Optional<ACP> acp = acpService.getACPByAddress(address);
        return acp.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "201", description = "ACP created successfully"), @ApiResponse(responseCode = "400", description = "Invalid request parameters")})
    @Operation(description = "Create an ACP")
    @PostMapping("/create")
    public ResponseEntity<Void> createACP(@RequestParam Long id, @RequestParam String address, @RequestParam Float capacity) {
        boolean created = acpService.createACP(id, address, capacity);
        if (created) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "404", description = "ACP not found")})
    @Operation(description = "Update an ACP")
    @PutMapping("/update/{id}")
    public ResponseEntity<Boolean> updateACP(@PathVariable("id") Long id, @RequestParam(required = false) String address, @RequestParam(required = false) Float capacity) {
        Optional<ACP> optionalACP = acpService.getACPById(id);
        if (optionalACP.isPresent()) {
            boolean updatedACP = acpService.updateACP(id, address, capacity);
            return ResponseEntity.ok(updatedACP);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "404", description = "ACP not found")})
    @Operation(description = "Delete an ACP by ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteACP(@PathVariable("id") long id) {
        return ResponseEntity.ok(acpService.deleteACP(id));
    }
}




