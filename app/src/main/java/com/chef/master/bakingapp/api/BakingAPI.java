package com.chef.master.bakingapp.api;


import com.chef.master.bakingapp.api.model.BakingResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Petya Marinova on 5/20/2018.
 */
public interface BakingAPI {
    String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/";

    @GET("2017/May/59121517_baking/baking.json")
    Observable<List<BakingResult>> getBakingObjects();
}
