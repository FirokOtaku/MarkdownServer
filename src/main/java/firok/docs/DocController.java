package firok.docs;

import firok.docs.entity.EntityDoc;
import firok.docs.entity.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/doc")
@CrossOrigin
public class DocController
{
	@Value("${app.basePath}")
	String basePath;

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
		try
		{
			String[] paths = doc.getPaths()!=null?doc.getPaths() : new String[0];
			Path path = Paths.get(basePath, paths);

			File file = new File(path.toFile(), doc.getFile());
			File fileParent = file.getParentFile();
			if(!fileParent.exists() || !fileParent.isDirectory()) fileParent.mkdirs();

			try(PrintStream out = new PrintStream(file))
			{
				out.println(doc.getData());
			}

			return Response.success();
		}
		catch (Exception e)
		{
			return Response.fail(e);
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
		try
		{
			String[] paths = doc.getPaths()!=null?doc.getPaths() : new String[0];
			Path path = Paths.get(basePath, paths);

			File file = new File(path.toFile(), doc.getFile());

			return file.exists() && file.isFile() && file.delete() ?
					Response.success():
					Response.fail();
		}
		catch (Exception e)
		{
			return Response.fail(e);
		}
	}

}
