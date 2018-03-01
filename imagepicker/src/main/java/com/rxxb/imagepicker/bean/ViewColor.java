package com.rxxb.imagepicker.bean;

/**
 * Created by 彭保生 on 2018/3/1.
 */

public class ViewColor {

    private String oKButtonTitleColorNormal;
    private String oKButtonTitleColorDisabled;
    private String naviBgColor;
    private String naviTitleColor;
    private String barItemTextColor;
    private String previewNaviBgColor;
    private String toolbarBgColor;
    private String toolbarTitleColorNormal;
    private String toolbarTitleColorDisabled;
    private String editNaviBgColor;
    private String editOKButtonTitleColorNormal;
    private String editCancelButtonTitleColorNormal;
    private String editToolbarBgColor;

    public ViewColor() {
        //确认按钮背景色
        oKButtonTitleColorNormal = "#1AAD19";
        oKButtonTitleColorDisabled = "#2D6830";

        //标题栏
        naviBgColor = "#393A3F";
        naviTitleColor = "#ffffff";
        barItemTextColor = "#ffffff";

        //底部bottomBar相关颜色
        toolbarBgColor = "#393A3F";
        toolbarTitleColorNormal = "#ffffff";
        toolbarTitleColorDisabled = "#66ffffff";

        previewNaviBgColor = "#393A3F";
        editNaviBgColor = "#393A3F";
        editToolbarBgColor = "#cc22292c";
    }

    public String getoKButtonTitleColorNormal() {
        return oKButtonTitleColorNormal;
    }

    public void setoKButtonTitleColorNormal(String oKButtonTitleColorNormal) {
        this.oKButtonTitleColorNormal = oKButtonTitleColorNormal;
    }

    public String getoKButtonTitleColorDisabled() {
        return oKButtonTitleColorDisabled;
    }

    public void setoKButtonTitleColorDisabled(String oKButtonTitleColorDisabled) {
        this.oKButtonTitleColorDisabled = oKButtonTitleColorDisabled;
    }

    public String getNaviBgColor() {
        return naviBgColor;
    }

    public void setNaviBgColor(String naviBgColor) {
        this.naviBgColor = naviBgColor;
    }

    public String getNaviTitleColor() {
        return naviTitleColor;
    }

    public void setNaviTitleColor(String naviTitleColor) {
        this.naviTitleColor = naviTitleColor;
    }

    public String getBarItemTextColor() {
        return barItemTextColor;
    }

    public void setBarItemTextColor(String barItemTextColor) {
        this.barItemTextColor = barItemTextColor;
    }

    public String getPreviewNaviBgColor() {
        return previewNaviBgColor;
    }

    public void setPreviewNaviBgColor(String previewNaviBgColor) {
        this.previewNaviBgColor = previewNaviBgColor;
    }

    public String getToolbarBgColor() {
        return toolbarBgColor;
    }

    public void setToolbarBgColor(String toolbarBgColor) {
        this.toolbarBgColor = toolbarBgColor;
    }

    public String getToolbarTitleColorNormal() {
        return toolbarTitleColorNormal;
    }

    public void setToolbarTitleColorNormal(String toolbarTitleColorNormal) {
        this.toolbarTitleColorNormal = toolbarTitleColorNormal;
    }

    public String getToolbarTitleColorDisabled() {
        return toolbarTitleColorDisabled;
    }

    public void setToolbarTitleColorDisabled(String toolbarTitleColorDisabled) {
        this.toolbarTitleColorDisabled = toolbarTitleColorDisabled;
    }

    public String getEditNaviBgColor() {
        return editNaviBgColor;
    }

    public void setEditNaviBgColor(String editNaviBgColor) {
        this.editNaviBgColor = editNaviBgColor;
    }

    public String getEditOKButtonTitleColorNormal() {
        return editOKButtonTitleColorNormal;
    }

    public void setEditOKButtonTitleColorNormal(String editOKButtonTitleColorNormal) {
        this.editOKButtonTitleColorNormal = editOKButtonTitleColorNormal;
    }

    public String getEditCancelButtonTitleColorNormal() {
        return editCancelButtonTitleColorNormal;
    }

    public void setEditCancelButtonTitleColorNormal(String editCancelButtonTitleColorNormal) {
        this.editCancelButtonTitleColorNormal = editCancelButtonTitleColorNormal;
    }

    public String getEditToolbarBgColor() {
        return editToolbarBgColor;
    }

    public void setEditToolbarBgColor(String editToolbarBgColor) {
        this.editToolbarBgColor = editToolbarBgColor;
    }
}
