package top.hcy.webtable.router;

import top.hcy.webtable.common.enums.WHandlerType;
import top.hcy.webtable.service.*;

public class RoutersManagement {
        public void invoke() {
            Router.addRouter(WHandlerType.LoginRequest,new LoginService());
            Router.addRouter(WHandlerType.GTABLE,new GetTableDataService());
            Router.addRouter(WHandlerType.UTABLE,new UpdateTableDataService());
            Router.addRouter(WHandlerType.GKVDATA,new GetKvDataService());
            Router.addRouter(WHandlerType.ATABLE,new AddTableDataService());
            Router.addRouter(WHandlerType.DTABLE,new DeteleTableDataService());
            Router.addRouter(WHandlerType.USERINFO,new UserInfoService());
        }
    }