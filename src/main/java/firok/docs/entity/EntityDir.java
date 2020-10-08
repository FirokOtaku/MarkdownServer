package firok.docs.entity;

import lombok.Data;

import java.util.List;

/**
 * 目录实体
 */
@Data
public class EntityDir
{
	String[] paths;
	List<String> childrenDir;
	List<String> childrenFile;
}
