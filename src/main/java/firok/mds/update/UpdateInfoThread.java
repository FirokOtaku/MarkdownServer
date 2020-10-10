package firok.mds.update;

import com.fasterxml.jackson.databind.ObjectMapper;
import firok.mds.MDSApplication;
import firok.mds.entity.EntityUpdateInfo;

import java.net.URL;

public class UpdateInfoThread extends Thread
{
	public UpdateInfoThread()
	{
		super();
		this.setDaemon(true);
	}

	@Override
	public void run()
	{
		try
		{
			URL url = new URL("https://api.github.com/repos/351768593/MarkdownServer/releases/latest");
			System.out.println("获取更新信息...");
			EntityUpdateInfo info = new ObjectMapper().readValue(url,EntityUpdateInfo.class);
			System.out.println("获取更新信息成功");

			Version versionCurrent = new Version(MDSApplication.version);
			Version versionRemote = new Version(info.getTagName());

			String msg;
			switch(versionCurrent.compareTo(versionRemote))
			{
				case 1:
				{
					msg = "您正在使用的版本["+MDSApplication.version+"]新于最新发布版本["+versionRemote.raw+"]. 如果需要更多信息请访问["+info.getUrl()+"].";
					break;
				}
				case -1:
				{
					msg = "您正在使用的版本["+MDSApplication.version+"]旧于最新版本["+versionRemote.raw+"], 请访问["+info.getUrl()+"]获取更新.";
					break;
				}
				default:
				{
					msg = "您正在使用的版本["+MDSApplication.version+"]已是最新版本. 如果需要更多信息请访问["+info.getUrl()+"].";
					break;
				}
			}
			System.out.println(msg);
		}
		catch (Exception e)
		{
			System.out.println("获取更新信息失败");
			e.printStackTrace();
		}
	}
}
