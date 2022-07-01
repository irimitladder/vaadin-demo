package irimitlad.contentdashboard.views.extra;

import com.vaadin.flow.component.html.Div;

public class Panel
     extends Div {

    public Panel(Dashboard dashboard) {
        super();
        setId("content_panel");
        new DashboardDraggableBlock(this, dashboard);
    }
}
