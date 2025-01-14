package com.mertncu.UniversityClubManagementFramework.service.user;

import com.mertncu.UniversityClubManagementFramework.dto.AuthReqResDTO;
import com.mertncu.UniversityClubManagementFramework.entity.Club;
import com.mertncu.UniversityClubManagementFramework.entity.ClubRole;
import com.mertncu.UniversityClubManagementFramework.entity.User;
import com.mertncu.UniversityClubManagementFramework.repository.ClubRepository;
import com.mertncu.UniversityClubManagementFramework.repository.ClubRoleRepository;
import com.mertncu.UniversityClubManagementFramework.repository.UserRepository;
import com.mertncu.UniversityClubManagementFramework.service.auth.JWTUtils;
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
    private ClubRepository clubRepository;

    @Autowired
    private ClubRoleRepository clubRoleRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional
    public AuthReqResDTO register(AuthReqResDTO registrationRequest) {
        AuthReqResDTO response = new AuthReqResDTO();

        try {
            // Check if user already exists
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

            // Handle primary club and role
            if (registrationRequest.getPrimaryClubId() != null) {
                Club primaryClub = clubRepository.findById(registrationRequest.getPrimaryClubId())
                        .orElseThrow(() -> new RuntimeException("Club not found"));
                user.setPrimaryClub(primaryClub);
            }

            if (registrationRequest.getPrimaryRoleId() != null) {
                ClubRole primaryRole = clubRoleRepository.findById(registrationRequest.getPrimaryRoleId())
                        .orElseThrow(() -> new RuntimeException("Role not found"));
                user.setPrimaryRole(primaryRole);
            }

            // Save the user
            userRepository.save(user);

            // Generate JWT tokens
            String accessToken = jwtUtils.generateToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

            response.setStatusCode(200);
            response.setMessage("User registered successfully");
            response.setAccessToken(accessToken);
            response.setRefreshToken(refreshToken);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }

        return response;
    }


    public AuthReqResDTO getAllUsers() {
        AuthReqResDTO reqRes = new AuthReqResDTO();

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

    public AuthReqResDTO login(AuthReqResDTO loginRequest) {
        AuthReqResDTO response = new AuthReqResDTO();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()));

            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();

            var jwtToken = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

            response.setStatusCode(200);
            response.setToken(jwtToken);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Login successful");

            String role = user.getRole();
            response.setRole(role);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }

        return response;
    }


    public AuthReqResDTO refreshToken(AuthReqResDTO refreshTokenRequest){
        AuthReqResDTO response = new AuthReqResDTO();
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

    public AuthReqResDTO getMyInfo(String email){
        AuthReqResDTO reqRes = new AuthReqResDTO();
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
    public AuthReqResDTO updateUser(Integer userId, User updatedUser) {
        AuthReqResDTO requestResponse = new AuthReqResDTO();
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

    public AuthReqResDTO deleteUser(Integer userId) {
        AuthReqResDTO requestResponse = new AuthReqResDTO();
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

    public AuthReqResDTO getUserById(Integer userId) {
        AuthReqResDTO requestRespone = new AuthReqResDTO();
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

    public AuthReqResDTO createUser(User user) {
        AuthReqResDTO requestRespone = new AuthReqResDTO();
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

