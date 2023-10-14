package com.example.blogframework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleStatusDto {

    private Long roleId;
    //角色状态（0正常 1停用）
    private String status;
}
