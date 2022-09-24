package com.hubert.downloader.external.coreapplication.enums;

import com.coreapplication.adapters.IconsAdapter;

/* loaded from: classes.dex */
public enum MediaType {
    UNDEFINED(1, IconsAdapter.ICON_TYPE_UNDEFINED),
    VIDEO(2, "Video"),
    IMAGE(3, IconsAdapter.ICON_TYPE_IMAGE),
    MUSIC(4, IconsAdapter.ICON_TYPE_MUSIC),
    DOCUMENT(5, IconsAdapter.ICON_TYPE_DOCUMENT),
    ARCHIVE(6, IconsAdapter.ICON_TYPE_ARCHIVE),
    APPLICATION(7, IconsAdapter.ICON_TYPE_APPLICATION),
    ANIMATED_GIF(8, "AnimatedGif");
    

    /* renamed from: id */
    private final int f262id;
    private final String name;

    MediaType(int i, String str) {
        this.name = str;
        this.f262id = i;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.f262id;
    }
}
