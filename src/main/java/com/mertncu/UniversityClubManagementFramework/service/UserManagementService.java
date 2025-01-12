package com.mertncu.UniversityClubManagementFramework.service;

import com.mertncu.UniversityClubManagementFramework.dto.RequestResponseDTO;
import com.mertncu.UniversityClubManagementFramework.entity.User;
import com.mertncu.UniversityClubManagementFramework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserManagementService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public RequestResponseDTO register(RequestResponseDTO registrationRequest) {
        RequestResponseDTO response = new RequestResponseDTO();

        try {
            if (userRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
                response.setStatusCode(400);
                response.setError("Email already exists");
                return response;
            }

            User user = new User();
            user.setId(registrationRequest.getId());
            user.setName(registrationRequest.getName());
            user.setEmail(registrationRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

            String role = registrationRequest.getRole();
            if (role == null || role.isEmpty()) {
                user.setRole("ROLE_USER");
            } else {

                if (role.equals("ADMIN")) {
                    user.setRole("ROLE_ADMIN");
                } else {
                    user.setRole("ROLE_USER");
                }
            }

            userRepository.save(user);

            if (user.getId() != null) {
                String accessToken = jwtUtils.generateToken(user);
                String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);


                response.setStatusCode(200);
                response.setMessage("User registered successfully");
                response.setAccessToken(accessToken);
                response.setRefreshToken(refreshToken);
            }

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }

        return response;
    }

    public RequestResponseDTO getAllUsers() {
        RequestResponseDTO reqRes = new RequestResponseDTO();

        try {
            List<User> result = userRepository.findAll();
            if (!result.isEmpty()) {
                reqRes.setUsers(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }

    public RequestResponseDTO login(RequestResponseDTO loginRequest) {
        RequestResponseDTO response = new RequestResponseDTO();

        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken
                            (loginRequest
                                    .getEmail(),
                                    loginRequest.getPassword()));

            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwtToken = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwtToken);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Login successful");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public RequestResponseDTO refreshToken(RequestResponseDTO refreshTokenRequest){
        RequestResponseDTO response = new RequestResponseDTO();
        try{
            String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            User users = userRepository.findByEmail(ourEmail).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)) {
                var jwt = jwtUtils.generateToken(users);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            }
            response.setStatusCode(200);
            return response;

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    public RequestResponseDTO getMyInfo(String email){
        RequestResponseDTO reqRes = new RequestResponseDTO();
        try {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                reqRes.setUser(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }

        }catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return reqRes;

    }

    @Transactional
    public RequestResponseDTO updateUser(Integer userId, User updatedUser) {
        RequestResponseDTO requestResponse = new RequestResponseDTO();
        try {
            User existingUser = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setName(updatedUser.getName());
            existingUser.setRole(updatedUser.getRole());

            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            User savedUser = userRepository.save(existingUser);
            requestResponse.setUser(savedUser);
            requestResponse.setStatusCode(200);
            requestResponse.setMessage("User updated successfully");
        } catch (RuntimeException e) {
            requestResponse.setStatusCode(404);
            requestResponse.setMessage("User not found for update");
        } catch (Exception e) {
            requestResponse.setStatusCode(500);
            requestResponse.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return requestResponse;
    }

    public RequestResponseDTO deleteUser(Integer userId) {
        RequestResponseDTO requestResponse = new RequestResponseDTO();
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                userRepository.delete(userOptional.get());
                requestResponse.setStatusCode(200);
                requestResponse.setMessage("User deleted successfully");
            } else {
                requestResponse.setStatusCode(404);
                requestResponse.setMessage("User not found for delete");
            }
        } catch (Exception e) {
            requestResponse.setStatusCode(500);
            requestResponse.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return requestResponse;
    }

    public RequestResponseDTO getUserById(Integer userId) {
        RequestResponseDTO requestRespone = new RequestResponseDTO();
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                requestRespone.setUser(userOptional.get());
                requestRespone.setStatusCode(200);
                requestRespone.setMessage("successful");
            } else {
                requestRespone.setStatusCode(404);
                requestRespone.setMessage("User not found");
            }
        } catch (Exception e) {
            requestRespone.setStatusCode(500);
            requestRespone.setMessage("Error occurred while getting user: " + e.getMessage());
        }
        return requestRespone;
    }

    public RequestResponseDTO createUser(User user) {
        RequestResponseDTO requestRespone = new RequestResponseDTO();
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            requestRespone.setUser(savedUser);
            requestRespone.setStatusCode(200);
            requestRespone.setMessage("User created successfully");
        } catch (Exception e) {
            requestRespone.setStatusCode(500);
            requestRespone.setMessage("Error occurred while creating user: " + e.getMessage());
        }
        return requestRespone;
    }
}

