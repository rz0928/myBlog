package com.example.admin;

import com.example.blogframework.model.RoutersVo;
import com.example.blogframework.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MenuTest {
    @Autowired
    MenuService menuService;
    @Test
    public void routerTest(){
        RoutersVo routersVo =new RoutersVo(menuService.selectRouterMenuTreeByUserId(6L));
        System.out.println(routersVo);
    }
}
