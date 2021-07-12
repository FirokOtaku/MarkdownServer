package firok.mds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MDSApplication
{
	public static final String version = "0.9.0";

	public static void main(String[] args)
	{
		SpringApplication.run(MDSApplication.class, args);
//		new UpdateInfoThread().start();
	}

//	public static File getFileFromPath(String basePath, String[] paths, String child)
//	{
//		if(paths==null) paths = new String[0];
//		Path path = Paths.get(basePath, paths);
//		return child!=null ? new File(path.toFile(),child) : path.toFile();
//	}

	public static String getSuccessString(boolean v)
	{
		return  v? "成功": "失败";
	}
}
