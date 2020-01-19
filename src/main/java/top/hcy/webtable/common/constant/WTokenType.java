package top.hcy.webtable.common.constant;

/**
 * @ClassName: WTokenType
 * @Author: hcy
 * @Description:
 * @Date: 2020-01-20 00:20
 * @Version: 1.0
 **/
public class WTokenType {

    public  static  final  long  DEFAULT_TOKE = 60 * 1000;
    public  static  final  long  SHORT_TOKE = 30 * 1000;
    public  static  final  long  LONG_TOKE = 120 * 1000;

    public static long customToken(long l){
        return l;
    }
}
