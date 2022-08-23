package com.tiheima.reggie.controller;

import com.tiheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //file形参必须和传入表单的name一样
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后就会删除
        //转存file
        //使用UUID重新生成文件名
        String name = UUID.randomUUID().toString();
        //获取后缀
        String orignalName = file.getOriginalFilename();
        String suffx = orignalName.substring(orignalName.lastIndexOf("."));
        //文件名拼接
        String fileName = name + suffx;
        //创建目录对象
        File dir = new File(basePath);
        if(!dir.exists()){
            dir.mkdirs();
        }

        log.info("文件上传{}：",basePath+file.getOriginalFilename());
        try {
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        //输入流读取文件内容
        try {
            FileInputStream inputStream = new FileInputStream(new File(basePath+name));
            //输出流返回浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = inputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
