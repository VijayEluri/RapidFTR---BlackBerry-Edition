package com.rapidftr.net;

public interface ScreenCallBack {

	void handleConnectionProblem();

	void handleAuthenticationFailure();

	void updateRequestProgress(int progress);

	void onProcessComplete();

	void onProcessFail(String failureMessage);

	void setProgressMessage(String message);
}
