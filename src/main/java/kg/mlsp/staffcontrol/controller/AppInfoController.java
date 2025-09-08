package kg.mlsp.staffcontrol.controller;

import kg.mlsp.staffcontrol.model.AppInfo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/staffcontrol")
public class AppInfoController {
    @GetMapping("/info")
    public AppInfo getAppInfo() {

        AppInfo appInfo = new AppInfo();
        appInfo.setName("Staff Control Application");
        appInfo.setVersion("1.0.0");
        appInfo.setDescription("This application is designed to manage staff control operations efficiently.");

        return appInfo;
    }

}
