package com.mertncu.UniversityClubManagementFramework.service.user;

import com.mertncu.UniversityClubManagementFramework.dto.AuthReqResDTO;
import com.mertncu.UniversityClubManagementFramework.entity.Club;
import com.mertncu.UniversityClubManagementFramework.entity.User;
import com.mertncu.UniversityClubManagementFramework.entity.UserClubMembership;
import com.mertncu.UniversityClubManagementFramework.repository.UserClubMembershipRepository;
import com.mertncu.UniversityClubManagementFramework.repository.UserRepository;
import com.mertncu.UniversityClubManagementFramework.service.auth.JWTUtils;
import com.mertncu.UniversityClubManagementFramework.service.club.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserManagementService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private ClubService clubService;

    @Autowired
    private UserClubMembershipRepository userClubMembershipRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public AuthReqResDTO register(AuthReqResDTO registrationRequest) {
        AuthReqResDTO response = new AuthReqResDTO();

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

    @Transactional
    public AuthReqResDTO login(AuthReqResDTO loginRequest) {
        AuthReqResDTO response = new AuthReqResDTO();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
            String token = jwtUtils.generateToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

            List<UserClubMembership> memberships = userClubMembershipRepository.findByUserId(user.getId());
            List<Club> clubs = memberships.stream()
                    .map(membership -> clubService.getClubById(membership.getClubId()))
                    .collect(Collectors.toList());

            response.setStatusCode(HttpStatus.OK.value());
            response.setToken(token);
            response.setRefreshToken(refreshToken);
            response.setMessage("Login successful.");
            response.setId(user.getId());
            response.setName(user.getName());
            response.setEmail(user.getEmail());
            response.setRole(user.getRole());
            response.setClubs(clubs); // Assuming you add a setClubs() method to AuthReqResDTO

        } catch (Exception e) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            response.setError("Authentication failed");
            response.setMessage("Invalid credentials or user not found.");
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
    public AuthReqResDTO updateUser(String userId, User updatedUser) {
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

    public AuthReqResDTO deleteUser(String userId) {
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

    public AuthReqResDTO getUserById(String userId) {
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

