package tqs.example.impostor.proofofconcept.lockerPanel;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tqs.example.impostor.models.ACP;
import tqs.example.impostor.models.Order;
import tqs.example.impostor.proofofconcept.adminPanel.AdminPanelHttpRequester;
import tqs.example.impostor.service.LockerService;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/locker")
public class LockerPanelController {

    LockerPanelHttpRequester httpRequester;

    private final LockerService lockerService;

    @Autowired
    LockerPanelController(LockerPanelHttpRequester requester, LockerService service){
        this.lockerService = service;
        this.httpRequester = requester;
    }

    @GetMapping("/")
    public String index() {
        return "locker/lockerMainPage";
    }

    @PostMapping("/order/get")
    public String getOrder(@RequestParam("id") String id, Model model) throws IOException, ParseException {
        Optional<Order> orderOptional = httpRequester.getOrderFromRemoteServer(id);
        System.out.println(id);
        System.out.println(orderOptional);
        orderOptional.ifPresent(order -> model.addAttribute("order", order));
        System.out.println("Test");
        return "locker/lockerRetrievePackage";
    }
}
