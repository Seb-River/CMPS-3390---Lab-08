import controllers.FindReplaceController;
import controllers.SearchController;
import views.MainView;

public class Launch {
    public static void main(String[] args) {
        MainView view = new MainView();
        new SearchController(view);
        new FindReplaceController(view);

        view.setVisible(true);
    }
}
