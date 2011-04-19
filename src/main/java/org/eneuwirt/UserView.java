package org.eneuwirt;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class UserView  extends VerticalLayout
{
	private static final long serialVersionUID = 1L;
    private MyVaadinApplication app;


    public UserView(MyVaadinApplication app)
    {
        super();
        this.app = app;

        Label label = new Label("Logged in");

        Button logout = new Button("logout");
        logout.addListener(new MyVaadinApplication.LogoutListener(this.app));

        this.addComponent(logout);
        this.addComponent(label);
    }
}
