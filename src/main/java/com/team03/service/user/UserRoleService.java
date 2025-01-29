package com.team03.service.user;

import com.team03.entity.enums.RoleType;
import com.team03.entity.user.UserRole;
import com.team03.exception.ResourceNotFoundException;
import com.team03.i18n.MessageUtil;
import com.team03.repository.user.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final UserRoleRepository roleRepository;


    public UserRole getUserRole(RoleType role_name) throws ResourceNotFoundException {

             return roleRepository.findByEnumRoleEquals(role_name).orElseThrow(()->
                     new ResourceNotFoundException(MessageUtil.getMessage ("not.found.user.role",role_name)));

    }

    // runner icin eklendi
    public List<UserRole> getAllUserRole(){
        return roleRepository.findAll();
    }
}
