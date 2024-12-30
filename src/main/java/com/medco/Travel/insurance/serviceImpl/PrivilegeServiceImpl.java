package com.medco.Travel.insurance.serviceImpl;

import com.medco.Travel.insurance.dto.Request.PrivilegeRequest;
import com.medco.Travel.insurance.dto.Response.MessageResponse;
import com.medco.Travel.insurance.dto.Response.PrivilegeMyResponse;
import com.medco.Travel.insurance.dto.Response.PrivilegeResponse;
import com.medco.Travel.insurance.entity.Privilege;
import com.medco.Travel.insurance.repository.PrivilegeRepository;
import com.medco.Travel.insurance.service.PrivilegeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    @Autowired
    PrivilegeRepository privilegeRepository;


    @Override
    public PrivilegeResponse createPrivilege(PrivilegeRequest privilegeRequest) {
        if (privilegeRepository.existsByPrivilegeName(privilegeRequest.getPrivilegeName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Privilege Name is already registered.");

        if (privilegeRepository.existsByPrivilegeDescription(privilegeRequest.getPrivilegeDescription()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Privilege description is already registered.");

        Privilege priv = new Privilege();
        BeanUtils.copyProperties(privilegeRequest, priv);
        PrivilegeResponse privResponse = new PrivilegeResponse();
        BeanUtils.copyProperties(priv, privResponse);
        privilegeRepository.save(priv);

        return privResponse;

    }

    @Override
    public ResponseEntity<MessageResponse> updatePrivilege(String privilegeUuid, PrivilegeRequest privilegeRequest) {
        Privilege privilege = privilegeRepository.findByPrivilegeUuid(privilegeUuid);
        List<Privilege> privilegeName = privilegeRepository.findAllByPrivilegeName(privilegeRequest.getPrivilegeName());
        List<Privilege> privDesc = privilegeRepository.findAllByPrivilegeDescription(privilegeRequest.getPrivilegeDescription());

        if (privilege == null)
            throw new RuntimeException("Privilege not found.");
        if (privilegeName.size() >= 2 || privDesc.size() >= 2)
            throw new RuntimeException("Privilege name or description is exist. Update privilege to have unique name and description");

        privilege.setPrivilegeName(privilegeRequest.getPrivilegeName());
        privilege.setPrivilegeCategory(privilegeRequest.getPrivilegeCategory());
        privilege.setPrivilegeDescription(privilegeRequest.getPrivilegeDescription());

        privilegeRepository.save(privilege);

        return ResponseEntity.ok(new MessageResponse("Privilege Updated successfully"));
    }

    @Override
    public List<PrivilegeResponse> searchPrivilege(String searchTerm, int page, int limit) {
        if (page>0) page = page-1;
        Pageable pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());
        Page<Privilege> PrivilegesPage = privilegeRepository.findAllByPrivilegeNameContainingOrPrivilegeDescriptionContainingOrPrivilegeCategoryContaining(searchTerm, searchTerm, searchTerm, pageRequest);

        long totalPages = PrivilegesPage.getTotalPages();
        List<Privilege> privilegeList = PrivilegesPage.getContent();

        List<PrivilegeResponse> privilegeResponse = new ArrayList<>();
        for (Privilege p : privilegeList){
            PrivilegeResponse pr = new PrivilegeResponse();
            if (privilegeResponse.size() == 0)
                pr.setTotalPages(totalPages);
            BeanUtils.copyProperties(p,pr);
            privilegeResponse.add(pr);
        }
        return privilegeResponse;
    }


    @Override
    public ResponseEntity<?> deletePrivilege(String privilegeUuid) {
        Privilege privilege  = privilegeRepository.findByPrivilegeUuid(privilegeUuid);
        if (privilege == null)
            throw new RuntimeException("Privilege not found.");
        privilegeRepository.delete(privilege);

        return ResponseEntity.ok(new MessageResponse("Privilege deleted successfully"));
    }

    @Override
    public PrivilegeResponse getPrivilege(String privilegeUuid) {
        Privilege priv = privilegeRepository.findByPrivilegeUuid(privilegeUuid);
        if (priv == null)
            throw new RuntimeException("privilegeUuid not found");
        PrivilegeResponse privResponse = new PrivilegeResponse();
        privResponse.setPrivilegeUuid(priv.getPrivilegeUuid());
        privResponse.setPrivilegeName(priv.getPrivilegeName());
        privResponse.setPrivilegeDescription(priv.getPrivilegeDescription());
        privResponse.setPrivilegeCategory(priv.getPrivilegeCategory());
        return privResponse;
    }

    @Override
    public PrivilegeMyResponse getPrivileges(int page, int limit, String searchKey) {
        if (page>0) page = page-1;
        Pageable pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());
        Page<Privilege> privilegePage = privilegeRepository.findAll(pageRequest);
        long totalPages = privilegePage.getTotalPages();
        List<Privilege> privilegeList = privilegePage.getContent();

        PrivilegeMyResponse response = new PrivilegeMyResponse();
        response.setTotalPages(totalPages);
        List<PrivilegeResponse> privilegeResponseList = new ArrayList<>();
        for (Privilege priv : privilegeList){
            PrivilegeResponse privilegeResponse = new PrivilegeResponse();
            BeanUtils.copyProperties(priv, privilegeResponse);
            privilegeResponseList.add(privilegeResponse);
        }
        response.setResponse(privilegeResponseList);
        return response;
    }

}

