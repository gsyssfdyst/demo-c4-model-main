package com.biblioteca.c4.application.service;

import com.biblioteca.c4.common.dto.LibraryUserCreateRequestDTO;
import com.biblioteca.c4.common.dto.LibraryUserDTO;
import com.biblioteca.c4.common.exception.BusinessRuleException;
import com.biblioteca.c4.common.exception.ResourceNotFoundException;
import com.biblioteca.c4.domain.model.LibraryUser;
import com.biblioteca.c4.persistence.repository.LibraryUserRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibraryUserService {

    private final LibraryUserRepository libraryUserRepository;
    private final PasswordEncoder passwordEncoder;

    public LibraryUserService(LibraryUserRepository libraryUserRepository,
                              PasswordEncoder passwordEncoder) {
        this.libraryUserRepository = libraryUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<LibraryUserDTO> listAll() {
        return libraryUserRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public LibraryUserDTO getById(Long id) {
        LibraryUser user = libraryUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado: " + id));
        return toDTO(user);
    }

    @Transactional
    public LibraryUserDTO create(LibraryUserCreateRequestDTO request) {
        libraryUserRepository.findByEmail(request.email()).ifPresent(user -> {
            throw new BusinessRuleException("Email ja cadastrado: " + request.email());
        });

        LibraryUser user = new LibraryUser();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(request.role());
        user.setActive(request.active());
        return toDTO(libraryUserRepository.save(user));
    }

    @Transactional
    public LibraryUserDTO update(Long id, LibraryUserCreateRequestDTO request) {
        LibraryUser user = libraryUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado: " + id));

        libraryUserRepository.findByEmail(request.email())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BusinessRuleException("Email ja cadastrado: " + request.email());
                });

        user.setName(request.name());
        user.setEmail(request.email());
        user.setRole(request.role());
        user.setActive(request.active());

        if (request.password() != null && !request.password().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        return toDTO(libraryUserRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        if (!libraryUserRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario nao encontrado: " + id);
        }
        libraryUserRepository.deleteById(id);
    }

    public LibraryUserDTO toDTO(LibraryUser user) {
        return new LibraryUserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.isActive()
        );
    }
}
