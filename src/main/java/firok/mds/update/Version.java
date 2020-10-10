package firok.mds.update;

public class Version implements Comparable<Version>
{
	public final int a,b,c;
	public final String raw;
	public Version(String raw) throws NumberFormatException
	{
		String[] words = raw.split("\\.");
		this.a = words.length > 0 ? Integer.parseInt(words[0]) : 0;
		this.b = words.length > 1 ? Integer.parseInt(words[1]) : 0;
		this.c = words.length > 2 ? Integer.parseInt(words[2]) : 0;

		this.raw = raw;
	}

	@Override
	public int compareTo(Version o)
	{
		return
				this.a > o.a ? 1 : this.a < o.a ? -1 :
				this.b > o.b ? 1 : this.b < o.b ? -1 :
				this.c > o.c ? 1 : this.c < o.c ? -1 :
				this.raw.compareTo(o.raw);
	}
}
