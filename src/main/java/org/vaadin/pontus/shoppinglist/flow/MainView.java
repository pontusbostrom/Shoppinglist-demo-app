/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.vaadin.pontus.shoppinglist.flow;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.router.Route;
import com.vaadin.router.event.BeforeNavigationEvent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;

@UIScope
@Route(value = "", layout = AppLayout.class)
@Tag("main-view")
@HtmlImport("main-view.html")
public class MainView extends AbstractView {

    @Autowired
    public MainView(AppState state) {
        super(state);
    }

    @Override
    public void beforeEnter(BeforeNavigationEvent event) {
        super.beforeEnter(event);
        getElement().callFunction("setupDB");
    }

}