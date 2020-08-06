package com.example.clientapp.model.rest;

import com.example.clientapp.model.entities.request.JobTypeArrayRequest;
import com.example.clientapp.model.entities.request.LoginRequest;
import com.example.clientapp.model.entities.request.RefreshTokenRequest;
import com.example.clientapp.model.entities.request.RegisterFcmTokenRequest;
import com.example.clientapp.model.entities.request.RegistrationRequest;
import com.example.clientapp.model.entities.request.UpdateReadStatusRequest;
import com.example.clientapp.model.entities.response.BaseServerResponse;
import com.example.clientapp.model.entities.response.IsFavouriteResponse;
import com.example.clientapp.model.entities.response.JobItemResponse;
import com.example.clientapp.model.entities.response.JobRoleResponse;
import com.example.clientapp.model.entities.response.LoginResponse;
import com.example.clientapp.model.entities.response.MyNotificationResponse;
import com.example.clientapp.model.entities.response.NotificationCountResponse;
import com.example.clientapp.model.entities.response.RefreshTokenResponse;
import com.example.clientapp.model.entities.response.RegisterFcmResponse;
import com.example.clientapp.model.entities.response.RegisterResponse;
import com.example.clientapp.model.entities.response.SubscrbeJobTypesResponse;
import com.example.clientapp.model.entities.response.UpdateReadStatusResponse;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JobAPI {

    @POST("user/register")
    Single<RegisterResponse> doRegisterAPI(
            @Body RegistrationRequest registrationRequest);

    @POST("user/login")
    Single<LoginResponse> doLoginAPI(
            @Body LoginRequest loginRequest);

    @POST("user/token")
    Single<RefreshTokenResponse> getRefreshTokenAPI(
            @Body RefreshTokenRequest refreshTokenRequest);

    @POST("fcm/registerToken")
    Single<RegisterFcmResponse> doRegisterFcmAPI(
            @Header("auth-token") String accessToken,
            @Body RegisterFcmTokenRequest registerFcmTokenRequest);

    @POST("fcm/registerFCMWithoutToken")
    Single<RegisterFcmResponse> doRegisterFcmWithoutTokenAPI(
            @Body RegisterFcmTokenRequest registerFcmTokenRequest);



}
