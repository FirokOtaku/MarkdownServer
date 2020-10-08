package firok.docs;

import firok.docs.entity.EntityDir;
import firok.docs.entity.EntityDoc;
import firok.docs.entity.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/dir")
@CrossOrigin
public class DirController
{
	@Value("${app.basePath}")
	String basePath;

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
		try
		{
			Path path = Paths.get(basePath,dir.getPaths());
			File file = path.toFile();
			return (!file.exists() || !file.isDirectory()) && file.mkdirs() ? Response.success(): Response.fail();
		}
		catch (Exception e)
		{
			return Response.fail(e);
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
		try
		{
			Path path = Paths.get(basePath, dir.getPaths());
			File file = path.toFile();
			return file.exists() && file.isDirectory() && file.delete() ? Response.success() : Response.fail();
		}
		catch (Exception e)
		{
			return Response.fail(e);
		}
	}


	@GetMapping("/test")
	public Response<?> test(
			@RequestParam(name="paths",required=false)String[] paths
	)
	{
		System.out.println(Arrays.toString(paths));
		return Response.success(paths);
	}

	public static void main(String[] args) {
		Path pathAbsolute = Paths.get("base/d1");
		Path pathBase = Paths.get("base/d2");
		Path pathRelative = pathBase.relativize(pathAbsolute);
		System.out.println(pathRelative);
	}
}
