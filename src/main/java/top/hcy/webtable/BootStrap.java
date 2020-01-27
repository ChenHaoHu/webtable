package top.hcy.webtable;

import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;
import top.hcy.webtable.annotation.WebTable;
import top.hcy.webtable.common.WTable;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.common.enums.WHandlerType;
import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.common.response.WResponseEntity;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.db.kv.KVType;
import top.hcy.webtable.filter.*;
import top.hcy.webtable.router.Router;
import top.hcy.webtable.service.LoginService;
import top.hcy.webtable.service.WService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;


/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable
 * @ClassName: BootStrap
 * @Author: hcy
 * @Description: web table 处理入口
 * @Date: 2020/1/14 19:52
 * @Version: 1.0
 */
@Slf4j
public class BootStrap {

    //普通请求前置处理
    WFiterChainImpl hPreRequest = null;

    public BootStrap() {
        init();
    }

    private void init() {
        initFilters();
        initDefaultAccount();
        initRouters();
        initTableData();
    }

    private void initTableData() {

        // 扫包
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackages(WConstants.PACKAGE_SCAN) // 指定路径URL
//                .addScanners(new SubTypesScanner()) // 添加子类扫描工具
                .addScanners(new FieldAnnotationsScanner()) // 添加 属性注解扫描工具
                .addScanners(new MethodAnnotationsScanner() ) // 添加 方法注解扫描工具
                .addScanners(new MethodParameterScanner() ) // 添加方法参数扫描工具
        );
        Set<Class<?>> c = reflections.getTypesAnnotatedWith(WebTable.class);
        Iterator<Class<?>> iterator = c.iterator();
        while (iterator.hasNext()){

        }


    }

    private void initRouters() {
        Router.addRouter(WHandlerType.LoginRequest,new LoginService());
    }

    //处理入口
    public WResponseEntity handler(HttpServletRequest request, HttpServletResponse response){
        WebTableContext ctx = new WebTableContext(request,response);
        //检查url 和 请求方法
        hPreRequest.doFilter(ctx);
        if (ctx.isError()){
          return defulteWResponseEntity(ctx);
        }
        //获取对应service
        WService wService = ctx.getWService();
        //校验参数
        wService.verifyParams(ctx);
        if (ctx.isError()){
            return defulteWResponseEntity(ctx);
        }
        //执行service
        try {
            wService.doService(ctx);
        } catch (Exception e) {
            log.error("service error"+e.getClass().getName() +"  ctx:"+ ctx.toString());
            ctx.setWRespCode(WRespCode.REQUEST_SERVICE_ERROR);
        }
        HashMap res = new HashMap();
        if (ctx.isRefreshToken()){
            res.put("token",ctx.getNewToken());
        }else{
            res.put("token","");
        }

        res.put("data",ctx.getRespsonseEntity());

        return new WResponseEntity(ctx.getWRespCode(),res);
    }


    private void initDefaultAccount() {
        ArrayList<HashMap> list = new ArrayList<>();
        HashMap<String,String> data = null;
        String[][] defaultAccounts = WConstants.DefaultAccounts;
        for (int i = 0; i < defaultAccounts.length; i++) {
            kvDBUtils.setValue(WConstants.PREFIX_ACCOUNTS +defaultAccounts[i][0],defaultAccounts[i][1], KVType.T_STRING);
        }
    }

    public WResponseEntity defulteWResponseEntity(WebTableContext ctx){
        return new WResponseEntity(ctx.getWRespCode(),ctx.getRespsonseEntity());
    }

    private void initFilters() {
        RequestMethodFilter requestMethodFilter = new RequestMethodFilter();
        RequestUrlFilter requestUrlFilter = new RequestUrlFilter();
        TokenJwtFilter tokenJwtFilter = new TokenJwtFilter();
        hPreRequest = new WFiterChainImpl(WHandlerType.HPreRequest);
        hPreRequest.addFiterOnLast(requestMethodFilter);
        hPreRequest.addFiterOnLast(requestUrlFilter);
        hPreRequest.addFiterOnLast(tokenJwtFilter);
    }
}
