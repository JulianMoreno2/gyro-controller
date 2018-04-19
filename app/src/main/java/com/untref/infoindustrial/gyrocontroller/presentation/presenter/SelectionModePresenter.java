package com.untref.infoindustrial.gyrocontroller.presentation.presenter;

public class SelectionModePresenter extends Presenter<SelectionModePresenter.View> {

    public void onSelectClientMode() {
        getView().onSelectClient();
    }

    public void onSelectServerMode() {
        getView().onSelectServer();
    }

    public interface View extends Presenter.View {
        void onSelectServer();

        void onSelectClient();
    }
}
