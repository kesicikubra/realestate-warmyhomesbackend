package com.team03.controller.user;

import com.team03.payload.request.user.*;

import com.team03.payload.response.business.ResponseMessage;
import com.team03.payload.response.user.*;
import com.team03.service.helper.RestPage;
import com.team03.service.user.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class AuthUserController {

    private final AuthenticationService authService;


    @PatchMapping("/auth")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','CUSTOMER')")
    @Operation(tags = "Users", summary = "F07",description = "ADMIN MANAGER CUSTOMER")
    public ResponseMessage<String>updatePassword(
            @RequestBody @Valid UpdatePasswordRequest updatePasswordRequest,
            HttpServletRequest request
    ){
        return authService.updatePassword(updatePasswordRequest,request);
    }

    @PutMapping("/auth")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','CUSTOMER')")
    @Operation(tags = "Users", summary = "F06",description = "ADMIN MANAGER CUSTOMER")
    public ResponseMessage<UpdatedUserResponse>updateUser(@RequestBody @Valid UserRequestForUpdate userRequest,
                                                          HttpServletRequest request){

        return authService.updateUser(userRequest,request);
    }

    @GetMapping("/auth")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','CUSTOMER')")
    @Operation(tags = "Users", summary = "F01",description = "ADMIN MANAGER CUSTOMER")
    public ResponseMessage<UserResponse> getUserInfo(HttpServletRequest request) {

        return authService.getUserInfo(request);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @Operation(tags = "Users", summary = "F09",description = "ADMIN MANAGER ")
    public ResponseMessage<Page<UserResponse>>getAllUsersByPage(
            @RequestParam(value = "q",required = false) String q,
            @RequestParam(value = "page",defaultValue = "0") int page ,
            @RequestParam(value = "size",defaultValue = "20") int size ,
            @RequestParam(value = "sort",defaultValue = "createAt") String sort ,
            @RequestParam(value = "type",defaultValue = "desc") String type

    ){
        return authService.getAllUsersByPage(q,page,size,sort,type);
    }

    @GetMapping("/{id}/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @Operation(tags = "Users", summary = "F10",description = "ADMIN MANAGER")
    public ResponseMessage< RestPage<UserResponseAllUsersInfo>>getUserById(@PathVariable Long id
//                                                                @RequestParam(defaultValue = "0") int page ,
//                                                                @RequestParam(defaultValue = "20") int size ,
//                                                                @RequestParam(defaultValue = "createAt") String sort ,
//                                                                @RequestParam(defaultValue = "desc") String type

    ){
        int page = 0;
        int size = 3;
        String sort = "createAt";
        String type = "desc";

        return authService.getUserById(id,page,size,sort,type);
    }


    @PutMapping("/{id}/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @Operation(tags = "Users", summary = "F11",description = "ADMIN MANAGER")
    public ResponseMessage<UserResponseForUpdateAdmin>updatesAdminUser(@RequestBody @Valid UserRequestForUpdateRole userRequest,
                                                                       @PathVariable(name = "id") Long userId,
                                                                       HttpServletRequest request)  {
        return authService.updatesAdminUser(userRequest,userId,request);
    }


    @DeleteMapping("/auth")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(tags = "Users", summary = "F08",description = "CUSTOMER")
    public ResponseMessage<UserResponse>deleteAccount(@RequestBody @Valid PasswordRequest password){
        return authService.deleteAccount(password);
    }

    @DeleteMapping("/{userId}/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @Operation(tags = "Users", summary = "F12",description = "ADMIN MANAGER")
    public ResponseMessage<UserResponse> deleteById(@PathVariable Long userId,
                                                    HttpServletRequest request) {
        return authService.deleteById(userId,request);
    }


    @PostMapping("/createPhoto")
    @PreAuthorize("hasAnyAuthority('ADMIN','CUSTOMER','MANAGER')")
    @Operation(tags = "ProfilePhoto",description = "ADMIN MANAGER CUSTOMER")
    public ResponseMessage< UserPhotoResponse >addUserPhoto(
            @RequestParam(name = "photo") MultipartFile photoRequest,
            HttpServletRequest request){
        return authService.addPhoto (photoRequest,request);

    }

    @PutMapping("/updatePhoto")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','CUSTOMER')")
    @Operation(tags = "ProfilePhoto",description = "ADMIN MANAGER CUSTOMER")
    public ResponseMessage< UserPhotoResponse> updatePhoto ( @RequestParam(name = "photo") MultipartFile photoRequest,
                                                             HttpServletRequest request){
        return authService.updatePhoto (photoRequest, request);

    }

    @DeleteMapping("/deletePhoto")
    @PreAuthorize("hasAnyAuthority('ADMIN','CUSTOMER','MANAGER')")
    @Operation(tags = "ProfilePhoto",description = "ADMIN MANAGER CUSTOMER")
    public ResponseMessage<UserPhotoResponse>deleteUserPhoto( HttpServletRequest request){
        return authService.deletePhoto (request);
    }

    @GetMapping("/getPhoto/{photoId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','CUSTOMER','MANAGER')")
    @Operation(tags = "ProfilePhoto",description = "ADMIN MANAGER CUSTOMER")
    public ResponseMessage<UserPhotoResponse>getPhotoById( @PathVariable(name = "photoId") Long photoId){
        return authService.getPhotoById(photoId);
    }
}
