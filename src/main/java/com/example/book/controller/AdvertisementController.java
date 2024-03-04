package com.example.book.controller;

import com.example.book.domain.AdvertisementDetails;
import com.example.book.domain.AdvertisementHandling;
import com.example.book.entity.Advertisement;
import com.example.book.repository.AdvertisementRepository;
import com.example.book.service.AdvertisementService;
import com.example.book.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class AdvertisementController {
    private final ModelMapper mapper;
    private final AdvertisementRepository advertisementRepository;
    private final UserService userService;
    private final AdvertisementService advertisementService;

    @RequestMapping(path = "/admin/view-advertisement", method = RequestMethod.GET)
    public String viewCurrentAdvertisements(Model model) {
        userService.addUserAttributesToModel(model);
        List<AdvertisementDetails> advertisements = advertisementService.viewCurrentAdvertisements();
        model.addAttribute("advertisements", advertisements);
        return "admin/advertisement_index";
    }

    @RequestMapping(path = "/admin/approve-advertisement", method = RequestMethod.GET)
    public String approveNewAdvertisement(Model model) {
        userService.updateModel(model);
        model.addAttribute("advertisement", new AdvertisementHandling());
        return "admin/approve_advertisement";
    }

    @RequestMapping(path = "/admin/approve-advertisement", method = RequestMethod.POST)
    public String approveAdvertisementForm(
            @ModelAttribute("advertisement") @Valid AdvertisementHandling advertisementHandling,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            userService.updateModel(model);
            return "admin/approve_advertisement";
        } else if (advertisementRepository.findAdvertisementByURL(advertisementHandling.getLink_url()).isPresent()) {
            userService.updateModel(model);
            result.rejectValue("link_url", null, "This advertisement URL already exists.");
            return "admin/approve_advertisement";
        } else {
            advertisementService.saveAdvertisement(advertisementHandling);
            return "redirect:/admin/view-advertisement";
        }
    }

    @RequestMapping(path = "/admin/update-advertisement/{id}", method = RequestMethod.GET)
    public String updateAdvertisement(@PathVariable("id") Integer advertisement_id, Model model) {
        userService.updateModel(model);
        Advertisement advertisement = advertisementRepository.findById(advertisement_id).orElseThrow();
        AdvertisementHandling advertisementHandling = mapper.map(advertisement, AdvertisementHandling.class);
        model.addAttribute("update_advertisement", advertisementHandling);
        return "admin/update_advertisement";
    }

    @RequestMapping(path = "/admin/update-advertisement", method = RequestMethod.POST)
    public String updateAdvertisementForm(
            @ModelAttribute("update_advertisement") @Valid AdvertisementHandling advertisementHandling,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            userService.updateModel(model);
            return "admin/update_advertisement";
        } else if (advertisementRepository.findAdvertisementByURL(advertisementHandling.getLink_url()).isPresent()) {
            Advertisement advertisement = advertisementRepository.findById(advertisementHandling.getId()).orElseThrow();
            if (Objects.equals(advertisementHandling.getLink_url(), advertisement.getLink_url())) {
                advertisementService.saveAdvertisement(advertisementHandling);
                return "redirect:/admin/view-advertisement";
            } else {
                userService.updateModel(model);
                result.rejectValue("link_url", null, "This advertisement URL already exists.");
                return "admin/update_advertisement";
            }
        } else {
            advertisementService.saveAdvertisement(advertisementHandling);
            return "redirect:/admin/view-advertisement";
        }
    }

    @RequestMapping(path = "/admin/cancel-advertisement/{id}", method = RequestMethod.GET)
    public String cancelAdvertisement(@PathVariable("id") Integer advertisement_id) {
        advertisementService.cancelAdvertisement(advertisement_id);
        return "redirect:/admin/view-advertisement";
    }
}
