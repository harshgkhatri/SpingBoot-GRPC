package com.java.controller;

import com.java.UserDetail;
import com.java.User;
import com.java.UserDetailFilter;
import com.java.service.UserDetailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserDetailController {

    @Autowired
    UserDetailService userDetailService ;

    @GetMapping("/{id}")
    public UserDetail getUsers(@PathVariable String id){
      return userDetailService.findUserDetailById(Long.valueOf(id));
    }



    @DeleteMapping("delete/{Id}")
    public void delete(@PathVariable long Id) {
        userDetailService.deleteUserDetails(Id);
    }



    @PutMapping("update/{Id}")
    public void update(@RequestBody User user,@PathVariable long Id) {
        userDetailService.updateUserDetails(Id);
    }

    @GetMapping("/search")
    public UserDetail getSearch(@RequestBody UserDetail user){
        UserDetailFilter fil = UserDetailFilter.newBuilder().build();
        return userDetailService.findUserDetailByFilter
    }







}
