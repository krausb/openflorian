package de.openflorian.web.admin.system;

import com.vaadin.ui.UI;

import de.openflorian.web.AbstractBrowser;

/**
 * Controller for encrypting text
 * 
 * @author Bastian Kraus <bofh@k-hive.de>
 */
public class EncryptController extends AbstractBrowser {
	public EncryptController(UI view) {
		super(view);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = -5618735217657780476L;

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	// private Textbox plain;
	// private Textbox encrypted;
	//
	// @Override
	// public String getLoginPage() {
	// return WebGlobals.PAGE_LOGIN;
	// }
	//
	// @Override
	// public boolean mayView(Principal user) {
	// return true;
	// }
	//
	// @Override
	// public boolean mustBeLoggedIn() {
	// return false;
	// }
	//
	// @Override
	// public void doAfterCompose(Component comp) throws Exception {
	// super.doAfterCompose(comp);
	// }
	//
	// /**
	// * Event-Handler: usersListbox.onClick
	// *
	// * @param event
	// */
	// public void onClick$encrypt(Event event) {
	// CryptCipherService cryptService = (CryptCipherService) ContainerManager
	// .getComponent(AvailableComponent.CryptService);
	//
	// if (cryptService != null) {
	// String cryptedStr =
	// cryptService.encrypt(cryptService.encrypt(plain.getText(),
	// CipherTarget.Xor),
	// CipherTarget.Blowfish);
	// log.debug("crypted string: " + cryptedStr);
	// encrypted.setText(cryptedStr);
	// }
	// }

}
