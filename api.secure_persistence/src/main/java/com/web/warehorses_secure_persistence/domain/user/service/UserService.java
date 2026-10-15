package com.web.warehorses_secure_persistence.domain.user.service;

import com.web.warehorses_secure_persistence.domain.user.dto.UserDetailsDto;
import com.web.warehorses_secure_persistence.domain.user.entity.User;
import com.web.warehorses_secure_persistence.domain.user.repository.UserRepository;
import com.web.warehorses_secure_persistence.domain.user.util.PasswordEncoderProvider;
import com.web.warehorses_secure_persistence.exception.UserNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository,
                       PasswordEncoderProvider passwordEncoderProvider) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(int id) {

        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User findByIdCardNumber(String idCardNumber) {
        return userRepository.findByIdCardNumber(idCardNumber)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByIdCardNumber(String idCardNumber) {
        return userRepository.existsByIdCardNumber(idCardNumber);
    }

    @Override
    @Transactional
    public User update(int id, UserDetailsDto dto) {
        Optional<User> existing = userRepository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("User that you are trying to update does not exists");
        } else {
            existing.get().setIdCardNumber(dto.getIdCardNumber());
            existing.get().setFullname(dto.getFullname());
            if (dto.getPassword()!=null && !dto.getPassword().isEmpty()) {
                existing.get()
                        .setPassword(PasswordEncoderProvider.encodePassword(dto.getPassword()));
            }
            return userRepository.saveAndFlush(existing.get());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByIdCardNumber(username)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with id card number: " + username));
        final Set<GrantedAuthority> authorities = new HashSet<>();
        // Add the role to the authorities set If the user is an ADMIN, add ROLE_USER as well This is to en-
        // sure that ADMIN users have both roles This is a common practice in role-based access control systems
        if (user.getRole().toString().equals("ADMIN") || user.getRole().toString().equals("MANAGER")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            authorities.add(new SimpleGrantedAuthority("ROLE_".concat(user.getRole().toString())));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_".concat(user.getRole().toString())));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getIdCardNumber(),
                user.getPassword(),
                authorities
        );
    }
}

