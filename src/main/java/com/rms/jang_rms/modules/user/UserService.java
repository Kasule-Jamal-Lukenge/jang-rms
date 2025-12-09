package com.rms.jang_rms.modules.user;

import com.rms.jang_rms.dtos.CreateUserRequest;
import com.rms.jang_rms.dtos.RoleRequest;
import com.rms.jang_rms.dtos.UserResponse;
import com.rms.jang_rms.dtos.UserUpdateRequest;
import com.rms.jang_rms.modules.role.Role;
import com.rms.jang_rms.modules.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
    }

    // Converting Entity → DTO
    private UserResponse mapToResponse(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setContact(user.getContact());
        dto.setOccupation(user.getOccupation());
        dto.setCurrentResidence(user.getCurrentResidence());
        dto.setGender(user.getGender());
        dto.setRoleName(user.getRole().getName());
        return dto;
    }

    public UserResponse createUser(@RequestBody CreateUserRequest req) {
        if(userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("A User With This Email Already Exists.");
        }

        Role role = roleRepository.findById(req.getRoleId()).orElseThrow(() -> new RuntimeException("Role Not Found"));

        User user = User.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .contact(req.getContact())
                .occupation(req.getOccupation())
                .currentResidence(req.getCurrentResidence())
                .gender(req.getGender())
                .role(role)
                .build();

        userRepository.save(user);
        return mapToResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public UserResponse getUser(long id){
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        return mapToResponse(user);
    }

    public UserResponse updateUser(Long id, UserUpdateRequest req) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setContact(req.getContact());
        user.setOccupation(req.getOccupation());
        user.setCurrentResidence(req.getCurrentResidence());

        userRepository.save(user);
        return mapToResponse(user);
    }

    public UserResponse updateUserRole(Long userId, Long roleId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        Role role = roleRepository.findById(roleId).orElseThrow(()-> new RuntimeException("Role Not Found"));

        user.setRole(role);
        userRepository.save(user);

        return mapToResponse(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
