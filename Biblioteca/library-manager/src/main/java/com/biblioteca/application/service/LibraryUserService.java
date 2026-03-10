package com.biblioteca.application.service;

import com.biblioteca.common.dto.LibraryUserDTO;
import com.biblioteca.common.exception.ResourceNotFoundException;
import com.biblioteca.domain.model.LibraryUser;
import com.biblioteca.persistence.repository.LibraryUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LibraryUserService {

    private final LibraryUserRepository userRepository;

    public LibraryUserService(LibraryUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LibraryUserDTO createUser(LibraryUserDTO dto) {
        LibraryUser user = new LibraryUser(
            dto.getName(),
            dto.getEmail(),
            dto.getRole()
        );
        LibraryUser savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    @Transactional(readOnly = true)
    public List<LibraryUserDTO> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LibraryUserDTO getUserById(Long id) {
        LibraryUser user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convertToDTO(user);
    }

    @Transactional(readOnly = true)
    public LibraryUser getUserEntityById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private LibraryUserDTO convertToDTO(LibraryUser user) {
        return new LibraryUserDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole()
        );
    }
}
