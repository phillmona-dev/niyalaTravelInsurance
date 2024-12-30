package com.medco.Travel.insurance.serviceImpl;

import com.medco.Travel.insurance.entity.User;
import com.medco.Travel.insurance.exception.NotFoundException;
import com.medco.Travel.insurance.repository.UserRepository;
import com.medco.Travel.insurance.shared.audit.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    public String approveUser(String userUuid, boolean approved) {

        User user = userRepository.findByUserUuid(userUuid);

//        if (!user.getRoleName().equalsIgnoreCase("PHARMACIST")) {
//            throw new BadRequestException("User is not a pharmacist");
//        }

        if (user == null){
            throw new NotFoundException("User not found");
        }

        if (approved) {
            user.setUserStatus(UserStatus.ACTIVE);
        } else {
            user.setUserStatus(UserStatus.DENIED);
        }

        userRepository.save(user);

        return approved ? "User approved successfully" : "User denied successfully";
    }
}
