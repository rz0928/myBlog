package com.example.blogframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blogframework.dto.AddUserDto;
import com.example.blogframework.dto.ChangeUserStatus;
import com.example.blogframework.dto.UpdateUserDto;
import com.example.blogframework.entity.Role;
import com.example.blogframework.entity.RoleMenu;
import com.example.blogframework.entity.User;
import com.example.blogframework.entity.UserRole;
import com.example.blogframework.model.ListUserVo;
import com.example.blogframework.model.PageVo;
import com.example.blogframework.model.UserRoleVo;
import com.example.blogframework.service.RoleService;
import com.example.blogframework.service.UserRoleService;
import com.example.constants.SystemConstants;
import com.example.enums.AppHttpCodeEnum;
import com.example.exception.SystemException;
import com.example.blogframework.mapper.UserMapper;
import com.example.blogframework.model.UserInfoVo;
import com.example.blogframework.service.UserService;
import com.example.utils.BeanCopyUtils;
import com.example.utils.ResponseResult;
import com.example.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(SysUser)表服务实现类
 *
 * @author makejava
 * @since 2023-08-29 19:21:38
 */
@Service("UserService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User>implements UserService {
    @Resource
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Override
        public ResponseResult userInfo() {
            Long userId= SecurityUtils.getUserId();
            User user=getById(userId);
            UserInfoVo userInfo= BeanCopyUtils.copyBean(user,UserInfoVo.class);
        return ResponseResult.okResult(userInfo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        LambdaUpdateWrapper<User> wrapper=new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId,user.getId());
        wrapper.set(User::getAvatar,user.getAvatar());
        wrapper.set(User::getEmail,user.getEmail());
        wrapper.set(User::getSex,user.getSex());
        wrapper.set(User::getNickName,user.getNickName());
        update(wrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //判断提交的内容是否为空
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.NEED_USERNAME);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.NEED_EMAIL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NEED_NICKNAME);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.NEED_PASSWORD);
        }
        //判断用户名和昵称是否已存在
        if(UserNameExist(user.getUserName())){
           throw new SystemException(AppHttpCodeEnum.USERNAME_EXIT);
        }
        if(NickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIT);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllUser(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(userName),User::getUserName,userName);
        queryWrapper.like(StringUtils.hasText(phonenumber),User::getUserName,userName);
        queryWrapper.eq(StringUtils.hasText(status),User::getStatus,status);
        Page<User> page = new Page<>(pageNum,pageSize);
        page(page, queryWrapper);
        List<ListUserVo> listUserVos = BeanCopyUtils.copyBeanList(page.getRecords(), ListUserVo.class);
        return  ResponseResult.okResult(new PageVo(listUserVos,page.getTotal()));
    }

    @Override
    @Transactional
    public ResponseResult addUser(AddUserDto addUserDto) {
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        save(user);
        List<UserRole> userRoleList = addUserDto.getRoleIds().stream()
                .map(roleId -> new UserRole(user.getId(),roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoleList);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteUserById(Long id) {
        if(id.equals(SecurityUtils.getUserId())) {
            throw new RuntimeException("不能删除当前角色");
        }
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(User::getDelFlag, SystemConstants.STATUS_DRAFT);
        updateWrapper.eq(User::getId,id);
        update(updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserRoleById(Long id) {
        UserRoleVo userRoleVo = new UserRoleVo();
        LambdaQueryWrapper<UserRole> userRoleQueryWrapper = new LambdaQueryWrapper<>();
        userRoleQueryWrapper.eq(UserRole::getUserId,id);
        userRoleQueryWrapper.select(UserRole::getRoleId);
        List<UserRole> userRoles = userRoleService.list(userRoleQueryWrapper);
        List<Long> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
        userRoleVo.setRoleIds(roleIds);
        List<Role> roles = new ArrayList<>();
        if(!roleIds.isEmpty()) {
            LambdaQueryWrapper<Role> roleQueryWrapper = new LambdaQueryWrapper<>();
            roleQueryWrapper.in(Role::getId, roleIds);
            roles = roleService.list(roleQueryWrapper);
        }
        userRoleVo.setRoles(roles);
        return ResponseResult.okResult(userRoleVo);
    }

    @Override
    public ResponseResult updateUser(UpdateUserDto updateUserDto) {
        User user = BeanCopyUtils.copyBean(updateUserDto, User.class);
        List<Long> roleIds = updateUserDto.getRoleIds();
        LambdaQueryWrapper<UserRole> userRoleQueryWrapper = new LambdaQueryWrapper<>();
        userRoleQueryWrapper.eq(UserRole::getUserId,updateUserDto.getRoleIds());
        userRoleQueryWrapper.select(UserRole::getRoleId);
        List<Long> userRoleIds = userRoleService.list(userRoleQueryWrapper).stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
        List<Long> needDeleteIds = new ArrayList<>();
        for (Long userRoleId:userRoleIds) {
            if(roleIds.contains(userRoleId)){
                roleIds.remove(userRoleId);
                needDeleteIds.add(userRoleId);
            }else{
                roleIds.add(userRoleId);
            }
        }
        List<UserRole> addUserRole = roleIds.stream()
                .map(roleId -> new UserRole(roleId, user.getId()))
                .collect(Collectors.toList());
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                try {
                    if(!needDeleteIds.isEmpty()) {
                        userRoleQueryWrapper.in(UserRole::getRoleId,needDeleteIds);
                        userRoleService.remove(userRoleQueryWrapper);
                    }
                    userRoleService.saveBatch(addUserRole);
                    updateById(user);
                } catch (Exception e){
                    //回滚
                    transactionStatus.setRollbackOnly();
                }
            }
        });
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeStatus(ChangeUserStatus changeUserStatus) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(User::getStatus,changeUserStatus.getStatus());
        updateWrapper.eq(User::getId,changeUserStatus.getUserId());
        update(updateWrapper);
        return ResponseResult.okResult();
    }

    private boolean UserNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        return count(queryWrapper) > 0;
    }
    private boolean NickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickName);
        return count(queryWrapper) > 0;
    }

}

