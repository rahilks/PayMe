package rahil.payme.data;

public class PayMeModel {
    private static PayMeModel payMeModel;
    private boolean isLoggedIn;
    private String userFirstName;

    public static final String SHARED_PREF_NAME = "LoginSettings";
    public static final String PREF_LOGGED_IN = "PREF_LOGGED_IN";
    public static final String PREF_FIRST_NAME = "PREF_FIRST_NAME";

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public static PayMeModel getInstance() {
        if (payMeModel == null) {
            payMeModel = new PayMeModel();
        }
        return payMeModel;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }
}
