package com.optimustechproject.project2.Interface;

import com.optimustechproject.project2.Models.CategoriesPOJO;
import com.optimustechproject.project2.Models.EnquiriesPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by satyam on 6/8/17.
 */

public interface CategoriesRequest {
    @GET("skills_req/FetchCategories.php")
    Call<CategoriesPOJO> requestResponse();
}
