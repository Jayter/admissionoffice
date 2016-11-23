package model;

public class BaseEntity {
    private long id;
    protected String name;

    private static long counter = 0;

    //used for creating new object
    public BaseEntity(String name) {
        this.name = name;
        this.id = counter++;
    }

    //used for restoring objects
    public BaseEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
