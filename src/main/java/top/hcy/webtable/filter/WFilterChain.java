package top.hcy.webtable.filter;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.filter
 * @ClassName: FiterChain
 * @Author: hcy
 * @Description: Handler failter
 * @Date: 2020/1/15 11:01
 * @Version: 1.0
 */
public interface WFilterChain extends WHandleFilter {

    public abstract boolean addFitersOnFirst(AbstractFilterChain f);
    public abstract boolean deleteFiter(WHandleFilter f);
    public abstract boolean addFiterOnLast(WHandleFilter f);
    public abstract boolean addFiterOnFirst(WHandleFilter f);

}
