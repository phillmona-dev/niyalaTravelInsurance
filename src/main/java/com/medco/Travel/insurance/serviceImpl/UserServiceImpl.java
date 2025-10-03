package com.medco.Travel.insurance.serviceImpl;

import com.medco.Travel.insurance.dto.Request.*;
import com.medco.Travel.insurance.dto.Response.*;
import com.medco.Travel.insurance.entity.*;
import com.medco.Travel.insurance.exception.*;
import com.medco.Travel.insurance.repository.*;
import com.medco.Travel.insurance.security.jwt.JwtUtils;
import com.medco.Travel.insurance.security.service.UserDetailsImpl;
import com.medco.Travel.insurance.service.UserService;
import com.medco.Travel.insurance.shared.audit.enums.UserStatus;
import com.medco.Travel.insurance.shared.audit.enums.UserType;
import com.medco.Travel.insurance.utils.ImageUtils;
import io.jsonwebtoken.io.IOException;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.core.io.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;


    @Autowired
    RoleRequestedRepository roleRequestedRepository;

    @Autowired
    EvidenceDocumentRepository evidenceDocumentRepository;
//
//    @Autowired
//    private SessionRegistry sessionRegistry;

    @Autowired
    JwtUtils jwtUtils;

    @Value("${file.dir-attachments}")
    private String uploadDirectory;

    private final ProfileRepository profileRepository;

    public UserServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Transactional
    @Override
    public ResponseEntity<?> createUser(UserRequest userRequest) {
        // Check if email already exists
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new EmailAlreadyExists("Error: Email is already in use!");
        }

        // Check if telephone already exists
        if (userRepository.existsByTelephone(userRequest.getTelephone())) {
            throw new InvalidPhoneException("Error: Telephone is already in use!");
        }

        // Fetch role based on roleUuid
        Role role = roleRepository.findByRoleUuid(userRequest.getRoleUuid());
        if (role == null) {
            throw new BadRequestException("No Role found for the provided roleUuid");
        }

        // Create new user and copy properties from the request
        User user = new User();
        user.setUserType(UserType.ADMIN); // You may want to adjust this
        user.setUserStatus(UserStatus.ACTIVE);
        BeanUtils.copyProperties(userRequest, user);
        user.setRoleName(role.getRoleName());

        // Encrypt password and save user
        user.setPassword(encoder.encode(userRequest.getPassword()));
        user.setUserUuid(UUID.randomUUID().toString()); // Set userUuid if needed
        userRepository.save(user);

        // Create response and return
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        userResponse.setUserUuid(user.getUserUuid());
        userResponse.setTelePhone(userRequest.getTelephone());
        userResponse.setDateOfBirth(userRequest.getDateOfBirth());

        return ResponseEntity.ok(userResponse);
    }


    @Override
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        try {
            System.out.println("DEBUG: Starting authentication for email: " + loginRequest.getEmail());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            System.out.println("DEBUG: Authentication successful");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            System.out.println("DEBUG: UserDetails retrieved, status: " + userDetails.getUserStatus());

            if (!userDetails.getUserStatus().equals(UserStatus.ACTIVE)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Your account is not active. Please contact support.");
            }

            System.out.println("DEBUG: Generating JWT token");
            String jwt = jwtUtils.generateJwtToken(authentication);
            System.out.println("DEBUG: JWT token generated successfully");

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            System.out.println("DEBUG: Creating JwtResponse");
            JwtResponse response = new JwtResponse(
                    jwt,
                    userDetails.getUserUuid(),
                    userDetails.getEmail(),
                    userDetails.getRoleUuid(),
//					userDetails.getRoleName(),
                    userDetails.getFirstName(),
                    userDetails.getFatherName(),
                    userDetails.getGrandFatherName(),
                    userDetails.getGender(),
                    userDetails.getTitle(),
                    userDetails.getTelePhone(),
                    userDetails.getUserStatus(),
                    userDetails.getUserType(),
//					userDetails.getInsuranceUuid(),
//					userDetails.getAgencyUuid(),
//					userDetails.getProfilePicture(),
                    roles
            );
            System.out.println("DEBUG: JwtResponse created successfully");
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            System.out.println("DEBUG: Authentication failed: " + e.getMessage());
            if (e instanceof BadCredentialsException) {
                throw new InvalidCredentialsException("Invalid UserName Or Password Provided!");
            } else {
                throw new InvalidCredentialsException("No User Account Found With the Provided Credentials!");
            }
        } catch (Exception e) {
            System.out.println("DEBUG: Unexpected error during authentication: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
        }
    }



    @Override
    public ResponseEntity<UserResponse> updateUser(String userUuid, UserUpdateRequest userRequest) {

        User user = userRepository.findByUserUuid(userUuid);

        if (user == null) {
            throw new RuntimeException("User not found.");
        }

        BeanUtils.copyProperties(userRequest, user);

        User updatedUser = userRepository.save(user);

        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(updatedUser, userResponse);
        userResponse.setRoleUuid(updatedUser.getRoleUuid());
        userResponse.setRoleName(updatedUser.getRoleName());
        userResponse.setUserType(updatedUser.getUserType());
        return ResponseEntity.ok(userResponse);
    }

    @Override
    public ResponseEntity<?> deleteUser(String userUuid) {

        User user = userRepository.findByUserUuid(userUuid);
        if (user == null)
            throw new RuntimeException("User not found.");
        user.setDeleted(true);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User soft deleted successfully!"));
    }

    @Override
    public UserResponse getUser(String userUuid) {

        User user = userRepository.findByUserUuid(userUuid);

        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        return userResponse;
    }




    @Override
    public ResponseEntity<?> approveUser(Long userId, UserAccountApprovalRequest request) {

        Optional<RequestedRole> existedUser=roleRequestedRepository.findFirstByUserId(userId);

        if (existedUser.isPresent()) {
            RequestedRole approvedUser = existedUser.get();
            approvedUser.setStatus("Approved");
            approvedUser.setApprovedDate(LocalDateTime.now());
            approvedUser.setReason(request.getReason());
            System.out.println("jsj  sjdlksf "+request.getReason());
            roleRequestedRepository.save(approvedUser);
        } else {
            return ResponseEntity.ok("Requested role not found for user ID: " + userId);

        }

        return ResponseEntity.ok("User Approved Successfully");


    }


    @Override
    public ResponseEntity<?> verifyAccount(String emailVerificationToken) {
        String returnValue = "";
        User user = userRepository.findByEmailVerificationToken(emailVerificationToken);
        if (user == null) {
            throw new RuntimeException("User not found.");
        }

        user.setUserStatus(UserStatus.ACTIVE);
        User updatedUser = userRepository.save(user);
        if (updatedUser.getUserStatus() == UserStatus.ACTIVE) {
            returnValue = "Account Verified Successfully";
        }
        return ResponseEntity.ok(new MessageResponse(returnValue));
    }

    @Override
    public List<UserResponse> searchUsers(String searchKey, int page, int limit) {
        if (page > 0) page = page - 1;
        String[] searchKeys = searchKey.split(" ");

        Pageable pageableRequest = PageRequest.of(page, limit, Sort.by("id").descending());
        int countSpaces = StringUtils.countOccurrencesOf(searchKey, " ");

        Page<User> usersPage = null;

        String upperCaseSearchKey = searchKey.toUpperCase();

        UserStatus status = null;
        UserType userType = null;

        if (EnumUtils.isValidEnum(UserStatus.class, upperCaseSearchKey)) {
            status = UserStatus.valueOf(upperCaseSearchKey);
        }

        if (EnumUtils.isValidEnum(UserType.class, upperCaseSearchKey)) {
            userType = UserType.valueOf(upperCaseSearchKey);
        }

        if (countSpaces == 0) {
            if (status != null) {

                usersPage = userRepository.findByUserStatus(status, pageableRequest);
            } else if (userType != null) {

                usersPage = userRepository.findByUserType(userType, pageableRequest);
            } else {

                usersPage = userRepository.findByFirstNameContainingOrFatherNameContainingOrGrandFatherNameContainingOrTelephoneContainingOrEmailContaining(
                        searchKey, searchKey, searchKey, searchKey, searchKey, pageableRequest);
            }
        } else if (countSpaces == 1) {
            String firstName = searchKeys[0];
            String fatherName = searchKeys[1];
            usersPage = userRepository.findByFirstNameContainingAndFatherNameContaining(firstName, fatherName, pageableRequest);
        } else if (countSpaces == 2) {

            String firstName = searchKeys[0];
            String fatherName = searchKeys[1];
            String grandFatherName = searchKeys[2];
            usersPage = userRepository.findByFirstNameContainingAndFatherNameContainingAndGrandFatherNameContaining(
                    firstName, fatherName, grandFatherName, pageableRequest);
        }

        if (usersPage == null || usersPage.isEmpty()) {
            return new ArrayList<>();
        }

        int totalPages = usersPage.getTotalPages();
        List<User> users = usersPage.getContent();
        List<UserResponse> userResponse = new ArrayList<>();

        for (User u : users) {
            UserResponse ur = new UserResponse();
            if (userResponse.size() == 0) {
                ur.setTotalPages(totalPages);
            }
            BeanUtils.copyProperties(u, ur);
            userResponse.add(ur);
        }

        return userResponse;
    }


    @Override
    public ResponseEntity<?> uploadProfilePicture(UploadProfileRequest requestDetail) throws IOException, java.io.IOException {
        String uploadDirectory = null;
        String uploadDir = uploadDirectory + "/profiles/";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String returnValue = "Image not Saved";
        byte[] bytes = requestDetail.getProfilePicture().getBytes();

        String fileName = requestDetail.getProfilePicture().getOriginalFilename();
        String extention = (fileName.substring(fileName.lastIndexOf(".") + 1)).toLowerCase();
        String newFileName = requestDetail.getUserUuid() + "." + extention;
        Path path = Paths.get(uploadDir + newFileName);
        Files.write(path, bytes);

        User user = userRepository.findByUserUuid(requestDetail.getUserUuid());

        if (user == null)
            throw new RuntimeException("User not found.");

        user.setProfilePicture(newFileName);
        User updatesUserDetails = userRepository.save(user);
        if (updatesUserDetails.getProfilePicture() != null) {
            returnValue = "Profile picture Saved";
        }

        return ResponseEntity.ok(new MessageResponse(returnValue));
    }

    @Override
    public ProfileResponse getProfile(String userUuid) {

        ProfilePicture profile = profileRepository.findByUserUuid(userUuid);
        if (profile == null) {
            throw new NullPointerException("Profile not found for userUuid: " + userUuid);
        }

        ProfileResponse response = new ProfileResponse();

        BeanUtils.copyProperties(profile, response);

        byte[] decompressedData = ImageUtils.decompressImage(profile.getImageData());

        String base64Image = Base64.getEncoder().encodeToString(decompressedData);

        response.setImageData(decompressedData);
        response.setProfilePicture(base64Image);

        response.setUserName(profile.getUsername());

        return response;
    }

    @Override
    public ResponseEntity<?> editProfile(String userUuid, String userName, byte[] profilePicture) throws java.io.IOException {
        ProfilePicture profile= profileRepository.findByUserUuid(userUuid);
        if (profile==null) {
            ProfilePicture newProfile= new ProfilePicture();
            newProfile.setUsername(userName);
            newProfile.setImageData(ImageUtils.compressImage(profilePicture));
            newProfile.setUserUuid(userUuid);
            profileRepository.save(newProfile);

        }else {
            profile.setUsername(userName);
            profile.setImageData(ImageUtils.compressImage(profilePicture));
            profileRepository.save(profile);
        }
        return ResponseEntity.ok("profile changed successfully");
    }

    @Override
    public ResponseEntity<?> deleteProfile(String userUuid) {
        ProfilePicture profile = profileRepository.findByUserUuid(userUuid);

        if (profile == null) {
            throw new RuntimeException("Profile not found for userUuid: " + userUuid);
        }

        profileRepository.delete(profile);

        return ResponseEntity.ok("Profile deleted successfully");
    }

    @Override
    public ResponseEntity<?> createProfile(String userUuid, String userName, byte[] profilePicture) throws IOException, java.io.IOException {
        if (profileRepository.existsByUserUuid(userUuid)) {
            throw new RuntimeException("Profile already exists for userUuid: " + userUuid);
        }

        ProfilePicture newProfile = new ProfilePicture();
        newProfile.setUsername(userName);
        newProfile.setImageData(ImageUtils.compressImage(profilePicture));
        newProfile.setUserUuid(userUuid);

        profileRepository.save(newProfile);

        return ResponseEntity.ok("Profile created successfully");
    }


    @Override
    public ResponseEntity<?> resetPassword(ResetPasswordRequest resetPasswordDetail) {
        String returnValue = "Password not changed";
        User userEntity = userRepository.findByEmailAndPasswordResetCode(resetPasswordDetail.getEmail(),resetPasswordDetail.getPasswordResetCode());
        if(userEntity == null) 	throw new RuntimeException("Password reset code not found.");

        userEntity.setPassword(encoder.encode(resetPasswordDetail.getNewPassword()));
        User passwordUpdated = userRepository.save(userEntity);
        if(passwordUpdated != null) {
            returnValue = "Password changed successfully";
        }
        return ResponseEntity.ok(new MessageResponse(returnValue));
    }

    @Override
    public ResponseEntity<?> changePassword(ResetPasswordRequest resetPasswordDetail, String userUuid) {
        String returnValue = "Password not changed";
        User userEntity = userRepository.findByUserUuid(userUuid);
        if(userEntity == null) 	throw new RuntimeException("User not found.");

        userEntity.setPassword(encoder.encode(resetPasswordDetail.getNewPassword()));
        User passworUpdated = userRepository.save(userEntity);
        if(passworUpdated != null) {
            returnValue = "Password changed successfully";
        }
        return ResponseEntity.ok(new MessageResponse(returnValue));
    }

    @Override
    public ResponseEntity <?> checkResetCode(ResetPasswordRequest resetPasswordDetail) {
        User userEntity = userRepository.findByEmailAndPasswordResetCode(resetPasswordDetail.getEmail(),resetPasswordDetail.getPasswordResetCode());
        if(userEntity == null) throw new RuntimeException("Invalid Reset Code.");
        return ResponseEntity.ok(new MessageResponse("Reset Code is valid"));

    }


    @Override
    public ResponseEntity<Resource> getEvidenceDocument(Long id) {

        EvidenceDocument evidenceDocument = evidenceDocumentRepository.findById(id).orElse(null);

        if (evidenceDocument == null) {
            return ResponseEntity.badRequest().body(null);
        }

        String fileName = evidenceDocument.getFile();
        String filePath = uploadDirectory + File.separator + fileName;

        try {

            File file = new File(filePath);
            if (!file.exists()) {
                return ResponseEntity.badRequest().body(null);
            }

            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(fileBytes));
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + evidenceDocument.getFileName());
            headers.add(HttpHeaders.CONTENT_TYPE, Files.probeContentType(Paths.get(filePath)));
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileBytes.length));

            return ResponseEntity.ok().headers(headers).body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }

    }


    @Override
    public ResponseEntity<?> updateDocument(Long id, MultipartFile file, String fileNames) {

        try {
            EvidenceDocument existingDocument = evidenceDocumentRepository.findById(id).orElse(null);
            if (existingDocument == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Attachment not found."));
            }

            File directory = new File(uploadDirectory);
            if (!directory.exists()) {
                boolean dirCreated = directory.mkdirs();
                if (!dirCreated) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Failed to create upload directory"));
                }
            }

            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("File name is empty."));
            }
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            String newFileName = fileNames + "_" + existingDocument.getId() + "." + extension;

            String fullFilePath = uploadDirectory + File.separator + newFileName;

            try (FileOutputStream fos = new FileOutputStream(new File(fullFilePath))) {
                fos.write(file.getBytes());
            } catch (java.io.IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error writing file: " + e.getMessage()));
            }

            existingDocument.setFile(newFileName);
            existingDocument.setFileSize(file.getSize());
            existingDocument.setFileName(fileNames);
            evidenceDocumentRepository.save(existingDocument);

            return ResponseEntity.ok(new MessageResponse("Document successfully updated."));
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error: " + ex.getMessage()));
        }
    }


    @Override
    public ResponseEntity<?> deleteDocument(Long id) {
        try {
            EvidenceDocument doc = evidenceDocumentRepository.findById(id).orElse(null);

            if (doc == null) {
                return ResponseEntity.ok(new MessageResponse("Claim attachment not found."));
            }

            String oldFileName = doc.getFile();
            String filePath = uploadDirectory + File.separator + oldFileName;
            File fileToDelete = new File(filePath);
            boolean deleted = fileToDelete.delete();

            if (deleted) {

                evidenceDocumentRepository.delete(doc);
                return ResponseEntity.ok(new MessageResponse("Document deleted successfully."));
            }
            else {
                return ResponseEntity.ok(new MessageResponse("Failed to delete claim attachment."));
            }

        } catch (Exception ex) {

            return ResponseEntity.ok(new MessageResponse("Error: " + ex.getMessage()));

        }

    }

    @Override
    public UserMyResponse getUsers(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);

        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponse> userResponses = userPage.getContent().stream()
                .map(user -> {
                    UserResponse userResponse = new UserResponse();
                    BeanUtils.copyProperties(user, userResponse);
                    return userResponse;
                })
                .collect(Collectors.toList());

        UserMyResponse response = new UserMyResponse();
        response.setTotalPages(userPage.getTotalPages());
        response.setTotalElements(userPage.getTotalElements());
        response.setPageNumber(page);
        response.setContent(userResponses);

        return response;
    }


    @Override
    public UserResponse getUserByEmail(String email) {

        User user = userRepository.findByEmail(email);

        if (user==null){
            throw new ResourceNotFoundException("User not found with email: " + email);
        }

        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);

        return userResponse;
    }



}


