package com.medco.Travel.insurance.service;

import com.medco.Travel.insurance.dto.Request.PrivilegeRequest;
import com.medco.Travel.insurance.dto.Response.MessageResponse;
import com.medco.Travel.insurance.dto.Response.PrivilegeMyResponse;
import com.medco.Travel.insurance.dto.Response.PrivilegeResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PrivilegeService {

    public PrivilegeResponse createPrivilege(PrivilegeRequest privilegeRequest);
    public ResponseEntity<?> deletePrivilege(String privilegeUuid);
    public PrivilegeResponse getPrivilege(String privilegeUuid);
    public PrivilegeMyResponse getPrivileges(int page, int limit, String searchKey);
    ResponseEntity<MessageResponse> updatePrivilege(String privilegeUuid, PrivilegeRequest privilegeRequest);

    List<PrivilegeResponse> searchPrivilege(String searchKey, int page, int limit);

}

