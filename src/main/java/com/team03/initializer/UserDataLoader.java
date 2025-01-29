package com.team03.initializer;

import com.team03.entity.enums.RoleType;
import com.team03.entity.user.User;
import com.team03.entity.user.UserRole;
import com.team03.repository.user.UserRoleRepository;
import com.team03.service.user.UserRoleService;
import com.team03.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserDataLoader {

    private final UserRoleService userRoleService;
    private final UserRoleRepository userRoleRepository;
    private final UserService userService;

    public void loadBuiltInUserAndRole(){
        if(userRoleService.getAllUserRole().isEmpty()){

            UserRole admin = new UserRole();
            admin.setRoleType(RoleType.ADMIN);
            admin.setName("Admin");
            userRoleRepository.save(admin);

            UserRole manager = new UserRole();
            manager.setRoleType(RoleType.MANAGER);
            manager.setName("Manager");
            userRoleRepository.save(manager);

            UserRole customer = new UserRole();
            customer.setRoleType(RoleType.CUSTOMER);
            customer.setName("Customer");
            userRoleRepository.save(customer);
        }

        if(userService.countAllAdmins() == 0) {
            User admin = new User();
            admin.setEmail("admin@gmail.com");
            admin.setPassword("Admin123.");
            admin.setEnabled(true);
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setPhoneNumber("111-111-1111");
            admin.setCreateAt(LocalDateTime.now());
            userService.saveAdmin(admin);
        }

        if (userService.countAllCustomers()==0){
            User Customer2=User.builder()
                    .email("kubrakesicidev@gmail.com")
                    .firstName("Kübra")
                    .lastName("Kesici")
                    .password("Kubra123.")
                    .createAt(LocalDateTime.now())
                    .phoneNumber("111-111-1112")
                    .enabled(true)
                    .build();
            userService.saveCustomer(Customer2);

            User Customer3=User.builder()
                    .email("zeyneppbakirtas@gmail.com")
                    .firstName("Zeynep")
                    .lastName("Bakirtas")
                    .password("Zeynep123.")
                    .createAt(LocalDateTime.now())
                    .phoneNumber("111-111-1113")
                    .enabled(true)
                    .build();
            userService.saveCustomer(Customer3);


            User Customer4=User.builder()
                    .email("enskerti@gmail.com")
                    .firstName("Enis")
                    .lastName("Kerti")
                    .password("Enis123.")
                    .createAt(LocalDateTime.now())
                    .phoneNumber("111-111-1114")
                    .enabled(true)
                    .build();
            userService.saveCustomer(Customer4);

            User Customer5=User.builder()
                    .email("benna.sozen@gmail.com")
                    .firstName("Benna")
                    .lastName("Sözen")
                    .password("Benna123.")
                    .createAt(LocalDateTime.now())
                    .phoneNumber("111-111-1115")
                    .enabled(true)
                    .build();
            userService.saveCustomer(Customer5);

            User Customer6=User.builder()
                    .email("yazilimcimiyim@gmail.com")
                    .firstName("Mustafa")
                    .lastName("Yildiz")
                    .password("Mustafa123.")
                    .createAt(LocalDateTime.now())
                    .phoneNumber("111-111-1116")
                    .enabled(true)
                    .build();
            userService.saveCustomer(Customer6);

            User Customer7=User.builder()
                    .email("ertugrulali3538@gmail.com")
                    .firstName("Ali Ihsan")
                    .lastName("Ertugrul")
                    .password("Ihsan123.")
                    .createAt(LocalDateTime.now())
                    .phoneNumber("111-111-1117")
                    .enabled(true)
                    .build();
            userService.saveCustomer(Customer7);

        }
    }



}
