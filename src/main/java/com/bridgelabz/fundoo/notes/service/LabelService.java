package com.bridgelabz.fundoo.notes.service;

import com.bridgelabz.fundoo.notes.dto.LabelRequestDTO;
import com.bridgelabz.fundoo.notes.dto.LabelResponseDTO;

import java.util.List;

public interface LabelService {

    LabelResponseDTO createLabel(LabelRequestDTO requestDTO, String email);

    List<LabelResponseDTO> getAllLabels(String email);

    LabelResponseDTO updateLabel(Long id, LabelRequestDTO requestDTO, String email);

    void deleteLabel(Long id, String email);
}