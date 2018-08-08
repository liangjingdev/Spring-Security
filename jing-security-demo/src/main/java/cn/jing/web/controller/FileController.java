package cn.jing.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.jing.dto.FileInfo;

/**
 * function:文件上传与文件下载
 * 
 * @author liangjing
 *
 */
@RestController
@RequestMapping("/file")
public class FileController {

	// 文件夹路径
	private String folder = "/Users/liangjing/Spring-Security/jing-security-demo/src/main/java/cn/jing/web/controller";

	@PostMapping
	public FileInfo upload(@RequestParam("file") MultipartFile file) throws Exception {

		System.out.println("上传文件的表单name值为：" + file.getName());
		System.out.println("上传文件的原始文件名" + file.getOriginalFilename());
		System.out.println("上传文件的大小" + file.getSize());

		File localFile = new File(folder, new Date().getTime() + ".txt");
		// 将上传的文件信息写入到本地文件中
		file.transferTo(localFile);

		return new FileInfo(localFile.getAbsolutePath());
	}

	@GetMapping("/{id}")
	public void download(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
		try (
				// 这是JDK7的特性，关于流的操作，可以写在try后面的圆括号里，这样就无需手动关闭流
				InputStream inputStream = new FileInputStream(new File(folder, id + ".txt"));
				OutputStream outputStream = response.getOutputStream();) {

			// 设置下载的文件类型
			response.setContentType("application/x-download");
			// 设置下载后的文件名
			response.setHeader("Content-Disposition", "attachment;filename=test.txt");
			// 将文件的输入流copy到输出流中，实际上就是把文件的内容写到响应里面
			IOUtils.copy(inputStream, outputStream);
			// 刷新输出流
			outputStream.flush();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
