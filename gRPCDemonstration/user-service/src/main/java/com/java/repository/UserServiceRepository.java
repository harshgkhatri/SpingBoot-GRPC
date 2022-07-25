package com.java.repository;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.UserDetailFilter;
import com.java.model.UserdetailEntity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserServiceRepository   {
    Map<Long, UserdetailEntity> userDetailEntityMap = null;

    public UserServiceRepository() {
        userDetailEntityMap = readDataFromFileStore();
    }

    public UserdetailEntity createUserDetail(UserdetailEntity userdetail) {
        userDetailEntityMap.put(userdetail.getId(), userdetail);
        return userdetail;
    }

    public UserdetailEntity updateUserDetail(UserdetailEntity userdetail) {
        userDetailEntityMap.put(userdetail.getId(), userdetail);
        return userdetail;
    }

    public List<UserdetailEntity> findAllUserDetails(){
        List<UserdetailEntity> userDetailEntityList = userDetailEntityMap.values().stream().collect(Collectors.toList());
        return userDetailEntityList;

    }

    public List<UserdetailEntity> findUserDetailsByFilter(UserDetailFilter userDetailFilter) {
        List<UserdetailEntity> filteredList = userDetailEntityMap.values().stream()
                .filter(Objects::nonNull)
                .filter(x -> ((userDetailFilter.getId() == 0 || x.getId().equals(userDetailFilter.getId()))
                        && (userDetailFilter.getLocation().isEmpty()  || x.getLocation().equals(userDetailFilter.getLocation()))
                        && (userDetailFilter.getDetailName().isEmpty() || x.getDetailName().equals(userDetailFilter.getDetailName())))
                )
                .collect(Collectors.toList());
        return filteredList;
    }

    public void deleteUserDetail(Long Id) {
        userDetailEntityMap.remove(Id);
    }

    public UserdetailEntity findUserDetailsById(Long id) {
        if (id < 0) {
            throw new IllegalArgumentException("User details Id cannot be negative!");
        }
        return userDetailEntityMap.get(id);
    }



    private Map<Long, UserdetailEntity> readDataFromFileStore() {
        List<UserdetailEntity> userdetailsList = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream jsonInput = UserServiceRepository.class.getResourceAsStream("/user-data.json");
            userdetailsList = mapper.readValue(jsonInput,
                    new TypeReference<List<UserdetailEntity>>() {
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
            userdetailsList = new ArrayList<UserdetailEntity>();
        }
        Map<Long, UserdetailEntity> userdetsMap = userdetailsList.stream().
                collect(Collectors.toMap(UserdetailEntity::getId, ud -> ud));
        return userdetsMap;
    }

}
