package org.vaadin.pontus.shoppinglist.flow;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.router.Route;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Tag;
import com.vaadin.ui.UI;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.EventHandler;

@UIScope
@Route(value = "login", layout = AppLayout.class)
@Tag("login-view")
@HtmlImport("login-view.html")
public class LoginView extends AbstractView {

    @Autowired
    public LoginView(AppState state) {
        super(state);
    }

    @EventHandler
    private void _handleLoginSuccess() {
        UI.getCurrent().navigateTo("");
    }

}
