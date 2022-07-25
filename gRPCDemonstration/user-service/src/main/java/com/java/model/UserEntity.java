package com.java.model;



public class UserEntity {

    private Long id;

    private String first_name;

    private String last_name;

    private String phone;

    private String email;

    public UserEntity() {
    }

    public UserEntity(Long id, String first_name, String last_name, String phone, String email) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public com.java.User toProto(){

        return com.java.User.newBuilder().setId(getId())
                .setFirstName(getFirst_name())
                .setLastName(getLast_name())
                .setPhone(getPhone())
                .setEmail(getEmail()).build();
    }

    public static UserEntity fromProto(com.java.User userRequest){
        UserEntity  userEntity = new UserEntity();
        userEntity.setId(userRequest.getId());
        userEntity.setFirst_name(userRequest.getFirstName());
        userEntity.setLast_name(userRequest.getLastName());
        userEntity.setPhone(userRequest.getPhone());
        userEntity.setEmail(userRequest.getEmail());
        return userEntity;
    }


}
