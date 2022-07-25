package com.java.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.java.UserDetail;
import com.java.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserdetailEntity {
    private Long id;
    private String detailName;
    private String location;
    @JsonProperty("users")
    private List<UserEntity> userEntities;

    public UserdetailEntity() {
    }

    public UserdetailEntity(Long id, String detailName, String location, List<UserEntity> users) {
        this.id = id;
        this.detailName = detailName;
        this.location = location;
        this.userEntities = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<UserEntity> getUserEntities() {
        return userEntities;
    }

    public void setEmployeeEntities(List<UserEntity> userEntities) {
        this.userEntities = userEntities;
    }

    public UserDetail toProto(){
        List<User> users = userEntities.stream().map(UserEntity::toProto).collect(Collectors.toList());
        return  UserDetail.newBuilder().setId(getId())
                .setDetailName(getDetailName())
                .setLocation(getLocation())
                .addAllUser(users)
                .build();
    }

    public static UserdetailEntity fromProto(UserDetail userRequest){
        UserdetailEntity userdetailEntity = new UserdetailEntity();
        userdetailEntity.setDetailName(userRequest.getDetailName());
        userdetailEntity.setId(userRequest.getId());
        userdetailEntity.setLocation(userRequest.getLocation());
        List<UserEntity> userEntities1 = userRequest.getUserList().stream().map(UserEntity::fromProto).collect(Collectors.toList());
        userdetailEntity.setEmployeeEntities( userEntities1);
        return userdetailEntity;
    }
}
