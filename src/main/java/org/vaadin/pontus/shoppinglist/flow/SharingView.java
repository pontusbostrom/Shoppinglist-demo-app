package org.vaadin.pontus.shoppinglist.flow;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.router.Route;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;

@UIScope
@Route(value = "sharing", layout = AppLayout.class)
@Tag("sharing-view")
@HtmlImport("sharing-view.html")
public class SharingView extends AbstractView {

    @Autowired
    public SharingView(AppState state) {
        super(state);
    }

    public static interface SharingModel extends ViewModel {
        public void setSelected(String selected);

        public String getSelected();
    }

}
