package com.jc.packagepndemo.api;

import com.jc.packagepndemo.model.ContributorBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by solar on 2016/8/3.
 */
public interface GithubService {
    //https://api.github.com/repos/{owner}/{repo}/contributors
    @GET("/repos/{owner}/{repo}/contributors")
    Call<List<ContributorBean>> getContributors(
            @Path("owner") String owner,
            @Path("repo") String repo);
}
