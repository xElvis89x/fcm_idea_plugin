package com.elvis.visualfsm.model;

import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.ISystemImage;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: el
 * Date: 23.03.14
 * Time: 19:27
 * To change this template use File | Settings | File Templates.
 */
public class AndroidTarget implements IAndroidTarget {
    @Override
    public String getLocation() {
        return null;
    }

    @Override
    public String getVendor() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getFullName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getClasspathName() {
        return null;
    }

    @Override
    public String getShortClasspathName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public AndroidVersion getVersion() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getVersionName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getRevision() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isPlatform() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public IAndroidTarget getParent() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getPath(int i) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean hasRenderingLibrary() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] getSkins() {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDefaultSkin() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public IOptionalLibrary[] getOptionalLibraries() {
        return new IOptionalLibrary[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] getPlatformLibraries() {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getProperty(String s) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Integer getProperty(String s, Integer integer) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Boolean getProperty(String s, Boolean aBoolean) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, String> getProperties() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getUsbVendorId() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ISystemImage[] getSystemImages() {
        return new ISystemImage[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ISystemImage getSystemImage(String s) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean canRunOn(IAndroidTarget iAndroidTarget) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String hashString() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int compareTo(IAndroidTarget o) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
