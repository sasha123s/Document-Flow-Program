package isedPackage;

public class IDGetter {
    private static IDGetter instance;

    public static IDGetter getInstance() {
        if (instance == null) {
            instance = new IDGetter();
        }
        return instance;
    }
    private String id;

    private String userID;

    public void setID(String aId) {
        id = aId;
    }

    public void setUserID(String aUserID) {
        userID = aUserID;
    }

    public String getID() {
        return id;
    }

    public String getUserID() {
        return userID;
    }
}
