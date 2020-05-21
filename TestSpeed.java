package lesson3HW.part2;

public class TestSpeed {
    private long id;
    private String someString;
    private long someNumber;

    public TestSpeed(long id, String someString, long someNumber) {
        this.id = id;
        this.someString = someString;
        this.someNumber = someNumber;
    }

    public long getId() {
        return id;
    }

    public String getSomeString() {
        return someString;
    }

    public long getSomeNumber() {
        return someNumber;
    }
}
