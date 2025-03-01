package com.incidentmanagement.service;

import com.incidentmanagement.dto.UserDTO;
import com.incidentmanagement.dto.UserRegistrationDTO;
import com.incidentmanagement.model.User;
import com.incidentmanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setFirstName(registrationDTO.getFirstName());
        user.setLastName(registrationDTO.getLastName());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setAddress(registrationDTO.getAddress());
        user.setPincode(registrationDTO.getPincode());
        user.setCity(registrationDTO.getCity());
        user.setState(registrationDTO.getState());
        user.setCountry(registrationDTO.getCountry());
        user.setMobileNumber(registrationDTO.getMobileNumber());
        user.setFax(registrationDTO.getFax());
        user.setPhone(registrationDTO.getPhone());
        user.setUserType(registrationDTO.getUserType());

        return userRepository.save(user);
    }

    public UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAddress(user.getAddress());
        userDTO.setPincode(user.getPincode());
        userDTO.setCity(user.getCity());
        userDTO.setState(user.getState());
        userDTO.setCountry(user.getCountry());
        userDTO.setMobileNumber(user.getMobileNumber());
        userDTO.setFax(user.getFax());
        userDTO.setPhone(user.getPhone());
        userDTO.setUserType(user.getUserType());
        return userDTO;
    }
}