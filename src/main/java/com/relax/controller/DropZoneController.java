package com.relax.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class DropZoneController {


    @ResponseBody
    @RequestMapping(value = "upload" ,method = RequestMethod.POST)
    public Map<String,Object> upload(MultipartFile dropFile, HttpServletRequest request){

        System.out.println(dropFile);
        System.out.println("inner upload");

        Map<String, Object> map = new HashMap<String, Object>();

        //设置文件=============上传路径==============
        String filePath = request.getSession().getServletContext().getRealPath("/static/upload");

        //获取上传的原始文件名
        String originalFilename = dropFile.getOriginalFilename();

        //获取文件后缀
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());

        System.out.println(fileSuffix);
        //判断并创建上传用的文件夹
        File destFile = new File(filePath);
        if (!destFile.exists()){
            destFile.mkdirs();
        }

        //重新设置文件名为UUID,以确保文件名唯一
        destFile = new File(filePath, UUID.randomUUID() + fileSuffix);

        System.out.println(destFile.getAbsolutePath());

        if (!destFile.exists()){
            try {
                destFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //写入文件
        try {
            dropFile.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //返回JSON数据这里只带了文件名
        map.put("originalFilename",destFile.getName());

        return map;


    }





}
