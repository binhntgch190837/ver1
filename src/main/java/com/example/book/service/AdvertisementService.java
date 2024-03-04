package com.example.book.service;

import com.example.book.domain.AdvertisementDetails;
import com.example.book.domain.AdvertisementHandling;
import com.example.book.entity.Advertisement;
import com.example.book.repository.AdvertisementRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@EnableAsync
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final ModelMapper mapper;

    public List<AdvertisementDetails> viewCurrentAdvertisements(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy 'at' hh:mm a");
        List<Advertisement> advertisements = advertisementRepository.findAll();
        List<AdvertisementDetails> mapped_advertisements = new ArrayList<>();

        for (Advertisement advertisement : advertisements){
            AdvertisementDetails advertisementDetails = mapper.map(advertisement, AdvertisementDetails.class);
            String start_day = advertisement.getStart_date().format(formatter);
            String expired_day = advertisement.getEnd_date().format(formatter);
            advertisementDetails.setDuration(start_day + " - " + expired_day);
            mapped_advertisements.add(advertisementDetails);
        }

        return mapped_advertisements;
    }

    public void saveAdvertisement(AdvertisementHandling advertisementHandling){
        // Format date from String
        String start_day = advertisementHandling.getStart_day();
        String end_day = advertisementHandling.getEnd_day();
        LocalDate startDay = LocalDate.parse(start_day.trim(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate endDay = LocalDate.parse(end_day.trim(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        //Format time from String
        String start_time = advertisementHandling.getStart_time();
        String end_time = advertisementHandling.getEnd_time();
        LocalTime startTime = LocalTime.parse(start_time.trim(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime endTime = LocalTime.parse(end_time.trim(), DateTimeFormatter.ofPattern("HH:mm"));
        // Set seconds component to zero
        startTime = startTime.withSecond(0);
        endTime = endTime.withSecond(0);
        //Merge time and date
        LocalDateTime startDateTime = startDay.atTime(startTime);
        LocalDateTime endDateTime = endDay.atTime(endTime);
        // Mapping object
        Advertisement advertisement = mapper.map(advertisementHandling, Advertisement.class);
        advertisement.setStart_date(startDateTime);
        advertisement.setEnd_date(endDateTime);
        advertisementRepository.save(advertisement);
    }

    public void cancelAdvertisement(Integer advertisement_id){
        advertisementRepository.deleteById(advertisement_id);
    }

    //Configure advertisements for user pages
    public void getEnabledAdvertisements(Model model){
        List<AdvertisementDetails> advertisementDetailsList = new ArrayList<>();
        List<Advertisement> advertisements = advertisementRepository.selectEnabledAdvertises("Enabled");
        // Shuffle the random_ads list to randomize the order
        Collections.shuffle(advertisements);
        // Select the first advertisement randomly
        Random random = new Random();
        Advertisement first_ad = advertisements.get(random.nextInt(0, advertisements.size()));
        AdvertisementDetails first_advertisementDetails = mapper.map(first_ad, AdvertisementDetails.class);
        model.addAttribute("first_ad", first_advertisementDetails);
        advertisements.remove(first_ad);
        // Select the second advertisement randomly
        Advertisement second_ad = advertisements.get(random.nextInt(0, advertisements.size()));
        AdvertisementDetails second_advertisementDetails = mapper.map(second_ad, AdvertisementDetails.class);
        model.addAttribute("second_ad", second_advertisementDetails);
        advertisements.remove(second_ad);
        // Select the last advertisement
        Advertisement third_ad = advertisements.get(0);
        AdvertisementDetails third_advertisementDetails = mapper.map(third_ad, AdvertisementDetails.class);
        model.addAttribute("third_ad", third_advertisementDetails);
    }

    @Async
    @Scheduled(fixedRate = 120000)
    public void updateStatusForExpiredAdvertisements(){
        LocalDateTime current_time = LocalDateTime.now();
        List<Advertisement> advertisements = advertisementRepository.selectEnabledAdvertises("Enabled");

        // Update status for expired advertisements
        for (Advertisement advertisement : advertisements){
            if (advertisement.getEnd_date().isBefore(current_time)){
                advertisement.setStatus("Disabled");
                advertisementRepository.save(advertisement);
            }
        }
    }
}
