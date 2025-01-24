package com.example.honeybeehaven;
import com.example.honeybeehaven.classes.*;
import com.example.honeybeehaven.repositories.*;
import com.example.honeybeehaven.services.IdService;
import com.example.honeybeehaven.services.PidService;
import com.example.honeybeehaven.tables.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.http.HttpHeaders;

import javax.crypto.Mac;

@Controller
@RequestMapping(path = "/HoneyBeeHaven")
public class HoneyBeeHaven {
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
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private ReportedReviewRepository rrRepository;
    @Autowired
    private ReportedBusinessRepository rbRepository;
    @Autowired
    private ReportedClientRepository rcRepository;
    @Autowired
    private ServiceCartRepository serviceCartRepository;
    private String PATHS = "D:\\assignments\\ideaProject\\HoneyBeeHaven\\src\\main\\resources\\static\\";


    @GetMapping(path="/client_signup")
    public String customerSignupPage(@RequestParam(name = "error", required = false) String error, Model model){
        model.addAttribute("max", Integer.valueOf(LocalDate.now().getYear() - 18).toString() + "-01-01");
        if (error == null) {
            ClientSignupForm clientSignupForm = new ClientSignupForm();
            model.addAttribute("clientSignupForm", clientSignupForm);
            return "/honeybeehaven/client/customer_signup";
        }
        else if (error.equals("1"))
        {
            ClientSignupForm clientSignupForm = new ClientSignupForm();
            model.addAttribute("clientSignupForm", clientSignupForm);
            model.addAttribute("error", 1);
            return "/honeybeehaven/client/customer_signup";
        }
        else if (error.equals("2"))
        {
            ClientSignupForm clientSignupForm = new ClientSignupForm();
            model.addAttribute("clientSignupForm", clientSignupForm);
            model.addAttribute("error", 2);
            return "/honeybeehaven/client/customer_signup";
        }
        else
        {
            ClientSignupForm clientSignupForm = new ClientSignupForm();
            model.addAttribute("clientSignupForm", clientSignupForm);
            model.addAttribute("error", 3);
            return "/honeybeehaven/client/customer_signup";
        }
    }

    @PostMapping(path="/submitClientSignup")
    public String clientsignup(@ModelAttribute("clientSignupForm") ClientSignupForm clientSignupForm, Model model, @RequestParam("file") MultipartFile file) throws IOException {
        Integer userid = idService.getNextId();

        System.out.println(userid);



        Client client = new Client();

        client.setAge(clientSignupForm.getAge());
        client.setAddress(clientSignupForm.getAddress());
        client.setGender(clientSignupForm.getGender());
        client.setName(clientSignupForm.getName());
        client.setPassword(clientSignupForm.getPassword());
        client.setEmail(clientSignupForm.getEmail());
        client.setUserid(userid);
        client.setPrimarylocation(clientSignupForm.primary);
        client.setDateBanned(null);
        client.setBanned(false);

        LocalDate currentDate = LocalDate.now();

        // Define a date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Format the current date as a string
        String formattedDate = currentDate.format(formatter);

        client.setDatejoined(formattedDate);

        if (!file.isEmpty()) {

            String uploadDirectory = PATHS + "images\\client\\";

            String originalFileName = file.getOriginalFilename();

            // Check the file extension
            if (originalFileName != null && originalFileName.lastIndexOf(".") != -1) {
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

                // Example: Check if the file extension is '.jpg' or '.jpeg'
                if (fileExtension.equalsIgnoreCase(".jpg") || fileExtension.equalsIgnoreCase(".jpeg") || fileExtension.equalsIgnoreCase(".png")) {
                    String fileName = userid.toString();

                    // Create the file object
                    File imageFile = new File(uploadDirectory + fileName + fileExtension);

                    // Save the file to the specified location
                    file.transferTo(imageFile);

                    // Save the file location as a string in the user's image attribute
                    String fileLocation = "/images/client/" + fileName + fileExtension; // Adjust the path as needed
                    // Save the file location in the user's image attribute (you'll need to retrieve the user)
                    client.setImage(fileLocation);

                } else {
                    return "redirect:/HoneyBeeHaven/client_signup?error=1";
                }
            } else {
                return "redirect:/HoneyBeeHaven/client_signup?error=1";
            }

        }
        else {
            client.setImage(null);
        }

        String numericRegex = ".*\\d.*";
        String uppercaseRegex = ".*[A-Z].*";

        // Create Pattern objects for the regular expressions
        Pattern numericPattern = Pattern.compile(numericRegex);
        Pattern uppercasePattern = Pattern.compile(uppercaseRegex);

        // Create Matcher objects to find matches
        Matcher numericMatcher = numericPattern.matcher(clientSignupForm.getPassword());
        Matcher uppercaseMatcher = uppercasePattern.matcher(clientSignupForm.getPassword());

        String emailRegex = ".*@.*\\..*";

        // Create a Pattern object for the regular expression
        Pattern pattern = Pattern.compile(emailRegex);

        // Create a Matcher object to find matches
        Matcher emailmatcher = pattern.matcher(clientSignupForm.email);

        // Check if the email matches the regular expression


        // Check if both conditions are met
        int success = 0;

        if (clientSignupForm.getPassword().length() >= 8 && numericMatcher.matches() && uppercaseMatcher.matches() &&
                emailmatcher.matches() && clientSignupForm.password.equals( clientSignupForm.confirmpassword))
        {

        }
        else{
            success = 1;
        }

        if (clientRepository.findByEmail(clientSignupForm.email) != null || businessRepository.findByEmail(clientSignupForm.email) != null)
        {
            success = 2;
        }

        if (success == 0)
        {
            clientRepository.save(client);

            return "redirect:/HoneyBeeHaven/login";
        }
        else if (success == 1)
        {
            return "redirect:/HoneyBeeHaven/client_signup?error=1";
        }
        else{
            return "redirect:/HoneyBeeHaven/client_signup?error=2";
        }
    }

    @GetMapping(path="/login")
    public String login(@RequestParam(name = "error", required = false) String error, Model model)
    {
        LoginData loginData = new LoginData();
        model.addAttribute("loginData", loginData);
        if (error == null)
        {}
        else if (error.equals("1"))
        {model.addAttribute("error", 1);}
        else if (error.equals("2"))
        {
            model.addAttribute("error", 2);
        }
        else{
            model.addAttribute("error", 3);
        }

        return "/honeybeehaven/home/login";
    }

    @PostMapping(path="/logindata")
    public String checklogin(@ModelAttribute("loginData") LoginData loginData, Model model, HttpServletResponse response)
    {
        if (clientRepository.findByEmailAndPassword(loginData.email, loginData.password) != null)
        {
            Client client = clientRepository.findByEmailAndPassword(loginData.email, loginData.password);

            if (client.getBanned()) {
                return "redirect:/HoneyBeeHaven/login?error=2";
            }
            else if (client.getDateBanned() != null){
                LocalDate providedDate = LocalDate.parse(client.getDateBanned(), DateTimeFormatter.ISO_LOCAL_DATE);

                // Get the current date
                LocalDate currentDate = LocalDate.now();

                // Calculate the difference in days
                int daysDifference = Math.abs((int) (currentDate.toEpochDay() - providedDate.toEpochDay()));

                if (daysDifference >= 30){
                    client.setDateBanned(null);
                    clientRepository.save(client);
                    Cookie userIdCookie = new Cookie("userid", client.getUserid().toString());

                    userIdCookie.setPath("/"); // Set the cookie's path (root path)

                    // Create and set the "usertype" cookie
                    Cookie userTypeCookie = new Cookie("usertype", "c");

                    userTypeCookie.setPath("/");

                    response.addCookie(userIdCookie);
                    response.addCookie(userTypeCookie);

                    return "redirect:/HoneyBeeHaven/clientdashboard";
                }
                else{
                    return "redirect:/HoneyBeeHaven/login?error=3";
                }
            }
            else {
                Cookie userIdCookie = new Cookie("userid", client.getUserid().toString());

                userIdCookie.setPath("/"); // Set the cookie's path (root path)

                // Create and set the "usertype" cookie
                Cookie userTypeCookie = new Cookie("usertype", "c");

                userTypeCookie.setPath("/");

                response.addCookie(userIdCookie);
                response.addCookie(userTypeCookie);

                return "redirect:/HoneyBeeHaven/clientdashboard";
            }

        }
        else if(businessRepository.findByEmailAndPassword(loginData.email, loginData.password) != null)
        {
            Business business = businessRepository.findByEmailAndPassword(loginData.email, loginData.password);

            if (business.getBanned()) {
                return "redirect:/HoneyBeeHaven/login?error=2";
            }
            else if (business.getDateBanned() != null){
                LocalDate providedDate = LocalDate.parse(business.getDateBanned(), DateTimeFormatter.ISO_LOCAL_DATE);

                // Get the current date
                LocalDate currentDate = LocalDate.now();

                // Calculate the difference in days
                int daysDifference = Math.abs((int) (currentDate.toEpochDay() - providedDate.toEpochDay()));

                if (daysDifference >= 30){
                    business.setDateBanned(null);
                    businessRepository.save(business);
                    Cookie userIdCookie = new Cookie("userid", business.getUserid().toString());

                    userIdCookie.setPath("/"); // Set the cookie's path (root path)
                    userIdCookie.setMaxAge(-1); // Set the cookie's path (root path)

                    // Create and set the "usertype" cookie
                    Cookie userTypeCookie = new Cookie("usertype", "b");

                    userTypeCookie.setPath("/");
                    userTypeCookie.setMaxAge(-1); // Set the cookie's path (root path)

                    response.addCookie(userIdCookie);
                    response.addCookie(userTypeCookie);

                    return "redirect:/HoneyBeeHaven/businessdashboard";
                }
                else{
                    return "redirect:/HoneyBeeHaven/login?error=3";
                }
            }
            else {
                Cookie userIdCookie = new Cookie("userid", business.getUserid().toString());

                userIdCookie.setPath("/"); // Set the cookie's path (root path)
                userIdCookie.setMaxAge(-1); // Set the cookie's path (root path)

                // Create and set the "usertype" cookie
                Cookie userTypeCookie = new Cookie("usertype", "b");

                userTypeCookie.setPath("/");
                userTypeCookie.setMaxAge(-1); // Set the cookie's path (root path)

                response.addCookie(userIdCookie);
                response.addCookie(userTypeCookie);

                return "redirect:/HoneyBeeHaven/businessdashboard";
            }
        }
        else{
            return "redirect:/HoneyBeeHaven/login?error=1";
        }
    }

    @GetMapping(path="/clientdashboard")
    public String clientdashboard(Model model, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        String cookieValuetype = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }

        ArrayList<MyProductCard> productCardsList=new ArrayList<>();
        Service servicesObj =new Service();
        Chemical chemcialObj =new Chemical();
        Machinery machineObj =new Machinery();
        Iterable<Service> objS=serviceRepository.findAll();
        Iterable<Chemical> objC=chemicalRepository.findAll();
        Iterable<Machinery> objM=machineryRepository.findAll();

        objS.forEach(service -> {
            if (service.getIsdeleted() != 1 && service.getIssponsored() == Boolean.TRUE) {
                productCardsList.add(new MyProductCard("Service",
                        service.getItemname(),
                        service.getItemPrice().toString(),
                        service.getItemid().toString(),
                        "Available",
                        service.getItemrating().toString(),
                        service.getImage(),
                        null,
                        null,
                        "/HoneyBeeHaven/serviceDesignPage?itemId="+service.getItemid().toString()
                ));
            }
        });

        objC.forEach(chemical -> {
            if (chemical.getIsdeleted() != 1 && chemical.getIssponsored() == Boolean.TRUE ) {
                productCardsList.add(new MyProductCard("Chemical",
                        chemical.getItemname(),
                        chemical.getItemPrice().toString(),
                        chemical.getItemid().toString(),
                        chemical.getQuantityinstock().toString(),
                        chemical.getItemrating().toString(),
                        chemical.getImage(),
                        null ,
                        null,
                        "/HoneyBeeHaven/chemicalDesignPage?itemId="+chemical.getItemid().toString()
                ));
            }
        });

        objM.forEach(machinery -> {
            if (machinery.getIsdeleted() != 1 && machinery.getIssponsored()== Boolean.TRUE) {
                productCardsList.add(new MyProductCard("Machinery",
                        machinery.getItemname(),
                        machinery.getItemPrice().toString(),
                        machinery.getItemid().toString(),
                        machinery.getQuantityinstock().toString(),
                        machinery.getItemrating().toString(),
                        machinery.getImage(),
                        null ,
                        null,
                        "/HoneyBeeHaven/machineryDesignPage?itemId="+machinery.getItemid().toString()
                ));
            }
        });

        Collections.shuffle(productCardsList);
        ArrayList<MyProductCard> finalProductCardsList=new ArrayList<>();
        for (int i=0;i<productCardsList.size() && i<8;i++){
            finalProductCardsList.add(productCardsList.get(i));
        }
        Collections.shuffle(finalProductCardsList);
        model.addAttribute("cards", finalProductCardsList);



        if (cookieValueid != null && cookieValuetype.equals("c"))
        {
            Client client = clientRepository.findById(Integer.parseInt(cookieValueid)).get();

            if (client.getBanned()) {
                Cookie userIdCookie = new Cookie("userid", "");

                userIdCookie.setPath("/"); // Set the cookie's path (root path)

                // Create and set the "usertype" cookie
                Cookie userTypeCookie = new Cookie("usertype","");

                userTypeCookie.setPath("/");

                response.addCookie(userIdCookie);
                response.addCookie(userTypeCookie);

                return "redirect:/HoneyBeeHaven/login?error=2";
            }
            else if (client.getDateBanned() != null){
                LocalDate providedDate = LocalDate.parse(client.getDateBanned(), DateTimeFormatter.ISO_LOCAL_DATE);

                // Get the current date
                LocalDate currentDate = LocalDate.now();

                // Calculate the difference in days
                int daysDifference = Math.abs((int) (currentDate.toEpochDay() - providedDate.toEpochDay()));

                if (daysDifference >= 30){
                    client.setDateBanned(null);
                    clientRepository.save(client);

                    model.addAttribute("userid", client.getUserid());
                    model.addAttribute("name", client.getName());
                    model.addAttribute("address", client.getAddress());
                    model.addAttribute("age", client.getAge());
                    model.addAttribute("gender", client.getGender());
                    model.addAttribute("email", client.getEmail());

                    if (client.getImage() == null)
                    {
                        model.addAttribute("image", "/images/client/avatar-01.jpg");
                    }
                    else{
                        model.addAttribute("image", client.getImage());
                    }





                    model.addAttribute("navbar",getNavbar(2,cookieValueid));

                    return "/honeybeehaven/client/clientdashboard";
                }
                else{
                    return "redirect:/HoneyBeeHaven/login?error=3";
                }
            }
            else {
                model.addAttribute("userid", client.getUserid());
                model.addAttribute("name", client.getName());
                model.addAttribute("address", client.getAddress());
                model.addAttribute("age", client.getAge());
                model.addAttribute("gender", client.getGender());
                model.addAttribute("email", client.getEmail());

                if (client.getImage() == null)
                {
                    model.addAttribute("image", "/images/client/avatar-01.jpg");
                }
                else{
                    model.addAttribute("image", client.getImage());
                }

                model.addAttribute("navbar",getNavbar(2,cookieValueid));
                return "/honeybeehaven/client/clientdashboard";
            }


        }
        else{
            return "redirect:/HoneyBeeHaven/login";
        }
    }

    @GetMapping(path="/business_signup")
    public String businessSignupPage(Model model, @RequestParam(name = "error", required = false) String error)
    {
        List<Keyword> keywords = Arrays.asList(
                new Keyword("agricultural chemicals", false),
                new Keyword("farm machinery", false),
                new Keyword("crop services", false),
                new Keyword("agrochemical solutions", false),
                new Keyword("precision farming equipment", false),
                new Keyword("agricultural technology", false),
                new Keyword("fertilizer application", false),
                new Keyword( "pest control services", false),
                new Keyword( "irrigation systems", false),
                new Keyword("agricultural equipment maintenance", false)
        );

        model.addAttribute("keywords", keywords);

        if (error == null) {
            BusinessSignupForm businessSignupForm = new BusinessSignupForm();
            model.addAttribute("businessSignupForm", businessSignupForm);
            return "/honeybeehaven/business/business_signup";
        }
        else if (error.equals("1"))
        {
            BusinessSignupForm businessSignupForm = new BusinessSignupForm();
            model.addAttribute("businessSignupForm", businessSignupForm);
            model.addAttribute("error", 1);
            return "/honeybeehaven/business/business_signup";
        }
        else if (error.equals("2"))
        {
            BusinessSignupForm businessSignupForm = new BusinessSignupForm();
            model.addAttribute("businessSignupForm", businessSignupForm);
            model.addAttribute("error", 2);
            return "/honeybeehaven/business/business_signup";
        }
        else
        {
            BusinessSignupForm businessSignupForm = new BusinessSignupForm();
            model.addAttribute("businessSignupForm", businessSignupForm);
            model.addAttribute("error", 3);
            return "/honeybeehaven/business/business_signup";
        }
    }

    @PostMapping(path="/submitBusinessSignup")
    public String businessSignup(@RequestParam(name = "selectedKeywords", required = false) List<String> selectedKeywords, @ModelAttribute("businessSignupForm") BusinessSignupForm businessSignupForm, Model model, @RequestParam("file") MultipartFile file) throws IOException {
        Integer userid = idService.getNextId();

        System.out.println(userid);

        List<String> keywordValues = new ArrayList<>();
        for (String keyword : selectedKeywords) {
            keywordValues.add(keyword);
        }

        String concatenatedKeywords = String.join(" / ", keywordValues);

        Business business = new Business();
        
        business.setAddress(businessSignupForm.getAddress());
        business.setName(businessSignupForm.getName());
        business.setPassword(businessSignupForm.getPassword());
        business.setEmail(businessSignupForm.getEmail());
        business.setUserid(userid);
        business.setBankaccountnumber(businessSignupForm.getBankaccountnumber());
        business.setBusinessdescription(businessSignupForm.businessDescription);
        business.setContactinfo(businessSignupForm.contactInfo);
        business.setBusinessname(businessSignupForm.businessName);
        business.setTargetkeywords(concatenatedKeywords);
        business.setPrimarylocation(businessSignupForm.primary);
        business.setDateBanned(null);
        business.setBanned(false);

        LocalDate currentDate = LocalDate.now();

        // Define a date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Format the current date as a string
        String formattedDate = currentDate.format(formatter);

        business.setDatejoined(formattedDate);

        if (!file.isEmpty()) {

            String uploadDirectory = PATHS + "images\\business\\";

            String originalFileName = file.getOriginalFilename();

            // Check the file extension
            if (originalFileName != null && originalFileName.lastIndexOf(".") != -1) {
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

                // Example: Check if the file extension is '.jpg' or '.jpeg'
                if (fileExtension.equalsIgnoreCase(".jpg") || fileExtension.equalsIgnoreCase(".jpeg") || fileExtension.equalsIgnoreCase(".png")) {
                    String fileName = userid.toString();

                    // Create the file object
                    File imageFile = new File(uploadDirectory + fileName + fileExtension);

                    // Save the file to the specified location
                    file.transferTo(imageFile);

                    // Save the file location as a string in the user's image attribute
                    String fileLocation = "/images/business/" + fileName + fileExtension; // Adjust the path as needed
                    // Save the file location in the user's image attribute (you'll need to retrieve the user)
                    business.setImage(fileLocation);

                } else {
                    return "redirect:/HoneyBeeHaven/business_signup?error=1";
                }
            } else {
                return "redirect:/HoneyBeeHaven/business_signup?error=1";
            }

        }
        else {
            business.setImage(null);
        }

        String numericRegex = ".*\\d.*";
        String uppercaseRegex = ".*[A-Z].*";

        // Create Pattern objects for the regular expressions
        Pattern numericPattern = Pattern.compile(numericRegex);
        Pattern uppercasePattern = Pattern.compile(uppercaseRegex);

        // Create Matcher objects to find matches
        Matcher numericMatcher = numericPattern.matcher(businessSignupForm.getPassword());
        Matcher uppercaseMatcher = uppercasePattern.matcher(businessSignupForm.getPassword());

        String emailRegex = ".*@.*\\..*";

        // Create a Pattern object for the regular expression
        Pattern pattern = Pattern.compile(emailRegex);

        // Create a Matcher object to find matches
        Matcher emailmatcher = pattern.matcher(businessSignupForm.email);
        business.setBusinessrating((float)0.0);

        // Check if the email matches the regular expression

        // Check if both conditions are met
        int success = 0;

        if (selectedKeywords.size() >= 2 && selectedKeywords.size() <= 5 && businessSignupForm.getPassword().length() >= 8 && numericMatcher.matches() && uppercaseMatcher.matches() && emailmatcher.matches() && businessSignupForm.password.equals( businessSignupForm.confirmpassword) && businessSignupForm.contactInfo.matches("^03\\d{9}$") && businessSignupForm.bankaccountnumber.matches("^03\\d{9}$"))
        {

        }
        else{
            success = 1;
        }

        if (clientRepository.findByEmail(businessSignupForm.email) != null || businessRepository.findByEmail(businessSignupForm.email) != null)
        {
            success = 2;
        }

        if (success == 0)
        {
            businessRepository.save(business);

            return "redirect:/HoneyBeeHaven/login";
        }
        else if (success == 1)
        {
            return "redirect:/HoneyBeeHaven/business_signup?error=1";
        }
        else{
            return "redirect:/HoneyBeeHaven/business_signup?error=2";
        }
    }

    @GetMapping(path="logout")
    public String logout( @ModelAttribute("loginData") LoginData loginData, Model model, HttpServletResponse response)
    {
        Cookie userIdCookie = new Cookie("userid", "");

        userIdCookie.setPath("/"); // Set the cookie's path (root path)

        // Create and set the "usertype" cookie
        Cookie userTypeCookie = new Cookie("usertype","");

        userTypeCookie.setPath("/");

        response.addCookie(userIdCookie);
        response.addCookie(userTypeCookie);

        return "/honeybeehaven/home/login";
    }

    @GetMapping(path="/businessdashboard")
    public String businessdashboard(Model model, HttpServletRequest request, HttpServletResponse response)
    {
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        String cookieValuetype = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }

        if (cookieValueid != null && cookieValuetype.equals("b"))
        {
            Business business = businessRepository.findById(Integer.parseInt(cookieValueid)).get();



            if (business.getBanned()) {
                Cookie userIdCookie = new Cookie("userid", "");

                userIdCookie.setPath("/"); // Set the cookie's path (root path)

                // Create and set the "usertype" cookie
                Cookie userTypeCookie = new Cookie("usertype","");

                userTypeCookie.setPath("/");

                response.addCookie(userIdCookie);
                response.addCookie(userTypeCookie);

                return "redirect:/HoneyBeeHaven/login?error=2";
            }
            else if (business.getDateBanned() != null){
                LocalDate providedDate = LocalDate.parse(business.getDateBanned(), DateTimeFormatter.ISO_LOCAL_DATE);

                // Get the current date
                LocalDate currentDate = LocalDate.now();

                // Calculate the difference in days
                int daysDifference = Math.abs((int) (currentDate.toEpochDay() - providedDate.toEpochDay()));

                if (daysDifference >= 30){
                    business.setDateBanned(null);
                    businessRepository.save(business);

                    model.addAttribute("userid", business.getUserid());
                    model.addAttribute("businessname", business.getBusinessname());
                    model.addAttribute("address", business.getAddress());
                    model.addAttribute("email", business.getEmail());
                    model.addAttribute("rating", business.getBusinessrating());
                    model.addAttribute("account", business.getBankaccountnumber());
                    model.addAttribute("description", business.getBusinessdescription());
                    model.addAttribute("contact", business.getContactinfo());

                    String keywords = business.getTargetkeywords();
                    String replacedString = keywords.replace("/", "/");

                    model.addAttribute("keywords", replacedString);
                    model.addAttribute("name", business.getName());

                    if (business.getImage() == null)
                    {
                        model.addAttribute("image", "/images/business/avatar-01.jpg");
                    }
                    else{
                        model.addAttribute("image", business.getImage());
                    }

                    Integer param=-1;
                    if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("c")){
                        return "redirect:/HoneyBeeHaven/clientdasboard";
                    }else if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("b")){
                        param=3;
                    }
                    model.addAttribute("navbar", getNavbar(param,cookieValueid));
                    return "/honeybeehaven/business/businessDashboard";
                }
                else{
                    return "redirect:/HoneyBeeHaven/login?error=3";
                }
            }
            else {
                model.addAttribute("userid", business.getUserid());
                model.addAttribute("businessname", business.getBusinessname());
                model.addAttribute("address", business.getAddress());
                model.addAttribute("email", business.getEmail());
                model.addAttribute("rating", business.getBusinessrating());
                model.addAttribute("account", business.getBankaccountnumber());
                model.addAttribute("description", business.getBusinessdescription());
                model.addAttribute("contact", business.getContactinfo());

                String keywords = business.getTargetkeywords();
                String replacedString = keywords.replace("/", "/");

                model.addAttribute("keywords", replacedString);
                model.addAttribute("name", business.getName());

                if (business.getImage() == null)
                {
                    model.addAttribute("image", "/images/business/avatar-01.jpg");
                }
                else{
                    model.addAttribute("image", business.getImage());
                }

                Integer param=-1;
                if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("c")){
                    return "redirect:/HoneyBeeHaven/clientdasboard";
                }else if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("b")){
                    param=3;
                }
                model.addAttribute("navbar", getNavbar(param,cookieValueid));
                return "/honeybeehaven/business/businessDashboard";
            }
        }
        else{
            return "redirect:/HoneyBeeHaven/login";
        }
    }

    @GetMapping(path="/edit_client")
    public String edit_client(Model model, @RequestParam(name = "error", required = false) String error, HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        String cookieValuetype = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }

        if (cookieValueid != null && cookieValuetype.equals("c"))
        {
            ClientSignupForm clientSignupForm = new ClientSignupForm();

            model.addAttribute("navbar", getNavbar(2,cookieValueid));

            Client client = clientRepository.findById(Integer.parseInt(cookieValueid)).get();

            if (client.getBanned()) {
                Cookie userIdCookie = new Cookie("userid", "");

                userIdCookie.setPath("/"); // Set the cookie's path (root path)

                // Create and set the "usertype" cookie
                Cookie userTypeCookie = new Cookie("usertype","");

                userTypeCookie.setPath("/");

                response.addCookie(userIdCookie);
                response.addCookie(userTypeCookie);

                return "redirect:/HoneyBeeHaven/login?error=2";
            }
            else if (client.getDateBanned() != null){
                LocalDate providedDate = LocalDate.parse(client.getDateBanned(), DateTimeFormatter.ISO_LOCAL_DATE);

                // Get the current date
                LocalDate currentDate = LocalDate.now();

                // Calculate the difference in days
                int daysDifference = Math.abs((int) (currentDate.toEpochDay() - providedDate.toEpochDay()));

                if (daysDifference >= 30){
                    client.setDateBanned(null);
                    clientRepository.save(client);

                    clientSignupForm.name = client.getName();
                    model.addAttribute("name", clientSignupForm.name);
                    model.addAttribute("email", client.getEmail());
                    clientSignupForm.address = client.getAddress();
                    clientSignupForm.age = client.getAge();


                    model.addAttribute("max", Integer.valueOf(LocalDate.now().getYear() - 18).toString() + "-01-01");

                    model.addAttribute(clientSignupForm);

                    if (client.getImage() == null)
                    {
                        model.addAttribute("image", "/images/business/avatar-01.jpg");
                    }
                    else{
                        model.addAttribute("image", client.getImage());
                    }
                    if (error == null) {
                        return "/honeybeehaven/client/edit_client";
                    }
                    else if (error.equals("1"))
                    {
                        model.addAttribute("error", 1);
                        return "/honeybeehaven/client/edit_client";
                    }
                    else if (error.equals("2"))
                    {
                        model.addAttribute("error", 2);
                        return "/honeybeehaven/client/edit_client";
                    }
                    else
                    {
                        model.addAttribute("error", 3);
                        return "/honeybeehaven/client/edit_client";
                    }
                }
                else{
                    return "redirect:/HoneyBeeHaven/login?error=3";
                }
            }
            else {
                clientSignupForm.name = client.getName();
                model.addAttribute("name", clientSignupForm.name);
                model.addAttribute("email", client.getEmail());
                clientSignupForm.address = client.getAddress();
                clientSignupForm.age = client.getAge();

                model.addAttribute("max", Integer.valueOf(LocalDate.now().getYear() - 18).toString() + "-01-01");

                model.addAttribute(clientSignupForm);

                if (client.getImage() == null)
                {
                    model.addAttribute("image", "/images/business/avatar-01.jpg");
                }
                else{
                    model.addAttribute("image", client.getImage());
                }
                if (error == null) {
                    return "/honeybeehaven/client/edit_client";
                }
                else if (error.equals("1"))
                {
                    model.addAttribute("error", 1);
                    return "/honeybeehaven/client/edit_client";
                }
                else if (error.equals("2"))
                {
                    model.addAttribute("error", 2);
                    return "/honeybeehaven/client/edit_client";
                }
                else
                {
                    model.addAttribute("error", 3);
                    return "/honeybeehaven/client/edit_client";
                }
            }
        }
        else{
            return "redirect:/HoneyBeeHaven/login";
        }
    }

    @PostMapping(path="/editclient")
    public String editclient(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("clientSignupForm") ClientSignupForm clientSignupForm, Model model, @RequestParam("file") MultipartFile file) throws IOException {

        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        String cookieValuetype = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }

        Integer userid = Integer.parseInt(cookieValueid);

        List<String> keywordValues = new ArrayList<>();

        String concatenatedKeywords = String.join(" / ", keywordValues);

        Client client = clientRepository.findById(userid).get();


        String numericRegex = ".*\\d.*";
        String uppercaseRegex = ".*[A-Z].*";

        // Create Pattern objects for the regular expressions
        Pattern numericPattern = Pattern.compile(numericRegex);
        Pattern uppercasePattern = Pattern.compile(uppercaseRegex);

        // Create Matcher objects to find matches
        Matcher numericMatcher = numericPattern.matcher(clientSignupForm.getPassword());
        Matcher uppercaseMatcher = uppercasePattern.matcher(clientSignupForm.getPassword());


        // Check if the email matches the regular expression


        // Check if both conditions are met
        int success = 0;

        if ((clientSignupForm.password.equals("") || (clientSignupForm.getPassword().length() >= 8 && numericMatcher.matches() && uppercaseMatcher.matches() && clientSignupForm.password.equals( clientSignupForm.confirmpassword))) && clientSignupForm.checkpassword.equals(client.getPassword()))
        {

        }
        else{
            success = 1;
        }

        if (success == 0)
        {
            client.setAddress(clientSignupForm.getAddress());
            client.setAge(clientSignupForm.getAge());
            client.setGender(clientSignupForm.getGender());
            client.setName(clientSignupForm.getName());
            client.setPrimarylocation(clientSignupForm.primary);

            if (!clientSignupForm.password.equals("")) {
                client.setPassword(clientSignupForm.getPassword());
            }


            if (!file.isEmpty()) {

                String uploadDirectory = PATHS + "images\\business\\";

                String originalFileName = file.getOriginalFilename();

                // Check the file extension
                if (originalFileName != null && originalFileName.lastIndexOf(".") != -1) {
                    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

                    // Example: Check if the file extension is '.jpg' or '.jpeg'
                    if (fileExtension.equalsIgnoreCase(".jpg") || fileExtension.equalsIgnoreCase(".jpeg") || fileExtension.equalsIgnoreCase(".png")) {
                        String fileName = userid.toString();

                        // Create the file object
                        File imageFile = new File(uploadDirectory + fileName + fileExtension);

                        // Save the file to the specified location
                        file.transferTo(imageFile);

                        // Save the file location as a string in the user's image attribute
                        String fileLocation = "/images/business/" + fileName + fileExtension; // Adjust the path as needed
                        // Save the file location in the user's image attribute (you'll need to retrieve the user)
                        client.setImage(fileLocation);

                    } else {
                        return "redirect:/HoneyBeeHaven/edit_client?error=1";
                    }
                } else {
                    return "redirect:/HoneyBeeHaven/edit_client?error=1";
                }

            }

            clientRepository.save(client);

            return "redirect:/HoneyBeeHaven/clientdashboard";
        }
        else if (success == 1)
        {
            return "redirect:/HoneyBeeHaven/edit_client?error=1";
        }
        else{
            return "redirect:/HoneyBeeHaven/edit_client?error=2";
        }
    }

    @GetMapping(path="/edit_business")
    public String edit_business(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "error", required = false) String error){

        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        String cookieValuetype = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }

        if (cookieValueid != null && cookieValuetype.equals("b"))
        {
            BusinessSignupForm businessSignupForm = new BusinessSignupForm();

            model.addAttribute(businessSignupForm);

            Business business = businessRepository.findById(Integer.parseInt(cookieValueid)).get();

            model.addAttribute("navbar", getNavbar(3, cookieValueid));
            model.addAttribute("businessname", business.getBusinessname());
            businessSignupForm.businessName = business.getBusinessname();

            businessSignupForm.address = business.getAddress();
            businessSignupForm.email = business.getEmail();
            businessSignupForm.bankaccountnumber = business.getBankaccountnumber();
            businessSignupForm.businessDescription = business.getBusinessdescription();
            businessSignupForm.contactInfo = business.getContactinfo();
            model.addAttribute("name", business.getName());
            businessSignupForm.name = business.getName();

            String words = business.getTargetkeywords();

            List<Keyword> keywords = Arrays.asList(
                    new Keyword("agricultural chemicals", words.contains("agricultural chemicals")),
                    new Keyword("farm machinery", words.contains("farm machinery")),
                    new Keyword("crop services", words.contains("crop services")),
                    new Keyword("agrochemical solutions", words.contains("agrochemical solutions")),
                    new Keyword("precision farming equipment", words.contains("precision farming equipment")),
                    new Keyword("agricultural technology", words.contains("agricultural technology")),
                    new Keyword("fertilizer application", words.contains("fertilizer application")),
                    new Keyword( "pest control services", words.contains("pest control services")),
                    new Keyword( "irrigation systems", words.contains("irrigation systems")),
                    new Keyword("agricultural equipment maintenance", words.contains("agricultural equipment maintenance"))
            );

            model.addAttribute("keywords", keywords);

            if (business.getImage() == null)
            {
                model.addAttribute("image", "/images/business/avatar-01.jpg");
            }
            else{
                model.addAttribute("image", business.getImage());
            }
            if (error == null) {
                return "/honeybeehaven/business/edit_bussiness";
            }
            else if (error.equals("1"))
            {
                model.addAttribute("error", 1);
                return "/honeybeehaven/business/edit_bussiness";
            }
            else if (error.equals("2"))
            {
                model.addAttribute("error", 2);
                return "/honeybeehaven/business/edit_bussiness";
            }
            else
            {
                model.addAttribute("error", 3);
                return "/honeybeehaven/business/edit_bussiness";
            }


        }
        else{
            return "redirect:/HoneyBeeHaven/login";
        }
    }

    @PostMapping(path="/editbusiness")
    public String editbusiness(HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "selectedKeywords", required = false) List<String> selectedKeywords, @ModelAttribute("businessSignupForm") BusinessSignupForm businessSignupForm, Model model, @RequestParam("file") MultipartFile file) throws IOException {

        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        String cookieValuetype = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }

        Integer userid = Integer.parseInt(cookieValueid);

        List<String> keywordValues = new ArrayList<>();
        for (String keyword : selectedKeywords) {
            keywordValues.add(keyword);
        }

        String concatenatedKeywords = String.join(" / ", keywordValues);

        Business business = businessRepository.findById(userid).get();


        String numericRegex = ".*\\d.*";
        String uppercaseRegex = ".*[A-Z].*";

        // Create Pattern objects for the regular expressions
        Pattern numericPattern = Pattern.compile(numericRegex);
        Pattern uppercasePattern = Pattern.compile(uppercaseRegex);

        // Create Matcher objects to find matches
        Matcher numericMatcher = numericPattern.matcher(businessSignupForm.getPassword());
        Matcher uppercaseMatcher = uppercasePattern.matcher(businessSignupForm.getPassword());


        // Check if the email matches the regular expression


        // Check if both conditions are met
        int success = 0;

        if (selectedKeywords.size() >= 2 && selectedKeywords.size() <= 5 && (businessSignupForm.password.equals("") || (businessSignupForm.getPassword().length() >= 8 && numericMatcher.matches() && uppercaseMatcher.matches() && businessSignupForm.password.equals( businessSignupForm.confirmpassword))) && businessSignupForm.checkpassword.equals(business.getPassword()))
        {

        }
        else{
            success = 1;
        }

        if (success == 0)
        {
            business.setAddress(businessSignupForm.getAddress());
            business.setName(businessSignupForm.getName());
            if (!businessSignupForm.password.equals("")) {
                business.setPassword(businessSignupForm.getPassword());
            }
            business.setBankaccountnumber(businessSignupForm.getBankaccountnumber());
            business.setBusinessdescription(businessSignupForm.businessDescription);
            business.setContactinfo(businessSignupForm.contactInfo);
            business.setBusinessname(businessSignupForm.businessName);
            business.setTargetkeywords(concatenatedKeywords);
            business.setPrimarylocation(businessSignupForm.primary);

            if (!file.isEmpty()) {

                String uploadDirectory = PATHS + "images\\business\\";

                String originalFileName = file.getOriginalFilename();

                // Check the file extension
                if (originalFileName != null && originalFileName.lastIndexOf(".") != -1) {
                    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

                    // Example: Check if the file extension is '.jpg' or '.jpeg'
                    if (fileExtension.equalsIgnoreCase(".jpg") || fileExtension.equalsIgnoreCase(".jpeg") || fileExtension.equalsIgnoreCase(".png")) {
                        String fileName = userid.toString();

                        // Create the file object
                        File imageFile = new File(uploadDirectory + fileName + fileExtension);

                        // Save the file to the specified location
                        file.transferTo(imageFile);

                        // Save the file location as a string in the user's image attribute
                        String fileLocation = "/images/business/" + fileName + fileExtension; // Adjust the path as needed
                        // Save the file location in the user's image attribute (you'll need to retrieve the user)
                        business.setImage(fileLocation);

                    } else {
                        return "redirect:/HoneyBeeHaven/edit_business?error=1";
                    }
                } else {
                    return "redirect:/HoneyBeeHaven/edit_business?error=1";
                }

            }

            businessRepository.save(business);

            return "redirect:/HoneyBeeHaven/businessdashboard";
        }
        else if (success == 1)
        {
            return "redirect:/HoneyBeeHaven/edit_business?error=1";
        }
        else{
            return "redirect:/HoneyBeeHaven/edit_businenss?error=2";
        }
    }

    @GetMapping(path="view_business")
    public String viewbusiness(@RequestParam(name = "userid", required = true) Integer userid, Model model, HttpServletRequest request)
    {
        if (businessRepository.findById(userid).isPresent()){
            Business business = businessRepository.findById(userid).get();
            model.addAttribute("businessname", business.getBusinessname());
            model.addAttribute("name", business.getName());
            model.addAttribute("contact", business.getContactinfo());
            model.addAttribute("keywords", business.getTargetkeywords());
            model.addAttribute("address", business.getAddress());
            model.addAttribute("email", business.getEmail());
            model.addAttribute("description", business.getBusinessdescription());
            model.addAttribute("rating", business.getBusinessrating());
            model.addAttribute("date", business.getDatejoined());
            model.addAttribute("primary", business.getPrimarylocation());
            model.addAttribute("userid", business.getUserid());

            if (business.getImage() == null){
                model.addAttribute("image", "/images/business/avatar-01.jpg");
            }
            else  {
                model.addAttribute("image", business.getImage());
            }
            Cookie[] cookies = request.getCookies();
            String cookieValueid = null,cookieValuetype=null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("usertype".equals(cookie.getName())) {
                        // You've found the cookie with the specific name
                        cookieValueid = cookie.getValue();
                    }
                    if ("usertype".equals(cookie.getName())) {
                        // You've found the cookie with the specific name
                        cookieValuetype = cookie.getValue();
                    }
                }
            }
            if (cookieValueid == null || cookieValueid.equals("c")){
                model.addAttribute("enable", 1);
            }
            Integer param=1;
            if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("c")){
                param=2;
            }else if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("b")){
                param=3;
            }
            model.addAttribute("navbar", getNavbar(param,cookieValueid));
        }
        return "/honeybeehaven/shared/ViewBuisnessProfile";
    }

    @GetMapping(path="view_client")
    public String view_client(@RequestParam(name = "userid", required = true) Integer userid, Model model, HttpServletRequest request)
    {
        if (clientRepository.findById(userid).isPresent()){
            Client client = clientRepository.findById(userid).get();
            model.addAttribute("name", client.getName());
            model.addAttribute("address", client.getAddress());
            model.addAttribute("email", client.getEmail());
            model.addAttribute("gender", client.getGender());
            model.addAttribute("age", client.getAge());
            model.addAttribute("date", client.getDatejoined());
            model.addAttribute("userid", client.getUserid());
            model.addAttribute("loc", client.getPrimarylocation());

            if (client.getImage() == null)
            {
                model.addAttribute("image", "/images/client/avatar-01.jpg");
            }
            else
            {
                model.addAttribute("image", client.getImage());
            }
            Cookie[] cookies = request.getCookies();
            String cookieValueid = null,cookieValuetype=null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("usertype".equals(cookie.getName())) {
                        cookieValueid = cookie.getValue();
                    }
                    if ("usertype".equals(cookie.getName())) {
                        cookieValuetype = cookie.getValue();
                    }
                }
            }

            if (cookieValueid == null || cookieValueid.equals("b")){
                model.addAttribute("enable", 1);
            }
            Integer param=1;
            if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("c")){
                param=2;
            }else if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("b")){
                param=3;
            }
            model.addAttribute("navbar", getNavbar(param,cookieValueid));
        }

        return "/honeybeehaven/shared/ViewClientProfile";
    }

    @GetMapping(path="myProducts")
    public String myProducts(Model model, HttpServletRequest request, HttpServletResponse response)
    {
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        String cookieValuetype = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    cookieValuetype = cookie.getValue();
                }
            }
        }

        if (cookieValueid != null && cookieValuetype.equals("b"))
        {
            Business business = businessRepository.findById(Integer.parseInt(cookieValueid)).get();

            if (business.getBanned()) {
                Cookie userIdCookie = new Cookie("userid", "");

                userIdCookie.setPath("/"); // Set the cookie's path (root path)

                // Create and set the "usertype" cookie
                Cookie userTypeCookie = new Cookie("usertype","");

                userTypeCookie.setPath("/");

                response.addCookie(userIdCookie);
                response.addCookie(userTypeCookie);

                return "redirect:/HoneyBeeHaven/login?error=2";
            }
            else if (business.getDateBanned() != null){
                LocalDate providedDate = LocalDate.parse(business.getDateBanned(), DateTimeFormatter.ISO_LOCAL_DATE);

                // Get the current date
                LocalDate currentDate = LocalDate.now();

                // Calculate the difference in days
                int daysDifference = Math.abs((int) (currentDate.toEpochDay() - providedDate.toEpochDay()));

                if (daysDifference >= 30){
                    business.setDateBanned(null);
                    businessRepository.save(business);

                    Integer businessId= Integer.parseInt(cookieValueid);
                    ArrayList<MyProductCard> productCardsList=new ArrayList<MyProductCard>();
                    Service servicesObj =new Service();
                    Chemical chemcialObj =new Chemical();
                    Machinery machineObj =new Machinery();

                    for (int i=0;i<serviceRepository.findAllByBusinessid(businessId).size();i++){
                        servicesObj=serviceRepository.findAllByBusinessid(businessId).get(i);
                        if (servicesObj.getIsdeleted() != 1) {
                            productCardsList.add(new MyProductCard("Service",
                                    servicesObj.getItemname(),
                                    servicesObj.getItemPrice().toString(),
                                    servicesObj.getItemid().toString(),
                                    "Available",
                                    servicesObj.getItemrating().toString(),
                                    servicesObj.getImage(),
                                    "/HoneyBeeHaven/editService?itemId="+servicesObj.getItemid().toString()
                                    ,"/HoneyBeeHaven/deleteService?itemId="+servicesObj.getItemid().toString(),
                                    "/HoneyBeeHaven/viewServicePage?itemId="+servicesObj.getItemid().toString()
                            ));
                        }
                    }

                    for (int i=0;i<chemicalRepository.findAllByBusinessid(businessId).size();i++){
                        chemcialObj=chemicalRepository.findAllByBusinessid(businessId).get(i);
                        if (chemcialObj.getIsdeleted() != 1) {
                            productCardsList.add(new MyProductCard("Chemical",
                                    chemcialObj.getItemname(),
                                    chemcialObj.getItemPrice().toString(),
                                    chemcialObj.getItemid().toString(),
                                    chemcialObj.getQuantityinstock().toString(),
                                    chemcialObj.getItemrating().toString(),
                                    chemcialObj.getImage(),
                                    "/HoneyBeeHaven/editChemical?itemId="+chemcialObj.getItemid().toString()
                                    ,"/HoneyBeeHaven/deleteChemical?itemId="+chemcialObj.getItemid().toString()
                                    ,"/HoneyBeeHaven/viewChemicalPage?itemId="+chemcialObj.getItemid().toString()
                            ));
                        }
                    }

                    for (int i=0;i<machineryRepository.findAllByBusinessid(businessId).size();i++){
                        machineObj=machineryRepository.findAllByBusinessid(businessId).get(i);
                        if (machineObj.getIsdeleted() != 1) {
                            productCardsList.add(new MyProductCard("Machinery",
                                    machineObj.getItemname(),
                                    machineObj.getItemPrice().toString(),
                                    machineObj.getItemid().toString(),
                                    machineObj.getQuantityinstock().toString(),
                                    machineObj.getItemrating().toString(),
                                    machineObj.getImage(),
                                    "/HoneyBeeHaven/editMachine?itemId="+machineObj.getItemid().toString()
                                    ,"/HoneyBeeHaven/deleteMachine?itemId="+machineObj.getItemid().toString()
                                    ,"/HoneyBeeHaven/viewMachinePage?itemId="+machineObj.getItemid().toString()
                            ));
                        }
                    }
                    model.addAttribute("cards", productCardsList);
                    model.addAttribute("navbar", getNavbar(3,cookieValueid));
                    Collections.shuffle(productCardsList);
                    return "/honeybeehaven/business/myProducts";
                }
                else{
                    return "redirect:/HoneyBeeHaven/login?error=3";
                }
            }
            else {
                Integer businessId= Integer.parseInt(cookieValueid);
                ArrayList<MyProductCard> productCardsList=new ArrayList<MyProductCard>();
                Service servicesObj =new Service();
                Chemical chemcialObj =new Chemical();
                Machinery machineObj =new Machinery();

                for (int i=0;i<serviceRepository.findAllByBusinessid(businessId).size();i++){
                    servicesObj=serviceRepository.findAllByBusinessid(businessId).get(i);
                    if (servicesObj.getIsdeleted() != 1) {
                        productCardsList.add(new MyProductCard("Service",
                                servicesObj.getItemname(),
                                servicesObj.getItemPrice().toString(),
                                servicesObj.getItemid().toString(),
                                "Available",
                                servicesObj.getItemrating().toString(),
                                servicesObj.getImage(),
                                "/HoneyBeeHaven/editService?itemId="+servicesObj.getItemid().toString()
                                ,"/HoneyBeeHaven/deleteService?itemId="+servicesObj.getItemid().toString(),
                                "/HoneyBeeHaven/viewServicePage?itemId="+servicesObj.getItemid().toString()
                        ));
                    }
                }

                for (int i=0;i<chemicalRepository.findAllByBusinessid(businessId).size();i++){
                    chemcialObj=chemicalRepository.findAllByBusinessid(businessId).get(i);
                    if (chemcialObj.getIsdeleted() != 1) {
                        productCardsList.add(new MyProductCard("Chemical",
                                chemcialObj.getItemname(),
                                chemcialObj.getItemPrice().toString(),
                                chemcialObj.getItemid().toString(),
                                chemcialObj.getQuantityinstock().toString(),
                                chemcialObj.getItemrating().toString(),
                                chemcialObj.getImage(),
                                "/HoneyBeeHaven/editChemical?itemId="+chemcialObj.getItemid().toString()
                                ,"/HoneyBeeHaven/deleteChemical?itemId="+chemcialObj.getItemid().toString()
                                ,"/HoneyBeeHaven/viewChemicalPage?itemId="+chemcialObj.getItemid().toString()
                        ));
                    }
                }

                for (int i=0;i<machineryRepository.findAllByBusinessid(businessId).size();i++){
                    machineObj=machineryRepository.findAllByBusinessid(businessId).get(i);
                    if (machineObj.getIsdeleted() != 1) {
                        productCardsList.add(new MyProductCard("Machinery",
                                machineObj.getItemname(),
                                machineObj.getItemPrice().toString(),
                                machineObj.getItemid().toString(),
                                machineObj.getQuantityinstock().toString(),
                                machineObj.getItemrating().toString(),
                                machineObj.getImage(),
                                "/HoneyBeeHaven/editMachine?itemId="+machineObj.getItemid().toString()
                                ,"/HoneyBeeHaven/deleteMachine?itemId="+machineObj.getItemid().toString()
                                ,"/HoneyBeeHaven/viewMachinePage?itemId="+machineObj.getItemid().toString()
                        ));
                    }
                }
                model.addAttribute("cards", productCardsList);
                model.addAttribute("navbar", getNavbar(3,cookieValueid));
                Collections.shuffle(productCardsList);

                return "/honeybeehaven/business/myProducts";
            }
        }
        else{
            return "redirect:/HoneyBeeHaven/login";
        }
    }

    @GetMapping(path="createItem")
    public String createItem(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "type", required = false) String type, @RequestParam(name = "error", required = false) String error)
    {
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        String cookieValuetype = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }

        if (cookieValuetype != null && cookieValuetype.equals("b")) {

            Business business = businessRepository.findById(Integer.parseInt(cookieValueid)).get();

            if (business.getBanned()) {
                Cookie userIdCookie = new Cookie("userid", "");

                userIdCookie.setPath("/"); // Set the cookie's path (root path)

                // Create and set the "usertype" cookie
                Cookie userTypeCookie = new Cookie("usertype","");

                userTypeCookie.setPath("/");

                response.addCookie(userIdCookie);
                response.addCookie(userTypeCookie);

                return "redirect:/HoneyBeeHaven/login?error=2";
            }
            else if (business.getDateBanned() != null){
                LocalDate providedDate = LocalDate.parse(business.getDateBanned(), DateTimeFormatter.ISO_LOCAL_DATE);

                // Get the current date
                LocalDate currentDate = LocalDate.now();

                // Calculate the difference in days
                int daysDifference = Math.abs((int) (currentDate.toEpochDay() - providedDate.toEpochDay()));

                if (daysDifference >= 30){
                    business.setDateBanned(null);
                    businessRepository.save(business);

                    Integer param = -1;
                    if (cookieValueid != null && !cookieValueid.isEmpty() && cookieValuetype.equals("c")) {
                        param = 2;
                    } else if (cookieValueid != null && !cookieValueid.isEmpty() && cookieValuetype.equals("b")) {
                        param = 3;
                    }
                    model.addAttribute("navbar", getNavbar(param, cookieValueid));


                    ItemForm itemForm = new ItemForm();
                    model.addAttribute("itemForm", itemForm);
                    if (type == null) {
                        return "/honeybeehaven/business/createItem";
                    } else if (type.equals("machine")) {
                        model.addAttribute("typer", "machine");

                        if (error == null) {

                        } else if (error.equals("1")) {
                            model.addAttribute("error", 1);
                        } else if (error.equals("2")) {
                            model.addAttribute("error", 2);
                        } else {
                            model.addAttribute("error", 3);
                        }

                        return "/honeybeehaven/business/createItem";

                    } else if (type.equals("service")) {
                        if (error == null) {

                        } else if (error.equals("1")) {
                            model.addAttribute("error", 1);
                        } else if (error.equals("2")) {
                            model.addAttribute("error", 2);
                        } else {
                            model.addAttribute("error", 3);
                        }

                        model.addAttribute("typer", "service");

                        return "/honeybeehaven/business/createItem";

                    } else if (type.equals("chemical")) {
                        if (error == null) {

                        } else if (error.equals("1")) {
                            model.addAttribute("error", 1);
                        } else if (error.equals("2")) {
                            model.addAttribute("error", 2);
                        } else {
                            model.addAttribute("error", 3);
                        }

                        model.addAttribute("typer", "chemical");

                        return "/honeybeehaven/business/createItem";

                    } else {

                        return "/honeybeehaven/business/createItem";
                    }
                }
                else{
                    return "redirect:/HoneyBeeHaven/login?error=3";
                }
            }
            else {
                Integer param = -1;
                if (cookieValueid != null && !cookieValueid.isEmpty() && cookieValuetype.equals("c")) {
                    param = 2;
                } else if (cookieValueid != null && !cookieValueid.isEmpty() && cookieValuetype.equals("b")) {
                    param = 3;
                }
                model.addAttribute("navbar", getNavbar(param, cookieValueid));


                ItemForm itemForm = new ItemForm();
                model.addAttribute("itemForm", itemForm);
                if (type == null) {
                    return "/honeybeehaven/business/createItem";
                } else if (type.equals("machine")) {
                    model.addAttribute("typer", "machine");

                    if (error == null) {

                    } else if (error.equals("1")) {
                        model.addAttribute("error", 1);
                    } else if (error.equals("2")) {
                        model.addAttribute("error", 2);
                    } else {
                        model.addAttribute("error", 3);
                    }

                    return "/honeybeehaven/business/createItem";

                } else if (type.equals("service")) {
                    if (error == null) {

                    } else if (error.equals("1")) {
                        model.addAttribute("error", 1);
                    } else if (error.equals("2")) {
                        model.addAttribute("error", 2);
                    } else {
                        model.addAttribute("error", 3);
                    }

                    model.addAttribute("typer", "service");

                    return "/honeybeehaven/business/createItem";

                } else if (type.equals("chemical")) {
                    if (error == null) {

                    } else if (error.equals("1")) {
                        model.addAttribute("error", 1);
                    } else if (error.equals("2")) {
                        model.addAttribute("error", 2);
                    } else {
                        model.addAttribute("error", 3);
                    }

                    model.addAttribute("typer", "chemical");

                    return "/honeybeehaven/business/createItem";

                } else {

                    return "/honeybeehaven/business/createItem";
                }
            }
        }
        else{
            return "redirect:/HoneyBeeHaven/login";
        }
    }

    @PostMapping(path="/submitmachine")
    public String submitmachine(@ModelAttribute("itemForm") ItemForm itemForm, HttpServletRequest request, Model model, @RequestParam("file1") MultipartFile file) throws IOException
    {
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        String cookieValuetype = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }

        Integer businessID = Integer.parseInt(cookieValueid);

        Machinery machinery = new Machinery();

        Integer itemID = pidService.getNextId();

        LocalDate currentDate = LocalDate.now();

        // Define a date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Format the current date as a string
        String formattedDate = currentDate.format(formatter);

        machinery.setDate(formattedDate);

        machinery.setItemid(itemID);
        machinery.setMachinetype(itemForm.machinetype);
        machinery.setMachinedimension(itemForm.machinedimension1 + " x " + itemForm.machinedimension2 + " x " + itemForm.machinedimension3);
        machinery.setMachineweight(itemForm.machineweight);
        machinery.setPowersource(itemForm.powersource);
        machinery.setWarranty(itemForm.warranty);
        machinery.setQuantityinstock(itemForm.mquantityinstock);
        machinery.setBusinessid(businessID);
        machinery.setItemPrice(itemForm.itemprice);
        machinery.setItemname(itemForm.itemname);
        machinery.setItemdescription(itemForm.itemdescription);
        machinery.setIsdeleted(0);

        if (subscriptionRepository.findByBusinessidAndExpired(businessID, false).isPresent()){
            machinery.setIssponsored(true);
        }
        else {
            machinery.setIssponsored(false);
        }

        if (!file.isEmpty()) {

            String uploadDirectory = PATHS + "images\\machinery\\";

            String originalFileName = file.getOriginalFilename();

            // Check the file extension
            if (originalFileName != null && originalFileName.lastIndexOf(".") != -1) {
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

                // Example: Check if the file extension is '.jpg' or '.jpeg'
                if (fileExtension.equalsIgnoreCase(".jpg") || fileExtension.equalsIgnoreCase(".jpeg") || fileExtension.equalsIgnoreCase(".png")) {
                    String fileName = itemID.toString();

                    // Create the file object
                    File imageFile = new File(uploadDirectory + fileName + fileExtension);

                    // Save the file to the specified location
                    file.transferTo(imageFile);

                    // Save the file location as a string in the user's image attribute
                    String fileLocation = "/images/machinery/" + fileName + fileExtension; // Adjust the path as needed
                    // Save the file location in the user's image attribute (you'll need to retrieve the user)
                    machinery.setImage(fileLocation);

                } else {
                    return "redirect:/HoneyBeeHaven/createItem?type=machine&error=1";
                }
            } else {
                return "redirect:/HoneyBeeHaven/createItem?type=machine&error=1";
            }

        }
        else {
            machinery.setImage(null);
        }

        int success = 0;

        if (itemForm.itemprice >= 1 && Float.parseFloat(itemForm.machinedimension1) >= 1 && Float.parseFloat(itemForm.machinedimension2) >= 1 && Float.parseFloat(itemForm.machinedimension3) >= 1 && itemForm.machineweight >= 1 && itemForm.mquantityinstock >= 0)
        {

        }
        else{
            success = 1;
        }


        if (success == 0)
        {
            machinery.setItemrating((float)0);
            machineryRepository.save(machinery);

            return "redirect:/HoneyBeeHaven/myProducts";
        }
        else
        {
            return "redirect:/HoneyBeeHaven/creaeItem?type=machine&error=1";
        }
    }

    @PostMapping(path="/submitchemical")
    public String submitchemical(@ModelAttribute("itemForm") ItemForm itemForm, HttpServletRequest request, Model model, @RequestParam("file2") MultipartFile file) throws IOException
    {
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        String cookieValuetype = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }

        Integer businessID = Integer.parseInt(cookieValueid);

        Chemical chemical = new Chemical();

        Integer itemID = pidService.getNextId();

        LocalDate currentDate = LocalDate.now();

        // Define a date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Format the current date as a string
        String formattedDate = currentDate.format(formatter);

        chemical.setDate(formattedDate);

        chemical.setItemid(itemID);
        chemical.setChemicalType(itemForm.chemicaltype);
        chemical.setMetricsystem(itemForm.metricsystem);
        chemical.setExpirydate(itemForm.expirydate);
        chemical.setHazardlevel(itemForm.hazardlevel);
        chemical.setQuantity(itemForm.quantity);
        chemical.setQuantityinstock(itemForm.cquantityinstock);
        chemical.setBusinessid(businessID);
        chemical.setItemPrice(itemForm.itemprice);
        chemical.setItemname(itemForm.itemname);
        chemical.setItemdescription(itemForm.itemdescription);
        chemical.setIsdeleted(0);

        if (subscriptionRepository.findByBusinessidAndExpired(businessID, false).isPresent()){
            chemical.setIssponsored(true);
        }
        else {
            chemical.setIssponsored(false);
        }

        if (!file.isEmpty()) {

            String uploadDirectory = PATHS + "images\\chemical\\";

            String originalFileName = file.getOriginalFilename();

            // Check the file extension
            if (originalFileName != null && originalFileName.lastIndexOf(".") != -1) {
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

                // Example: Check if the file extension is '.jpg' or '.jpeg'
                if (fileExtension.equalsIgnoreCase(".jpg") || fileExtension.equalsIgnoreCase(".jpeg") || fileExtension.equalsIgnoreCase(".png")) {
                    String fileName = itemID.toString();

                    // Create the file object
                    File imageFile = new File(uploadDirectory + fileName + fileExtension);

                    // Save the file to the specified location
                    file.transferTo(imageFile);

                    // Save the file location as a string in the user's image attribute
                    String fileLocation = "/images/chemical/" + fileName + fileExtension; // Adjust the path as needed
                    // Save the file location in the user's image attribute (you'll need to retrieve the user)
                    chemical.setImage(fileLocation);

                } else {
                    return "redirect:/HoneyBeeHaven/createItem?type=chemical&error=1";
                }
            } else {
                return "redirect:/HoneyBeeHaven/createItem?type=chemical&error=1";
            }

        }
        else {
            chemical.setImage(null);
        }

        String dateString = itemForm.expirydate;

        // Parse the date string into a LocalDate object
        LocalDate inputDate = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);

        // Get the current date
        currentDate = LocalDate.now();

        int success = 0;

        if (itemForm.itemprice >= 1 && itemForm.hazardlevel >= 0 && itemForm.quantity > 0 && itemForm.cquantityinstock >= 0 && inputDate.isAfter(currentDate))
        {

        }
        else{
            success = 1;
        }


        if (success == 0){
            chemical.setItemrating((float)0);
            chemicalRepository.save(chemical);
            return "redirect:/HoneyBeeHaven/myProducts";
        }
        else
        {
            return "redirect:/HoneyBeeHaven/createItem?type=chemical&error=1";
        }
    }

    @PostMapping(path="/submitservice")
    public String submitservice(@ModelAttribute("itemForm") ItemForm itemForm, HttpServletRequest request, Model model, @RequestParam("file3") MultipartFile file) throws IOException
    {
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        String cookieValuetype = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }



        Integer businessID = Integer.parseInt(cookieValueid);

        Service service = new Service();

        Integer itemID = pidService.getNextId();
        LocalDate currentDate = LocalDate.now();

        // Define a date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Format the current date as a string
        String formattedDate = currentDate.format(formatter);

        service.setDate(formattedDate);
        service.setItemid(itemID);
        service.setServicetype(itemForm.servicetype);
        service.setBasecharges(itemForm.basecharges);
        service.setBusinessid(businessID);
        service.setItemPrice(itemForm.itemprice);
        service.setItemname(itemForm.itemname);
        service.setItemdescription(itemForm.itemdescription);
        service.setIsavailable(itemForm.isavailable);
        service.setIsdeleted(0);

        if (subscriptionRepository.findByBusinessidAndExpired(businessID, false).isPresent()){
            service.setIssponsored(true);
        }
        else {
            service.setIssponsored(false);
        }

        if (!file.isEmpty()) {

            String uploadDirectory = PATHS + "images\\service\\";

            String originalFileName = file.getOriginalFilename();

            // Check the file extension
            if (originalFileName != null && originalFileName.lastIndexOf(".") != -1) {
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

                // Example: Check if the file extension is '.jpg' or '.jpeg'
                if (fileExtension.equalsIgnoreCase(".jpg") || fileExtension.equalsIgnoreCase(".jpeg") || fileExtension.equalsIgnoreCase(".png")) {
                    String fileName = itemID.toString();

                    // Create the file object
                    File imageFile = new File(uploadDirectory + fileName + fileExtension);

                    // Save the file to the specified location
                    file.transferTo(imageFile);

                    // Save the file location as a string in the user's image attribute
                    String fileLocation = "/images/service/" + fileName + fileExtension; // Adjust the path as needed
                    // Save the file location in the user's image attribute (you'll need to retrieve the user)
                    service.setImage(fileLocation);

                } else {
                    return "redirect:/HoneyBeeHaven/createItem?type=service&error=1";
                }
            } else {
                return "redirect:/HoneyBeeHaven/createItem?type=service&error=1";
            }

        }
        else {
            service.setImage(null);
        }

      
        int success = 0;

        if (itemForm.itemprice >= 1 && Integer.parseInt(itemForm.basecharges) >= 0)
        {

        }
        else{
            success = 1;
        }
        
        if (success == 0)
        {
            service.setItemrating((float)0);
            serviceRepository.save(service);

            return "redirect:/HoneyBeeHaven/myProducts";
        }
        else
        {
            return "redirect:/HoneyBeeHaven/createItem?type=service&error=1";
        }
    }

    @GetMapping(path="/chats")
    public String chats(HttpServletRequest request, Model model, HttpServletResponse response)
    {
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        String cookieValuetype = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }

        if (cookieValueid != null)
        {
           if (cookieValuetype.equals("b"))
           {
               Business business = businessRepository.findById(Integer.parseInt(cookieValueid)).get();


               if (business.getBanned()) {
                   Cookie userIdCookie = new Cookie("userid", "");

                   userIdCookie.setPath("/"); // Set the cookie's path (root path)

                   // Create and set the "usertype" cookie
                   Cookie userTypeCookie = new Cookie("usertype","");

                   userTypeCookie.setPath("/");

                   response.addCookie(userIdCookie);
                   response.addCookie(userTypeCookie);

                   return "redirect:/HoneyBeeHaven/login?error=2";
               }
               else if (business.getDateBanned() != null){
                   LocalDate providedDate = LocalDate.parse(business.getDateBanned(), DateTimeFormatter.ISO_LOCAL_DATE);

                   // Get the current date
                   LocalDate currentDate = LocalDate.now();

                   // Calculate the difference in days
                   int daysDifference = Math.abs((int) (currentDate.toEpochDay() - providedDate.toEpochDay()));

                   if (daysDifference >= 30){
                       business.setDateBanned(null);
                       businessRepository.save(business);

                       Integer businessid = Integer.parseInt(cookieValueid);
                       List<Conversation> convos = conversationRepository.findAllByBusinessid(businessid);
                       List<Chat> chats = new ArrayList<Chat>();
                       for (int i = 0; i < convos.size(); i++) {
                           Conversation conversation = convos.get(i);
                           Chat chat = new Chat();
                           chat.name = clientRepository.findById(conversation.getClientid()).get().getName();
                           chat.image = clientRepository.findById(conversation.getClientid()).get().getImage();
                           if (chat.image == null){
                               chat.image = "/images/client/avatar-01.jpg";
                           }
                           chat.convid = conversation.getConversationid();
                           chats.add(chat);
                       }
                       model.addAttribute("chats", chats);
                       return "/honeybeehaven/shared/chat";
                   }
                   else{
                       return "redirect:/HoneyBeeHaven/login?error=3";
                   }
               }
               else {
                   Integer businessid = Integer.parseInt(cookieValueid);
                   List<Conversation> convos = conversationRepository.findAllByBusinessid(businessid);
                   List<Chat> chats = new ArrayList<Chat>();
                   for (int i = 0; i < convos.size(); i++) {
                       Conversation conversation = convos.get(i);
                       Chat chat = new Chat();
                       chat.name = clientRepository.findById(conversation.getClientid()).get().getName();
                       chat.image = clientRepository.findById(conversation.getClientid()).get().getImage();
                       if (chat.image == null){
                           chat.image = "/images/client/avatar-01.jpg";
                       }
                       chat.convid = conversation.getConversationid();
                       chats.add(chat);
                   }
                   model.addAttribute("chats", chats);
                   return "/honeybeehaven/shared/chat";
               }

           }
           else{
              Client client = clientRepository.findById(Integer.parseInt(cookieValueid)).get();


               if (client.getBanned()) {
                   Cookie userIdCookie = new Cookie("userid", "");

                   userIdCookie.setPath("/"); // Set the cookie's path (root path)

                   // Create and set the "usertype" cookie
                   Cookie userTypeCookie = new Cookie("usertype","");

                   userTypeCookie.setPath("/");

                   response.addCookie(userIdCookie);
                   response.addCookie(userTypeCookie);

                   return "redirect:/HoneyBeeHaven/login?error=2";
               }
               else if (client.getDateBanned() != null){
                   LocalDate providedDate = LocalDate.parse(client.getDateBanned(), DateTimeFormatter.ISO_LOCAL_DATE);

                   // Get the current date
                   LocalDate currentDate = LocalDate.now();

                   // Calculate the difference in days
                   int daysDifference = Math.abs((int) (currentDate.toEpochDay() - providedDate.toEpochDay()));

                   if (daysDifference >= 30){
                       client.setDateBanned(null);
                       clientRepository.save(client);

                       Integer clientid = Integer.parseInt(cookieValueid);
                       List<Conversation> convos = conversationRepository.findAllByClientid(clientid);
                       List<Chat> chats = new ArrayList<Chat>();
                       for (int i = 0; i < convos.size(); i++) {
                           Conversation conversation = convos.get(i);
                           Chat chat = new Chat();
                           chat.name = businessRepository.findById(conversation.getBusinessid()).get().getBusinessname();
                           chat.image = businessRepository.findById(conversation.getBusinessid()).get().getImage();
                           if (chat.image == null){
                               chat.image = "/images/business/avatar-01.jpg";
                           }
                           chat.convid = conversation.getConversationid();
                           chats.add(chat);
                       }
                       model.addAttribute("chats", chats);
                       return "/honeybeehaven/shared/chat";
                   }
                   else{
                       return "redirect:/HoneyBeeHaven/login?error=3";
                   }
               }
               else {
                   Integer clientid = Integer.parseInt(cookieValueid);
                   List<Conversation> convos = conversationRepository.findAllByClientid(clientid);
                   List<Chat> chats = new ArrayList<Chat>();
                   for (int i = 0; i < convos.size(); i++) {
                       Conversation conversation = convos.get(i);
                       Chat chat = new Chat();
                       chat.name = businessRepository.findById(conversation.getBusinessid()).get().getBusinessname();
                       chat.image = businessRepository.findById(conversation.getBusinessid()).get().getImage();
                       if (chat.image == null){
                           chat.image = "/images/business/avatar-01.jpg";
                       }
                       chat.convid = conversation.getConversationid();
                       chats.add(chat);
                   }
                   model.addAttribute("chats", chats);
                   return "/honeybeehaven/shared/chat";
               }

           }
        }
        else{
            return "redirect:/HoneyBeeHaven/login";
        }

    }

    @GetMapping(path="/chatwith")
    public String chats(Model model, HttpServletRequest request, @RequestParam("convid") String cid) {
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        String cookieValuetype = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    cookieValuetype = cookie.getValue();
                }
            }
        }

        Integer userid = 0;
        Integer convid = Integer.parseInt(cid);
        if (cookieValueid != null) {
            userid = Integer.parseInt(cookieValueid);
        }

        String name = null;
        String image = null;
        Integer otherid = 0;
        Conversation conversation = conversationRepository.findById(convid).get();
        if(cookieValuetype != null && !cookieValuetype.isEmpty() && cookieValuetype.equals("b")){
            name = clientRepository.findById(conversation.getClientid()).get().getName();
            image = clientRepository.findById(conversation.getClientid()).get().getImage();
            otherid = clientRepository.findById(conversation.getClientid()).get().getUserid();
            model.addAttribute("href", "view_client?userid=" + otherid.toString());
        }
        else if(cookieValuetype != null && !cookieValuetype.isEmpty() && cookieValuetype.equals("c")){
            name = businessRepository.findById(conversation.getBusinessid()).get().getBusinessname();
            image = businessRepository.findById(conversation.getBusinessid()).get().getImage();
            otherid = businessRepository.findById(conversation.getBusinessid()).get().getUserid();
            model.addAttribute("href", "view_business?userid=" + otherid.toString());

        }
        else{
            return "redirect:/HoneyBeeHaven/login";
        }
        if (image == null){
            image ="/images/client/avatar-01.jpg";
        }

        model.addAttribute("name", name);
        model.addAttribute("image", image);
        model.addAttribute("convid", convid);
        model.addAttribute("otherid", otherid);

        List<WebMessage> webMessages = new ArrayList<WebMessage>();

        List<Message> messages = messageRepository.findAllByConversationid(convid);

        if (cookieValuetype.equals("b")){
            Business self = businessRepository.findById(conversation.getBusinessid()).get();
            Client other = clientRepository.findById(conversation.getClientid()).get();

            for (int i = 0; i < messages.size(); i++){
                WebMessage webMessage = new WebMessage();
                if(messages.get(i).getSenderid().equals(userid)){
                    webMessage.image = self.getImage();
                    if(webMessage.image == null){
                        webMessage.image = "/images/client/avatar-01.jpg";
                    }
                    webMessage.type = 1;
                    webMessage.text = messages.get(i).getText();
                    webMessage.name = self.getBusinessname();
                }
                else{
                    webMessage.image = other.getImage();
                    otherid = other.getUserid();
                    name = other.getName();
                    image = other.getImage();
                    if(webMessage.image == null){
                        webMessage.image = "/images/client/avatar-01.jpg";
                        image = "/images/client/avatar-01.jpg";
                    }
                    webMessage.type = 2;
                    webMessage.text = messages.get(i).getText();
                    webMessage.name = other.getName();
                }

                webMessages.add(webMessage);
            }
        }
        else{
            Business other = businessRepository.findById(conversation.getBusinessid()).get();
            Client self = clientRepository.findById(conversation.getClientid()).get();
            for (int i = 0; i < messages.size(); i++){
                WebMessage webMessage = new WebMessage();
                if(messages.get(i).getSenderid().equals(userid)){
                    webMessage.image = self.getImage();
                    if(webMessage.image == null){
                        webMessage.image = "/images/client/avatar-01.jpg";
                    }
                    webMessage.type = 1;
                    webMessage.text = messages.get(i).getText();
                    webMessage.name = self.getName();
                }
                else{
                    webMessage.image = other.getImage();
                    name = other.getBusinessname();
                    otherid = other.getUserid();
                    image = other.getImage();
                    if(webMessage.image == null){
                        webMessage.image = "/images/client/avatar-01.jpg";
                        image = "/images/client/avatar-01.jpg";
                    }
                    webMessage.type = 2;
                    webMessage.text = messages.get(i).getText();
                    webMessage.name = other.getBusinessname();
                }
                webMessages.add(webMessage);

            }
        }
        model.addAttribute("messages", webMessages);
        Integer param=-1;
        if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("c")){
            param=2;
        }else if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("b")){
            param=3;
        }
        model.addAttribute("navbar", getNavbar(param,cookieValueid));
        return "/honeybeehaven/shared/inbox";
    }

    @PostMapping(path="/sendMessage")
    public String sendMessage(Model model, HttpServletRequest request,  @RequestParam("convid") Integer convid,  @RequestParam("otherid") Integer otherid, @RequestParam("mtext") String text){
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        String cookieValuetype = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }

        Integer userid = 0;

        if (cookieValueid != null) {
            userid = Integer.parseInt(cookieValueid);
        }

        Message message = new Message();
        message.setConversationid(convid);
        message.setSenderid(userid);
        message.setReceiverid(otherid);
        message.setText(text);
        messageRepository.save(message);
        return "redirect:/HoneyBeeHaven/chatwith?convid=" + convid.toString();
    }

    @GetMapping(path="/addchat")
    public String addchats(Model model, HttpServletRequest request, @RequestParam("userid") String uid, @RequestParam("type") String type)
    {
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
            }
        }



        if (type.equals("c")){

            Integer businessid = 0;
            if (cookieValueid != null) {
                businessid = Integer.parseInt(cookieValueid);
            }

            Integer clientid = Integer.parseInt(uid);

            if ((conversationRepository.findByClientidAndBusinessid(clientid, businessid)).isPresent()){
                Conversation conversation = (conversationRepository.findByClientidAndBusinessid(clientid, businessid)).get();
                return "redirect:/HoneyBeeHaven/chatwith?convid=" + conversation.getConversationid();
            }
            else {
                Conversation conversation = new Conversation();
                conversation.setBusinessid(businessid);
                conversation.setClientid(clientid);
                conversationRepository.save(conversation);
                conversation = conversationRepository.findByClientidAndBusinessid(clientid, businessid).get();

                Notifications notifications = new Notifications();
                notifications.setNotificationMessage("A new Client has started a chat with you");

                notifications.setUserId(businessid);
                notifications.setTime(LocalDateTime.now().toString());
                notificationRepository.save(notifications);

                return "redirect:/HoneyBeeHaven/chatwith?convid=" + conversation.getConversationid();
            }
        }
        else{
            Integer clientid = 0;
            if (cookieValueid != null) {
                clientid = Integer.parseInt(cookieValueid);
            }

            Integer businessid = Integer.parseInt(uid);

            if ((conversationRepository.findByClientidAndBusinessid(clientid, businessid)).isPresent()){
                Conversation conversation = (conversationRepository.findByClientidAndBusinessid(clientid, businessid)).get();
                return "redirect:/HoneyBeeHaven/chatwith?convid=" + conversation.getConversationid();
            }
            else {
                Conversation conversation = new Conversation();
                conversation.setBusinessid(businessid);
                conversation.setClientid(clientid);
                conversationRepository.save(conversation);
                conversation = conversationRepository.findByClientidAndBusinessid(clientid, businessid).get();

                Notifications notifications = new Notifications();
                notifications.setNotificationMessage("A new Business has started a chat with you");

                notifications.setUserId(businessid);
                notifications.setTime(LocalDateTime.now().toString());
                notificationRepository.save(notifications);

                return "redirect:/HoneyBeeHaven/chatwith?convid=" + conversation.getConversationid();
            }
        }
    }

    @GetMapping(path="/addreview")
    public String addreview(Model model, HttpServletRequest request, @RequestParam("itemid") String iid, HttpServletResponse response){
        ReviewForm reviewForm = new ReviewForm();
        Integer itemid = Integer.parseInt(iid);
        reviewForm.itemid = itemid;
        model.addAttribute("reviewForm", reviewForm);

        Cookie[] cookies = request.getCookies();
        String cookieValueid = null,cookieValuetype= null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }

        if (cookieValuetype != null && cookieValuetype.equals("c")) {
            Client client = clientRepository.findById(Integer.parseInt(cookieValueid)).get();

            if (client.getBanned()) {
                Cookie userIdCookie = new Cookie("userid", "");

                userIdCookie.setPath("/"); // Set the cookie's path (root path)

                // Create and set the "usertype" cookie
                Cookie userTypeCookie = new Cookie("usertype","");

                userTypeCookie.setPath("/");

                response.addCookie(userIdCookie);
                response.addCookie(userTypeCookie);

                return "redirect:/HoneyBeeHaven/login?error=2";
            }
            else if (client.getDateBanned() != null){
                LocalDate providedDate = LocalDate.parse(client.getDateBanned(), DateTimeFormatter.ISO_LOCAL_DATE);

                // Get the current date
                LocalDate currentDate = LocalDate.now();

                // Calculate the difference in days
                int daysDifference = Math.abs((int) (currentDate.toEpochDay() - providedDate.toEpochDay()));

                if (daysDifference >= 30){
                    client.setDateBanned(null);
                    clientRepository.save(client);

                    Integer param = -1;
                    if (cookieValueid != null && !cookieValueid.isEmpty() && cookieValuetype.equals("c")) {
                        param = 2;
                    } else if (cookieValueid != null && !cookieValueid.isEmpty() && cookieValuetype.equals("b")) {
                        param = 3;
                    }
                    model.addAttribute("navbar", getNavbar(param, cookieValueid));
                    return "/honeybeehaven/client/review";
                }
                else{
                    return "redirect:/HoneyBeeHaven/login?error=3";
                }
            }
            else {
                Integer param = -1;
                if (cookieValueid != null && !cookieValueid.isEmpty() && cookieValuetype.equals("c")) {
                    param = 2;
                } else if (cookieValueid != null && !cookieValueid.isEmpty() && cookieValuetype.equals("b")) {
                    param = 3;
                }
                model.addAttribute("navbar", getNavbar(param, cookieValueid));
                return "/honeybeehaven/client/review";
            }
        }
        else{
            return "redirect:/HoneyBeeHaven/login";
        }
    }

    @PostMapping(path="/submitreview")
    public String submitreview(Model model, HttpServletRequest request, @ModelAttribute("reviewForm") ReviewForm reviewForm){
        Review review = new Review();
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
            }
        }
        Integer clientid = Integer.parseInt(cookieValueid);
        review.setItemid(reviewForm.itemid);
        review.setRating(reviewForm.rating);
        review.setText(reviewForm.text);
        review.setTitle(reviewForm.title);
        review.setClientid(clientid);
        LocalDate currentDate = LocalDate.now();

        // Define a date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Format the current date as a string
        String formattedDate = currentDate.format(formatter);

        review.setDate(formattedDate);

        int number = reviewRepository.findAllByItemid(reviewForm.itemid).size();

        Integer businessid = 1;

        if (chemicalRepository.findById(reviewForm.itemid).isPresent()){
            Chemical chemical = chemicalRepository.findById(reviewForm.itemid).get();
            chemical.setItemrating((chemical.getItemrating() * number + reviewForm.rating)/(number + 1));
            chemicalRepository.save(chemical);
            businessid = chemical.getBusinessid();
        }
        else if(serviceRepository.findById(reviewForm.itemid).isPresent()){
            Service service = serviceRepository.findById(reviewForm.itemid).get();
            service.setItemrating((service.getItemrating() * number + reviewForm.rating)/(number + 1));
            serviceRepository.save(service);
            businessid = service.getBusinessid();
        }
        else {
            Machinery machinery = machineryRepository.findById(reviewForm.itemid).get();
            machinery.setItemrating((machinery.getItemrating() * number + reviewForm.rating)/(number + 1));
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

        business.setBusinessrating((reviewed * business.getBusinessrating() + reviewForm.rating)/(reviewed + 1));

        businessRepository.save(business);

        Notifications notifications = new Notifications();
        notifications.setNotificationMessage("A new Review has been given for your item with id " + reviewForm.itemid);

        notifications.setUserId(business.getUserid());
        notifications.setTime(LocalDateTime.now().toString());
        notificationRepository.save(notifications);

        reviewRepository.save(review);

        return "redirect:/HoneyBeeHaven/index";
    }

    @GetMapping(path = "/chemicalDesignPage")
    public String chemicalDesignPage(@RequestParam("itemId")String iid, Model model,HttpServletRequest request){
        Chemical obj =new Chemical();
        Integer itemId=Integer.parseInt(iid);
        obj=chemicalRepository.findById(itemId).get();
        model.addAttribute("chemicalImagePath", obj.getImage());
        model.addAttribute("productHeading", obj.getItemname());
        model.addAttribute("productRating", obj.getItemrating());
        model.addAttribute("productQuantityInStock", obj.getQuantityinstock());
        model.addAttribute("productPrice", obj.getItemPrice());
        model.addAttribute("productDescription", obj.getItemdescription());
        model.addAttribute("productType","Chemical" );
        model.addAttribute("metricSystem", obj.getMetricsystem() );
        model.addAttribute("chemicalType", obj.getChemicalType());
        model.addAttribute("expiryDate", obj.getExpirydate());
        model.addAttribute("hazardLevel", obj.getHazardlevel());
        model.addAttribute("businessId", obj.getBusinessid());
        model.addAttribute("businessName",businessRepository.findById(obj.getBusinessid()).get().getBusinessname());
        model.addAttribute("qty", obj.getQuantity());

        Integer itemid = Integer.parseInt(iid);

        List<Review> reviews = reviewRepository.findAllByItemid(itemid);
        model.addAttribute("reviews", reviews);
        model.addAttribute("itemid", itemid);

        Cookie[] cookies = request.getCookies();
        String cookieValueid = null,cookieValuetype= null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }
        Integer param=1;
        if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("c")){
            param=2;
        }else if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("b")){
            return "redirect:/HoneyBeeHaven/viewChemicalPage?itemId="+iid;
        }
        CartForm cartForm = new CartForm();
        model.addAttribute(cartForm);
        model.addAttribute("navbar", getNavbar(param,cookieValueid));
        if (cookieValueid != null && !cookieValueid.isEmpty()) {
            model.addAttribute("idd", Integer.parseInt(cookieValueid));
        }
        return "/honeybeehaven/client/chemicalDesignPage";
    }

    @GetMapping(path = "/machineryDesignPage")
    public String machineDesignPage(@RequestParam("itemId")String iid, Model model,HttpServletRequest request){
        Machinery obj =new Machinery();
        Integer itemId=Integer.parseInt(iid);
        obj=machineryRepository.findById(itemId).get();

        model.addAttribute("businessImagePath", obj.getImage());
        model.addAttribute("productHeading", obj.getItemname());
        model.addAttribute("productRating", obj.getItemrating());
        model.addAttribute("productQuantityInStock", obj.getQuantityinstock());
        model.addAttribute("productPrice", obj.getItemPrice());
        model.addAttribute("productDescription", obj.getItemdescription());
        model.addAttribute("productType", "Machinery");
        model.addAttribute("machineType",obj.getMachinetype() );
        model.addAttribute("machineDimension", obj.getMachinedimension() );
        model.addAttribute("machineWeight", obj.getMachineweight());
        model.addAttribute("machinePowerSource", obj.getPowersource());
        model.addAttribute("machineWarranty", obj.getWarranty());
        model.addAttribute("businessId", obj.getBusinessid());
        model.addAttribute("businessName",businessRepository.findById(obj.getBusinessid()).get().getBusinessname());

        Integer itemid = Integer.parseInt(iid);

        List<Review> reviews = reviewRepository.findAllByItemid(itemid);
        model.addAttribute("reviews", reviews);
        model.addAttribute("itemid", itemid);

        Cookie[] cookies = request.getCookies();
        String cookieValueid = null,cookieValuetype= null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }
        Integer param=1;
        if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("c")){
            param=2;
        }else if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("b")){
            return "redirect:/HoneyBeeHaven/viewMachinePage?itemId="+iid;

        }
        model.addAttribute("navbar", getNavbar(param,cookieValueid));
        CartForm cartForm = new CartForm();
        model.addAttribute(cartForm);
        if (cookieValueid != null && !cookieValueid.isEmpty()) {
            model.addAttribute("idd", Integer.parseInt(cookieValueid));
        }
        return "/honeybeehaven/client/machineDesignPage";
    }

    @GetMapping(path = "/serviceDesignPage")
    public String serviceDesignPage(@RequestParam("itemId")String iid, Model model,HttpServletRequest request){
        Service obj =new Service();
        Integer itemId=Integer.parseInt(iid);
        obj=serviceRepository.findById(itemId).get();
        model.addAttribute("serviceImagePath", obj.getImage());
        model.addAttribute("productHeading", obj.getItemname());
        model.addAttribute("productRating", obj.getItemrating());
        model.addAttribute("productPrice", obj.getItemPrice());
        model.addAttribute("productDescription", obj.getItemdescription());
        model.addAttribute("productType", "Machinery");
        model.addAttribute("baseCharges", obj.getBasecharges());
        model.addAttribute("businessId", obj.getBusinessid());
        model.addAttribute("businessName",businessRepository.findById(obj.getBusinessid()).get().getBusinessname());
        if (obj.getIsavailable()){
            model.addAttribute("available", 1);
        }
        else{
            model.addAttribute("available", 0);
        }

        Integer itemid = Integer.parseInt(iid);

        List<Review> reviews = reviewRepository.findAllByItemid(itemid);
        model.addAttribute("reviews", reviews);
        model.addAttribute("itemid", itemid);

        Cookie[] cookies = request.getCookies();
        String cookieValueid = null,cookieValuetype= null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }
        Integer param=1;
        if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("c")){
            param=2;
        }else if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("b")){
            return "redirect:/HoneyBeeHaven/viewServicePage?itemId="+iid;

        }
        model.addAttribute("navbar", getNavbar(param,cookieValueid));
        CartForm cartForm = new CartForm();
        model.addAttribute(cartForm);
        if (cookieValueid != null && !cookieValueid.isEmpty()) {
            model.addAttribute("idd", Integer.parseInt(cookieValueid));
        }
        return "/honeybeehaven/client/serviceDesignPage";
    }

    @GetMapping(path = "/viewServicePage")
    public String viewServicePage(HttpServletRequest request,Model model,@RequestParam("itemId") String iid){
        Service obj =new Service();
        Integer itemId=Integer.parseInt(iid);
        obj=serviceRepository.findById(itemId).get();
        model.addAttribute("serviceImagePath", obj.getImage());
        model.addAttribute("productHeading", obj.getItemname());
        model.addAttribute("productRating", obj.getItemrating());
        model.addAttribute("productPrice", obj.getItemPrice());
        model.addAttribute("productDescription", obj.getItemdescription());
        model.addAttribute("productType", "Service");
        model.addAttribute("baseCharges", obj.getBasecharges());
        model.addAttribute("businessId", obj.getBusinessid());
        model.addAttribute("businessName",businessRepository.findById(obj.getBusinessid()).get().getBusinessname());

        Integer itemid = Integer.parseInt(iid);

        List<Review> reviews = reviewRepository.findAllByItemid(itemid);
        model.addAttribute("reviews", reviews);
        model.addAttribute("itemid", itemid);

        Cookie[] cookies = request.getCookies();
        String cookieValueid = null,cookieValuetype= null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }
        Integer param=1;
        if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("c")){
            return "redirect:/HoneyBeeHaven/serviceDesignPage?itemId="+iid;
        }else if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("b")){
            param=3;
        }

        model.addAttribute("navbar", getNavbar(param,cookieValueid));
        return "/honeybeehaven/business/viewServicePage";
    }

    @GetMapping(path = "/viewMachinePage")
    public String viewMachinePage(HttpServletRequest request,Model model,@RequestParam("itemId") String iid){
        Machinery obj =new Machinery();
        Integer itemId=Integer.parseInt(iid);
        obj=machineryRepository.findById(itemId).get();

        model.addAttribute("businessImagePath", obj.getImage());
        model.addAttribute("productHeading", obj.getItemname());
        model.addAttribute("productRating", obj.getItemrating());
        model.addAttribute("productQuantityInStock", obj.getQuantityinstock());
        model.addAttribute("productPrice", obj.getItemPrice());
        model.addAttribute("productDescription", obj.getItemdescription());
        model.addAttribute("productType", "Machinery");
        model.addAttribute("machineType",obj.getMachinetype() );
        model.addAttribute("machineDimension", obj.getMachinedimension() );
        model.addAttribute("machineWeight", obj.getMachineweight());
        model.addAttribute("machinePowerSource", obj.getPowersource());
        model.addAttribute("machineWarranty", obj.getWarranty());
        model.addAttribute("businessId", obj.getBusinessid());
        model.addAttribute("businessName",businessRepository.findById(obj.getBusinessid()).get().getBusinessname());

        Integer itemid = Integer.parseInt(iid);

        List<Review> reviews = reviewRepository.findAllByItemid(itemid);
        model.addAttribute("reviews", reviews);
        model.addAttribute("itemid", itemid);

        Cookie[] cookies = request.getCookies();
        String cookieValueid = null,cookieValuetype= null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }
        Integer param=-1;
        if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("c")){
            return "redirect:/HoneyBeeHaven/machineDesignPage?itemId="+iid;
        }else if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("b")){
            param=3;
        }
        model.addAttribute("navbar", getNavbar(param,cookieValueid));
        return "/honeybeehaven/business/viewMachinePage";
    }

    @GetMapping(path = "/viewChemicalPage")
    public String viewChemicalPage(HttpServletRequest request,Model model,@RequestParam("itemId") String iid){
        Chemical obj =new Chemical();
        Integer itemId=Integer.parseInt(iid);
        obj=chemicalRepository.findById(itemId).get();
        model.addAttribute("chemicalImagePath", obj.getImage());
        model.addAttribute("productHeading", obj.getItemname());
        model.addAttribute("productRating", obj.getItemrating());
        model.addAttribute("productQuantityInStock", obj.getQuantityinstock());
        model.addAttribute("productPrice", obj.getItemPrice());
        model.addAttribute("productDescription", obj.getItemdescription());
        model.addAttribute("productType","Chemical" );
        model.addAttribute("metricSystem", obj.getMetricsystem() );
        model.addAttribute("chemicalType", obj.getChemicalType());
        model.addAttribute("expiryDate", obj.getExpirydate());
        model.addAttribute("hazardLevel", obj.getHazardlevel());
        model.addAttribute("businessId", obj.getBusinessid());
        model.addAttribute("businessName",businessRepository.findById(obj.getBusinessid()).get().getBusinessname());
        model.addAttribute("qty",obj.getQuantity());
        Integer itemid = Integer.parseInt(iid);

        List<Review> reviews = reviewRepository.findAllByItemid(itemid);
        model.addAttribute("reviews", reviews);
        model.addAttribute("itemid", itemid);

        Cookie[] cookies = request.getCookies();
        String cookieValueid = null,cookieValuetype= null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }
        Integer param=-1;
        if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("c")){
            return "redirect:/HoneyBeeHaven/chemicalDesignPage?itemId="+iid;
        }else if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("b")){
            param=3;
        }
        model.addAttribute("navbar", getNavbar(param,cookieValueid));
        return "/honeybeehaven/business/viewChemicalPage";
    }

    @GetMapping(path = "/editChemical")
    public String editChemical(@RequestParam("itemId")String iid, Model model,HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "error", required = false) String error){
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null,cookieValuetype= null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }
        Integer param=1;
        if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("c")){
            return "redirect:/HoneyBeeHaven/login";
        }else if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("b")){
            param=3;
        }

        if (param == 1){
            return "redirect:/HoneyBeeHaven/login";
        }

        Business business = businessRepository.findById(Integer.parseInt(cookieValueid)).get();

        if (business.getBanned()) {
            Cookie userIdCookie = new Cookie("userid", "");

            userIdCookie.setPath("/"); // Set the cookie's path (root path)

            // Create and set the "usertype" cookie
            Cookie userTypeCookie = new Cookie("usertype","");

            userTypeCookie.setPath("/");

            response.addCookie(userIdCookie);
            response.addCookie(userTypeCookie);

            return "redirect:/HoneyBeeHaven/login?error=2";
        }
        else if (business.getDateBanned() != null){
            LocalDate providedDate = LocalDate.parse(business.getDateBanned(), DateTimeFormatter.ISO_LOCAL_DATE);

            // Get the current date
            LocalDate currentDate = LocalDate.now();

            // Calculate the difference in days
            int daysDifference = Math.abs((int) (currentDate.toEpochDay() - providedDate.toEpochDay()));

            if (daysDifference >= 30){
                business.setDateBanned(null);
                businessRepository.save(business);

                Chemical obj = new Chemical();
                Integer itemId=Integer.parseInt(iid);
                obj=chemicalRepository.findById(itemId).get();
                EditChemical editChemical=new EditChemical(obj.getItemname(), obj.getItemPrice(), obj.getItemdescription(), obj.getMetricsystem(),obj.getExpirydate(),obj.getQuantity(),obj.getQuantityinstock());
                model.addAttribute(editChemical);
                model.addAttribute("itemId",itemId);
                model.addAttribute("navbar", getNavbar(param,cookieValueid));
                model.addAttribute("min", LocalDate.now());
                if (error != null){
                    if (error.equals("1")){
                        model.addAttribute("error", 1);
                    }
                    else{
                        model.addAttribute("error", 3);
                    }
                }
                return "/honeybeehaven/business/editChemical";
            }
            else{
                return "redirect:/HoneyBeeHaven/login?error=3";
            }
        }
        else {
            Chemical obj = new Chemical();
            Integer itemId=Integer.parseInt(iid);
            obj=chemicalRepository.findById(itemId).get();
            EditChemical editChemical=new EditChemical(obj.getItemname(), obj.getItemPrice(), obj.getItemdescription(), obj.getMetricsystem(),obj.getExpirydate(),obj.getQuantity(),obj.getQuantityinstock());
            model.addAttribute(editChemical);
            model.addAttribute("itemId",itemId);
            model.addAttribute("navbar", getNavbar(param,cookieValueid));
            model.addAttribute("min", LocalDate.now());
            if (error != null){
                if (error.equals("1")){
                    model.addAttribute("error", 1);
                }
                else{
                    model.addAttribute("error", 3);
                }
            }
            return "/honeybeehaven/business/editChemical";
        }
    }

    @PostMapping(path = "/editChemical")
    public String updateEditedChemical(@RequestParam("itemId")String iid, Model model,@ModelAttribute("editChemical") EditChemical editChemical,  @RequestParam("file") MultipartFile file) throws IOException{
        Chemical obj = new Chemical();
        Integer itemId=Integer.parseInt(iid);
        obj=chemicalRepository.findById(itemId).get();

        int success = 0;
        if (editChemical.productPrice > 0  && editChemical.quantity > 0 && editChemical.quantityInStock >= 0)
        {}
        else{
            success = 1;
        }
        if (success == 0){
            obj.setItemname(editChemical.productHeading);
            obj.setItemPrice(editChemical.productPrice);
            obj.setItemdescription(editChemical.productDescription);
            obj.setMetricsystem(editChemical.metricSystem);
            obj.setExpirydate(editChemical.expiryDate);
            obj.setQuantity(editChemical.quantity);
            obj.setQuantityinstock(editChemical.quantityInStock);

            if (!file.isEmpty()) {

                String uploadDirectory = PATHS + "images\\chemical\\";

                String originalFileName = file.getOriginalFilename();

                // Check the file extension
                if (originalFileName != null && originalFileName.lastIndexOf(".") != -1) {
                    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

                    // Example: Check if the file extension is '.jpg' or '.jpeg'
                    if (fileExtension.equalsIgnoreCase(".jpg") || fileExtension.equalsIgnoreCase(".jpeg") || fileExtension.equalsIgnoreCase(".png")) {
                        String fileName = itemId.toString();

                        // Create the file object
                        File imageFile = new File(uploadDirectory + fileName + fileExtension);

                        // Save the file to the specified location
                        file.transferTo(imageFile);

                        // Save the file location as a string in the user's image attribute
                        String fileLocation = "/images/chemical/" + fileName + fileExtension; // Adjust the path as needed
                        // Save the file location in the user's image attribute (you'll need to retrieve the user)
                        obj.setImage(fileLocation);

                    } else {
                        return "redirect:/HoneyBeeHaven/editChemical?itemId="+ iid + "&error=1";

                    }
                } else {
                    return "redirect:/HoneyBeeHaven/editChemical?itemId="+ iid + "&error=1";

                }
            }
            chemicalRepository.save(obj);



            return "redirect:/HoneyBeeHaven/myProducts";
        }
        else{
            return "redirect:/HoneyBeeHaven/editChemical?itemId="+ iid + "&error=1";
        }

    }

    @GetMapping(path = "/editMachine")
    public String editMachine(@RequestParam("itemId")String iid, Model model,HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "error", required = false) String error){

        Cookie[] cookies = request.getCookies();
        String cookieValueid = null,cookieValuetype= null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    cookieValuetype = cookie.getValue();
                }
            }
        }

        Integer param=1;

        if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("c")){
            param=2;
        }else if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("b")){
            param=3;
        }
        if (param == 1){
            return "redirect:/HoneyBeeHaven/login";
        }


        Business business = businessRepository.findById(Integer.parseInt(cookieValueid)).get();

        if (business.getBanned()) {
            Cookie userIdCookie = new Cookie("userid", "");

            userIdCookie.setPath("/"); // Set the cookie's path (root path)

            // Create and set the "usertype" cookie
            Cookie userTypeCookie = new Cookie("usertype","");

            userTypeCookie.setPath("/");

            response.addCookie(userIdCookie);
            response.addCookie(userTypeCookie);

            return "redirect:/HoneyBeeHaven/login?error=2";
        }
        else if (business.getDateBanned() != null){
            LocalDate providedDate = LocalDate.parse(business.getDateBanned(), DateTimeFormatter.ISO_LOCAL_DATE);

            // Get the current date
            LocalDate currentDate = LocalDate.now();

            // Calculate the difference in days
            int daysDifference = Math.abs((int) (currentDate.toEpochDay() - providedDate.toEpochDay()));

            if (daysDifference >= 30){
                business.setDateBanned(null);
                businessRepository.save(business);

                model.addAttribute("navbar", getNavbar(param,cookieValueid));
                Machinery obj = new Machinery();
                Integer itemId=Integer.parseInt(iid);
                obj=machineryRepository.findById(itemId).get();
                EditMachinery  editMachinery = new EditMachinery( obj.getItemname(), obj.getItemPrice(), obj.getItemdescription(), obj.getMachinedimension(),obj.getMachineweight(),obj.getWarranty(),obj.getQuantityinstock());
                model.addAttribute(editMachinery);
                model.addAttribute("itemId",itemId);

                if (error != null){
                    if (error.equals("1")){
                        model.addAttribute("error", 1);
                    }
                    else{
                        model.addAttribute("error", 3);
                    }
                }
                return "/honeybeehaven/business/editMachine";
            }
            else{
                return "redirect:/HoneyBeeHaven/login?error=3";
            }
        }
        else {
            model.addAttribute("navbar", getNavbar(param,cookieValueid));
            Machinery obj = new Machinery();
            Integer itemId=Integer.parseInt(iid);
            obj=machineryRepository.findById(itemId).get();
            EditMachinery  editMachinery = new EditMachinery( obj.getItemname(), obj.getItemPrice(), obj.getItemdescription(), obj.getMachinedimension(),obj.getMachineweight(),obj.getWarranty(),obj.getQuantityinstock());
            model.addAttribute(editMachinery);
            model.addAttribute("itemId",itemId);

            if (error != null){
                if (error.equals("1")){
                    model.addAttribute("error", 1);
                }
                else{
                    model.addAttribute("error", 3);
                }
            }
            return "/honeybeehaven/business/editMachine";
        }
    }

    @PostMapping(path = "/editMachine")
    public String updateEditedMachine(@RequestParam("itemId")String iid, Model model,@ModelAttribute("editMachine") EditMachinery editMachine ,  @RequestParam("file") MultipartFile file) throws IOException{
        Machinery obj = new Machinery();
        Integer itemId=Integer.parseInt(iid);
        obj=machineryRepository.findById(itemId).get();
        int success = 0;
        if (editMachine.productPrice > 0  && editMachine.weight > 0 && editMachine.quantityInStock >= 0)
        { }
        else{
            success = 1;
        }
        if (success == 0) {
            obj.setItemname(editMachine.productHeading);
            obj.setItemPrice(editMachine.productPrice);
            obj.setItemdescription(editMachine.productDescription);
            obj.setMachinedimension(editMachine.dimension);
            obj.setMachineweight(editMachine.weight);
            obj.setWarranty(editMachine.warranty);
            obj.setQuantityinstock(editMachine.quantityInStock);

            if (!file.isEmpty()) {

                String uploadDirectory = PATHS + "images\\machinery\\";

                String originalFileName = file.getOriginalFilename();

                // Check the file extension
                if (originalFileName != null && originalFileName.lastIndexOf(".") != -1) {
                    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

                    // Example: Check if the file extension is '.jpg' or '.jpeg'
                    if (fileExtension.equalsIgnoreCase(".jpg") || fileExtension.equalsIgnoreCase(".jpeg") || fileExtension.equalsIgnoreCase(".png")) {
                        String fileName = itemId.toString();

                        // Create the file object
                        File imageFile = new File(uploadDirectory + fileName + fileExtension);

                        // Save the file to the specified location
                        file.transferTo(imageFile);

                        // Save the file location as a string in the user's image attribute
                        String fileLocation = "/images/machinery/" + fileName + fileExtension; // Adjust the path as needed
                        // Save the file location in the user's image attribute (you'll need to retrieve the user)
                        obj.setImage(fileLocation);

                    } else {
                        return "redirect:/HoneyBeeHaven/editMachine?itemId="+iid+"&error=1";
                    }
                } else {
                    return "redirect:/HoneyBeeHaven/editMachine?itemId="+iid+"&error=1";
                }
            }

            machineryRepository.save(obj);
            return "redirect:/HoneyBeeHaven/myProducts";
        }
        else {
            return "redirect:/HoneyBeeHaven/editMachine?itemId="+iid+"&error=1";
        }
    }

    @GetMapping(path = "/editService")
    public String editService(@RequestParam("itemId")String iid, Model model,HttpServletRequest request,HttpServletResponse response, @RequestParam(name = "error", required = false) String error){

        Cookie[] cookies = request.getCookies();
        String cookieValueid = null,cookieValuetype= null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    cookieValuetype = cookie.getValue();
                }
            }
        }
        Integer param=1;
        if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("c")){
            param=2;
        }else if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("b")){
            param=3;
        }
        if(param == 1){
            return "redirect:/HoneyBeeHaven/login";
        }




        Business business = businessRepository.findById(Integer.parseInt(cookieValueid)).get();

        if (business.getBanned()) {
            Cookie userIdCookie = new Cookie("userid", "");

            userIdCookie.setPath("/"); // Set the cookie's path (root path)

            // Create and set the "usertype" cookie
            Cookie userTypeCookie = new Cookie("usertype","");

            userTypeCookie.setPath("/");

            response.addCookie(userIdCookie);
            response.addCookie(userTypeCookie);

            return "redirect:/HoneyBeeHaven/login?error=2";
        }
        else if (business.getDateBanned() != null){
            LocalDate providedDate = LocalDate.parse(business.getDateBanned(), DateTimeFormatter.ISO_LOCAL_DATE);

            // Get the current date
            LocalDate currentDate = LocalDate.now();

            // Calculate the difference in days
            int daysDifference = Math.abs((int) (currentDate.toEpochDay() - providedDate.toEpochDay()));

            if (daysDifference >= 30){
                business.setDateBanned(null);
                businessRepository.save(business);

                model.addAttribute("navbar", getNavbar(param,cookieValueid));
                Service obj = new Service();
                Integer itemId=Integer.parseInt(iid);
                obj=serviceRepository.findById(itemId).get();
                EditService editServiceForm=new EditService(obj.getItemname(), obj.getItemPrice(), obj.getItemdescription(), obj.getBasecharges(), obj.getIsavailable());
                model.addAttribute(editServiceForm);
                model.addAttribute("itemId",itemId);


                if (error != null){
                    if (error.equals("1")){
                        model.addAttribute("error", 1);
                    }
                    else{
                        model.addAttribute("error", 3);
                    }
                }
                return "/honeybeehaven/business/editService";
            }
            else{
                return "redirect:/HoneyBeeHaven/login?error=3";
            }
        }
        else {
            model.addAttribute("navbar", getNavbar(param,cookieValueid));
            Service obj = new Service();
            Integer itemId=Integer.parseInt(iid);
            obj=serviceRepository.findById(itemId).get();
            EditService editServiceForm=new EditService(obj.getItemname(), obj.getItemPrice(), obj.getItemdescription(), obj.getBasecharges(), obj.getIsavailable());
            model.addAttribute(editServiceForm);
            model.addAttribute("itemId",itemId);


            if (error != null){
                if (error.equals("1")){
                    model.addAttribute("error", 1);
                }
                else{
                    model.addAttribute("error", 3);
                }
            }
            return "/honeybeehaven/business/editService";
        }
    }

    @PostMapping(path = "/editService")
    public String updateEditedService(@RequestParam("itemId")String iid, Model model,@ModelAttribute("editService") EditService editService,  @RequestParam("file") MultipartFile file) throws IOException {
        Service obj = new Service();
        Integer itemId=Integer.parseInt(iid);
        obj=serviceRepository.findById(itemId).get();
        int success = 0;
        if (editService.productPrice > 0 && Integer.parseInt(editService.baseCharges) > 0) {
        }
        else{
            success = 1;
        }
        if (success == 0) {
            obj.setItemname(editService.productHeading);
            obj.setItemPrice(editService.productPrice);
            obj.setItemdescription(editService.productDescription);
            obj.setBasecharges(editService.getBaseCharges());
            obj.setIsavailable(editService.isavailable);
            if (!file.isEmpty()) {

                String uploadDirectory = PATHS + "images\\service\\";

                String originalFileName = file.getOriginalFilename();

                // Check the file extension
                if (originalFileName != null && originalFileName.lastIndexOf(".") != -1) {
                    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

                    // Example: Check if the file extension is '.jpg' or '.jpeg'
                    if (fileExtension.equalsIgnoreCase(".jpg") || fileExtension.equalsIgnoreCase(".jpeg") || fileExtension.equalsIgnoreCase(".png")) {
                        String fileName = itemId.toString();

                        // Create the file object
                        File imageFile = new File(uploadDirectory + fileName + fileExtension);

                        // Save the file to the specified location
                        file.transferTo(imageFile);

                        // Save the file location as a string in the user's image attribute
                        String fileLocation = "/images/service/" + fileName + fileExtension; // Adjust the path as needed
                        // Save the file location in the user's image attribute (you'll need to retrieve the user)
                        obj.setImage(fileLocation);

                    } else {
                        return "redirect:/HoneyBeeHaven/editService?itemId="+iid+"&error=1";
                    }
                } else {
                    return "redirect:/HoneyBeeHaven/editService?itemId="+iid+"&error=1";

                }
            }

            serviceRepository.save(obj);
            return "redirect:/HoneyBeeHaven/myProducts";
        }
        else
        {
            return "redirect:/HoneyBeeHaven/editService?itemId="+iid+"&error=1";

        }
    }

    @GetMapping(path = "/deleteMachine")
    public String deleteMachine(@RequestParam("itemId")String iid){
        Machinery obj=new Machinery();
        obj= machineryRepository.findById(Integer.parseInt(iid)).get();
        obj.setIsdeleted(1);
        machineryRepository.save(obj);
        return "redirect:/HoneyBeeHaven/myProducts";
    }

    @GetMapping(path = "/deleteChemical")
    public String deleteChemical(@RequestParam("itemId")String iid){
        Chemical obj=new Chemical();
        obj= chemicalRepository.findById(Integer.parseInt(iid)).get();
        obj.setIsdeleted(1);
        chemicalRepository.save(obj);
        return "redirect:/HoneyBeeHaven/myProducts";
    }

    @GetMapping(path = "/deleteService")
    public String deleteService(@RequestParam("itemId")String iid){
        Service obj=new Service();
        obj= serviceRepository.findById(Integer.parseInt(iid)).get();
        obj.setIsdeleted(1);
        serviceRepository.save(obj);
        return "redirect:/HoneyBeeHaven/myProducts";
    }

    @PostMapping(path="/addtocart")
    public String addtocart(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam("itemid") String iid, @ModelAttribute("cartForm") CartForm cartForm, @RequestParam("type") String type){
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        String cookieValueType = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())){
                    cookieValueType = cookie.getValue();
                }
            }
        }

        if (cookieValueType != null && cookieValueType.equals("c")) {


            Client client = clientRepository.findById(Integer.parseInt(cookieValueid)).get();




            if (client.getBanned()) {
                Cookie userIdCookie = new Cookie("userid", "");

                userIdCookie.setPath("/"); // Set the cookie's path (root path)

                // Create and set the "usertype" cookie
                Cookie userTypeCookie = new Cookie("usertype","");

                userTypeCookie.setPath("/");

                response.addCookie(userIdCookie);
                response.addCookie(userTypeCookie);

                return "redirect:/HoneyBeeHaven/login?error=2";
            }
            else if (client.getDateBanned() != null){
                LocalDate providedDate = LocalDate.parse(client.getDateBanned(), DateTimeFormatter.ISO_LOCAL_DATE);

                // Get the current date
                LocalDate currentDate = LocalDate.now();

                // Calculate the difference in days
                int daysDifference = Math.abs((int) (currentDate.toEpochDay() - providedDate.toEpochDay()));

                if (daysDifference >= 30){
                    client.setDateBanned(null);
                    clientRepository.save(client);

                    String qty = cartForm.qty;

                    Integer userid = Integer.parseInt(cookieValueid);
                    Integer itemid = Integer.parseInt(iid);

                    if (cartRepository.findByUseridAndItemid(userid, itemid).isPresent()) {
                        Cart cart = cartRepository.findByUseridAndItemid(userid, itemid).get();

                        if (type.equals("c")) {
                            Chemical chemical = chemicalRepository.findById(itemid).get();
                            chemical.setQuantityinstock(chemical.getQuantityinstock() - Integer.parseInt(qty));
                        } else if (type.equals("m")) {
                            Machinery machinery = machineryRepository.findById(itemid).get();
                            machinery.setQuantityinstock(machinery.getQuantityinstock() - Integer.parseInt(qty));
                        }

                        cart.setItemqty(cart.getItemqty() + Integer.parseInt(qty));
                        cartRepository.save(cart);
                    } else {
                        Cart cart = new Cart();
                        cart.setItemqty(Integer.parseInt(qty));
                        cart.setItemid(itemid);
                        cart.setUserid(userid);
                        cart.setItemtype(type);

                        if (type.equals("c")) {
                            Chemical chemical = chemicalRepository.findById(itemid).get();
                            chemical.setQuantityinstock(chemical.getQuantityinstock() - Integer.parseInt(qty));
                            cart.setBusinessid(chemical.getBusinessid());
                            cart.setPrice(chemical.getItemPrice());
                        } else if (type.equals("m")) {
                            Machinery machinery = machineryRepository.findById(itemid).get();
                            machinery.setQuantityinstock(machinery.getQuantityinstock() - Integer.parseInt(qty));
                            cart.setBusinessid(machinery.getBusinessid());
                            cart.setPrice(machinery.getItemPrice());
                        } else {
                            Service service = serviceRepository.findById(itemid).get();

                            ServiceCart serviceCart = new ServiceCart();
                            serviceCart.setItemqty(Integer.parseInt(qty));
                            serviceCart.setItemid(itemid);
                            serviceCart.setUserid(userid);
                            serviceCart.setItemtype(type);
                            serviceCart.setBusinessid(service.getBusinessid());
                            serviceCart.setPrice(service.getItemPrice());

                            serviceCartRepository.save(serviceCart);

                            return "redirect:/HoneyBeeHaven/order?type=1";
                        }

                        cartRepository.save(cart);
                    }

                    return "redirect:/HoneyBeeHaven/showcart";
                }
                else{
                    return "redirect:/HoneyBeeHaven/login?error=3";
                }
            }
            else {
                String qty = cartForm.qty;

                Integer userid = Integer.parseInt(cookieValueid);
                Integer itemid = Integer.parseInt(iid);

                if (cartRepository.findByUseridAndItemid(userid, itemid).isPresent()) {
                    Cart cart = cartRepository.findByUseridAndItemid(userid, itemid).get();

                    if (type.equals("c")) {
                        Chemical chemical = chemicalRepository.findById(itemid).get();
                        chemical.setQuantityinstock(chemical.getQuantityinstock() - Integer.parseInt(qty));
                    } else if (type.equals("m")) {
                        Machinery machinery = machineryRepository.findById(itemid).get();
                        machinery.setQuantityinstock(machinery.getQuantityinstock() - Integer.parseInt(qty));
                    }

                    cart.setItemqty(cart.getItemqty() + Integer.parseInt(qty));
                    cartRepository.save(cart);
                } else {
                    Cart cart = new Cart();
                    cart.setItemqty(Integer.parseInt(qty));
                    cart.setItemid(itemid);
                    cart.setUserid(userid);
                    cart.setItemtype(type);

                    if (type.equals("c")) {
                        Chemical chemical = chemicalRepository.findById(itemid).get();
                        chemical.setQuantityinstock(chemical.getQuantityinstock() - Integer.parseInt(qty));
                        cart.setBusinessid(chemical.getBusinessid());
                        cart.setPrice(chemical.getItemPrice());
                    } else if (type.equals("m")) {
                        Machinery machinery = machineryRepository.findById(itemid).get();
                        machinery.setQuantityinstock(machinery.getQuantityinstock() - Integer.parseInt(qty));
                        cart.setBusinessid(machinery.getBusinessid());
                        cart.setPrice(machinery.getItemPrice());
                    } else {
                        Service service = serviceRepository.findById(itemid).get();

                        ServiceCart serviceCart = new ServiceCart();
                        serviceCart.setItemqty(Integer.parseInt(qty));
                        serviceCart.setItemid(itemid);
                        serviceCart.setUserid(userid);
                        serviceCart.setItemtype(type);
                        serviceCart.setBusinessid(service.getBusinessid());
                        serviceCart.setPrice(service.getItemPrice());

                        serviceCartRepository.save(serviceCart);

                        return "redirect:/HoneyBeeHaven/order?type=1";
                    }

                    cartRepository.save(cart);
                }

                return "redirect:/HoneyBeeHaven/showcart";
            }
        }
        else {
            return "redirect:/HoneyBeeHaven/login";
        }
    }

    @GetMapping(path="/showcart")
    public String showcart(Model model, HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        String cookieValueType = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())){
                    cookieValueType = cookie.getValue();
                }
            }
        }

        if (cookieValueType != null && cookieValueType.equals("c")) {

            Client client = clientRepository.findById(Integer.parseInt(cookieValueid)).get();

            if (client.getBanned()) {
                Cookie userIdCookie = new Cookie("userid", "");

                userIdCookie.setPath("/"); // Set the cookie's path (root path)

                // Create and set the "usertype" cookie
                Cookie userTypeCookie = new Cookie("usertype","");

                userTypeCookie.setPath("/");

                response.addCookie(userIdCookie);
                response.addCookie(userTypeCookie);

                return "redirect:/HoneyBeeHaven/login?error=2";
            }
            else if (client.getDateBanned() != null){
                LocalDate providedDate = LocalDate.parse(client.getDateBanned(), DateTimeFormatter.ISO_LOCAL_DATE);

                // Get the current date
                LocalDate currentDate = LocalDate.now();

                // Calculate the difference in days
                int daysDifference = Math.abs((int) (currentDate.toEpochDay() - providedDate.toEpochDay()));

                if (daysDifference >= 30){
                    client.setDateBanned(null);
                    clientRepository.save(client);

                    Integer userid = Integer.parseInt(cookieValueid);
                    List<Cart> cartItems = cartRepository.findAllByUserid(userid);
                    List<DisplayCart> carts = new ArrayList<>();
                    for (int i = 0; i < cartItems.size(); i++) {
                        Cart cart = cartItems.get(i);
                        DisplayCart displayCart = new DisplayCart();
                        displayCart.itemid = cart.getItemid();
                        displayCart.userid = cart.getUserid();
                        displayCart.type = cart.getItemtype();
                        displayCart.qty = cart.getItemqty();
                        displayCart.deleteURL = "/HoneyBeeHaven/deletecart?itemid=" + displayCart.itemid;
                        if (cart.getItemtype().equals("c")) {
                            Chemical chemical = chemicalRepository.findById(cart.getItemid()).get();
                            displayCart.itemqty = chemical.getQuantityinstock();
                            displayCart.itemName = chemical.getItemname();
                            displayCart.itemPrice = chemical.getItemPrice();
                            displayCart.subTotal = displayCart.itemPrice * displayCart.qty;
                            displayCart.type = "Chemical";
                            displayCart.viewHref = "/HoneyBeeHaven/chemicalDesignPage?itemId=" + chemical.getItemid();
                        } else if (cart.getItemtype().equals("m")) {
                            Machinery machinery = machineryRepository.findById(cart.getItemid()).get();
                            displayCart.itemqty = machinery.getQuantityinstock();
                            displayCart.itemName = machinery.getItemname();
                            displayCart.itemPrice = machinery.getItemPrice();
                            displayCart.subTotal = displayCart.itemPrice * displayCart.qty;
                            displayCart.type = "Machinery";
                            displayCart.viewHref = "/HoneyBeeHaven/machineDesignPage?itemId=" + machinery.getItemid();
                        }

                        carts.add(displayCart);
                    }
                    model.addAttribute("cartItems", carts);

                    Float HoneyBeeHavenCommission = Float.valueOf( (float)0.0), cartSubtotal = Float.valueOf( (float)0.0), cartTotal = Float.valueOf( (float)0.0);
                    for (int i = 0; i < carts.size(); i++) {
                        cartSubtotal += carts.get(i).subTotal;
                    }

                    DecimalFormat decimalFormat = new DecimalFormat("#.###");




                    HoneyBeeHavenCommission = Float.valueOf( (float)0.08) * cartSubtotal;
                    cartTotal = HoneyBeeHavenCommission + cartSubtotal;
                    model.addAttribute("cartSubtotal", Float.parseFloat(decimalFormat.format(cartSubtotal)));
                    model.addAttribute("HoneyBeeHavenCommission", Float.parseFloat(decimalFormat.format(HoneyBeeHavenCommission)));
                    model.addAttribute("cartTotal", Float.parseFloat(decimalFormat.format(cartTotal)));
                    model.addAttribute("navbar", getNavbar(2, cookieValueid));
                    return "/honeybeehaven/client/cart";

                }
                else{
                    return "redirect:/HoneyBeeHaven/login?error=3";
                }
            }
            else {
                Integer userid = Integer.parseInt(cookieValueid);
                List<Cart> cartItems = cartRepository.findAllByUserid(userid);
                List<DisplayCart> carts = new ArrayList<>();
                for (int i = 0; i < cartItems.size(); i++) {
                    Cart cart = cartItems.get(i);
                    DisplayCart displayCart = new DisplayCart();
                    displayCart.itemid = cart.getItemid();
                    displayCart.userid = cart.getUserid();
                    displayCart.type = cart.getItemtype();
                    displayCart.qty = cart.getItemqty();
                    displayCart.deleteURL = "/HoneyBeeHaven/deletecart?itemid=" + displayCart.itemid;
                    if (cart.getItemtype().equals("c")) {
                        Chemical chemical = chemicalRepository.findById(cart.getItemid()).get();
                        displayCart.itemqty = chemical.getQuantityinstock();
                        displayCart.itemName = chemical.getItemname();
                        displayCart.itemPrice = chemical.getItemPrice();
                        displayCart.subTotal = displayCart.itemPrice * displayCart.qty;
                        displayCart.type = "Chemical";
                        displayCart.viewHref = "/HoneyBeeHaven/chemicalDesignPage?itemId=" + chemical.getItemid();
                    } else if (cart.getItemtype().equals("m")) {
                        Machinery machinery = machineryRepository.findById(cart.getItemid()).get();
                        displayCart.itemqty = machinery.getQuantityinstock();
                        displayCart.itemName = machinery.getItemname();
                        displayCart.itemPrice = machinery.getItemPrice();
                        displayCart.subTotal = displayCart.itemPrice * displayCart.qty;
                        displayCart.type = "Machinery";
                        displayCart.viewHref = "/HoneyBeeHaven/machineDesignPage?itemId=" + machinery.getItemid();
                    }

                    carts.add(displayCart);
                }
                model.addAttribute("cartItems", carts);

                Float HoneyBeeHavenCommission = Float.valueOf( (float)0.0), cartSubtotal = Float.valueOf( (float)0.0), cartTotal = Float.valueOf( (float)0.0);
                for (int i = 0; i < carts.size(); i++) {
                    cartSubtotal += carts.get(i).subTotal;
                }

                DecimalFormat decimalFormat = new DecimalFormat("#.###");




                HoneyBeeHavenCommission = Float.valueOf( (float)0.08) * cartSubtotal;
                cartTotal = HoneyBeeHavenCommission + cartSubtotal;
                model.addAttribute("cartSubtotal", Float.parseFloat(decimalFormat.format(cartSubtotal)));
                model.addAttribute("HoneyBeeHavenCommission", Float.parseFloat(decimalFormat.format(HoneyBeeHavenCommission)));
                model.addAttribute("cartTotal", Float.parseFloat(decimalFormat.format(cartTotal)));
                model.addAttribute("navbar", getNavbar(2, cookieValueid));

                return "/honeybeehaven/client/cart";
            }
        }
        else{
            return "redirect:/HoneyBeeHaven/login";
        }
    }

    @GetMapping(path="/deletecart")
    public String deletecart(Model model, HttpServletRequest request, @RequestParam("itemid") String iid){
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
            }
        }

        Integer userid = Integer.parseInt(cookieValueid);
        Integer itemid = Integer.parseInt(iid);

        Cart cart = cartRepository.findByUseridAndItemid(userid, itemid).get();

        if (chemicalRepository.findById(itemid).isPresent()){
            Chemical chemical = chemicalRepository.findById(itemid).get();
            chemical.setQuantityinstock(chemical.getQuantityinstock() + cart.getItemqty());
            chemicalRepository.save(chemical);
        }
        else if (machineryRepository.findById(itemid).isPresent()){
            Machinery machinery = machineryRepository.findById(itemid).get();
            machinery.setQuantityinstock(machinery.getQuantityinstock() + cart.getItemqty());
            machineryRepository.save(machinery);
        }

        cartRepository.delete(cart);

        return "redirect:/HoneyBeeHaven/showcart";
    }

    @GetMapping(path="/checkout")
    public String checkoutCart(Model model, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
            }
        }
        Integer userid = Integer.parseInt(cookieValueid);

        return "redirect:/HoneyBeeHaven/order";
    }

    @GetMapping(path="/order")
    public String orders(Model model, HttpServletRequest request, @RequestParam(value = "error", required = false) String error,  @RequestParam(value = "type", required = false) String type){
        if (error != null){
            model.addAttribute("error", 1);
        }

        if (type != null && type.equals("1")){
            OrderForm orderForm = new OrderForm();
            model.addAttribute(orderForm);

            Cookie[] cookies = request.getCookies();
            String cookieValueid = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("userid".equals(cookie.getName())) {
                        // You've found the cookie with the specific name
                        cookieValueid = cookie.getValue();
                    }
                }
            }

            Integer userid = Integer.parseInt(cookieValueid);
            List<ServiceCart> cartItems = serviceCartRepository.findAllByUserid(userid);
            List<DisplayCart> displayCarts = new ArrayList<>();

            float price = 0;

            if (cartItems.size() == 0){
                return "redirect:/HoneyBeeHaven/marketplace?search=";
            }

            for (int i = 0; i < cartItems.size(); i++) {
                ServiceCart cart = cartItems.get(i);
                DisplayCart displayCart = new DisplayCart();
                displayCart.itemqty = cartItems.get(i).getItemqty();


                price += cart.getItemqty() * serviceRepository.findById(cart.getItemid()).get().getItemPrice() + Integer.parseInt(serviceRepository.findById(cart.getItemid()).get().getBasecharges());
                displayCart.itemName = serviceRepository.findById(cart.getItemid()).get().getItemname();
                displayCart.itemPrice = displayCart.itemqty * serviceRepository.findById(cart.getItemid()).get().getItemPrice();


                displayCarts.add(displayCart);
            }

            String action = "/HoneyBeeHaven/submitorder?type=1";
            DecimalFormat decimalFormat = new DecimalFormat("#.###");
            model.addAttribute("cartItems", displayCarts);
            model.addAttribute("total", Float.parseFloat(decimalFormat.format(price * 0.08 + price)));

            model.addAttribute("action", action);

            return "/honeybeehaven/client/checkout";
        }
        else {

            OrderForm orderForm = new OrderForm();
            model.addAttribute(orderForm);

            Cookie[] cookies = request.getCookies();
            String cookieValueid = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("userid".equals(cookie.getName())) {
                        // You've found the cookie with the specific name
                        cookieValueid = cookie.getValue();
                    }
                }
            }

            Integer userid = Integer.parseInt(cookieValueid);
            List<Cart> cartItems = cartRepository.findAllByUserid(userid);
            List<DisplayCart> displayCarts = new ArrayList<>();
            if (cartItems.size() == 0){
                return "redirect:/HoneyBeeHaven/marketplace?search=";
            }
            float price = 0;

            for (int i = 0; i < cartItems.size(); i++) {
                Cart cart = cartItems.get(i);
                DisplayCart displayCart = new DisplayCart();
                displayCart.itemqty = cartItems.get(i).getItemqty();

                if (cart.getItemtype().equals("c")) {
                    price += cart.getItemqty() * chemicalRepository.findById(cart.getItemid()).get().getItemPrice();
                    displayCart.itemName = chemicalRepository.findById(cart.getItemid()).get().getItemname();
                    displayCart.itemPrice = displayCart.itemqty * chemicalRepository.findById(cart.getItemid()).get().getItemPrice();

                } else if (cart.getItemtype().equals("m")) {
                    price += cart.getItemqty() * machineryRepository.findById(cart.getItemid()).get().getItemPrice();
                    displayCart.itemName = machineryRepository.findById(cart.getItemid()).get().getItemname();
                    displayCart.itemPrice = displayCart.itemqty * machineryRepository.findById(cart.getItemid()).get().getItemPrice();
                } else {
                    price += cart.getItemqty() * serviceRepository.findById(cart.getItemid()).get().getItemPrice() + Integer.parseInt(serviceRepository.findById(cart.getItemid()).get().getBasecharges());
                    displayCart.itemName = serviceRepository.findById(cart.getItemid()).get().getItemname();
                    displayCart.itemPrice = displayCart.itemqty * serviceRepository.findById(cart.getItemid()).get().getItemPrice();
                }

                displayCarts.add(displayCart);
            }

            DecimalFormat decimalFormat = new DecimalFormat("#.###");


            String action = "/HoneyBeeHaven/submitorder";

            model.addAttribute("cartItems", displayCarts);
            model.addAttribute("total", Float.parseFloat(decimalFormat.format(price * 0.08 + price)));
            model.addAttribute("action", action);

            return "/honeybeehaven/client/checkout";
        }
    }

    @GetMapping(path="/marketplace")
    public String marketplace(Model model, HttpServletRequest request, @RequestParam(value = "search", required = false) String search, @RequestParam(value = "rating", required = false) String rating, @RequestParam(value = "location", required = false) String location, @RequestParam(value = "mtype", required = false) String mtype, @RequestParam(value = "ctype", required = false) String ctype, @RequestParam(value = "stype", required = false) String stype, @RequestParam(value = "businesskeywords", required = false) String businesskeywords){

        List<MarketplaceCard> cards = new ArrayList<>();

        List<Chemical> chemicals = chemicalRepository.findAllByItemnameContaining(search);

        List<Machinery> machineries = machineryRepository.findAllByItemnameContaining(search);

        List<Service> services = serviceRepository.findAllByItemnameContaining(search);

        
        for (int i = 0; i < chemicals.size(); i++){
            if (rating!=null&&!rating.isEmpty()){
                if (chemicals.get(i).getItemrating() < Float.parseFloat(rating)){
                    chemicals.remove(i);
                    i--;
                    continue;
                }
            }
            if (ctype!=null&&!ctype.isEmpty()){
                if (!chemicals.get(i).getChemicalType().equals(ctype)){
                    chemicals.remove(i);
                    i--;
                    continue;

                }
            }
            if (businesskeywords!=null&&!businesskeywords.isEmpty()){
                if (!businessRepository.findById(chemicals.get(i).getBusinessid()).get().getTargetkeywords().contains(businesskeywords)){
                    chemicals.remove(i);
                    i--;
                    continue;

                }
            }
            if (location!=null&&!location.isEmpty()){
                if (!businessRepository.findById(chemicals.get(i).getBusinessid()).get().getPrimarylocation().equals(location)){
                    chemicals.remove(i);
                    i--;
                    continue;

                }
            }
            if (chemicals.get(i).getIsdeleted() == 1){
                chemicals.remove(i);
                i--;
                continue;
            }
            if (businessRepository.findById(chemicals.get(i).getBusinessid()).get().getBanned()){
                chemicals.remove(i);
                i--;
                continue;
            }
            if (businessRepository.findById(chemicals.get(i).getBusinessid()).get().getDateBanned() != null){
                Business business = businessRepository.findById(chemicals.get(i).getBusinessid()).get();

                LocalDate providedDate = LocalDate.parse(business.getDateBanned(), DateTimeFormatter.ISO_LOCAL_DATE);

                // Get the current date
                LocalDate currentDate = LocalDate.now();

                // Calculate the difference in days
                int daysDifference = Math.abs((int) (currentDate.toEpochDay() - providedDate.toEpochDay()));

                if (daysDifference >= 30){
                    business.setDateBanned(null);
                    businessRepository.save(business);
                }
                else{
                    chemicals.remove(i);
                    i--;
                    continue;
                }
            }
            if (chemicals.get(i).getQuantityinstock() == 0){
                chemicals.remove(i);
                i--;
                continue;
            }
        }

        for (int i = 0; i < machineries.size(); i++){
            if (rating!=null&&!rating.isEmpty()){
                if (machineries.get(i).getItemrating() < Float.parseFloat(rating)){
                    machineries.remove(i);
                    i--;
                    continue;

                }
            }
            if (mtype!=null&&!mtype.isEmpty()){
                if (!machineries.get(i).getMachinetype().equals(mtype)){
                    machineries.remove(i);
                    i--;
                    continue;

                }
            }
            if (businesskeywords!=null&&!businesskeywords.isEmpty()){
                if (!businessRepository.findById(machineries.get(i).getBusinessid()).get().getTargetkeywords().contains(businesskeywords)){
                    machineries.remove(i);
                    i--;
                    continue;

                }
            }
            if (location!=null&&!location.isEmpty()){
                if (!businessRepository.findById(machineries.get(i).getBusinessid()).get().getPrimarylocation().equals(location)){
                    machineries.remove(i);
                    i--;
                    continue;

                }
            }
            if (machineries.get(i).getIsdeleted() == 1){
                machineries.remove(i);
                i--;
                continue;
            }
            if (businessRepository.findById(machineries.get(i).getBusinessid()).get().getBanned()){
                machineries.remove(i);
                i--;
                continue;
            }
            if (businessRepository.findById(machineries.get(i).getBusinessid()).get().getDateBanned() != null){
                Business business = businessRepository.findById(machineries.get(i).getBusinessid()).get();

                LocalDate providedDate = LocalDate.parse(business.getDateBanned(), DateTimeFormatter.ISO_LOCAL_DATE);

                // Get the current date
                LocalDate currentDate = LocalDate.now();

                // Calculate the difference in days
                int daysDifference = Math.abs((int) (currentDate.toEpochDay() - providedDate.toEpochDay()));

                if (daysDifference >= 30){
                    business.setDateBanned(null);
                    businessRepository.save(business);
                }
                else{
                    machineries.remove(i);
                    i--;
                    continue;
                }
            }
            if( machineries.get(i).getQuantityinstock() == 0){
                machineries.remove(i);
                i--;
                continue;
            }
        }

        for (int i = 0; i < services.size(); i++){
            if (rating!=null&&!rating.isEmpty()){
                if (services.get(i).getItemrating() < Float.parseFloat(rating)){
                    services.remove(i);
                    i--;
                    continue;

                }
            }
            if (stype!=null&&!stype.isEmpty()){
                if (!services.get(i).getServicetype().equals(stype)){
                    services.remove(i);
                    i--;
                    continue;

                }
            }
            if (businesskeywords!=null&&!businesskeywords.isEmpty()){
                if (!businessRepository.findById(services.get(i).getBusinessid()).get().getTargetkeywords().contains(businesskeywords)){
                    services.remove(i);
                    i--;
                    continue;

                }
            }
            if (location!=null&&!location.isEmpty()){
                if (!businessRepository.findById(services.get(i).getBusinessid()).get().getPrimarylocation().equals(location)){
                    services.remove(i);
                    i--;
                    continue;

                }
            }
            if (services.get(i).getIsdeleted() == 1){
                services.remove(i);
                i--;
                continue;
            }
            if (businessRepository.findById(services.get(i).getBusinessid()).get().getBanned()){
                services.remove(i);
                i--;
                continue;
            }
            if (businessRepository.findById(services.get(i).getBusinessid()).get().getDateBanned() != null){
                Business business = businessRepository.findById(services.get(i).getBusinessid()).get();

                LocalDate providedDate = LocalDate.parse(business.getDateBanned(), DateTimeFormatter.ISO_LOCAL_DATE);

                // Get the current date
                LocalDate currentDate = LocalDate.now();

                // Calculate the difference in days
                int daysDifference = Math.abs((int) (currentDate.toEpochDay() - providedDate.toEpochDay()));

                if (daysDifference >= 30){
                    business.setDateBanned(null);
                    businessRepository.save(business);
                }
                else{
                    services.remove(i);
                    i--;
                    continue;
                }
            }
            if (!services.get(i).getIsavailable()){
                services.remove(i);
                i--;
                continue;
            }
        }

        for (Chemical chemical : chemicals) {
            MarketplaceCard marketplaceCard = new MarketplaceCard();
            marketplaceCard.desc = chemical.getItemdescription();
            marketplaceCard.name = chemical.getItemname();
            marketplaceCard.href = "/HoneyBeeHaven/chemicalDesignPage?itemId=" + chemical.getItemid().toString();
            marketplaceCard.type = "Chemical";
            marketplaceCard.price = chemical.getItemPrice();
            marketplaceCard.rating = chemical.getItemrating().toString();
            marketplaceCard.sponsored = chemical.getIssponsored();
            marketplaceCard.img = chemical.getImage();

            cards.add(marketplaceCard);
        }

        for (Machinery machinery : machineries) {
            MarketplaceCard marketplaceCard = new MarketplaceCard();
            marketplaceCard.desc = machinery.getItemdescription();
            marketplaceCard.name = machinery.getItemname();
            marketplaceCard.href = "/HoneyBeeHaven/machineryDesignPage?itemId=" + machinery.getItemid().toString();
            marketplaceCard.type = "Machine";
            marketplaceCard.price = machinery.getItemPrice();
            marketplaceCard.rating = machinery.getItemrating().toString();
            marketplaceCard.sponsored = machinery.getIssponsored();
            marketplaceCard.img = machinery.getImage();

            cards.add(marketplaceCard);
        }

        for (Service service : services) {
            MarketplaceCard marketplaceCard = new MarketplaceCard();
            marketplaceCard.desc = service.getItemdescription();
            marketplaceCard.name = service.getItemname();
            marketplaceCard.href = "/HoneyBeeHaven/serviceDesignPage?itemId=" + service.getItemid().toString();
            marketplaceCard.type = "Service";
            marketplaceCard.price = service.getItemPrice();
            marketplaceCard.rating = service.getItemrating().toString();
            marketplaceCard.sponsored = service.getIssponsored();
            marketplaceCard.img = service.getImage();

            cards.add(marketplaceCard);
        }

        Collections.shuffle(cards);

        cards.sort(Comparator.comparing(MarketplaceCard::isSponsored, Comparator.reverseOrder()));

        model.addAttribute("items", cards);


        Cookie[] cookies = request.getCookies();
        String cookieValueid = null,cookieValuetype= null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }
        Integer param=1;
        if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("c")){
            param=2;
        }else if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("b")){
            param=3;
        }
        model.addAttribute("navbar", getNavbar(param,cookieValueid));
        return "/honeybeehaven/shared/marketplace";
    }

    @PostMapping(path="/submitorder")
    public String makeorder(Model model, HttpServletRequest request, @ModelAttribute("orderForm") OrderForm orderForm, @RequestParam(value = "type", required = false) String type){
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
            }
        }

        Integer userid = Integer.parseInt(cookieValueid);

        if (type != null){
            Order order = new Order();

            List<ServiceCart> cartItems = serviceCartRepository.findAllByUserid(userid);

            order.setClientid(userid);
            order.setShippingaddress(orderForm.shipping);

            Float price = Float.valueOf(0);

            for (int i = 0; i < cartItems.size(); i++) {
                ServiceCart cart = cartItems.get(i);
                price += cart.getItemqty() * serviceRepository.findById(cart.getItemid()).get().getItemPrice() + Float.parseFloat(serviceRepository.findById(cart.getItemid()).get().getBasecharges());

            }

            LocalDate currentDate = LocalDate.now();

            // Define a date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Format the current date as a string
            String formattedDate = currentDate.format(formatter);


            order.setOrderCost(price);
            order.setOrderdate(formattedDate);
            order.setHoneybeehavencommision(price * (float) 0.08);
            order.setTotalcost(order.getHoneybeehavencommision() + order.getOrderCost());

            List<OrderDetails> orderDetails = new ArrayList<>();

            for (int i = 0; i < cartItems.size(); i++) {
                OrderDetails orderDetail = new OrderDetails();
                orderDetail.setItemid(cartItems.get(i).getItemid());
                orderDetail.setItemtype(cartItems.get(i).getItemtype());
                orderDetail.setItemqty(cartItems.get(i).getItemqty());
                orderDetails.add(orderDetail);
            }

            if (orderForm.accountNumber.matches("^03\\d{9}$")) {
                Payment payment = new Payment();
                payment.setTransactionamount(order.getTotalcost());
                payment.setTransactionaccount(orderForm.accountNumber);
                payment.setTransactionplatform("Jazzcash");


                String secretKey = "811s5ca59f";    // Replace with your actual secret key
                String pp_SecureHash = null;


                String apiUrl = "https://sandbox.jazzcash.com.pk/ApplicationAPI/API/Payment/DoTransaction"; // Replace with your actual API endpoint

                RestTemplate restTemplate = new RestTemplate();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                Float amount = payment.getTransactionamount() * 100;

                LocalDateTime currentDateTime = LocalDateTime.now();
                formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                String date = currentDateTime.format(formatter);

                LocalDateTime dateTimePlusOneHour = currentDateTime.plusHours(1);

                formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                String expiry = dateTimePlusOneHour.format(formatter);

                payment.setTransactiondate(order.getOrderdate());
                payment.setTransactionid("T" + date + order.getClientid().toString());

                // Create your JSON data
                String json = "{\n" +
                        "    \"pp_Amount\": \"" + String.valueOf(amount.intValue()) + "\",\n" +
                        "    \"pp_BillReference\": \"Bill\",\n" +
                        "    \"pp_Description\": \"This is description\",\n" +
                        "    \"pp_Language\": \"EN\",\n" +
                        "    \"pp_MerchantID\": \"MC60595\",\n" +
                        "    \"pp_MobileNumber\": \"" + payment.getTransactionaccount() + "\",\n" +
                        "    \"pp_Password\": \"wcs32dd34x\",\n" +
                        "    \"pp_ReturnURL\": \"http://127.0.0.1:5500/jazzcashAPI.html\",\n" +
                        "    \"pp_SecureHash\": \"\",\n" +
                        "    \"pp_TxnCurrency\": \"PKR\",\n" +
                        "    \"pp_TxnDateTime\": \"" + date + "\",\n" +
                        "    \"pp_TxnExpiryDateTime\": \"" + expiry + "\",\n" +
                        "    \"pp_TxnRefNo\": \"" + "T" + date + order.getClientid().toString() + "\",\n" +
                        "    \"pp_TxnType\": \"MWALLET\",\n" +
                        "    \"pp_Version\": \"1.1\",\n" +
                        "    \"ppmpf_1\": \"03123456789\"\n" +
                        "}";

                String str = secretKey + "&" + String.valueOf(amount.intValue()) + "&Bill&This is description&EN&MC60595&" + payment.getTransactionaccount() + "&wcs32dd34x&"
                        + "http://127.0.0.1:5500/jazzcashAPI.html&PKR&" + date + "&" + expiry + "&" + "T" + date + order.getClientid().toString() +
                        "&MWALLET&1.1&03123456789";

                try {
                    pp_SecureHash = Payment.generateHmacSHA256(str, secretKey);
                } catch (Exception e) {

                }

                order = orderRepository.save(order);
                payment.setOrderid(order.getOrderid());

                paymentRepository.save(payment);

                json = json.replace("\"pp_SecureHash\": \"\"", "\"pp_SecureHash\": \"" + pp_SecureHash + "\"");

                HttpEntity<String> requests = new HttpEntity<>(json, headers);

                ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requests, String.class);

                List<Integer> ids = new ArrayList<>();

                for (int i = 0; i < cartItems.size(); i++) {
                    if (!ids.contains(cartItems.get(i).getBusinessid())) {
                        payment = new Payment();
                        payment.setOrderid(order.getOrderid());
                        float prices = 0;
                        for (int j = 0; j < cartItems.size(); j++) {
                            if (cartItems.get(j).getBusinessid().equals(cartItems.get(i).getBusinessid())) {
                                prices += cartItems.get(j).getPrice() * cartItems.get(j).getItemqty() + Float.valueOf(serviceRepository.findById(cartItems.get(j).getItemid()).get().getBasecharges());
                            }
                        }

                        payment.setTransactiondate(order.getOrderdate());
                        payment.setTransactionamount(prices);
                        payment.setTransactionplatform("HoneyBeeHaven");
                        payment.setTransactionaccount(businessRepository.findById(cartItems.get(i).getBusinessid()).get().getBankaccountnumber());

                        paymentRepository.save(payment);
                        ids.add(cartItems.get(i).getBusinessid());

                        Notifications notifications = new Notifications();
                        notifications.setNotificationMessage("A new order has arrived for you");

                        notifications.setUserId(cartItems.get(i).getBusinessid());
                        notifications.setTime(LocalDateTime.now().toString());
                        notificationRepository.save(notifications);
                    }
                }

                for (int i = 0; i < orderDetails.size(); i++) {
                    orderDetails.get(i).setOrderid(order.getOrderid());

                }

                orderDetailsRepository.saveAll(orderDetails);
                serviceCartRepository.deleteAllByUserid(userid);

                return "redirect:/HoneyBeeHaven/clientorders";
            } else {
                return "redirect:/HoneyBeeHaven/order?error=1";
            }
        }
        else {

            Order order = new Order();

            List<Cart> cartItems = cartRepository.findAllByUserid(userid);

            order.setClientid(userid);
            order.setShippingaddress(orderForm.shipping);

            float price = 0;

            for (int i = 0; i < cartItems.size(); i++) {
                Cart cart = cartItems.get(i);

                if (cart.getItemtype().equals("c")) {
                    price += cart.getItemqty() * chemicalRepository.findById(cart.getItemid()).get().getItemPrice();
                } else if (cart.getItemtype().equals("m")) {
                    price += cart.getItemqty() * machineryRepository.findById(cart.getItemid()).get().getItemPrice();
                } else {
                    price += cart.getItemqty() * serviceRepository.findById(cart.getItemid()).get().getItemPrice() + Integer.parseInt(serviceRepository.findById(cart.getItemid()).get().getBasecharges());
                }
            }

            LocalDate currentDate = LocalDate.now();

            // Define a date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Format the current date as a string
            String formattedDate = currentDate.format(formatter);


            order.setOrderCost(price);
            order.setOrderdate(formattedDate);
            order.setHoneybeehavencommision(price * (float) 0.08);
            order.setTotalcost(order.getHoneybeehavencommision() + order.getOrderCost());

            List<OrderDetails> orderDetails = new ArrayList<>();

            for (int i = 0; i < cartItems.size(); i++) {
                OrderDetails orderDetail = new OrderDetails();
                orderDetail.setItemid(cartItems.get(i).getItemid());
                orderDetail.setItemtype(cartItems.get(i).getItemtype());
                orderDetail.setItemqty(cartItems.get(i).getItemqty());
                orderDetails.add(orderDetail);
            }

            if (orderForm.accountNumber.matches("^03\\d{9}$")) {
                Payment payment = new Payment();
                payment.setTransactionamount(order.getTotalcost());
                payment.setTransactionaccount(orderForm.accountNumber);
                payment.setTransactionplatform("Jazzcash");


                String secretKey = "811s5ca59f";    // Replace with your actual secret key
                String pp_SecureHash = null;


                String apiUrl = "https://sandbox.jazzcash.com.pk/ApplicationAPI/API/Payment/DoTransaction"; // Replace with your actual API endpoint

                RestTemplate restTemplate = new RestTemplate();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                Float amount = payment.getTransactionamount() * 100;

                LocalDateTime currentDateTime = LocalDateTime.now();
                formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                String date = currentDateTime.format(formatter);

                LocalDateTime dateTimePlusOneHour = currentDateTime.plusHours(1);

                formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                String expiry = dateTimePlusOneHour.format(formatter);

                payment.setTransactiondate(order.getOrderdate());
                payment.setTransactionid("T" + date + order.getClientid().toString());

                // Create your JSON data
                String json = "{\n" +
                        "    \"pp_Amount\": \"" + String.valueOf(amount.intValue()) + "\",\n" +
                        "    \"pp_BillReference\": \"Bill\",\n" +
                        "    \"pp_Description\": \"This is description\",\n" +
                        "    \"pp_Language\": \"EN\",\n" +
                        "    \"pp_MerchantID\": \"MC60595\",\n" +
                        "    \"pp_MobileNumber\": \"" + payment.getTransactionaccount() + "\",\n" +
                        "    \"pp_Password\": \"wcs32dd34x\",\n" +
                        "    \"pp_ReturnURL\": \"http://127.0.0.1:5500/jazzcashAPI.html\",\n" +
                        "    \"pp_SecureHash\": \"\",\n" +
                        "    \"pp_TxnCurrency\": \"PKR\",\n" +
                        "    \"pp_TxnDateTime\": \"" + date + "\",\n" +
                        "    \"pp_TxnExpiryDateTime\": \"" + expiry + "\",\n" +
                        "    \"pp_TxnRefNo\": \"" + "T" + date + order.getClientid().toString() + "\",\n" +
                        "    \"pp_TxnType\": \"MWALLET\",\n" +
                        "    \"pp_Version\": \"1.1\",\n" +
                        "    \"ppmpf_1\": \"03123456789\"\n" +
                        "}";

                String str = secretKey + "&" + String.valueOf(amount.intValue()) + "&Bill&This is description&EN&MC60595&" + payment.getTransactionaccount() + "&wcs32dd34x&"
                        + "http://127.0.0.1:5500/jazzcashAPI.html&PKR&" + date + "&" + expiry + "&" + "T" + date + order.getClientid().toString() +
                        "&MWALLET&1.1&03123456789";

                try {
                    pp_SecureHash = Payment.generateHmacSHA256(str, secretKey);
                } catch (Exception e) {

                }

                order = orderRepository.save(order);
                payment.setOrderid(order.getOrderid());

                paymentRepository.save(payment);

                json = json.replace("\"pp_SecureHash\": \"\"", "\"pp_SecureHash\": \"" + pp_SecureHash + "\"");

                HttpEntity<String> requests = new HttpEntity<>(json, headers);

                ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requests, String.class);

                List<Integer> ids = new ArrayList<>();

                for (int i = 0; i < cartItems.size(); i++) {
                    if (!ids.contains(cartItems.get(i).getBusinessid())) {
                        payment = new Payment();
                        payment.setOrderid(order.getOrderid());
                        float prices = 0;
                        for (int j = 0; j < cartItems.size(); j++) {
                            if (cartItems.get(j).getBusinessid().equals(cartItems.get(i).getBusinessid())) {
                                prices += cartItems.get(j).getPrice() * cartItems.get(j).getItemqty();
                            }
                        }

                        payment.setTransactiondate(order.getOrderdate());
                        payment.setTransactionamount(prices);
                        payment.setTransactionplatform("HoneyBeeHaven");
                        payment.setTransactionaccount(businessRepository.findById(cartItems.get(i).getBusinessid()).get().getBankaccountnumber());

                        paymentRepository.save(payment);
                        ids.add(cartItems.get(i).getBusinessid());

                        Notifications notifications = new Notifications();
                        notifications.setNotificationMessage("A new order has arrived for you");

                        notifications.setUserId(cartItems.get(i).getBusinessid());
                        notifications.setTime(LocalDateTime.now().toString());
                        notificationRepository.save(notifications);
                    }
                }

                for (int i = 0; i < orderDetails.size(); i++) {
                    orderDetails.get(i).setOrderid(order.getOrderid());

                }

                orderDetailsRepository.saveAll(orderDetails);
                cartRepository.deleteAllByUserid(userid);

                return "redirect:/HoneyBeeHaven/clientorders";
            } else {
                return "redirect:/HoneyBeeHaven/order?error=1";
            }
        }
    }

    @GetMapping(path="/clientorders")
    public String clientorders(Model model, HttpServletRequest request){

        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        String cookieValueType = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    cookieValueType = cookie.getValue();
                }
            }
        }
        if (cookieValueType != null && cookieValueType.equals("c")) {

            Integer userid = Integer.parseInt(cookieValueid);

            List<Order> orders = orderRepository.findAllByClientid(userid);
            List<OrderCard> ordercards = new ArrayList<>();
            List<OrderDetails> orderDetails = new ArrayList<>();

            for (int i = 0; i < orders.size(); i++) {
                orderDetails = orderDetailsRepository.findAllByOrderid(orders.get(i).getOrderid());
                for (int j = 0; j < orderDetails.size(); j++) {
                    OrderCard orderCard = new OrderCard();
                    orderCard.orderid = orders.get(i).getOrderid();
                    orderCard.orderdate = orders.get(i).getOrderdate();
                    orderCard.itemqty = orderDetails.get(j).getItemqty();
                    if (orderDetails.get(j).getItemDispatchDate() != null) {
                        orderCard.dispatchdate = orderDetails.get(j).getItemDispatchDate();
                    } else {
                        orderCard.dispatchdate = null;
                    }
                    if (orderDetails.get(j).getItemDeliveryDate() != null) {
                        orderCard.deliverdate = orderDetails.get(j).getItemDeliveryDate();
                    } else {
                        orderCard.deliverdate = null;
                    }

                    if (orderDetails.get(j).getItemtype().equals("c")) {
                        orderCard.itemname = chemicalRepository.findById(orderDetails.get(j).getItemid()).get().getItemname();
                        orderCard.itemprice = chemicalRepository.findById(orderDetails.get(j).getItemid()).get().getItemPrice();
                        orderCard.itemid = chemicalRepository.findById(orderDetails.get(j).getItemid()).get().getItemid();
                        orderCard.href = "/HoneyBeeHaven/chemicalDesignPage?itemId=" + orderCard.itemid;
                    } else if (orderDetails.get(j).getItemtype().equals("m")) {
                        orderCard.itemname = machineryRepository.findById(orderDetails.get(j).getItemid()).get().getItemname();
                        orderCard.itemprice = machineryRepository.findById(orderDetails.get(j).getItemid()).get().getItemPrice();
                        orderCard.itemid = machineryRepository.findById(orderDetails.get(j).getItemid()).get().getItemid();
                        orderCard.href = "/HoneyBeeHaven/machineryDesignPage?itemId=" + orderCard.itemid;

                    } else {
                        orderCard.itemname = serviceRepository.findById(orderDetails.get(j).getItemid()).get().getItemname();
                        orderCard.itemprice = serviceRepository.findById(orderDetails.get(j).getItemid()).get().getItemPrice();
                        orderCard.itemid = serviceRepository.findById(orderDetails.get(j).getItemid()).get().getItemid();
                        orderCard.href = "/HoneyBeeHaven/serviceDesignPage?itemId=" + orderCard.itemid;
                    }

                    ordercards.add(orderCard);
                }
            }

            model.addAttribute("orderDetails", ordercards);

            model.addAttribute("navbar", getNavbar(2, cookieValueid));
            return "/honeybeehaven/client/clientOrderHistory";
        }
        else{
            return "redirect:/HoneyBeeHaven/login";
        }
    }

    @GetMapping(path="/businessorders")
    public String businessorders(Model model, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        String cookieValueType = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    cookieValueType = cookie.getValue();
                }
            }
        }

        if (cookieValueType != null && cookieValueType.equals("b")) {

            Integer userid = Integer.parseInt(cookieValueid);

            Iterable<Order> orders = orderRepository.findAll();
            List<OrderCard> ordercards = new ArrayList<>();
            List<OrderDetails> orderDetails = new ArrayList<>();

            for (Order order : orders) {
                orderDetails = orderDetailsRepository.findAllByOrderid(order.getOrderid());

                for (int i = 0; i < orderDetails.size(); i++) {
                    if (orderDetails.get(i).getItemtype().equals("c")) {
                        if (chemicalRepository.findById(orderDetails.get(i).getItemid()).get().getBusinessid().equals(userid)) {
                            OrderCard orderCard = new OrderCard();
                            orderCard.itemqty = orderDetails.get(i).getItemqty();
                            orderCard.itemprice = chemicalRepository.findById(orderDetails.get(i).getItemid()).get().getItemPrice();
                            orderCard.itemname = chemicalRepository.findById(orderDetails.get(i).getItemid()).get().getItemname();
                            orderCard.orderdate = order.getOrderdate();
                            orderCard.orderid = order.getOrderid();
                            orderCard.clientname = clientRepository.findById(order.getClientid()).get().getName();

                            orderCard.shippingaddress = order.getShippingaddress();
                            orderCard.itemid = orderDetails.get(i).getItemid();
                            orderCard.clienthref = "/HoneyBeeHaven/view_client?userid="+clientRepository.findById(order.getClientid()).get().getUserid();
                            orderCard.href = "/HoneyBeehaven/viewChemicalPage?itemId="+orderCard.itemid;
                            orderCard.deliverdate = null;
                            orderCard.dispatchdate = null;

                            if (orderDetails.get(i).getItemDispatchDate() != null) {
                                orderCard.dispatchdate = orderDetails.get(i).getItemDispatchDate();
                            }
                            if (orderDetails.get(i).getItemDeliveryDate() != null) {
                                orderCard.deliverdate = orderDetails.get(i).getItemDeliveryDate();
                            }

                            ordercards.add(orderCard);
                        }
                    } else if (orderDetails.get(i).getItemtype().equals("m")) {
                        if (machineryRepository.findById(orderDetails.get(i).getItemid()).get().getBusinessid().equals(userid)) {
                            OrderCard orderCard = new OrderCard();
                            orderCard.itemqty = orderDetails.get(i).getItemqty();
                            orderCard.itemprice = machineryRepository.findById(orderDetails.get(i).getItemid()).get().getItemPrice();
                            orderCard.itemname = machineryRepository.findById(orderDetails.get(i).getItemid()).get().getItemname();
                            orderCard.orderdate = order.getOrderdate();
                            orderCard.orderid = order.getOrderid();
                            orderCard.clientname = clientRepository.findById(order.getClientid()).get().getName();
                            orderCard.shippingaddress = order.getShippingaddress();
                            orderCard.itemid = orderDetails.get(i).getItemid();
                            orderCard.clienthref = "/HoneyBeeHaven/view_client?userid="+clientRepository.findById(order.getClientid()).get().getUserid();
                            orderCard.href = "/HoneyBeehaven/viewMachinePage?itemId="+orderCard.itemid;
                            orderCard.deliverdate = null;
                            orderCard.dispatchdate = null;

                            if (orderDetails.get(i).getItemDispatchDate() != null) {
                                orderCard.dispatchdate = orderDetails.get(i).getItemDispatchDate();
                            }
                            if (orderDetails.get(i).getItemDeliveryDate() != null) {
                                orderCard.deliverdate = orderDetails.get(i).getItemDeliveryDate();
                            }

                            ordercards.add(orderCard);
                        }
                    } else {
                        if (serviceRepository.findById(orderDetails.get(i).getItemid()).get().getBusinessid().equals(userid)) {
                            OrderCard orderCard = new OrderCard();
                            orderCard.itemqty = orderDetails.get(i).getItemqty();
                            orderCard.itemprice = serviceRepository.findById(orderDetails.get(i).getItemid()).get().getItemPrice();
                            orderCard.itemname = serviceRepository.findById(orderDetails.get(i).getItemid()).get().getItemname();
                            orderCard.orderdate = order.getOrderdate();
                            orderCard.orderid = order.getOrderid();
                            orderCard.clientname = clientRepository.findById(order.getClientid()).get().getName();
                            orderCard.shippingaddress = order.getShippingaddress();
                            orderCard.itemid = orderDetails.get(i).getItemid();
                            orderCard.clienthref = "/HoneyBeeHaven/view_client?userid="+clientRepository.findById(order.getClientid()).get().getUserid();
                            orderCard.href = "/HoneyBeehaven/viewServicePage?itemId="+orderCard.itemid;
                            orderCard.deliverdate = null;
                            orderCard.dispatchdate = null;

                            if (orderDetails.get(i).getItemDispatchDate() != null) {
                                orderCard.dispatchdate = orderDetails.get(i).getItemDispatchDate();
                            }
                            if (orderDetails.get(i).getItemDeliveryDate() != null) {
                                orderCard.deliverdate = orderDetails.get(i).getItemDeliveryDate();
                            }

                            ordercards.add(orderCard);
                        }
                    }

                }
            }

            model.addAttribute("orderDetails", ordercards);
            model.addAttribute("navbar", getNavbar(3, cookieValueid));
            return "/honeybeehaven/business/orderPage";
        }
        else {
            return "redirect:/HoneyBeeHaven/login";
        }
    }

    @GetMapping(path="/adddispatchdate")
    public String adddispatchdate(Model model, HttpServletRequest request, @RequestParam("orderid") String oid, @RequestParam("itemid") String iid){
        Integer orderid = Integer.parseInt(oid);
        Integer itemid = Integer.parseInt(iid);
        OrderDetails orderDetails = orderDetailsRepository.findByOrderidAndItemid(orderid, itemid);
        orderDetails.setItemDispatchDate(LocalDate.now().toString());

        Notifications notifications = new Notifications();
        notifications.setNotificationMessage("One of your items you ordered have been dispatched");

        Integer clientid = orderRepository.findById(orderid).get().getClientid();

        notifications.setUserId(clientid);
        notifications.setTime(LocalDateTime.now().toString());
        notificationRepository.save(notifications);
        orderDetailsRepository.save(orderDetails);


        return "redirect:/HoneyBeeHaven/businessorders";
    }

    @GetMapping(path="/adddeliverydate")
    public String adddeliverydate(Model model, HttpServletRequest request, @RequestParam("orderid") String oid, @RequestParam("itemid") String iid){
        Integer orderid = Integer.parseInt(oid);
        Integer itemid = Integer.parseInt(iid);
        OrderDetails orderDetails = orderDetailsRepository.findByOrderidAndItemid(orderid, itemid);

        Notifications notifications = new Notifications();
        notifications.setNotificationMessage("One of your item you sold has been delivered");
        Integer businessid = 1;
        if (chemicalRepository.findById(itemid).isPresent()){
            businessid = chemicalRepository.findById(itemid).get().getBusinessid();
        }
        if (machineryRepository.findById(itemid).isPresent()){
            businessid = machineryRepository.findById(itemid).get().getBusinessid();
        }
        if (serviceRepository.findById(itemid).isPresent()){
            businessid = serviceRepository.findById(itemid).get().getBusinessid();
        }

        notifications.setUserId(businessid);
        notifications.setTime(LocalDateTime.now().toString());
        notificationRepository.save(notifications);
        orderDetailsRepository.save(orderDetails);

        orderDetails.setItemDeliveryDate(LocalDate.now().toString());
        orderDetailsRepository.save(orderDetails);
        return "redirect:/HoneyBeeHaven/clientorders";
    }

    @GetMapping(path="/promote")
    public String promote(Model model, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        String cookieValueType = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())){
                    cookieValueType = cookie.getValue();
                }
            }
        }

        if (cookieValueType != null && cookieValueType.equals("b")) {

            Integer userid = Integer.parseInt(cookieValueid);

            if (subscriptionRepository.findByBusinessidAndExpired(userid, false).isPresent()) {
                model.addAttribute("disabled", 1);
            } else {
                model.addAttribute("disabled", 0);
            }
            model.addAttribute("navbar", getNavbar(3, cookieValueid));
            return "/honeybeehaven/business/promote";
        }
        else{
            return "redirect:/HoneyBeeHaven/login";
        }
    }

    @GetMapping(path="/businesscheckout")
    public String businessorder(Model model, HttpServletRequest request, @RequestParam(value = "error", required = false) String error){
        if (error == null){
            model.addAttribute("error", 1);
        }

        OrderForm orderForm = new OrderForm();
        model.addAttribute(orderForm);

        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
            }
        }

        Integer userid = Integer.parseInt(cookieValueid);
        float price = 3999;
        List<DisplayCart> displayCarts = new ArrayList<>();
        DisplayCart displayCart = new DisplayCart();
        displayCart.itemName = "AgroElite Subscription";
        displayCart.itemqty = 1;

        displayCart.itemPrice = Float.valueOf(3999);
        displayCarts.add(displayCart);


        model.addAttribute("cartItems", displayCarts);
        model.addAttribute("total", price);

        return "/honeybeehaven/business/business_checkout";
    }

    @PostMapping(path = "/buysubscription")
    public String buysubscription(Model model, HttpServletRequest request, @ModelAttribute("orderForm") OrderForm orderForm){
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
            }
        }

        Integer userid = Integer.parseInt(cookieValueid);

        Subscription subscription = new Subscription();

        LocalDate currentDate = LocalDate.now();

        // Define a date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Format the current date as a string
        String formattedDate = currentDate.format(formatter);

        LocalDate dateOneMonthLater = currentDate.plusMonths(1);

        // Format the new date as a string
        String formattedDateOneMonthLater = dateOneMonthLater.format(formatter);

        subscription.setBusinessid(userid);
        subscription.setSubtype(1);
        subscription.setStartdate(formattedDate);
        subscription.setEnddate(formattedDateOneMonthLater);

        if (orderForm.accountNumber.matches("^03\\d{9}$")){
            Payment payment = new Payment();
            payment.setTransactionamount(3999);
            payment.setTransactionaccount(orderForm.accountNumber);
            payment.setTransactionplatform("Jazzcash");


            String secretKey = "811s5ca59f";    // Replace with your actual secret key
            String pp_SecureHash = null;


            String apiUrl = "https://sandbox.jazzcash.com.pk/ApplicationAPI/API/Payment/DoTransaction"; // Replace with your actual API endpoint

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Float amount = payment.getTransactionamount() * 100;

            LocalDateTime currentDateTime = LocalDateTime.now();
            formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String date = currentDateTime.format(formatter);

            LocalDateTime dateTimePlusOneHour = currentDateTime.plusHours(1);

            formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String expiry = dateTimePlusOneHour.format(formatter);

            payment.setTransactiondate(date);
            payment.setTransactionid("T" + date + userid.toString());

            // Create your JSON data
            String json = "{\n" +
                    "    \"pp_Amount\": \"" +  String.valueOf(amount.intValue()) + "\",\n" +
                    "    \"pp_BillReference\": \"Bill\",\n" +
                    "    \"pp_Description\": \"This is description\",\n" +
                    "    \"pp_Language\": \"EN\",\n" +
                    "    \"pp_MerchantID\": \"MC60595\",\n" +
                    "    \"pp_MobileNumber\": \"" + payment.getTransactionaccount() + "\",\n" +
                    "    \"pp_Password\": \"wcs32dd34x\",\n" +
                    "    \"pp_ReturnURL\": \"http://127.0.0.1:5500/jazzcashAPI.html\",\n" +
                    "    \"pp_SecureHash\": \"\",\n" +
                    "    \"pp_TxnCurrency\": \"PKR\",\n" +
                    "    \"pp_TxnDateTime\": \"" + date + "\",\n" +
                    "    \"pp_TxnExpiryDateTime\": \"" + expiry + "\",\n" +
                    "    \"pp_TxnRefNo\": \"" + "T" + date + userid.toString() + "\",\n" +
                    "    \"pp_TxnType\": \"MWALLET\",\n" +
                    "    \"pp_Version\": \"1.1\",\n" +
                    "    \"ppmpf_1\": \"03123456789\"\n" +
                    "}";

            String str = secretKey + "&" + String.valueOf(amount.intValue()) + "&Bill&This is description&EN&MC60595&" + payment.getTransactionaccount() + "&wcs32dd34x&"
                    + "http://127.0.0.1:5500/jazzcashAPI.html&PKR&" + date + "&" + expiry + "&" + "T" + date + userid.toString() +
                    "&MWALLET&1.1&03123456789";

            try {
                pp_SecureHash = Payment.generateHmacSHA256(str, secretKey);
            }
            catch (Exception e){

            }




            json = json.replace("\"pp_SecureHash\": \"\"", "\"pp_SecureHash\": \"" + pp_SecureHash + "\"");

            HttpEntity<String> requests = new HttpEntity<>(json, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requests, String.class);

            subscription.setExpired(false);
            subscription = subscriptionRepository.save(subscription);
            payment.setSubscriptionid(subscription.getSubscriptionid());
            paymentRepository.save(payment);

            List<Chemical> chemicals = chemicalRepository.findAllByBusinessid(userid);
            List<Service> services = serviceRepository.findAllByBusinessid(userid);
            List<Machinery> machineries = machineryRepository.findAllByBusinessid(userid);

            for (int i = 0; i < chemicals.size(); i++){
                chemicals.get(i).setIssponsored(true);
                chemicalRepository.save(chemicals.get(i));
            }
            for (int i = 0; i < services.size(); i++){
                services.get(i).setIssponsored(true);
                serviceRepository.save(services.get(i));
            }
            for (int i = 0; i < machineries.size(); i++){
                machineries.get(i).setIssponsored(true);
                machineryRepository.save(machineries.get(i));
            }


            return "redirect:/HoneyBeeHaven/businessdashboard";
        }
        else{
            return "redirect:/HoneyBeeHaven/businesscheckout?error=1";
        }
    }

    @GetMapping(path = "/privacyPolicy")
    public String privacyPolicy(){
        return "honeybeehaven/home/privacyPolicy";
    }

    @GetMapping(path = "/termsAndConditions")
    public String termsAndConditions(){
        return "honeybeehaven/home/termsAndConditions";
    }

    @GetMapping(path = "/about")
    public String about(Model model){

        return "honeybeehaven/home/about";
    }

    @GetMapping(path = "/contact")
    public String contact(Model model){
        ContactCard obj=new ContactCard();
        model.addAttribute("contactCard",obj);
        return "honeybeehaven/home/contact";
    }

    @PostMapping(path = "/contact")
    public String saveContact(Model model,@ModelAttribute("contactCard") ContactCard contactCard){
        Contact obj=new Contact();
        obj.setEmail(contactCard.contactEmail);
        obj.setName(contactCard.contactName);
        obj.setMessage(contactCard.contactMessage);
        obj.setSubject(contactCard.contactSubject);
        contactRepository.save(obj);
        return "redirect:/HoneyBeeHaven/contact";
    }

    @GetMapping(path = "/index")
    public String index(Model model, HttpServletRequest request){
        model.addAttribute("navbar", getNavbar(1, ""));

        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        String cookieValuetype = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }

        if (cookieValuetype != null && !cookieValuetype.isEmpty() && cookieValuetype.equals("c")){
            return "redirect:/HoneyBeeHaven/clientdashboard";
        }
        if (cookieValuetype != null && !cookieValuetype.isEmpty() && cookieValuetype.equals("b")){
            return "redirect:/HoneyBeeHaven/businessdashboard";
        }

        return "honeybeehaven/index";
    }

    @GetMapping(path = "/notifications")
    public String notifications(@RequestParam("userid") String userid,Model model,HttpServletRequest request){
        List<Notifications> notifications= notificationRepository.findAllByUserId(Integer.parseInt(userid));
        List<Notification> notificationSet=new ArrayList<>();
        for (int i=0;i<notifications.size();i++){
            Notification notificate=new Notification();
             notificate.notificationId=notifications.get(i).getNotificationId();
             notificate.userId=notifications.get(i).getUserId();
             notificate.notificationMessage=notifications.get(i).getNotificationMessage();
             notificate.href="/HoneyBeeHaven/deleteNotification?notificationId="+notificate.notificationId;
             notificate.time = notifications.get(i).getTime();
            notificationSet.add(notificate);
        }

        model.addAttribute("notifications",notificationSet);

        Cookie[] cookies = request.getCookies();
        String cookieValueid = null,cookieValuetype= null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }
        Integer param=1;
        if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("c")){
            param=2;
        }else if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("b")){
            param=3;
        }
        if (param == 1){
            return "redirect:/HoneyBeeHaven/login";
        }

        model.addAttribute("navbar", getNavbar(param,cookieValueid));
        return "honeybeehaven/shared/notifications";
    }

    @GetMapping(path = "deleteNotification")
    public String deleteNotification(@RequestParam("notificationId") String notifiId,HttpServletRequest request){
      Notifications obj=new Notifications();
      obj= notificationRepository.findById(Integer.parseInt(notifiId)).get();
      notificationRepository.delete(obj);

        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    cookieValueid = cookie.getValue();
                }
            }
        }
        Integer userid = Integer.parseInt(cookieValueid);
      return "redirect:/HoneyBeeHaven/notifications?userid="+userid.toString();
    }

    public String getNavbar(Integer var,String userId){
        String navbar="";
        if(var==1){
            navbar="  <nav class=\"navbar navbar-expand-lg bg-primary navbar-dark shadow-sm py-3 py-lg-0 px-3 px-lg-5 sticky-lg-top\">\n" +
                    "        <a th:href=\"#\" class=\"navbar-brand d-flex d-lg-none\">\n" +
                    "            <h1 class=\"m-0 display-4 text-secondary\"><span class=\"text-white\">Honeybee</span>Haven</h1>\n" +
                    "        </a>\n" +
                    "        <button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\" data-bs-target=\"#navbarCollapse\">\n" +
                    "            <span class=\"navbar-toggler-icon\"></span>\n" +
                    "        </button>\n" +
                    "        <div class=\"collapse navbar-collapse\" id=\"navbarCollapse\">\n" +
                    "            <div class=\"navbar-nav mx-auto py-0\">\n" +
                    "                <a href=\"/HoneyBeeHaven/index\" class=\"nav-item nav-link active\">Home</a>\n" +
                    "                <a href=\"/HoneyBeeHaven/about\" class=\"nav-item nav-link\">About</a>\n" +
                    "                <a href=\"/HoneyBeeHaven/marketplace?search=\" class=\"nav-item nav-link\">Marketplace</a>\n" +
                    "                <a href=\"/HoneyBeeHaven/contact\" class=\"nav-item nav-link\">Contact</a>\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "        </div>\n" +
                    "    </nav>";
        }
        else if (var==2) {
            navbar=" <nav class=\"navbar navbar-expand-lg bg-primary navbar-dark shadow-sm py-3 py-lg-0 px-3 px-lg-5\">\n" +
                    "        <a href=\"#\" class=\"navbar-brand d-flex d-lg-none\">\n" +
                    "            <h1 class=\"m-0 display-4 text-secondary\" ><span class=\"text-white\">Honeybee</span>Haven</h1>\n" +
                    "        </a>\n" +
                    "        <button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\" data-bs-target=\"#navbarCollapse\">\n" +
                    "            <span class=\"navbar-toggler-icon\"></span>\n" +
                    "        </button>\n" +
                    "        <div class=\"collapse navbar-collapse\" id=\"navbarCollapse\">\n" +
                    "            <div class=\"navbar-nav mx-auto py-0\">\n" +
                    "                <a href=\"/HoneyBeeHaven/marketplace?search=\" class=\"nav-item nav-link \">Marketplace</a>\n" +
                    "                <a href=\"/HoneyBeeHaven/chats\" class=\"nav-item nav-link\">Inbox</a>\n" +
                    "                <a href=\"/HoneyBeeHaven/clientdashboard\" class=\"nav-item nav-link\">Me</a>\n" +
                    "                <a href=\"/HoneyBeeHaven/clientorders\" class=\"nav-item nav-link\">My Orders</a>\n" +
                    "      <a href=\"/HoneyBeeHaven/notifications?userid="+userId+"\" class=\"nav-item nav-link\"><i class=\"fa-solid fa-bell fa-xl fa-flip-horizontal\" style=\"color: #e1e727;\"></i></a>\n" +
                    "             </div >\n" +
                    "                         <a  href=\"/HoneyBeeHaven/showcart\" class=\"nav-item nav-link\" style=\"color: black;\" title=\"Cart\">\n" +
                    "                            <i href=\"\" class=\"fa-solid fa-xl fa-cart-shopping\" style=\"color: \t#FAF9F6;  \" ></i>\n" +
                    "                        </a>\n" +

                    "                          <a  href=\"/HoneyBeeHaven/logout\" class=\"nav-item nav-link\" style=\"color: red;\"> <button style=\"padding: 5px;  border-radius: 50%; background-color: #E10118 \" > "+
                    "<i class=\"fa-solid fa-xl fa-power-off\" style=\"color: white;  \" ></i>\n" +
                    "                        </button></a>\n" +
                    "                    </div>"+
                    "            </div>\n" +
                    "              </div>\n" +
                    "        </div>\n" +
                    "    </nav>";
        }
        else if (var==3) {
            navbar="<nav class=\"navbar navbar-expand-lg bg-primary navbar-dark shadow-sm py-3 py-lg-0 px-3 px-lg-5 sticky-lg-top\">\n" +
                    "        <a href=\"#\" class=\"navbar-brand d-flex d-lg-none\">\n" +
                    "            <h1 class=\"m-0 display-4 text-secondary\" ><span class=\"text-white\">Honeybee</span>Haven</h1>\n" +
                    "        </a>\n" +
                    "        <button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\" data-bs-target=\"#navbarCollapse\">\n" +
                    "            <span class=\"navbar-toggler-icon\"></span>\n" +
                    "        </button>\n" +
                    "        <div class=\"collapse navbar-collapse\" id=\"navbarCollapse\">\n" +
                    "            <div class=\"navbar-nav mx-auto py-0\">\n" +
                    "                <a href=\"/HoneyBeeHaven/marketplace?search=\" class=\"nav-item nav-link \">Marketplace</a>\n" +
                    "                <a href=\"/HoneyBeeHaven/createItem\" class=\"nav-item nav-link\">Create!</a>\n" +
                    "                <a href=\"/HoneyBeeHaven/myProducts\" class=\"nav-item nav-link\">My products</a>\n" +
                    "                <a href=\"/HoneyBeeHaven/businessorders\" class=\"nav-item nav-link\">Orders</a>\n" +
                    "                <a href=\"/HoneyBeeHaven/promote\" class=\"nav-item nav-link\">Promote</a>\n" +
                    "                <a href=\"/HoneyBeeHaven/chats\" class=\"nav-item nav-link\">Inbox</a>\n" +
                    "                <a href=\"/HoneyBeeHaven/businessdashboard\" class=\"nav-item nav-link\">Me</a>\n" +
                    "      <a href=\"/HoneyBeeHaven/notifications?userid="+userId+"\" class=\"nav-item nav-link\"><i class=\"fa-solid fa-bell fa-xl fa-flip-horizontal\" style=\"color: #e1e727;\"></i></a>\n" +
                    "             </div >\n" +

                    "                          <a  href=\"/HoneyBeeHaven/logout\" class=\"nav-item nav-link\" style=\"color: red;\"> <button style=\"padding: 5px;  border-radius: 50%; background-color: #E10118 \" > "+
                    "<i class=\"fa-solid fa-xl fa-power-off\" style=\"color: white;  \" ></i>\n" +
                    "                        </button></a>\n" +
                    "                    </div>"+
                    "            </div>\n" +
                    "              </div>\n" +
                    "        </div>\n" +
                    "    </nav>";
        }
        return navbar;
    }

    @GetMapping(path="/reportReview")
    public String reportReview(Model model, @RequestParam("reviewID")String rid, @RequestParam("complaineeID")String cid, HttpServletRequest request){
        reportreviewForm rrf = new reportreviewForm();
        rrf.reviewid = Integer.parseInt(rid);
        rrf.complaineeid = Integer.parseInt(cid);

        rrf.reviewcontent = reviewRepository.findById(Integer.parseInt(rid)).get().getText();

        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        String cookieValuetype = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }Integer param=1;
        if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("c")){
            param=2;
        }else if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("b")){
            param=3;
        }
        if (param == 1){
            return "redirect:/HoneyBeeHaven/login";
        }

        model.addAttribute("navbar", getNavbar(param,cookieValueid));

        model.addAttribute("reportreviewForm", rrf);



        return "/honeybeehaven/business/reportReview";
    }

    @PostMapping(path="/submitreportReview")
    public String submitreportReview(Model model, HttpServletRequest request, @ModelAttribute("reportreviewFrom")reportreviewForm rrf){
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    cookieValueid = cookie.getValue();
                }
            }
        }
        Integer userid = Integer.parseInt(cookieValueid);

        ReportedReviews rrobj = new ReportedReviews();
        rrobj.setComplaineeID(rrf.complaineeid);
        rrobj.setComplainerID(userid);
        rrobj.setReviewID(rrf.reviewid);
        rrobj.setReportReason(rrf.reportreason);
        rrobj.setReportType(rrf.reportType);
        rrobj.setReviewContent(rrf.reviewcontent);

        rrRepository.save(rrobj);

        return "redirect:/HoneyBeeHaven/businessdashboard";
    }

    @GetMapping(path="/reportBusiness")
    public String reportBusiness(Model model,  @RequestParam("complaineeID")String cid, HttpServletRequest request){
        reportbusinessForm rbf = new reportbusinessForm();
        rbf.complainee3id = Integer.parseInt(cid);

        model.addAttribute("reportbusinessForm", rbf);
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        String cookieValuetype = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }Integer param=1;
        if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("c")){
            param=2;
        }else if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("b")){
            param=3;
        }
        if (param == 1){
            return "redirect:/HoneyBeeHaven/login";
        }

        model.addAttribute("navbar", getNavbar(param,cookieValueid));

        return "/honeybeehaven/client/reportBusiness";
    }

    @PostMapping(path="/submitreportBusiness")
    public String submitreportBusiness(Model model, HttpServletRequest request, @ModelAttribute("reportbusinessFrom")reportbusinessForm rbf){
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    cookieValueid = cookie.getValue();
                }
            }
        }
        Integer userid = Integer.parseInt(cookieValueid);

        ReportedBusiness rbobj = new ReportedBusiness();
        rbobj.setComplainee3ID(rbf.complainee3id);
        rbobj.setComplainer3ID(userid);
        rbobj.setReportReason3(rbf.reportreason3);
        rbobj.setReportType(rbf.reportType);

        rbRepository.save(rbobj);

        return "redirect:/HoneyBeeHaven/clientdashboard";
    }

    @GetMapping(path="/reportClient")
    public String reportClient(Model model,  @RequestParam("complaineeID")String cid, HttpServletRequest request){
        reportclientForm rcf = new reportclientForm();
        rcf.complainee2id = Integer.parseInt(cid);

        model.addAttribute("reportclientForm", rcf);

        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        String cookieValuetype = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValueid = cookie.getValue();
                }
                if ("usertype".equals(cookie.getName())) {
                    // You've found the cookie with the specific name
                    cookieValuetype = cookie.getValue();
                }
            }
        }Integer param=1;
        if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("c")){
            param=2;
        }else if(cookieValueid!=null&&!cookieValueid.isEmpty()&&cookieValuetype.equals("b")){
            param=3;
        }
        if (param == 1){
            return "redirect:/HoneyBeeHaven/login";
        }

        model.addAttribute("navbar", getNavbar(param,cookieValueid));

        return "/honeybeehaven/business/reportClient";
    }

    @PostMapping(path="/submitreportClient")
    public String submitreportClient(Model model, HttpServletRequest request, @ModelAttribute("reportclientFrom")reportclientForm rcf){
        Cookie[] cookies = request.getCookies();
        String cookieValueid = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userid".equals(cookie.getName())) {
                    cookieValueid = cookie.getValue();
                }
            }
        }
        Integer userid = Integer.parseInt(cookieValueid);

        ReportedClients rcobj = new ReportedClients();
        rcobj.setComplainee2ID(rcf.complainee2id);
        rcobj.setComplainer2ID(userid);
        rcobj.setReportReason2(rcf.reportreason2);
        rcobj.setReportType(rcf.reportType);

        rcRepository.save(rcobj);

        return "redirect:/HoneyBeeHaven/businessdashboard";
    }
}

