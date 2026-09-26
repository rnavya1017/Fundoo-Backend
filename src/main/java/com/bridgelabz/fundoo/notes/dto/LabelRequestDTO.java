package com.bridgelabz.fundoo.notes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelRequestDTO {

    @NotBlank(message = "Label name is required")
    @Size(max = 50, message = "Label name must not exceed 50 characters")
    private String name;
}