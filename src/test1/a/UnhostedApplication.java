package test1.a;

import android.app.Application;

public class UnhostedApplication extends Application {
    private Unhosted mUnhosted;

    public void setUnhosted(Unhosted mUnhosted) {
        this.mUnhosted = mUnhosted;
    }

    public Unhosted getUnhosted() {
        return mUnhosted;
    }
}
