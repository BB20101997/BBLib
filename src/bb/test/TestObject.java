package bb.test;

class TestObject {

	public TestObject() {

		//noinspection UseOfSystemOutOrSystemErr
		System.out.println("Successful");
	}

	public TestObject(String text) {

		//noinspection UseOfSystemOutOrSystemErr
		System.out.println(text);
	}

	public TestObject(int i)

	{

		//noinspection UseOfSystemOutOrSystemErr
		System.out.println(i);
	}

	public TestObject(TestObject to) {
		//noinspection UseOfSystemOutOrSystemErr
		System.out.print(to);
	}
}
