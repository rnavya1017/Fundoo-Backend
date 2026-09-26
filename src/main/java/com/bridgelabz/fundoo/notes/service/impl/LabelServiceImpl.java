package com.bridgelabz.fundoo.notes.service.impl;

import com.bridgelabz.fundoo.notes.dto.LabelRequestDTO;
import com.bridgelabz.fundoo.notes.dto.LabelResponseDTO;
import com.bridgelabz.fundoo.notes.entity.Label;
import com.bridgelabz.fundoo.notes.entity.Note;
import com.bridgelabz.fundoo.notes.entity.User;
import com.bridgelabz.fundoo.notes.exception.LabelNotFoundException;
import com.bridgelabz.fundoo.notes.exception.UserNotFoundException;
import com.bridgelabz.fundoo.notes.repository.LabelRepository;
import com.bridgelabz.fundoo.notes.repository.NoteRepository;
import com.bridgelabz.fundoo.notes.repository.UserRepository;
import com.bridgelabz.fundoo.notes.service.LabelService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;
    private final UserRepository userRepository;


    public LabelServiceImpl(LabelRepository labelRepository, UserRepository userRepository) {
        this.labelRepository = labelRepository;
        this.userRepository = userRepository;
    }

    @Override
    public LabelResponseDTO createLabel(LabelRequestDTO requestDTO, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if(labelRepository.existsByNameAndUserId(requestDTO.getName(), user.getId())){
            throw new IllegalArgumentException("Label already exists");
        }

        Label label = new Label();
        label.setName(requestDTO.getName());
        label.setUser(user);

        Label savedLabel = labelRepository.save(label);

        return convertToResponse(savedLabel);
    }


    @Override
    public List<LabelResponseDTO> getAllLabels(String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return labelRepository
                .findAllByUserId(user.getId())
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public LabelResponseDTO updateLabel(Long id, LabelRequestDTO requestDTO, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Label label = labelRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new LabelNotFoundException("Label not found"));

        label.setName(requestDTO.getName());

        Label updatedLabel = labelRepository.save(label);

        return convertToResponse(updatedLabel);
    }

    @Override
    public void deleteLabel(Long id, String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Label label = labelRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new LabelNotFoundException("Label not found"));

        labelRepository.delete(label);
    }

    private LabelResponseDTO convertToResponse(Label label) {
        return new LabelResponseDTO(
                label.getId(),
                label.getName()
        );
    }
}
