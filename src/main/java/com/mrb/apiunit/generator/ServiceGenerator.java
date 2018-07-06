
package com.mrb.apiunit.generator;

import com.mrb.apiunit.ConverUtils;
import com.mrb.apiunit.client.base.BaseClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 *
 * @author MRB
 */
public class ServiceGenerator{
    
    final static ServiceLoader loader = new ServiceLoader();
    
    /**
     * 根据client获取其service
     * @param clazz
     * @return
     * @throws IOException 
     * @throws java.lang.ClassNotFoundException 
     */
    public static Class loadService( Class<? extends BaseClient> clazz) throws IOException, ClassNotFoundException{
         //项目路径字节码路径
        String bytePath = clazz.getResource("").getPath();
        //client包名
        String packageName = clazz.getPackage().getName();
        
        String className = new  StringBuilder(packageName)
                .append('.')
                .append(clazz.getSimpleName())
                .append("Impl").toString();
        
        String fileName =  new StringBuilder(bytePath)
                .append(clazz.getSimpleName())
                .append("Impl")
                .append(".class").toString();
        
        File classFile = new File(fileName);
        //获取文件流
        InputStream  clzIn = new FileInputStream(generatorService(clazz,classFile));
        
        byte[] result = new byte[2048];  
        int count = clzIn.read(result);  
        
        return loader.defineServiceClass(className, result, 0, count);
    }
    
    /**
     * 生成实现类字节码文件
     * @param clazz
     * @param classFile
     * @return
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    private static File generatorService(Class<? extends BaseClient> clazz,File classFile) throws IOException, ClassNotFoundException{
        if(!classFile.exists()){
            classFile.createNewFile();
        }
        String packagePath = clazz.getPackage().getName().replaceAll("\\.", "/");
        OutputStream out = new FileOutputStream(classFile);
        //client方法
        Method[] methods = clazz.getDeclaredMethods();
        
        ClassWriter classWriter = new ClassWriter(0);
        classWriter.visit(Opcodes.V1_8,// java版本  
                Opcodes.ACC_PUBLIC+Opcodes.ACC_ABSTRACT+Opcodes.ACC_INTERFACE,// 类修饰符  
                packagePath+"/"+clazz.getSimpleName()+"Impl", // 类的全限定名  
                null, "java/lang/Object", null); 
        
        for(Method method : methods){
            /**
             * 方法descriptor
             */
            StringBuilder descriptorSb = new StringBuilder();
            descriptorSb.append("(");
            Parameter[] parameters = method.getParameters();
            for(Parameter parameter : parameters){
                descriptorSb.append(ConverUtils.conveTypeToDescriptor(parameter.getType()));
            }
            descriptorSb.append(")");
            descriptorSb.append("Lretrofit2/Call");
            descriptorSb.append("<");
            descriptorSb.append(ConverUtils.conveTypeToDescriptor(method.getReturnType()));
            descriptorSb.append(">;");
            //声明方法
            MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC+Opcodes.ACC_ABSTRACT, 
                   method.getName(), descriptorSb.toString(),  
                null, null);
            //添加方法注解
           for(Annotation ann : method.getDeclaredAnnotations()){
               if(ann instanceof GET){
                    AnnotationVisitor  methodAnnVisitor = 
                        methodVisitor.visitAnnotation(ConverUtils.conveTypeToDescriptor(GET.class) , true);
                    methodAnnVisitor.visit("value", ((GET)ann).value());
                    methodAnnVisitor.visitEnd();
                    break;
               }else if(ann instanceof POST){
                    AnnotationVisitor  methodAnnVisitor = 
                        methodVisitor.visitAnnotation(ConverUtils.conveTypeToDescriptor(POST.class) , true);
                    methodAnnVisitor.visit("value", ((POST)ann).value());
                    methodAnnVisitor.visitEnd();
                    break;
               }
           }
           /**
            * 添加参数注解
            */
           for(int i=0;i<parameters.length;i++){
               Class annType = null;
               String value = "";
               Boolean flag = true;
               for(Annotation ann : parameters[i].getAnnotations()){
                   if(ann instanceof Query){
                       annType = Query.class;
                       value = ((Query)ann).value();
                       break;
                   }else if(ann instanceof Path){
                       annType = Path.class;
                       value = ((Path)ann).value();
                       break;
                   }else if(ann instanceof Body){
                       annType = Body.class;
                       flag = false;
                       break;
                   }
               }
               if(annType == null){
                   continue;
               }
               AnnotationVisitor paramAnnotationVisitor = methodVisitor.visitParameterAnnotation(i,
                      ConverUtils.conveTypeToDescriptor(annType), true);
               //添加注解value值
               if(flag){
                   paramAnnotationVisitor.visit("value", value);
               }
               paramAnnotationVisitor.visitEnd();
           }
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();   
        byte[] data = classWriter.toByteArray();  
        //填写字节码内容
        out.write(data);
        out.flush();
        out.close();
        return classFile;
    } 
    
    
    
    /**
     * 类加载器
     */
    static class ServiceLoader extends ClassLoader{ 
        public Class<?> defineServiceClass(String name, byte[] b, int off, int len){  
            return super.defineClass(name, b, off, len);
        }  
    }
}
