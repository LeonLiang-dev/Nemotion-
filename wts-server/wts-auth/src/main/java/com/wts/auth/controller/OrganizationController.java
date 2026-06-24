package com.wts.auth.controller;

import com.wts.auth.entity.SysOrganization;
import com.wts.auth.service.OrganizationService;
import com.wts.common.result.R;
import com.wts.common.security.LoginUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping("/tree")
    public R<List<OrganizationService.OrgTreeNode>> getTree() {
        return R.ok(organizationService.getOrgTree());
    }

    @PostMapping
    public R<SysOrganization> create(@RequestBody SysOrganization org,
                                     @AuthenticationPrincipal LoginUserDetails loginUser) {
        return R.ok(organizationService.createOrganization(org, loginUser.getUserId()));
    }

    @PutMapping("/{id}")
    public R<SysOrganization> update(@PathVariable String id,
                                     @RequestBody SysOrganization org,
                                     @AuthenticationPrincipal LoginUserDetails loginUser) {
        return R.ok(organizationService.updateOrganization(id, org, loginUser.getUserId()));
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable String id,
                          @AuthenticationPrincipal LoginUserDetails loginUser) {
        organizationService.deleteOrganization(id, loginUser.getUserId());
        return R.ok();
    }
}
