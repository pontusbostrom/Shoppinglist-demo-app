package org.vaadin.pontus.shoppinglist.flow;

import com.vaadin.router.RouterLayout;
import com.vaadin.router.RouterLink;
import com.vaadin.ui.common.HasElement;
import com.vaadin.ui.common.JavaScript;
import com.vaadin.ui.common.StyleSheet;
import com.vaadin.ui.html.Div;
import com.vaadin.ui.layout.HorizontalLayout;
import com.vaadin.ui.layout.VerticalLayout;

@JavaScript("frontend://bower_components/pouchdb/dist/pouchdb.js")
@JavaScript("frontend://pouchdb.authentication.min.js")
@JavaScript("frontend://config.js")
@StyleSheet("frontend://styles.css")
public class AppLayout extends VerticalLayout implements RouterLayout {

    Div contentWrapper;

    public AppLayout() {
        VerticalLayout menu = new VerticalLayout();
        menu.setHeight("100%");
        menu.setWidth("200px");
        Div menuHeader = new Div();
        menuHeader.setText("Menu");
        menuHeader.setClassName("menu-header");
        RouterLink main = new RouterLink("Main", MainView.class);
        RouterLink sharingKey = new RouterLink("Sharing", SharingView.class);
        RouterLink login = new RouterLink("Login", LoginView.class);
        menu.add(menuHeader, main, sharingKey, login);

        Div header = new Div();
        header.setText("Shopping list");
        header.setClassName("header");
        VerticalLayout mainLayout = new VerticalLayout();
        contentWrapper = new Div();
        contentWrapper.setSizeFull();

        mainLayout.add(header, contentWrapper);
        mainLayout.setFlexGrow(1, contentWrapper);
        HorizontalLayout hl = new HorizontalLayout();

        hl.add(menu, mainLayout);
        hl.setFlexGrow(1, contentWrapper);
        hl.setSizeFull();
        add(hl);
        setSizeFull();
    }

    @Override
    public void setRouterLayoutContent(HasElement content) {
        contentWrapper.getElement().appendChild(content.getElement());
    }

}
