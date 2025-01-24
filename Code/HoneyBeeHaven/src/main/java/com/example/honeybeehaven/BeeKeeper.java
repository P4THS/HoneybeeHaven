package com.example.honeybeehaven;

import com.example.honeybeehaven.classes.*;
import com.example.honeybeehaven.repositories.*;
import com.example.honeybeehaven.services.IdService;
import com.example.honeybeehaven.services.PidService;
import com.example.honeybeehaven.tables.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.ref.Cleaner;
import java.time.LocalDateTime;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Controller
@RequestMapping(path="/BeeKeeper")
public class BeeKeeper {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private Admin2Repository admin2Repository;
    @Autowired
    private ReportedClientRepository rcRepository;
    @Autowired
    private ReportedReviewRepository rrRepository;
    @Autowired
    private ReportedBusinessRepository rbRepository;
    @Autowired
    private IdService idService;
    @Autowired
    private PidService pidService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private ChemicalRepository chemicalRepository;
    @Autowired
    private MachineryRepository machineryRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private ContactRepository contactRepository;

    @GetMapping(path="/AdminDashboard")
    public String AdminDashboard(Model model, HttpServletRequest request ){
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("adminid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
            }
        }
        if(cookieValueid != null && !cookieValueid.isEmpty()) {

            ///////////////////////////Graph 1////////////////////////////////////
            List<Client> clients = (List<Client>) clientRepository.findAll();
            List<Business> business = (List<Business>) businessRepository.findAll();

            Integer no_of_clients = clients.size();
            Integer no_of_business = business.size();
            Integer total_no_of_users = no_of_business + no_of_clients;

            Float client_ratio = (no_of_clients.floatValue() /total_no_of_users.floatValue()) * 300;
            Float business_ratio = (no_of_business.floatValue() /total_no_of_users.floatValue()) * 300;

            Graph1 graph1 = new Graph1();
            graph1.setNo_of_clients(no_of_clients);
            graph1.setNo_of_business(no_of_business);
            graph1.setTotal_no_of_users(total_no_of_users);
            graph1.setClient_ratio(client_ratio);
            graph1.setBusiness_ratio(business_ratio);
            graph1.setG1("<div id=\"product111\" class=\"bar12\" style=\"--g1:"+graph1.getBusiness_ratio()+"px\"  >Business<br>"+graph1.getNo_of_business()+"</div>" +
                    "<div id=\"product222\" class=\"bar121\" style=\"--g11:"+graph1.getClient_ratio()+"px\"  >Customer<br>"+graph1.getNo_of_clients()+"</div> ");

            model.addAttribute("graph1", graph1);


            ////////////////////////////Graph 2/////////////////////////////////////
            List <Client> clientList = (List <Client>)clientRepository.findAll();
            List <Business> businessList  = (List <Business>)businessRepository.findAll();
            Integer PunjanbCounter=0;
            Integer SindhCounter=0;
            Integer KpkCounter=0;
            Integer FATACounter=0;
            Integer BalochistanCounter=0;
            for (Client clientObj : clientList)
            {
                if(Objects.equals(clientObj.getPrimarylocation(),"Punjab"))
                {
                    PunjanbCounter++;
                }
                else if(Objects.equals(clientObj.getPrimarylocation(),"Sindh"))
                {
                    SindhCounter++;
                }
                else if(Objects.equals(clientObj.getPrimarylocation(),"Balochistan"))
                {
                    BalochistanCounter++;
                }
                else if(Objects.equals(clientObj.getPrimarylocation(),"KPK"))
                {
                    KpkCounter++;
                }
                else if(Objects.equals(clientObj.getPrimarylocation(),"FATA"))
                {
                    FATACounter++;
                }
                else
                {
                    System.out.println("MISMATCH ERROR");
                }
            }
            for (Business businessobj : businessList)
            {
                if(Objects.equals(businessobj.getPrimarylocation(),"Punjab"))
                {
                    PunjanbCounter++;
                }
                else if(Objects.equals(businessobj.getPrimarylocation(),"Sindh"))
                {
                    SindhCounter++;
                }
                else if(Objects.equals(businessobj.getPrimarylocation(),"Balochistan"))
                {
                    BalochistanCounter++;
                }
                else if(Objects.equals(businessobj.getPrimarylocation(),"KPK"))
                {
                    KpkCounter++;
                }
                else if(Objects.equals(businessobj.getPrimarylocation(),"FATA"))
                {
                    FATACounter++;
                }
                else
                {
                    System.out.println("MISMATCH ERROR");
                }
            }
            Integer total= PunjanbCounter + SindhCounter+BalochistanCounter+KpkCounter+FATACounter;
            float punjabRatio = (((float) PunjanbCounter/total)*100);
            float sindhRatio = (((float) SindhCounter/total)*100) + punjabRatio;
            float balochistanRatio = (((float) BalochistanCounter/total)*100) + sindhRatio;
            float kpkRatio = (((float) KpkCounter/total)*100)+balochistanRatio;
            float fataRatio = (((float) FATACounter/total)*100)+kpkRatio;

            Graph2 graph2 = new Graph2();
            graph2.pRatio = punjabRatio;
            graph2.sRatio = sindhRatio;
            graph2.bRatio = balochistanRatio;
            graph2.kRatio = kpkRatio;
            graph2.fRatio = fataRatio;

            graph2.g2 = "<div class=\"pie-chart\" style=\"--val21: "+0+"%; --val22: "+graph2.pRatio+"%; --val23: "+graph2.pRatio+"%; --val24: "+graph2.sRatio+"%; --val25: "+graph2.sRatio+"%; --val26: "+graph2.bRatio+"%; --val27: "+graph2.bRatio+"%; --val28: "+graph2.kRatio+"%; --val29: "+graph2.kRatio+"%; --val30: "+graph2.fRatio+"%\"></div>\n" +
                    "\t\t\t\t\t\t\t<div class=\"wrapper\">\n" +
                    "\t\t\t\t\t\t\t\t<div class=\"key-wrap\" style=\"padding-left: 200px;\">\n" +
                    "\t\t\t\t\t\t\t\t\t<!--Pie chart keys  -->\n" +
                    "\n" +
                    "\n" +
                    "\t\t\t\t\t\t\t\t\t<input type=\"radio\" name=\"values\" id=\"potato\" class=\"potato-key\"/>\n" +
                    "\t\t\t\t\t\t\t\t\t<label  for=\"potato\" class=\"potato-label\" style=\" background-color:  green; width: 120px; \">Punjab:"+PunjanbCounter+"</label>\n" +
                    "\n" +
                    "\t\t\t\t\t\t\t\t\t<input type=\"radio\" name=\"values\" id=\"yam\" class=\"yam-key\"/>\n" +
                    "\t\t\t\t\t\t\t\t\t<label for=\"yam\" class=\"yam-label\" style=\"background-color:   deeppink;width: 120px;\">Sindh:"+SindhCounter+"</label>\n" +
                    "\n" +
                    "\t\t\t\t\t\t\t\t\t<input type=\"radio\" name=\"values\" id=\"pasta\" class=\"pasta-key\"/>\n" +
                    "\t\t\t\t\t\t\t\t\t<label for=\"pasta\" class=\"pasta-label\" style=\"background-color:  orange;width: 120px;\">Balochistan:"+BalochistanCounter+"</label>\n" +
                    "\n" +
                    "\t\t\t\t\t\t\t\t\t<input type=\"radio\" name=\"values\" id=\"pasta\" class=\"pasta-key\"/>\n" +
                    "\t\t\t\t\t\t\t\t\t<label for=\"pasta\" class=\"pasta-label\" style=\"background-color: skyblue;width: 120px;\">KPK:"+KpkCounter+"</label>\n" +
                    "\n" +
                    "\t\t\t\t\t\t\t\t\t<input type=\"radio\" name=\"values\" id=\"pasta\" class=\"pasta-key\"/>\n" +
                    "\t\t\t\t\t\t\t\t\t<label for=\"pasta\" class=\"pasta-label\" style=\"background-color: red;width: 120px;\">Fata:"+FATACounter+"</label>\n" +
                    "\n" +
                    "\t\t\t\t\t\t\t\t</div>\n" +
                    "\n" +
                    "\n" +
                    "\t\t\t\t\t\t\t</div>\n" +
                    "\t\t\t\t\t\t\t<canvas id=\"myChart2\"></canvas>";

            model.addAttribute("graph2", graph2);
            ///////////////////////////////////////Graph 3/////////////////////////////////////////////

            List <Order> orderList = (List <Order>)orderRepository.findAll();
            int currentYear2 = LocalDate.now().getYear();
            Integer count18=0;
            Integer count19=0;
            Integer count20=0;
            Integer count21=0;
            Integer count22=0;
            Integer count23=0;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (Order orderDate : orderList)
            {
                LocalDate date = LocalDate.parse(orderDate.getOrderdate(),formatter);
                int x = date.getYear();
                if(Objects.equals(x,(currentYear2 - 5)))
                {
                    count18++;
                }
                else  if (Objects.equals(x,(currentYear2 - 4)))
                {
                    count19++;
                }
                else if(Objects.equals(x,(currentYear2 - 3)))
                {
                    count20++;
                }
                else if(Objects.equals(x,(currentYear2 - 2)))
                {
                    count21++;
                }
                else if(Objects.equals(x,(currentYear2 - 1)))
                {
                    count22++;
                }
                else if(Objects.equals(x,(currentYear2)))
                {
                    count23++;
                }
            }

            Integer total_3 = count18 + count19 + count20 + count21 + count22 + count23;

            float ratio18 = ((float)count18 / (float) total_3) * 100;
            float ratio19 = ((float)count19 / (float) total_3) * 100;
            float ratio20 = ((float)count20 / (float) total_3) * 100;
            float ratio21 = ((float)count21 / (float) total_3) * 100;
            float ratio22 = ((float)count22 / (float) total_3) * 100;
            float ratio23 = ((float)count23 / (float) total_3) * 100;

            Graph3 graph3 = new Graph3();
            graph3.Ratio2018 = ratio18;
            graph3.Ratio2019 = ratio19;
            graph3.Ratio2020 = ratio20;
            graph3.Ratio2021 = ratio21;
            graph3.Ratio2022 = ratio22;
            graph3.Ratio2023 = ratio23;

            graph3.g3 = "\t\t\t\t\t\t\t<div class=\"simple-bar-chart\">\n" +
                    "\n" +
                    "\t\t\t\t\t\t\t\t<div class=\"item\" style=\"--clr: #5EB344; --val: "+graph3.Ratio2018+"\">\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"label\">"+(currentYear2 - 5)+"</div>\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"value\">"+count18+"</div>\n" +
                    "\t\t\t\t\t\t\t\t</div>\n" +
                    "\n" +
                    "\t\t\t\t\t\t\t\t<div class=\"item\" style=\"--clr: #FCB72A; --val: "+graph3.Ratio2019+"\">\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"label\">"+(currentYear2 - 4)+"</div>\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"value\">"+count19+"</div>\n" +
                    "\t\t\t\t\t\t\t\t</div>\n" +
                    "\n" +
                    "\t\t\t\t\t\t\t\t<div class=\"item\" style=\"--clr: #F8821A; --val: "+graph3.Ratio2020+"\">\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"label\">"+(currentYear2 - 3)+"</div>\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"value\">"+count20+"</div>\n" +
                    "\t\t\t\t\t\t\t\t</div>\n" +
                    "\n" +
                    "\t\t\t\t\t\t\t\t<div class=\"item\" style=\"--clr: #E0393E; --val: "+graph3.Ratio2021+"\">\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"label\">"+(currentYear2 - 2)+"</div>\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"value\">"+count21+"</div>\n" +
                    "\t\t\t\t\t\t\t\t</div>\n" +
                    "\n" +
                    "\t\t\t\t\t\t\t\t<div class=\"item\" style=\"--clr: #963D97; --val: "+graph3.Ratio2022+"\">\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"label\">"+(currentYear2 - 1)+"</div>\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"value\">"+count22+"</div>\n" +
                    "\t\t\t\t\t\t\t\t</div>\n" +
                    "\n" +
                    "\t\t\t\t\t\t\t\t<div class=\"item\" style=\"--clr: #069CDB; --val: "+graph3.Ratio2023+"\">\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"label\">"+(currentYear2 )+"</div>\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"value\">"+count23+"</div>\n" +
                    "\t\t\t\t\t\t\t\t</div>\n" +
                    "\t\t\t\t\t\t\t</div>\n" +
                    "\t\t\t\t\t\t\t<canvas id=\"myChart3\"></canvas>";

            model.addAttribute("graph3", graph3);

            ///////////////////////////////////////Graph 4/////////////////////////////////////////////

            List<Order> orders = (List<Order>) orderRepository.findAll();
            Integer PunjanbsalesCounter=0;
            Integer SindhsalesCounter=0;
            Integer KpksalesCounter=0;
            Integer FATAsalesCounter=0;
            Integer BalochistansalesCounter=0;

            for (Order orderit : orders){
                Integer id = orderit.getClientid();
                Optional<Client> optionalClient = clientRepository.findById(id);

                // Check if the client is present in the Optional
                if (optionalClient.isPresent()) {
                    Client client2 = optionalClient.get();
                    if(Objects.equals(client2.getPrimarylocation(), "Punjab")){
                        PunjanbsalesCounter++;
                    }
                    else if (Objects.equals(client2.getPrimarylocation(), "Sindh")) {
                        SindhsalesCounter++;
                    }
                    else if (Objects.equals(client2.getPrimarylocation(), "Balochistan")){
                        BalochistansalesCounter++;
                    }
                    else if (Objects.equals(client2.getPrimarylocation(), "KPK")){
                        KpksalesCounter++;
                    }
                    else if (Objects.equals(client2.getPrimarylocation(), "FATA")){
                        FATAsalesCounter++;
                    }
                }
            }
            Integer Total = PunjanbsalesCounter+SindhsalesCounter+BalochistansalesCounter+KpksalesCounter+FATAsalesCounter;

            float punjabsalesRatio = ((float)PunjanbsalesCounter/Total) * 500;
            float sindhsalesRatio = ((float)SindhsalesCounter/Total) * 500;
            float balochistansalesRatio = ((float)BalochistansalesCounter/Total) * 500;
            float kpksalesRatio = ((float)KpksalesCounter/Total) * 500;
            float fatasalesRatio = ((float)FATAsalesCounter/Total) * 500;

            Graph4 graph4 = new Graph4();
            graph4.psalesRatio = punjabsalesRatio;
            graph4.ssalesRatio = sindhsalesRatio;
            graph4.bsalesRatio = balochistansalesRatio;
            graph4.ksalesRatio = kpksalesRatio;
            graph4.fsalesRatio = fatasalesRatio;

            graph4.g4 = "\t<div id=\"newbarChart\" style=\" --val41:"+graph4.psalesRatio+"px ; --val42:"+graph4.ssalesRatio+"px ; --val43:"+graph4.bsalesRatio+"px ; --val44:"+graph4.ksalesRatio+"px ; --val45:"+graph4.fsalesRatio+"px ;\">\n" +
                    "\t\t\t\t\t\t\t\t<div id=\"product1\" class=\"bar1\">Punjab<br>"+PunjanbsalesCounter+"</div>\n" +
                    "\t\t\t\t\t\t\t\t<div id=\"product2\" class=\"bar1\">Sindh<br>"+SindhsalesCounter+"</div>\n" +
                    "\t\t\t\t\t\t\t\t<div id=\"product3\" class=\"bar1\">Balochistan<br>"+BalochistansalesCounter+"</div>\n" +
                    "\t\t\t\t\t\t\t\t<div id=\"product4\" class=\"bar1\">KPK<br>"+KpksalesCounter+"</div>\n" +
                    "\t\t\t\t\t\t\t\t<div id=\"product5\" class=\"bar1\">FATA<br>"+FATAsalesCounter+"</div>\n" +
                    "\t\t\t\t\t\t\t\t<!-- Add more products as needed -->\n" +
                    "\t\t\t\t\t\t\t</div>\n" +
                    "\t\t\t\t\t\t\t<canvas id=\"myChart4\"></canvas>";

            model.addAttribute("graph4", graph4);

            ////////////////////////////////////////Graph 5/////////////////////////////////////////////

            List <Order> orderList1 = (List <Order>)orderRepository.findAll();
            int currentYear = LocalDate.now().getYear();
            float profit18=0;
            float profit19=0;
            float profit20=0;
            float profit21=0;
            float profit22=0;
            float profit23=0;
            for (Order orderDate : orderList1)
            {
                LocalDate date = LocalDate.parse(orderDate.getOrderdate(),formatter);
                int x = date.getYear();
                if(Objects.equals(x,(currentYear - 5)))
                {
                    profit18 = profit18 + orderDate.getHoneybeehavencommision();
                }
                else  if (Objects.equals(x,(currentYear - 4)))
                {
                    profit19 = profit19 + orderDate.getHoneybeehavencommision();
                }
                else if(Objects.equals(x,(currentYear - 3)))
                {
                    profit20 = profit20 + orderDate.getHoneybeehavencommision();
                }
                else if(Objects.equals(x,(currentYear - 2)))
                {
                    profit21 = profit21 + orderDate.getHoneybeehavencommision();
                }
                else if(Objects.equals(x,(currentYear - 1)))
                {
                    profit22 = profit22 + orderDate.getHoneybeehavencommision();
                }
                else if(Objects.equals(x,(currentYear )))
                {
                    profit23 = profit23 + orderDate.getHoneybeehavencommision();
                }
            }
            float total4 = profit18 + profit19 + profit20 + profit21 + profit22 + profit23;
            float profitatio18  = (profit18/total4)*100;
            float profitratio19 = (profit19/total4)*100;
            float profitratio20 = (profit20/total4)*100;
            float profitratio21 = (profit21/total4)*100;
            float profitratio22 = (profit22/total4)*100;
            float profitratio23 = (profit23/total4)*100;

            Graph5 graph5 = new Graph5();
            graph5.pRatio2018 = profitatio18;
            graph5.pRatio2019 = profitratio19;
            graph5.pRatio2020 = profitratio20;
            graph5.pRatio2021 = profitratio21;
            graph5.pRatio2022 = profitratio22;
            graph5.pRatio2023 = profitratio23;

            graph5.g5 = "<div class=\"simple-bar-chart\">\n" +
                    "\n" +
                    "\t\t\t\t\t\t\t\t<div class=\"item\" style=\"--clr: #9be085; --val: "+graph5.pRatio2018+"\">\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"label\">"+(currentYear - 5)+"</div>\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"value\">"+profit18+"</div>\n" +
                    "\t\t\t\t\t\t\t\t</div>\n" +
                    "\n" +
                    "\t\t\t\t\t\t\t\t<div class=\"item\" style=\"--clr: #73c957; --val: "+graph5.pRatio2019+"\">\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"label\">"+(currentYear - 4)+"</div>\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"value\">"+profit19+"</div>\n" +
                    "\t\t\t\t\t\t\t\t</div>\n" +
                    "\n" +
                    "\t\t\t\t\t\t\t\t<div class=\"item\" style=\"--clr: #5acf33; --val: "+graph5.pRatio2020+"\">\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"label\">"+(currentYear - 3)+"</div>\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"value\">"+profit20+"</div>\n" +
                    "\t\t\t\t\t\t\t\t</div>\n" +
                    "\n" +
                    "\t\t\t\t\t\t\t\t<div class=\"item\" style=\"--clr: #3db913; --val: "+graph5.pRatio2021+"\">\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"label\">"+(currentYear - 2)+"</div>\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"value\">"+profit21+"</div>\n" +
                    "\t\t\t\t\t\t\t\t</div>\n" +
                    "\n" +
                    "\t\t\t\t\t\t\t\t<div class=\"item\" style=\"--clr: #2b8f09; --val: "+graph5.pRatio2022+"\">\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"label\">"+(currentYear - 1)+"</div>\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"value\">"+profit22+"</div>\n" +
                    "\t\t\t\t\t\t\t\t</div>\n" +
                    "\n" +
                    "\t\t\t\t\t\t\t\t<div class=\"item\" style=\"--clr: #1f6b05; --val: "+graph5.pRatio2023+"\">\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"label\">"+(currentYear)+"</div>\n" +
                    "\t\t\t\t\t\t\t\t\t<div class=\"value\">"+profit23+"</div>\n" +
                    "\t\t\t\t\t\t\t\t</div>\n" +
                    "\t\t\t\t\t\t\t</div>\n" +
                    "\t\t\t\t\t\t\t<canvas id=\"myChart5\"></canvas>";

            model.addAttribute("graph5", graph5);

            return "/beekeeper/admin/dashboard";
        }
        else {
            return "redirect:/BeeKeeper/AdminLogin";
        }
    }

    @GetMapping(path="/Adminlogout")
    public String Adminlogout(Model model, HttpServletRequest request, HttpServletResponse response){
        Cookie userIdCookie = new Cookie("adminid", "");

        userIdCookie.setPath("/"); // Set the cookie's path (root path)


        response.addCookie(userIdCookie);

        return "redirect:/BeeKeeper/AdminLogin";
    }

    @GetMapping(path="/AdminReports")
    public String AdminReports(Model model, HttpServletRequest request ) {
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("adminid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
            }
        }
        if(cookieValueid != null && !cookieValueid.isEmpty()) {
            List<ReportedReviews> report = (List<ReportedReviews>) rrRepository.findAll();

            model.addAttribute("data", report);

            return "/beekeeper/admin/all-reports";
        }
        else {
            return "redirect:/BeeKeeper/AdminLogin";
        }
    }

    @GetMapping(path="/banUser")
    public String banUser(Model model, @RequestParam("userID")String uid, @RequestParam("reviewID")String rid, @RequestParam("reportID")String rpid){
        Integer userId = Integer.parseInt(uid);
        Integer reviewId = Integer.parseInt(rid);
        Integer reportId = Integer.parseInt(rpid);

        if(clientRepository.findById(userId).isPresent()) {
            Client client = clientRepository.findById(userId).get();
            client.setBanned(true);
            clientRepository.save(client);

            Review review = reviewRepository.findById(reviewId).get();

            int number = reviewRepository.findAllByItemid(review.getItemid()).size();

            Integer businessid = 1;

            if (chemicalRepository.findById(review.getItemid()).isPresent()){
                Chemical chemical = chemicalRepository.findById(review.getItemid()).get();
                if (number - 1 != 0)
                    chemical.setItemrating((chemical.getItemrating() * (number) - review.getRating())/(number - 1));
                else
                    chemical.setItemrating(Float.valueOf(0));
                chemicalRepository.save(chemical);
                businessid = chemical.getBusinessid();
            }
            else if(serviceRepository.findById(review.getItemid()).isPresent()){
                Service service = serviceRepository.findById(review.getItemid()).get();
                if (number - 1 != 0)
                    service.setItemrating((service.getItemrating() * (number) - review.getRating())/(number - 1));
                else
                    service.setItemrating(Float.valueOf(0));
                serviceRepository.save(service);
                businessid = service.getBusinessid();
            }
            else {
                Machinery machinery = machineryRepository.findById(review.getItemid()).get();
                if (number - 1 != 0)
                    machinery.setItemrating((machinery.getItemrating() * (number) - review.getRating())/(number - 1));
                else
                    machinery.setItemrating(Float.valueOf(0));
                machineryRepository.save(machinery);
                businessid = machinery.getBusinessid();
            }

            List<Chemical> chemicals = chemicalRepository.findAllByBusinessid(businessid);
            List<Service> services = serviceRepository.findAllByBusinessid(businessid);
            List<Machinery> machineries = machineryRepository.findAllByBusinessid(businessid);

            int reviewed = 0;

            for (int i = 0; i < chemicals.size(); i++){
                if (chemicals.get(i).getItemrating() > 0){
                    reviewed++;
                }
            }

            for (int i = 0; i < machineries.size(); i++){
                if (machineries.get(i).getItemrating() > 0){
                    reviewed++;
                }
            }

            for (int i = 0; i < services.size(); i++){
                if (services.get(i).getItemrating() > 0){
                    reviewed++;
                }
            }

            Business business = businessRepository.findById(businessid).get();

            if (reviewed - 1 != 0)
                business.setBusinessrating((reviewed * business.getBusinessrating() - review.getRating())/(reviewed - 1));
            else
                business.setBusinessrating(Float.valueOf(0));

            businessRepository.save(business);

            reviewRepository.deleteById(reviewId);



            List<ReportedReviews> R_report = (List<ReportedReviews>) rrRepository.findAll();

            for (ReportedReviews report : R_report) {
                Integer x = report.getReportID();
                if(Objects.equals(report.getComplaineeID(), userId)){
                    rrRepository.deleteById(x);
                }
            }

            List<ReportedClients> r2_report = (List<ReportedClients>) rcRepository.findAll();

            for(ReportedClients report : r2_report){
                Integer x = report.getReport2ID();
                if(Objects.equals(report.getComplainee2ID(), userId)){
                    rcRepository.deleteById(x);
                }
            }
        }

        return "redirect:/BeeKeeper/AdminReports";
    }
    @GetMapping(path="/tempBanUser")
    public String tempBanUser(Model model, @RequestParam("userID")String uid, @RequestParam("reviewID")String rid, @RequestParam("reportID")String rpid){
        Integer userId = Integer.parseInt(uid);
        Integer reviewId = Integer.parseInt(rid);
        Integer reportId = Integer.parseInt(rpid);

        if(clientRepository.findById(userId).isPresent()) {
            Client client = clientRepository.findById(userId).get();
            //client.setBanned(true);
            LocalDate currentDate = LocalDate.now();
            client.setDateBanned(currentDate.toString());
            clientRepository.save(client);

            Review review = reviewRepository.findById(reviewId).get();

            int number = reviewRepository.findAllByItemid(review.getItemid()).size();

            Integer businessid = 1;

            if (chemicalRepository.findById(review.getItemid()).isPresent()){
                Chemical chemical = chemicalRepository.findById(review.getItemid()).get();
                if (number - 1 != 0)
                    chemical.setItemrating((chemical.getItemrating() * (number) - review.getRating())/(number - 1));
                else
                    chemical.setItemrating(Float.valueOf(0));
                chemicalRepository.save(chemical);
                businessid = chemical.getBusinessid();
            }
            else if(serviceRepository.findById(review.getItemid()).isPresent()){
                Service service = serviceRepository.findById(review.getItemid()).get();
                if (number - 1 != 0)
                    service.setItemrating((service.getItemrating() * (number) - review.getRating())/(number - 1));
                else
                    service.setItemrating(Float.valueOf(0));
                serviceRepository.save(service);
                businessid = service.getBusinessid();
            }
            else {
                Machinery machinery = machineryRepository.findById(review.getItemid()).get();
                if (number - 1 != 0)
                    machinery.setItemrating((machinery.getItemrating() * (number) - review.getRating())/(number - 1));
                else
                    machinery.setItemrating(Float.valueOf(0));
                machineryRepository.save(machinery);
                businessid = machinery.getBusinessid();
            }

            List<Chemical> chemicals = chemicalRepository.findAllByBusinessid(businessid);
            List<Service> services = serviceRepository.findAllByBusinessid(businessid);
            List<Machinery> machineries = machineryRepository.findAllByBusinessid(businessid);

            int reviewed = 0;

            for (int i = 0; i < chemicals.size(); i++){
                if (chemicals.get(i).getItemrating() > 0){
                    reviewed++;
                }
            }

            for (int i = 0; i < machineries.size(); i++){
                if (machineries.get(i).getItemrating() > 0){
                    reviewed++;
                }
            }

            for (int i = 0; i < services.size(); i++){
                if (services.get(i).getItemrating() > 0){
                    reviewed++;
                }
            }

            Business business = businessRepository.findById(businessid).get();

            if (reviewed - 1 != 0)
                business.setBusinessrating((reviewed * business.getBusinessrating() - review.getRating())/(reviewed - 1));
            else
                business.setBusinessrating(Float.valueOf(0));

            businessRepository.save(business);

            reviewRepository.deleteById(reviewId);
            rrRepository.deleteById(reportId);
        }

        return "redirect:/BeeKeeper/AdminReports";
    }

    @GetMapping(path="/deleteReview")
    public String deleteReview(Model model, @RequestParam("reviewID")String rid, @RequestParam("reportID")String rpid){
        Integer reviewId = Integer.parseInt(rid);
        Integer reportId = Integer.parseInt(rpid);
        if(reviewRepository.findById(reviewId).isPresent()) {


            Review review = reviewRepository.findById(reviewId).get();

            int number = reviewRepository.findAllByItemid(review.getItemid()).size();

            Integer businessid = 1;

            if (chemicalRepository.findById(review.getItemid()).isPresent()){
                Chemical chemical = chemicalRepository.findById(review.getItemid()).get();
                if (number - 1 != 0)
                    chemical.setItemrating((chemical.getItemrating() * (number) - review.getRating())/(number - 1));
                else
                    chemical.setItemrating(Float.valueOf(0));
                chemicalRepository.save(chemical);
                businessid = chemical.getBusinessid();
            }
            else if(serviceRepository.findById(review.getItemid()).isPresent()){
                Service service = serviceRepository.findById(review.getItemid()).get();
                if (number - 1 != 0)
                    service.setItemrating((service.getItemrating() * (number) - review.getRating())/(number - 1));
                else
                    service.setItemrating(Float.valueOf(0));
                serviceRepository.save(service);
                businessid = service.getBusinessid();
            }
            else {
                Machinery machinery = machineryRepository.findById(review.getItemid()).get();
                if (number - 1 != 0)
                    machinery.setItemrating((machinery.getItemrating() * (number) - review.getRating())/(number - 1));
                else
                    machinery.setItemrating(Float.valueOf(0));
                machineryRepository.save(machinery);
                businessid = machinery.getBusinessid();
            }

            List<Chemical> chemicals = chemicalRepository.findAllByBusinessid(businessid);
            List<Service> services = serviceRepository.findAllByBusinessid(businessid);
            List<Machinery> machineries = machineryRepository.findAllByBusinessid(businessid);

            int reviewed = 0;

            for (int i = 0; i < chemicals.size(); i++){
                if (chemicals.get(i).getItemrating() > 0){
                    reviewed++;
                }
            }

            for (int i = 0; i < machineries.size(); i++){
                if (machineries.get(i).getItemrating() > 0){
                    reviewed++;
                }
            }

            for (int i = 0; i < services.size(); i++){
                if (services.get(i).getItemrating() > 0){
                    reviewed++;
                }
            }

            Business business = businessRepository.findById(businessid).get();

            if (reviewed - 1 != 0)
                business.setBusinessrating((reviewed * business.getBusinessrating() - review.getRating())/(reviewed - 1));
            else
                business.setBusinessrating(Float.valueOf(0));



            Notifications notifications = new Notifications();
            notifications.setNotificationMessage("Multiple reports and suspicious activity has been detected. You have been warned further involvment will result in ban");

            notifications.setUserId(review.getClientid());
            notifications.setTime(LocalDateTime.now().toString());
            notificationRepository.save(notifications);

            businessRepository.save(business);
            reviewRepository.deleteById(reviewId);
            rrRepository.deleteById(reportId);
        }

        return "redirect:/BeeKeeper/AdminReports";
    }

    @GetMapping(path="/dismissReport")
    public String dismissReport(Model model, @RequestParam("reportID")String rpid){
        Integer reportId = Integer.parseInt(rpid);
        if(rrRepository.findById(reportId).isPresent())
        {
            rrRepository.deleteById(reportId);
        }
        return "redirect:/BeeKeeper/AdminReports";
    }

    @GetMapping(path = "/ReportedClients")
    public String ReportedClients(Model model, HttpServletRequest request ){

        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("adminid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
            }
        }
        if(!cookieValueid.isEmpty()) {
            List<ReportedClients> creport = (List<ReportedClients>) rcRepository.findAll();

            model.addAttribute("data2", creport);

            return "/beekeeper/admin/reported-clients";
        }
        else {
            return "redirect:/BeeKeeper/AdminLogin";
        }
    }

    @GetMapping(path="/banUser2")
    public String banUser2(Model model, @RequestParam("userID")String uid, @RequestParam("reportID")String rpid){
        Integer userId = Integer.parseInt(uid);
        Integer reportId = Integer.parseInt(rpid);
        System.out.println("hello" + userId);

        if(clientRepository.findById(userId).isPresent()) {
            Client client = clientRepository.findById(userId).get();
            client.setBanned(true);
            clientRepository.save(client);

            List<ReportedClients> R_report = (List<ReportedClients>) rcRepository.findAll();

            for (ReportedClients report : R_report) {
                Integer x = report.getReport2ID();
                if(Objects.equals(report.getComplainee2ID(), userId)){
                    rcRepository.deleteById(x);
                }
            }
        }

        return "redirect:/BeeKeeper/ReportedClients";
    }

    @GetMapping(path="/tempBanUser2")
    public String tempBanUser2(Model model, @RequestParam("userID")String uid, @RequestParam("reportID")String rpid){
        Integer userId = Integer.parseInt(uid);
        Integer reportId = Integer.parseInt(rpid);

        if(clientRepository.findById(userId).isPresent()) {
            Client client = clientRepository.findById(userId).get();
            LocalDate currentDate = LocalDate.now();
            client.setDateBanned(currentDate.toString());
            clientRepository.save(client);
            rcRepository.deleteById(reportId);
        }

        return "redirect:/BeeKeeper/ReportedClients";
    }

    @GetMapping(path="/dismissReport2")
    public String dismissReport2(Model model, @RequestParam("reportID")String rpid){
        Integer reportId = Integer.parseInt(rpid);
        if(rcRepository.findById(reportId).isPresent())
        {
            rcRepository.deleteById(reportId);
        }
        return "redirect:/BeeKeeper/ReportedClients";
    }

    @GetMapping(path = "/ReportedBusiness")
    public String ReportedBusiness(Model model, HttpServletRequest request ){

        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("adminid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
            }
        }
        if(!cookieValueid.isEmpty()) {
            List<ReportedBusiness> breport = (List<ReportedBusiness>) rbRepository.findAll();

            model.addAttribute("data3", breport);

            return "/beekeeper/admin/reported-business";
        }
        else {
            return "redirect:/BeeKeeper/AdminLogin";
        }
    }

    @GetMapping(path="/banUser3")
    public String banUser3(Model model, @RequestParam("userID")String uid, @RequestParam("reportID")String rpid){
        Integer userId = Integer.parseInt(uid);
        Integer reportId = Integer.parseInt(rpid);
        System.out.println("hello" + userId);

        if(businessRepository.findById(userId).isPresent()) {
            Business business = businessRepository.findById(userId).get();
            business.setBanned(true);
            businessRepository.save(business);

            List<ReportedBusiness> R_report = (List<ReportedBusiness>) rbRepository.findAll();

            for (ReportedBusiness report : R_report) {
                Integer x = report.getReport3ID();
                if(Objects.equals(report.getComplainee3ID(), userId)){
                    rbRepository.deleteById(x);
                }
            }
        }

        return "redirect:/BeeKeeper/ReportedBusiness";
    }

    @GetMapping(path="/tempBanUser3")
    public String tempBanUser3(Model model, @RequestParam("userID")String uid, @RequestParam("reportID")String rpid){
        Integer userId = Integer.parseInt(uid);
        Integer reportId = Integer.parseInt(rpid);

        if(businessRepository.findById(userId).isPresent()) {
            Business business = businessRepository.findById(userId).get();
            LocalDate currentDate = LocalDate.now();
            business.setDateBanned(currentDate.toString());
            businessRepository.save(business);
            rbRepository.deleteById(reportId);
        }

        return "redirect:/BeeKeeper/ReportedBusiness";
    }

    @GetMapping(path="/dismissReport3")
    public String dismissReport3(Model model, @RequestParam("reportID")String rpid){
        Integer reportId = Integer.parseInt(rpid);
        if(rbRepository.findById(reportId).isPresent())
        {
            rbRepository.deleteById(reportId);
        }
        return "redirect:/BeeKeeper/ReportedBusiness";
    }

    @GetMapping(path="/AdminLogin")
    public String AdminLogin(@RequestParam(name = "error", required = false) String error, Model model){

        LoginData loginData = new LoginData();
        model.addAttribute("loginData", loginData);

        return "/beekeeper/index";
    }

    @GetMapping(path = "/contact")
    public String contactPage(Model model,HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("adminid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
            }
        }
        if(!cookieValueid.isEmpty()) {
            Iterable<Contact> obj= contactRepository.findAll();
            List<ContactCard> contacts=new ArrayList<>();
            obj.forEach((object)->{
                ContactCard contact=new ContactCard();
                contact.contactSubject=object.getSubject();
                contact.contactMessage=object.getMessage();
                contact.contactEmail=object.getEmail();
                contact.contactName=object.getName();
                contacts.add(contact);
            });
            Collections.shuffle(contacts);
            model.addAttribute("contacts",contacts);
            return "/beekeeper/admin/contacts";
        }
        else {
            return "redirect:/BeeKeeper/AdminLogin";
        }
    }

    @PostMapping(path="/logindata")
    public String checklogin(@ModelAttribute("loginData") LoginData loginData, Model model, HttpServletResponse response)
    {
       /* if (admin2Repository.findByAdminEmailAndAdminPassword(loginData.email, loginData.password) != null) {
            Admin2 admin = admin2Repository.findByAdminEmailAndAdminPassword(loginData.email, loginData.password);

            Cookie userIdCookie = new Cookie("adminid", admin.getAdminID().toString());

            userIdCookie.setPath("/"); // Set the cookie's path (root path)


            response.addCookie(userIdCookie);
            //response.addCookie(userTypeCookie);

            return "redirect:/BeeKeeper/AdminDashboard";
        }*/

        Admin admin = Admin.getInstance();

        if (admin.getEmail().equals(loginData.email) && admin.getPassword().equals(loginData.getPassword())){
            Cookie userIdCookie = new Cookie("adminid", admin.getAdminID().toString());

            userIdCookie.setPath("/"); // Set the cookie's path (root path)


            response.addCookie(userIdCookie);
            //response.addCookie(userTypeCookie);

            return "redirect:/BeeKeeper/AdminDashboard";
        }

        return "redirect:/BeeKeeper/AdminLogin";
    }

}
