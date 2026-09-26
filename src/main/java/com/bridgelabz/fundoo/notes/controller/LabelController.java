package com.bridgelabz.fundoo.notes.controller;

import com.bridgelabz.fundoo.notes.dto.LabelRequestDTO;
import com.bridgelabz.fundoo.notes.dto.LabelResponseDTO;
import com.bridgelabz.fundoo.notes.service.LabelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
public class LabelController {

    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @PostMapping
    public ResponseEntity<LabelResponseDTO> createLabel(@Valid @RequestBody LabelRequestDTO requestDTO, Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(labelService.createLabel(requestDTO, email));
    }

    @GetMapping
    public ResponseEntity<List<LabelResponseDTO>> getAllLabels(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(labelService.getAllLabels(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabelResponseDTO> updateLabel(@PathVariable Long id, @Valid @RequestBody LabelRequestDTO requestDTO, Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(labelService.updateLabel(id, requestDTO, email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabel(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        labelService.deleteLabel(id, email);

        return ResponseEntity.noContent().build();
    }
}