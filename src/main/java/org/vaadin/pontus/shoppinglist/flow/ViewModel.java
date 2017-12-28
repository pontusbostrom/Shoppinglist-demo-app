package org.vaadin.pontus.shoppinglist.flow;

import com.vaadin.flow.model.TemplateModel;

public interface ViewModel extends TemplateModel {

    public String getSharingKey();

    public void setSharingKey(String sharingKey);

    public String getUsername();

    public void setUsername(String username);
}
