package com.mrb.apiunit.factory;
import com.mrb.apiunit.client.base.BaseClient;
import com.mrb.apiunit.excutor.ClientExcutor;


/**
 * client 工厂
 * @author MRB
 */
public class CAClientFactory {
    
    String url;
    
    public CAClientFactory(String url){
        this.url = url;
    }
    
    /**
     * 获取client
     * @param <T>
     * @param clazz
     * @return 
     */
    public  <T>T getClient(Class<? extends BaseClient> clazz){
        try {
            return (T) new ClientExcutor(url).getProxy(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
