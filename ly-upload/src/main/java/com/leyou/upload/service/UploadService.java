/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: UploadService
 * Author:   Administrator
 * Date:     2021/5/9 10:47
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.leyou.upload.service;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Administrator
 * @create 2021/5/9
 * @since 1.0.0
 */
@Service
@Slf4j
public class UploadService {
	private static final List<String> ALLOW_TYPES = Arrays.asList("image/jpeg","image/png","image/bmp");

    public String uploadImage(MultipartFile file) {

		try {
			// 校验文件类型
			String contentType = file.getContentType();
			if( !ALLOW_TYPES.contains(contentType)){
				throw new LyException( ExceptionEnums.INVALID_FILE_TYPE);
			}
			//校验文件内容
		BufferedImage image = null;

			image = ImageIO.read(file.getInputStream());

		if( image == null ) { // 如果不是图片，则ImageIO 读到是null。
				throw new LyException( ExceptionEnums.INVALID_FILE_TYPE);
			}
			
			
			long startTime = System.currentTimeMillis();
			// 准备目标路径
			  // this.getClass().getClassLoader().getResource()
			String fileName = new Date().getTime()+file.getOriginalFilename();
			File dest = new File("G:/H/dev/ideawork2/AgainJava2/leyou/upload/", fileName );
			// 保存文件到本地
			file.transferTo(dest);
			long endTime = System.currentTimeMillis();
			
			log.info("采用file.Transto的运行时间：" + String.valueOf(endTime - startTime) + "ms");
			// 返回路径
			return "http://image.leyou.com/" + fileName ;
		} catch (IOException e) {
			// 使用 Slf4j 记录日志
			log.error("上传文件失败",e);
			throw new LyException(ExceptionEnums.UPLOAD_FILE_ERROR);
		}


	}

}












