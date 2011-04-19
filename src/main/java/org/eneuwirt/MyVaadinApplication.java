/*
 * Copyright 2009 IT Mill Ltd.
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
package org.eneuwirt;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;

/**
 * The Application's "main" class
 */
public class MyVaadinApplication extends Application implements ApplicationContext.TransactionListener
{
	private static final long serialVersionUID = 1L;
	private static ThreadLocal<MyVaadinApplication> currentApplication = new ThreadLocal<MyVaadinApplication>();
	private Window mainWindow;


	public Window getMainWindow()
	{
		return mainWindow;
	}


	@Override
	public void init()
	{
		this.getContext().addTransactionListener(this);

		this.mainWindow = new Window("My Vaadin Application");

		this.setMainWindow(mainWindow);

		mainWindow.setContent(new LoginScreen(this));
	}


	public void login(String username, String password)
	{
		UsernamePasswordToken token;

		token = new UsernamePasswordToken(username, password);
		// ”Remember Me” built-in, just do this:
		token.setRememberMe(true);

		// With most of Shiro, you'll always want to make sure you're working with the currently executing user,
		// referred to as the subject
		Subject currentUser = SecurityUtils.getSubject();

		// Authenticate
		currentUser.login(token);
	}


	public void logout()
	{
		getMainWindow().getApplication().close();

		Subject currentUser = SecurityUtils.getSubject();

		if (currentUser.isAuthenticated())
		{
			currentUser.logout();
		}
	}


	public static MyVaadinApplication getInstance()
	{
		return MyVaadinApplication.currentApplication.get();
	}


	@Override
	public void transactionStart(Application application, Object transactionData)
	{
		if (application == MyVaadinApplication.this)
		{
			MyVaadinApplication.currentApplication.set(this);
		}
	}


	@Override
	public void transactionEnd(Application application, Object transactionData)
	{
		if (application == MyVaadinApplication.this)
		{
			MyVaadinApplication.currentApplication.set(null);

			MyVaadinApplication.currentApplication.remove();
		}
	}

	// Logout Listener is defined for the application
	public static class LogoutListener implements Button.ClickListener
	{
		private static final long serialVersionUID = 1L;
		private MyVaadinApplication app;


		public LogoutListener(MyVaadinApplication app)
		{
			this.app = app;
		}


		@Override
		public void buttonClick(ClickEvent event)
		{
			this.app.logout();
		}
	}
}
