package com.example.blogframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blogframework.dto.AddRoleDto;
import com.example.blogframework.dto.UpdateRoleDto;
import com.example.blogframework.dto.UpdateRoleStatusDto;
import com.example.blogframework.entity.Role;
import com.example.blogframework.entity.RoleMenu;
import com.example.blogframework.mapper.RoleMapper;
import com.example.blogframework.model.PageVo;
import com.example.blogframework.model.RoleVo;
import com.example.blogframework.service.RoleMenuService;
import com.example.blogframework.service.RoleService;
import com.example.constants.SystemConstants;
import com.example.utils.BeanCopyUtils;
import com.example.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-09-17 18:19:21
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    RoleMenuService roleMenuService;
    @Autowired
    TransactionTemplate transactionTemplate;
    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        if(id == 1L){
            List<String> roles = new ArrayList<>();
            roles.add("admin");
            return roles;
        }
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult listAllRole(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getDelFlag, SystemConstants.STATUS_NORMAL);
        queryWrapper.eq(StringUtils.hasText(roleName),Role::getRoleName,roleName);
        queryWrapper.eq(StringUtils.hasText(status),Role::getStatus,status);
        Page<Role> page = new Page<>(pageNum,pageSize );
        page(page,queryWrapper);
        List<RoleVo> roleVoList = BeanCopyUtils.copyBeanList(page.getRecords(), RoleVo.class);
        return ResponseResult.okResult(new PageVo(roleVoList,page.getTotal()));
    }

    @Override
    public ResponseResult updateRoleStatus(UpdateRoleStatusDto updateRoleStatusDto) {
        Role role = BeanCopyUtils.copyBean(updateRoleStatusDto, Role.class);
        role.setId(updateRoleStatusDto.getRoleId());
        updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult addRole(AddRoleDto addRoleDto) {
        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);
        save(role);
        List<RoleMenu> roleMenus = addRoleDto.getMenuIds().stream()
                .map(menuId -> new RoleMenu(role.getId(), menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRoleById(Long id) {
        LambdaUpdateWrapper<Role> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Role::getDelFlag,SystemConstants.STATUS_DRAFT);
        updateWrapper.eq(Role::getId,id);
        update(updateWrapper);
        return ResponseResult.okResult();
    }
    @Override
    public ResponseResult getRoleById(Long id) {
        Role role = getById(id);
        RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    @Override
    public ResponseResult updateRoleById(UpdateRoleDto updateRoleDto) {
        Role role = BeanCopyUtils.copyBean(updateRoleDto, Role.class);
        Long roleId = updateRoleDto.getId();
        HashSet<Long> menuIds = new HashSet<>(updateRoleDto.getMenuIds());
        LambdaQueryWrapper<RoleMenu> roleMenuQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuQueryWrapper.eq(RoleMenu::getRoleId,role.getId());
        roleMenuQueryWrapper.select(RoleMenu::getMenuId);
        List<RoleMenu> originalRoleMenuList = roleMenuService.list(roleMenuQueryWrapper);
        List<RoleMenu> needDeleteRoleMenu = new ArrayList<>();
        for (RoleMenu rolemenu: originalRoleMenuList) {
            Long originalMenuId = rolemenu.getMenuId();
            if(!menuIds.remove(originalMenuId)){
                needDeleteRoleMenu.add(new RoleMenu(originalMenuId,roleId));
            }
        }
        List<RoleMenu> addRoleMenu = menuIds.stream()
                .map(menuId -> new RoleMenu(menuId, roleId))
                .collect(Collectors.toList());
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                try {
                    if(!needDeleteRoleMenu.isEmpty()) {
                        roleMenuQueryWrapper.in(RoleMenu::getMenuId,needDeleteRoleMenu);
                        roleMenuService.remove(roleMenuQueryWrapper);
                    }
                    roleMenuService.saveBatch(addRoleMenu);
                    updateById(role);
                } catch (Exception e){
                    //回滚
                    transactionStatus.setRollbackOnly();
                }
            }
        });
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllUsefulRole() {
        LambdaQueryWrapper<Role> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus,SystemConstants.STATUS_NORMAL);
        List<Role> RoleList = list(queryWrapper);
        return ResponseResult.okResult(RoleList);
    }
}
