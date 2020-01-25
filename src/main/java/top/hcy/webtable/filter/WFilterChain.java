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

    boolean addFitersOnFirst(AbstractFilterChain f);
    boolean deleteFiter(WHandleFilter f);
    boolean addFiterOnLast(WHandleFilter f);
    boolean addFiterOnFirst(WHandleFilter f);

}
