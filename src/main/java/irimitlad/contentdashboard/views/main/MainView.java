package irimitlad.contentdashboard.views.main;

import irimitlad.contentdashboard.views.extra.Dashboard;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import irimitlad.contentdashboard.views.extra.Panel;

@PageTitle("Content Dashboard")
@Route(value = "")
public class MainView
     extends HorizontalLayout {

    private final Dashboard dashboard;
    private final Panel panel;

    public MainView() {
        setSizeFull();
        setId("content_container");
        dashboard = new Dashboard();
        panel = new Panel(dashboard);
        add(panel, dashboard);
    }
}
