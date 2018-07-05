/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrb.apiunit.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.Test;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

/**
 *
 * @author MRB
 */
public class TestASM {
    
    @Test
    public void testNewClass()throws IOException {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,
                "asm/demo/AddOper", null, "java/lang/Object",
                new String[]{"asm/demo/Oper"});
        cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "SYMBOL", "Ljava/lang/String;",
                null, "+").visitEnd();
        cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, "add",
                "(Lcom/efushui/certificate/authority/model/dto/ContractFillDto;)Lretrofit2/Call;", null, null).visitEnd();
        cw.visitEnd();
        FileOutputStream fos = new FileOutputStream(new File("E:/tmp/asm/AddOper.class"));
        fos.write(cw.toByteArray());
        fos.close();
    }
}
