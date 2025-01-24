package com.example.honeybeehaven.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        String sourcePage = request.getHeader("Referer"); // Get the referring page URL

        if (sourcePage != null) {
            if (sourcePage.contains("client_signup")) {
                return "redirect:/HoneyBeeHaven/client_signup?error=3";
            } else if (sourcePage.contains("business_signup")) {
                return "redirect:/HoneyBeeHaven/business_signup?error=3";
            } else if (sourcePage.contains("edit_business")) {
                return "redirect:/HoneyBeeHaven/edit_business?error=3";
            }
            else if (sourcePage.contains("edit_client")) {
                return "redirect:/HoneyBeeHaven/edit_client?error=3";
            }
            else if(sourcePage.contains("createItem")){


                if (sourcePage.contains("machine"))
                {
                    return "redirect:/HoneyBeeHaven/createItem?type=machine&error=3";
                }
                else if (sourcePage.contains("service"))
                {
                    return "redirect:/HoneyBeeHaven/createItem?type=service&error=3";
                }
                else
                {
                    return "redirect:/HoneyBeeHaven/createItem?type=chemical&error=3";
                }

            }
            else if(sourcePage.contains("editChemical")){
                return "redirect:/HoneyBeeHaven/editChemical?itemId="+ sourcePage.substring((sourcePage.indexOf('=')) + 1) +"&error=3";
            }
            else if(sourcePage.contains("editMachine")){
                return "redirect:/HoneyBeeHaven/editMachine?itemId="+ sourcePage.substring((sourcePage.indexOf('=')) + 1) +"&error=3";
            }
            else if(sourcePage.contains("editService")){
                return "redirect:/HoneyBeeHaven/editService?itemId="+ sourcePage.substring((sourcePage.indexOf('=')) + 1) +"&error=3";
            }
        }

        // Handle other cases or redirect to a default page
        return "redirect:/HoneyBeeHaven/default_page?error=3";
    }
}