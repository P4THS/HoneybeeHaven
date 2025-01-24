package com.example.honeybeehaven.controllers;

import com.example.honeybeehaven.repositories.*;
import com.example.honeybeehaven.tables.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class StartupRunner implements CommandLineRunner {

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
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Subscription> subscriptions = subscriptionRepository.findAllByExpired(false);

        for (int i =0; i < subscriptions.size(); i++){
            if (LocalDate.now().toString().equals(subscriptions.get(i).getEnddate())){
                subscriptions.get(i).setExpired(true);
                subscriptionRepository.save(subscriptions.get(i));

                List<Chemical> chemicals = chemicalRepository.findAllByBusinessid(subscriptions.get(i).getBusinessid());
                List<Machinery> machineries = machineryRepository.findAllByBusinessid(subscriptions.get(i).getBusinessid());
                List<Service> services = serviceRepository.findAllByBusinessid(subscriptions.get(i).getBusinessid());

                for (int j = 0; j < chemicals.size(); j++){
                    chemicals.get(j).setIssponsored(false);
                    chemicalRepository.save(chemicals.get(j));
                }
                for (int j = 0; j < machineries.size(); j++){
                    machineries.get(j).setIssponsored(false);
                    machineryRepository.save(machineries.get(j));
                }
                for (int j = 0; j < services.size(); j++){
                    services.get(j).setIssponsored(false);
                    serviceRepository.save(services.get(j));
                }

                Notifications notifications = new Notifications();
                notifications.setNotificationMessage("Your SponsorShip Subscription has been expired");

                notifications.setUserId(subscriptions.get(i).getBusinessid());
                notifications.setTime(LocalDateTime.now().toString());
                notificationRepository.save(notifications);
            }
        }
        Iterable<Client> clients = clientRepository.findAll();
        Iterable<Business> businesses = businessRepository.findAll();

        for (Business business : businesses) {

            if (business.getDateBanned() != null) {

                LocalDate providedDate = LocalDate.parse(business.getDateBanned(), DateTimeFormatter.ISO_LOCAL_DATE);

                // Get the current date
                LocalDate currentDate = LocalDate.now();

                // Calculate the difference in days
                int daysDifference = Math.abs((int) (currentDate.toEpochDay() - providedDate.toEpochDay()));

                if (daysDifference >= 30) {
                    business.setDateBanned(null);
                    businessRepository.save(business);
                }
            }
        }
        for (Client client : clients) {

            if (client.getDateBanned() != null) {

                LocalDate providedDate = LocalDate.parse(client.getDateBanned(), DateTimeFormatter.ISO_LOCAL_DATE);

                // Get the current date
                LocalDate currentDate = LocalDate.now();

                // Calculate the difference in days
                int daysDifference = Math.abs((int) (currentDate.toEpochDay() - providedDate.toEpochDay()));

                if (daysDifference >= 30) {
                    client.setDateBanned(null);
                    clientRepository.save(client);
                }
            }
        }
    }
}
