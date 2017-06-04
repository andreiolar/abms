package com.andreiolar.abms.client.jso;

import com.google.gwt.core.client.JavaScriptObject;

public class Error extends JavaScriptObject {

	protected Error() {

	}

	public final native String getCode() /*-{
		return this.code;
	}-*/;

	public final native String getMessage() /*-{
		return this.message;
	}-*/;

	public final native String getParam() /*-{
		return this.param;
	}-*/;

	public final native String getType() /*-{
		return this.type;
	}-*/;

}
