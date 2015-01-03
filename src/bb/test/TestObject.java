package bb.test;

class TestObject
{

	public TestObject()
	{

		System.out.println("Successful");
	}

	public TestObject(String text)
	{

		System.out.println(text);
	}

	public TestObject(int i)

	{

		System.out.println(i);
	}

	public TestObject(TestObject to)
	{
		System.out.print(to);
	}
}
