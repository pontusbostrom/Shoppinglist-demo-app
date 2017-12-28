package org.vaadin.pontus.shoppinglist.flow;

import java.util.Random;

import org.springframework.stereotype.Component;

import com.vaadin.spring.annotation.UIScope;

@UIScope
@Component
public class AppState implements ViewModel {

    private String sharingKey;
    private String username;

    private String keySymbols = "0123456789abcdefghijklmnopqrstuvxyz";

    public AppState() {
        Random r = new Random();
        sharingKey = "f";
        for (int i = 0; i < 6; i++) {
            sharingKey += keySymbols.charAt(r.nextInt(keySymbols.length()));
        }
    }

    @Override
    public String getSharingKey() {
        return sharingKey;
    }

    @Override
    public void setSharingKey(String sharingKey) {
        this.sharingKey = sharingKey;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

}
