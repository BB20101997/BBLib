package bb.util.exceptions;

@SuppressWarnings({ "javadoc", "serial" })
public class WrongVersionException extends Exception
{

	float	versionExpected;
	float	versionGotten;

	/**
	 * @param ex
	 *            the version the Throwing class was able to handle
	 * @param got
	 *            the version the Throwing class got
	 */
	public WrongVersionException(float ex, float got)
	{

		versionExpected = ex;
		versionGotten = got;
	}

	@Override
	public String getMessage()
	{

		return "Wrong Version Expected Version : " + versionExpected + " ,but got Version : "
				+ versionGotten;
	}

}
