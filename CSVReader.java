import java.util.List;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Comma Separated Values (CSV) is a standard format for data exchange. Read
 * data in CSV style and return data in String array lines.
 * 
 * @author Wasuthun Wanaphongthipakorn.
 *
 */
public class CSVReader implements Iterator
{
	private InputStream input;
	private String name = "";
	private char delim = ',';
	private BufferedReader buffer;

	/**
	 * Read file input by using BufferedReader.
	 * 
	 * @param instream
	 *            input file.
	 */
	public CSVReader(InputStream instream)
	{
		this.buffer = new BufferedReader(new InputStreamReader(instream));
	}

	/**
	 * return char of delimiter.
	 * 
	 * @return delim
	 * 
	 */
	public char getDelimiter()
	{
		return this.delim;
	}

	/**
	 * Check input file if it's URL or local file.
	 * 
	 * @param name
	 *            input file.
	 * @throws IOException
	 *             if not found the file
	 */
	public CSVReader(String name) throws IOException
	{
		String URLPATTERN = "^\\w\\w+://\\S+";
		if (name.matches(URLPATTERN))
		{
			URL url = new URL(name);
			buffer = new BufferedReader(new InputStreamReader(url.openStream()));
		} else
		{
			buffer = new BufferedReader(new InputStreamReader(new FileInputStream(name)));
		}
	}

	/**
	 * set char of delimiter.
	 * 
	 * @param delim
	 * 
	 */
	public void setDelimiter(char delim)
	{
		this.delim = delim;
	}

	/**
	 * close BufferedReader.
	 * 
	 * @throws IOException
	 *             if not found the file
	 */
	public void close() throws IOException
	{
		input.close();
	}

	/**
	 * Read and check if there is available string left.
	 * 
	 * @return true if there is next line of data, false otherwise.
	 * 
	 * @throws IOException
	 *             if not found the file
	 */

	@Override
	public boolean hasNext()
	{
		try
		{
			while (name.equals("") || name.startsWith("#"))
			{
				name = buffer.readLine();
				if (name == null)
				{
					return false;
				}
			}
			return true;
		} catch (IOException e)
		{
			throw new RuntimeException();
		}
	}

	/**
	 * return a next line of string array.
	 * 
	 * @return String array.
	 */
	@Override
	public String[] next()
	{
		List<String> list = new ArrayList<String>();
		String[] words = null;
		boolean check = false;
		String temp = "";
		if (hasNext())
		{
			words = name.trim().split(delim + "");
			for (String word : words)
			{
				if (word.trim().startsWith("\"") && word.endsWith("\""))
				{
					list.add(word.replaceAll("\"", ""));
				} else if (word. trim().startsWith("\""))
				{
					temp = temp + word + delim;
					check = true;
				} else if (word.endsWith("\""))
				{
					temp = temp + word;
					check = false;
					temp = "";
				} else
				{
					if (check)
					{
						temp = temp + word + delim;
					}
					list.add(word.trim());
				}
			}
		}
		name = "";
		return list.toArray(new String[list.size()]);
	}

	@Override
	public void remove()
	{
		// TODO Auto-generated method stub

	}

}
