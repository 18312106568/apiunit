/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrb.apiunit.context;

import com.mrb.apiunit.client.base.BaseClient;
import com.mrb.apiunit.generator.ServiceGenerator;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author MRB
 */
public class ApiUnitContext {
    
    static Map<String,Class> serviceMap = new HashMap();
    
    /**
     * 获取service类
     * @param clazz
     * @return
     * @throws IOException 
     */
    public static Class getServiceClass(Class<? extends BaseClient> clazz ) throws IOException, ClassNotFoundException{
        if(serviceMap.get(clazz.getSimpleName())==null){
            synchronized(serviceMap){
                serviceMap.put(clazz.getSimpleName(), ServiceGenerator.loadService(clazz));
            }
        }
        return serviceMap.get(clazz.getSimpleName());
    }
}
