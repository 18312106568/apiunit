package com.mrb.apiunit.client;


import com.mrb.apiunit.client.base.BaseClient;
import com.mrb.apiunit.vo.response.ResultVo;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * CA请求服务
 *
 * @author MRB
 */

public interface CertAuthClient extends BaseClient{

    /**
     * 获取企业CA信息
     *
     * @param corId
     * @return
     */
    @GET("/ca/corporation/get")
    ResultVo getCAInfo(@Query("corId") String corId);
}
