package firok.mds.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/img")
public class ImageController
{
	@Value("${app.basePath}")
	String basePath;

	@GetMapping(value = "/get",produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public byte[] get(
			@RequestParam(name="paths",required=false)String[] paths,
			@RequestParam(name="file")String filename
	)
	{
		if(paths==null) paths=new String[0];
		Path path = Paths.get(basePath, paths);
		File fileImg = new File(path.toFile(), filename);
		try(FileInputStream ifs = new FileInputStream(fileImg))
		{
			int length = ifs.available();
			byte[] ret = new byte[length];
			ifs.read(ret,0,length);
			return ret;
		}
		catch (Exception e)
		{
			return new byte[0];
		}
	}

}
