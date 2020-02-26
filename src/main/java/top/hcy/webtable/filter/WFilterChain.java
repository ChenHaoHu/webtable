package top.hcy.webtable.filter;


public interface WFilterChain extends WHandleFilter {
    boolean addFitersOnFirst(AbstractFilterChain f);
    boolean deleteFiter(WHandleFilter f);
    boolean addFiterOnLast(WHandleFilter f);
    boolean addFiterOnFirst(WHandleFilter f);
}
