package tqs.example.impostor.proofofconcept.eshop;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tqs.example.impostor.proofofconcept.BasicHttpClient;

import java.io.IOException;
import java.net.URISyntaxException;

@Controller
@RequestMapping("/poc/eshop")
public class EShopController {

    EShopHttpRequester httpRequester;

    @Autowired
    public EShopController(EShopHttpRequester requester) {
        httpRequester = requester;
    }

    @GetMapping()
    public String index(){
        return "eshop/index";
    }

    @GetMapping("cart")
    public String cart(Model model) throws IOException, ParseException {
        model.addAttribute("acpList", httpRequester.getAcpAddressesFromRemoteServer());
        return "eshop/cart";
    }

    @PostMapping("order")
    public String order(Model model, @RequestParam String address, @RequestParam String owner) throws IOException, URISyntaxException, ParseException {
        boolean result = httpRequester.postNewOrder(address, "pokeShop", owner, "FedUp");

        model.addAttribute("postResult" ,result);

        return "eshop/order";
    }
}
