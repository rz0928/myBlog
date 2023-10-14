package com.example.blogframework.model;

import com.example.blogframework.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleVo {
    List<Long> roleIds;
    List<Role> roles;
}
