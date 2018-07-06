/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrb.apiunit.test;

import com.mrb.apiunit.client.CertAuthClient;
import com.mrb.apiunit.factory.CAClientFactory;
import org.junit.Test;

/**
 *
 * @author MRB
 */
public class TestFactory {
    
    @Test
    public void testClient(){
        CAClientFactory clientFactory = new CAClientFactory("http://localhost:8094");
        CertAuthClient caClient = clientFactory.getClient(CertAuthClient.class);
        System.out.println(caClient);
    }
}
