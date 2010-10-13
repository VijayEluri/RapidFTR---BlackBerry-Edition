package com.rapidftr.screens;

import net.rim.device.api.system.Characters;
import net.rim.device.api.system.KeyListener;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.PasswordEditField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import com.rapidftr.controllers.LoginController;
import com.rapidftr.controls.Button;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.services.ScreenCallBack;
import com.rapidftr.utilities.Properties;
import com.rapidftr.utilities.SettingsStore;

public class LoginScreen extends CustomScreen implements ScreenCallBack,
		KeyListener {

	private static final int MAX_SIZE = 200;

	private final PasswordEditField passwordField = new PasswordEditField(
			"Password:", "", MAX_SIZE, USE_ALL_WIDTH);
	private final BasicEditField usernameField = basicField("Username:");
	private final BasicEditField hostField = basicField("Host:");
	private final BasicEditField portField = basicField("Port:");

	private final SettingsStore store;
	private Manager progressMsgFieldmanager;
	private LabelField progressMsg;
	private Button loginButton;
	private Manager buttonManager;
	private Button cancelButton;

	public LoginScreen(SettingsStore store) {
		super();
		this.store = store;
		layoutScreen();
		usernameField.setFocus();
	}

	private void layoutScreen() {
		// addLogo();
		// add(new SeparatorField());
		usernameField.setPadding(PADDING);
		usernameField.setText(store.getLastUsedLoginUsername());
		add(usernameField);
		passwordField.setPadding(PADDING);
		add(passwordField);
		add(new SeparatorField());
		addButtons();
		createProgressMsg();
	}

	private void createProgressMsg() {

		progressMsg = new LabelField();
		progressMsgFieldmanager = new HorizontalFieldManager(FIELD_HCENTER);
		progressMsgFieldmanager.setPadding(PADDING);
		progressMsg.setPadding(PADDING);
		progressMsgFieldmanager.add(progressMsg);

	}

	private BasicEditField basicField(String field) {
		return new BasicEditField(field, "", MAX_SIZE, USE_ALL_WIDTH
				| TextField.NO_NEWLINE);
	}

	private void addField(BasicEditField field, String defaultValue) {
		if (field.getManager() != null) {
			return;
		}
		field.setPadding(PADDING);
		field.setText(defaultValue);
		insert(field, getFieldCount() - 2);
	}

	private void addButtons() {
		loginButton = new Button("Login");
		loginButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onLoginButtonClicked();
			}
		});

		cancelButton = new Button("Cancel");
		cancelButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onCancelButtonClicked();
			}
		});
		
		buttonManager = new HorizontalFieldManager(FIELD_HCENTER);
		buttonManager.setPadding(PADDING);
		add(buttonManager);
	}

	protected void makeMenu(Menu menu, int instance) {
		menu.add(new MenuItem("Change Host", 1, 1) {
			public void run() {
				addField(hostField, store.getLastUsedLoginHost());
			}
		});

		menu.add(new MenuItem("Change Port", 2, 1) {
			public void run() {
				addField(portField, store.getLastUsedLoginPort());
			}
		});
	}

	public void setProgressMsg(String msg) {

		progressMsg.setText(msg);

		try {
			add(progressMsgFieldmanager);
		} catch (IllegalStateException ex) {

		}

	}

	public boolean isDirty() {
		return false;
	}

	public void removeProgressMsgIfExist() {

		try {
			delete(progressMsgFieldmanager);
		} catch (IllegalArgumentException ex) {

		}

	}

	private void onLoginButtonClicked() {
		Properties.getInstance().setHostName(hostField.getText());
		Properties.getInstance().setPort(portField.getText());

		((LoginController) controller).login(usernameField.getText(),
				passwordField.getText());
		showCancelButton();
	}

	private void onCancelButtonClicked() {
		cleanUp();
	}

	private void showCancelButton() {
		buttonManager.deleteAll();
		buttonManager.add(cancelButton);
	}

	public void setUp() {
		showLoginButton();
		removeProgressMsgIfExist();
	}

	private void showLoginButton() {
		buttonManager.deleteAll();
		buttonManager.add(loginButton);
	}

	public boolean onClose() {

		cleanUp();
		return super.onClose();

	}

	public void cleanUp() {

		removeProgressMsgIfExist();
		showLoginButton();
		((LoginController) controller).loginCancelled();
	}

	public void onAuthenticationFailure() {
		onProcessFail("Authentication Failure ");
	}

	public void onConnectionProblem() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				setProgressMsg(" Connection Problem ");
				showLoginButton();
			}
		});
	}

	public void updateProgress(int progress) {
		// TODO Auto-generated method stub

	}

	public void onProcessSuccess() {
		// controller.popScreen();

	}

	public void onProcessFail(final String message) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				setProgressMsg("Login Failed");
				showLoginButton();
			}
		});
	}

	public void setProgressMessage(String message) {
		setProgressMsg(message);
	}

	public void onProcessStart() {
		// TODO Auto-generated method stub

	}

	public boolean keyUp(int keycode, int time) {
		return super.keyUp(keycode, time);
	}

	public boolean keyDown(int keycode, int time) {
		if (keycode == Characters.ESCAPE) {
			((LoginController) controller).homeScreen();

		}
		return super.keyDown(keycode, time);
	}

	public boolean keyRepeat(int keycode, int time) {
		return super.keyRepeat(keycode, time);

	}

	public boolean keyStatus(int keycode, int time) {
		return super.keyStatus(keycode, time);
	}

	public boolean keyChar(char keycode, int time, int arg2) {
		if (keycode == Characters.ESCAPE) {
			((LoginController) controller).homeScreen();
			return true;
		} else {
			return super.keyChar(keycode, time, arg2);
		}

	}

	public void resetCredentials() {
		usernameField.setText(store.getLastUsedLoginUsername());
		passwordField.setText("");

	}

}
