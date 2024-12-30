package com.medco.Travel.insurance.controller;

import com.medco.Travel.insurance.dto.Request.PrivilegeRequest;
import com.medco.Travel.insurance.dto.Response.MessageResponse;
import com.medco.Travel.insurance.dto.Response.PrivilegeMyResponse;
import com.medco.Travel.insurance.dto.Response.PrivilegeResponse;
import com.medco.Travel.insurance.service.PrivilegeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/travel/privilege")
public class PrivilegeController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PrivilegeService privilegeService;

    @PostMapping("/create")
    public PrivilegeResponse createPrivilege(@Valid @RequestBody PrivilegeRequest privilegeRequest) {
        return privilegeService.createPrivilege(privilegeRequest);
    }

    @GetMapping("/{privilegeUuid}")
    public PrivilegeResponse getPrivilege(@PathVariable String privilegeUuid){
        return privilegeService.getPrivilege(privilegeUuid);
    }

    @GetMapping("/list")
    public PrivilegeMyResponse getPrivileges(@RequestParam(name = "search", required = false) String searchKey,
                                             @RequestParam(value = "page", defaultValue = "1") int page,
                                             @RequestParam(value="limit", defaultValue = "500") int limit){
        return privilegeService.getPrivileges(page, limit, searchKey);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PrivilegeResponse>> searchPrivileges(
            @RequestParam String searchKey,
            @RequestParam int page,
            @RequestParam int limit
    ){
        List<PrivilegeResponse> privileges = privilegeService.searchPrivilege(searchKey, page, limit);
        return ResponseEntity.ok(privileges);
    }

    @PutMapping("/{privilegeUuid}")
    public ResponseEntity<MessageResponse> updatePrivilege(@PathVariable String privilegeUuid,
                                                           @Valid @RequestBody PrivilegeRequest privilegeRequest){
        return privilegeService.updatePrivilege(privilegeUuid, privilegeRequest);
    }

    @DeleteMapping("/{privilegeUuid}")
    public ResponseEntity<?> deletePrivilege(@PathVariable String privilegeUuid){
        return privilegeService.deletePrivilege(privilegeUuid);
    }

}
