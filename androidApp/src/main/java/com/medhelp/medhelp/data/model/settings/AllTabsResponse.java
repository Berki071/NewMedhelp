package com.medhelp.medhelp.data.model.settings;

import com.google.gson.annotations.SerializedName;

public class AllTabsResponse {
    @SerializedName("id_service")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("izbrannoe")
    private String favorites;

    private boolean loading;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFavorites() {
        return favorites;
    }

    public void setFavorites(String favorites) {
        this.favorites = favorites;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }
}
