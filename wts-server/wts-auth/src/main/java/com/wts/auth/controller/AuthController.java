package com.wts.auth.controller;

import com.wts.auth.dto.LoginDTO;
import com.wts.auth.dto.LoginVO;
import com.wts.auth.dto.MenuVO;
import com.wts.auth.entity.SysUser;
import com.wts.auth.service.AuthService;
import com.wts.common.result.R;
import com.wts.common.security.LoginUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public R<LoginVO> login(@RequestBody LoginDTO dto) {
        LoginVO vo = authService.login(dto);
        return R.ok(vo);
    }

    @PostMapping("/refresh")
    public R<LoginVO> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        LoginVO vo = authService.refreshToken(refreshToken);
        return R.ok(vo);
    }

    @PostMapping("/logout")
    public R<Void> logout() {
        // JWT 无状态，前端清除 Token 即可
        return R.ok();
    }

    @GetMapping("/me")
    public R<SysUser> getCurrentUser(@AuthenticationPrincipal LoginUserDetails loginUser) {
        SysUser user = authService.getCurrentUser(loginUser.getUserId());
        return R.ok(user);
    }

    @GetMapping("/menus")
    public R<List<MenuVO>> getMenus(@AuthenticationPrincipal LoginUserDetails loginUser) {
        List<MenuVO> menus = authService.getUserMenus(loginUser.getUserId());
        return R.ok(menus);
    }
}
