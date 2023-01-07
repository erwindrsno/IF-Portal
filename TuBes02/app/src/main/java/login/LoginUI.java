package login;

public interface LoginUI {
    void updateViewFromAPI(boolean valid, String title);
    void updateViewForInputValidation();
}
