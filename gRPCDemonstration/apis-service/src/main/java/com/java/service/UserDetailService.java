package com.java.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import com.java.UserDetail;
import com.java.UserDetailFilter;
import com.java.UserDetailList;
import com.java.UserDetailServiceGrpc;
import com.java.model.UserdetailEntity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class UserDetailService {
    private ManagedChannel channel;
    private UserDetailServiceGrpc.UserDetailServiceBlockingStub userServiceBlockingStub;
    private UserDetailServiceGrpc.UserDetailServiceStub userServiceAsyncStub;

    public UserDetailService() {
        initializeStub();
    }



    private static void log(String message) {
        System.out.println(message);
    }





    public UserDetail updateUserDetails(Long id) {
        UserDetail userDetail = UserDetail.newBuilder().setId(id)
                .setDetailName("Administration")
                .setLocation("Foster City")
                .build();
        userServiceBlockingStub.updateUserDetail(userDetail);
        return userDetail;
    }


    private void initializeStub() {
        channel = ManagedChannelBuilder.forAddress("localhost", 8080).usePlaintext().build();
        userServiceBlockingStub = UserDetailServiceGrpc.newBlockingStub(channel);
        userServiceAsyncStub = UserDetailServiceGrpc.newStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public UserDetail findUserDetailById(Long id) {
        return userServiceBlockingStub.findUserById(Int64Value.of(id));
    }




    public List<UserDetail> findUserDetailByFilter(UserDetailFilter filter) {

        UserDetailList departmentList = userServiceBlockingStub.findUserByFilter(filter);
        return departmentList.getResultListList();
    }

    public void fetchAllUserDetailsUsingStream(final CountDownLatch finishLatch) throws InterruptedException {
        userServiceAsyncStub.findAllUser(Empty.getDefaultInstance(), new StreamObserver<UserDetail>() {

            @Override
            public void onNext(UserDetail userDetail) {
                log("fetchAllUserdetails:: userdetail~ " + userDetail);
                finishLatch.countDown();
            }

            public void onError(Throwable throwable) {
                finishLatch.countDown();
            }

            public void onCompleted() {
                finishLatch.countDown();
            }
        });

    }

    public void deleteUserDetails(Long id) {
        userServiceBlockingStub.deleteUserDetail(Int64Value.of(id));
    }

    private List<UserdetailEntity> readHRDataFromFileStore() {
        List<UserdetailEntity> usersList = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream jsonInput = this.getClass().getResourceAsStream("/test-data.json");
            usersList = mapper.readValue(jsonInput,
                    new TypeReference<List<UserdetailEntity>>() {
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
            usersList = Collections.emptyList();
        }

        return usersList;
    }

    public void updateDepartmentsUsingStream(final CountDownLatch finishLatch) throws InterruptedException {
        List<UserdetailEntity> userDList = readHRDataFromFileStore();

        StreamObserver<UserDetail> requestObserver = userServiceAsyncStub.updateUserInBatch(new StreamObserver<UserDetail>() {
            public void onNext(UserDetail userDetails) {
                log("updateUserInBatch::UserDetails  ~ " + userDList);
            }

            public void onError(Throwable th) {
                th.printStackTrace();
            }

            public void onCompleted() {
                log("Completed!");
                finishLatch.countDown();
            }
        });

        try {
            for (UserdetailEntity usersDetails : userDList) {
                requestObserver.onNext(usersDetails.toProto());
            }
        } catch (Exception ex) {
            requestObserver.onError(ex);
        }
        requestObserver.onCompleted();

    }




}
