/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrb.apiunit;

/**
 *
 * @author MRB
 */
public class ConverUtils {
    
    public static String conveTypeToDescriptor(Class clazz) throws ClassNotFoundException{
        if(clazz == null){
            throw new ClassNotFoundException("====》类型不能为空");
        }
        String typeName = clazz.getTypeName();
        return new StringBuilder("L").append(typeName.replaceAll("\\.", "/")).append(";").toString();
    }
}
