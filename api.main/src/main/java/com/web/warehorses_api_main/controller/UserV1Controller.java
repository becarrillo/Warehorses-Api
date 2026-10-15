package com.web.warehorses_api_main.controller;

import com.web.warehorses_secure_persistence.domain.user.dto.UserResponseDto;
import com.web.warehorses_secure_persistence.domain.user.service.IUserService;
import com.web.warehorses_secure_persistence.domain.user.util.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import java.util.Objects;

@RestController
@RequestMapping("/v1/users")
public class UserV1Controller {
    private final IUserService iUserService;
    private final UserMapper userMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserV1Controller.class);

    public UserV1Controller(IUserService iUserServiceImpl, UserMapper userMapper) {
        this.iUserService = iUserServiceImpl;
        this.userMapper = userMapper;
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("userId") int id,
                                                       HttpServletRequest request) {
        try {
            logger.info("jwt Cookie val: {}",
                    Objects.requireNonNull(
                            WebUtils.getCookie(request, "jwt"))
                            .getValue()
            );
            return ResponseEntity.ok(userMapper.toDto(iUserService.findById(id)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(path = "/user", params = {"id-card-number"})
    public ResponseEntity<UserResponseDto> getUserByIdCardNumber(
            @RequestParam("id-card-number") String idCardNumber) {

            try {
                return ResponseEntity.ok(userMapper.toDto(iUserService.findByIdCardNumber(idCardNumber)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }
}
