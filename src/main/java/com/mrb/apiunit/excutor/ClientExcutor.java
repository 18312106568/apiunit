package com.mrb.apiunit.excutor;



import com.mrb.apiunit.client.base.BaseClient;
import com.mrb.apiunit.context.ApiUnitContext;
import com.mrb.apiunit.factory.CAServiceFactory;
import com.mrb.apiunit.vo.response.ResultVo;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;

import retrofit2.Call;

/**
 *
 * @author MRB
 */
public class ClientExcutor implements InvocationHandler {

    private Object service;
    
    private Class serviceClass;

    private String url;

    public ClientExcutor(String url) {
        this.url = url;
    }

    /**
     * 获取代理对象
     *
     * @param <T>
     * @param clazz
     * @return
     */
    public <T> T getProxy(Class<? extends BaseClient> clazz) throws IOException, ClassNotFoundException {
        this.serviceClass  = ApiUnitContext.getServiceClass(clazz);
        this.service = CAServiceFactory.getInstance(url).getService(serviceClass);
        Object newProxyInstance = Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                this);
        return (T) newProxyInstance;
    }

    /**
     * 执行service接口
     * @param baseResultCall
     * @return 
     */
    private ResultVo execute(Call<ResultVo> baseResultCall) {
        assert baseResultCall != null;
        try {
            return baseResultCall.execute().body();
        } catch (IOException e) {
            return ResultVo.error(e.getMessage());
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            try {
                return method.invoke(this, args);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                throw ex;
            }
            //如果传进来的是一个接口（核心)
        } else {
            Method serviceMethod = serviceClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
            return execute((Call<ResultVo>) run(serviceMethod, args));
        }
    }

    /**
     * 实现接口的核心方法
     *
     * @param method
     * @param args
     * @return
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     */
    public Object run( Method method, Object[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        return method.invoke(this.service, args); 
    }

}
