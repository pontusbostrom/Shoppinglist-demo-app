package org.vaadin.pontus.shoppinglist.flow;

import com.vaadin.router.event.BeforeEnterObserver;
import com.vaadin.router.event.BeforeNavigationEvent;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

public class AbstractView extends PolymerTemplate<ViewModel>
implements BeforeEnterObserver {

    private AppState state;

    public AbstractView(AppState state) {
        this.state = state;
        getElement().addPropertyChangeListener("sharingKey", event -> {
            state.setSharingKey(getModel().getSharingKey());
        });
        getElement().addPropertyChangeListener("username", event -> {
            state.setUsername(getModel().getUsername());
        });
    }

    @Override
    public void beforeEnter(BeforeNavigationEvent event) {
        getModel().setSharingKey(state.getSharingKey());
        getModel().setUsername(state.getUsername());

    }
}
