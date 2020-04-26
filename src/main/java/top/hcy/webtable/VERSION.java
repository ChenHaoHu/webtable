package top.hcy.webtable;

public final class VERSION {
    public static final int MajorVersion = 2;
    public static final int MinorVersion = 0;
    public static final int RevisionVersion = 0;

    public VERSION() {
    }

    public static String getVersionNumber() {
        return MajorVersion+"."+MinorVersion+"."+RevisionVersion;
    }
}
