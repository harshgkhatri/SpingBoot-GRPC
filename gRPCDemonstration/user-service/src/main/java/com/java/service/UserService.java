package com.java.service;

import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import com.java.*;
import com.java.model.UserdetailEntity;
import com.java.repository.UserServiceRepository;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class UserService extends UserDetailServiceGrpc.UserDetailServiceImplBase{

    @Autowired
    UserServiceRepository userServiceRepository;


    @Override
    public void createUserDetails( UserDetail request, StreamObserver<UserDetail> responseObserver) {
        UserdetailEntity entry = UserdetailEntity.fromProto(request);
        entry = userServiceRepository.createUserDetail(entry);
        responseObserver.onNext(entry.toProto());
        responseObserver.onCompleted();
    }

    public void updateUserDetails(UserDetail request, StreamObserver<UserDetail> responseObserver) {
        UserdetailEntity entry = UserdetailEntity.fromProto(request);
        entry = userServiceRepository.updateUserDetail(entry);
        responseObserver.onNext(entry.toProto());
        responseObserver.onCompleted();
    }

    @Override
    public void createUserDetails(UserDetailFilter request, StreamObserver<UserDetail> responseObserver) {

    }

    /**
     * @param filter
     * @param responseObserver
     */
    @Override
    public void findUserDetailsByFilter(UserDetailFilter filter, StreamObserver<UserDetailList> responseObserver) {
        List<UserdetailEntity> userEntities = userServiceRepository.findUserDetailsByFilter(filter);
        List<UserDetail> userdets = userEntities.stream().map(UserdetailEntity::toProto).collect(Collectors.toList());
        UserDetailList userDetailList= UserDetailList.newBuilder().addAllResultList(userdets)
                .setResultCount(Int64Value.newBuilder().setValue(userEntities.size()).build()).build();
        responseObserver.onNext(userDetailList);
        responseObserver.onCompleted();
    }

    /**
     * @param request
     * @param responseObserver
     */
    @Override
    public void deleteUserDetail(Int64Value request, StreamObserver<Empty> responseObserver) {
        userServiceRepository.deleteUserDetail(request.getValue());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void findUserDetailsById(Int64Value id, StreamObserver<UserDetail> responseObserver) {
        UserdetailEntity userdetailEntity= userServiceRepository.findUserDetailsById(id.getValue());
        if (userdetailEntity == null) {
            Metadata metadata = new Metadata();
            responseObserver.onError(
                    Status.UNAVAILABLE.withDescription("userdetails  not found !")
                            .asRuntimeException(metadata));
        } else {
            responseObserver.onNext(userdetailEntity.toProto());
            responseObserver.onCompleted();
        }

    }
    @Override
    public void findAllUserDetails(Empty request, StreamObserver<UserDetail> responseObserver) {
        List<UserdetailEntity> userDetailEntities = userServiceRepository.findAllUserDetails();
        for (UserdetailEntity usersDetail : userDetailEntities) {
            responseObserver.onNext(usersDetail.toProto());
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<UserDetail> updateUserDetailsInBatch(StreamObserver<UserDetail> responseObserver) {
        return new StreamObserver<UserDetail>() {
            @Override
            public void onNext(UserDetail request) {

                UserdetailEntity savedUsersDetails = userServiceRepository.updateUserDetail( UserdetailEntity.fromProto(request));
                responseObserver.onNext(savedUsersDetails.toProto());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

}
