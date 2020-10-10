package firok.mds.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.logging.Level;

@Service
public class LogService
{
	@Value("${app.logPath}")
	String logPath;

	@Value("${app.logLevel}")
	Level level;

	/**
	 * 日志输出流
	 */
	private PrintStream os;

	@PostConstruct
	public void construct() throws IOException
	{
		File fileLogPath = new File(logPath);
		fileLogPath.mkdirs();
		if(!fileLogPath.exists()) throw new IOException("创建日志文件夹失败:"+fileLogPath.getAbsolutePath());

		System.out.println("启动日志输出流, 日志输出等级为:"+level);
		File file =new File(fileLogPath,getLogFilename());
		os = new PrintStream(file, "utf8");
	}

	@PreDestroy
	public void destroy()
	{
		System.out.println("停止日志输出流");
		os.close();
	}

	private static String getLogFilename()
	{
		java.time.LocalDateTime ldt = LocalDateTime.now();

		return String.format(
				"%04d%02d%02d_%02d%02d%02d.log",
				ldt.getYear(),ldt.getMonthValue(),ldt.getDayOfMonth(),
				ldt.getHour(),ldt.getMinute(),ldt.getSecond()
		);
	}

	public void log(Level level,Object... obj)
	{
		if(level.intValue() < this.level.intValue()) return;

		java.time.LocalDateTime ldt = LocalDateTime.now();

		os.print('[');
		os.print(level);
		os.print('|');
		os.print(ldt.getHour());
		os.print(':');
		os.print(ldt.getMinute());
		os.print(':');
		os.print(ldt.getSecond());
		os.print("]  ");
//		os.print(obj instanceof Throwable ? ((Throwable) obj).getMessage() : obj);
		for(Object o:obj)
		{
			os.print(o instanceof Throwable? ((Throwable) o).getMessage(): o);
		}
		os.println();
	}
	public void all(Object... obj)
	{
		log(Level.ALL,obj);
	}
	public void info(Object... obj)
	{
		log(Level.INFO,obj);
	}
	public void warn(Object... obj)
	{
		log(Level.WARNING,obj);
	}
	public void err(Object... obj)
	{
		log(Level.SEVERE,obj);
	}
}
