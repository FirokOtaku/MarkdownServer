package firok.mds.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityUpdateInfo
{
	Long id;
	String name;
	@JsonProperty("tag_name")
	String tagName;
	String url;
	@JsonProperty("published_at")
	String publishedAt;
}
