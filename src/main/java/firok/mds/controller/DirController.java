package firok.mds.controller;

import firok.mds.entity.EntityDir;
import firok.mds.entity.Response;
import firok.mds.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static firok.mds.MDSApplication.getSuccessString;

@RestController
@RequestMapping("/api/dir")
public class DirController
{
	@Value("${app.basePath}")
	String basePath;

	@Autowired
	public HttpServletRequest request;

	@Autowired
	public LogService logService;

	/**
	 * 获取指定路径的子目录和文件信息
	 * @param dir 路径
	 * @return 路径信息
	 */
	@PostMapping("/list")
	public Response<EntityDir> listDir(
			@RequestBody(required = false) EntityDir dir
	)
	{
		Path path = Paths.get(basePath,dir!=null&&dir.getPaths()!=null?dir.getPaths():new String[0]);

		File file = path.toFile();
		File[] childrenFiles = file.listFiles();
		if(childrenFiles == null) childrenFiles = new File[0];

		EntityDir ret = new EntityDir();
		ret.setPaths(dir!=null?dir.getPaths():new String[0]);
		List<String> dirChildrenFiles = new ArrayList<>();
		List<String> dirChildrenDirs = new ArrayList<>();
		ret.setChildrenDir(dirChildrenDirs);
		ret.setChildrenFile(dirChildrenFiles);

		for(File childrenFile:childrenFiles)
		{
			if(childrenFile.isFile() && childrenFile.getName().endsWith(".md"))
			{
				dirChildrenFiles.add(childrenFile.getName());
			}
			if(childrenFile.isDirectory())
			{
				dirChildrenDirs.add(childrenFile.getName());
			}
		}

		logService.info("读取文件夹 [",dir!=null&&dir.getPaths()!=null?Arrays.toString(dir.getPaths()):"/","] |",request.getRemoteAddr());

		return Response.success(ret);
	}

	/**
	 * 创建目录
	 * @param dir 路径
	 * @return 信息
	 */
	@PostMapping("/create")
	public Response<?> createDir(
			@RequestBody EntityDir dir
	)
	{
		boolean success = false;
		try
		{
			Path path = Paths.get(basePath,dir.getPaths());
			File file = path.toFile();

			success = (!file.exists() || !file.isDirectory()) && file.mkdirs();

			return success ? Response.success(): Response.fail();
		}
		catch (Exception e)
		{
			return Response.fail(e);
		}
		finally
		{
			logService.info("创建文件夹 [",Arrays.toString(dir.getPaths()),"] ",getSuccessString(success)," |",request.getRemoteAddr());
		}
	}

	/**
	 * 删除目录
	 * @param dir 路径
	 * @return 信息
	 */
	@PostMapping("/delete")
	public Response<?> deleteDir(
			@RequestBody EntityDir dir
	)
	{
		boolean success = false;
		try
		{
			Path path = Paths.get(basePath, dir.getPaths());
			File file = path.toFile();

			success = file.exists() && file.isDirectory() && file.delete();

			return success ? Response.success() : Response.fail();
		}
		catch (Exception e)
		{
			return Response.fail(e);
		}
		finally
		{
			logService.info("创建文件夹 [",Arrays.toString(dir.getPaths()),"] ",getSuccessString(success)," |",request.getRemoteAddr());
		}
	}
}
