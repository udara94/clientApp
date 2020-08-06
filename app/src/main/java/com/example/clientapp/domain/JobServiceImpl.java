package com.example.clientapp.domain;

import com.example.clientapp.model.entities.request.JobTypeArrayRequest;
import com.example.clientapp.model.entities.response.BaseServerResponse;
import com.example.clientapp.model.entities.response.IsFavouriteResponse;
import com.example.clientapp.model.entities.response.JobItemResponse;
import com.example.clientapp.model.entities.response.JobRoleResponse;
import com.example.clientapp.model.entities.response.SubscrbeJobTypesResponse;
import com.example.clientapp.model.rest.JobAPIService;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class JobServiceImpl implements JobService {

    private JobAPIService jobAPIService;

    public JobServiceImpl(JobAPIService jobAPIService){
        super();
        this.jobAPIService = jobAPIService;
    }


}
