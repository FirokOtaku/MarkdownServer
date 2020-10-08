package firok.docs.entity;

import lombok.Data;

/**
 * 文档实体
 */
@Data
public class EntityDoc
{
	String[] paths;
	String file;
	String data;
}
