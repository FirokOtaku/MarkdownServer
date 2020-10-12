package firok.mds.controller;

import firok.mds.entity.EntityDoc;
import firok.mds.entity.EntityDocInfo;
import firok.mds.entity.Response;
import firok.mds.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static firok.mds.MDSApplication.getSuccessString;

@RestController
@RequestMapping("/api/doc")
public class DocController
{
	@Value("${app.basePath}")
	String basePath;

	@Autowired
	public LogService logService;

	@Autowired
	public HttpServletRequest request;

//	/**
//	 * 读取指定文档信息
//	 * @param path 路径
//	 * @return 信息
//	 */
//	@GetMapping("/read")
//	public Response<?> read(
//			@RequestParam("path") String path,
//			HttpServletResponse response
//	)
//	{
//		try
//		{
//			File file = new File(basePath, path);
//
//			try(FileInputStream ifs = new FileInputStream(file); )
//			{
//				;
//			}
//
//
//			return Response.success();
//		}
//		catch (Exception e)
//		{
//			return Response.fail(e);
//		}
//	}


	/*
	* 下面的代码从这抄的
	* https://blog.csdn.net/han1396735592/article/details/103128588
	* */

	@GetMapping(value = "/file")
	public ResponseEntity<FileSystemResource> getFile(
			@RequestParam(name="paths",required=false) String[] paths,
			@RequestParam(name="file") String fileName
	)
	{
		if(paths==null) paths = new String[0];
		Path path = Paths.get(basePath, paths);

		logService.info("读取文件 [",Arrays.toString(paths),",",fileName,"] |",request.getRemoteAddr());

		File file = new File(path.toFile(), fileName);
		if (file.exists())
		{
			return export(file);
		}
		return null;
	}

	public ResponseEntity<FileSystemResource> export(File file)
	{
		if (file == null)
		{
			return null;
		}
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//		headers.add("Content-Disposition", "attachment; filename=" + file.getName());
//		headers.add("Pragma", "no-cache");  headers.add("Expires", "0");
//		headers.add("Last-Modified", new Date().toString());
//		headers.add("ETag", String.valueOf(System.currentTimeMillis()));
		return ResponseEntity.ok()
//				.headers(headers)
//				.contentLength(file.length())
//				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(new FileSystemResource(file));
	}


	/**
	 * 覆盖指定文档信息, 如果没有则创建
	 * @param doc 文档数据
	 * @return 信息
	 */
	@PostMapping("/write")
	public Response<?> write(
			@RequestBody EntityDoc doc
	)
	{
		boolean success = false;
		String[] paths = null;
		try
		{
			paths = doc.getPaths()!=null?doc.getPaths() : new String[0];
			Path path = Paths.get(basePath, paths);

			File file = new File(path.toFile(), doc.getFile());
			File fileParent = file.getParentFile();
			if(!fileParent.exists() || !fileParent.isDirectory()) fileParent.mkdirs();

			try(PrintStream out = new PrintStream(file,"utf8"))
			{
				out.println(doc.getData());
			}

			success = true;
			return Response.success();
		}
		catch (Exception e)
		{
			success = false;
			return Response.fail(e);
		}
		finally
		{
			logService.info("写入文件 [",Arrays.toString(paths),",",doc.getFile(),"] ",getSuccessString(success)," | ",request.getRemoteAddr());
		}
	}

	/**
	 * 删除指定路径文档信息
	 * @return 信息
	 */
	@PostMapping("/delete")
	public Response<?> delete(
			@RequestBody EntityDoc doc
	)
	{
		boolean success = false;
		String[] paths = null;
		try
		{
			paths = doc.getPaths()!=null?doc.getPaths() : new String[0];
			Path path = Paths.get(basePath, paths);

			File file = new File(path.toFile(), doc.getFile());

			success = file.exists() && file.isFile() && file.delete();

			return success ? Response.success() : Response.fail();
		}
		catch (Exception e)
		{
			success = false;
			return Response.fail(e);
		}
		finally
		{
			logService.info("删除文件 [",Arrays.toString(paths),",",doc.getFile(),"] ",getSuccessString(success)," | ",request.getRemoteAddr());
		}
	}

	/**
	 * 获取文件信息
	 */
	@PostMapping("/info")
	public Response<?> info(
			@RequestBody EntityDoc doc
	)
	{
		boolean success = false;
		String[] paths = null;
		try
		{
			paths = doc.getPaths()!=null?doc.getPaths() : new String[0];
			Path path = Paths.get(basePath, paths);

			File file = new File(path.toFile(), doc.getFile());

			EntityDocInfo info = new EntityDocInfo();
			info.setLength(file.length());
			info.setUpdateTime(file.lastModified());

			success = true;

			return Response.success(info);
		}
		catch (Exception e)
		{
			success = false;
			return Response.fail(e);
		}
		finally
		{
			logService.all("读取文件信息 [",Arrays.toString(paths),",",doc.getFile(),"] ",getSuccessString(success)," | ",request.getRemoteAddr());
		}
	}


}
