package eu.dariusgovedas.businessapp.navigation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class NavigationController {

    @GetMapping("/public/home")
    public String openStartPage(){

        return "startPage";
    }

    @GetMapping("/private/admin")
    public String openAdminPage(){

        return "adminPage";
    }
}
