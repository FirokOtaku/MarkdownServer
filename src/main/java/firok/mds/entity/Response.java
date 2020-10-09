package firok.mds.entity;

import lombok.Data;

@Data
public class Response<T>
{
	boolean success;
	String msg;
	T data;


	public static Response<String> success(Exception e)
	{
		return success(e.getLocalizedMessage());
	}
	public static <T> Response<T> success(T data)
	{
		Response<T> response = new Response<>();

		response.data=data;
		response.success=true;
		response.msg="成功";

		return response;
	}
	public static Response<?> success()
	{
		Response<?> response = new Response<>();

		response.success=true;
		response.msg="成功";

		return response;
	}
	public static Response<String> fail(Exception e)
	{
		return fail(e.getLocalizedMessage());
	}
	public static <T> Response<T> fail(T data)
	{
		Response<T> response = new Response<>();

		response.data=data;
		response.success=false;
		response.msg="失败";

		return response;
	}

	public static Response<?> fail()
	{
		Response<?> response = new Response<>();

		response.success=false;
		response.msg="失败";

		return response;
	}

}
