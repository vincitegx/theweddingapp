package com.slinkdigital.wedding.controller;

import com.slinkdigital.wedding.constant.GiftType;
import com.slinkdigital.wedding.dto.GiftAndSupportDto;
import com.slinkdigital.wedding.dto.PaymentRequest;
import com.slinkdigital.wedding.service.GiftAndSupportService;
import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TEGA
 */
@RestController
@RequestMapping("api/ws/v1/weddings")
@RequiredArgsConstructor
public class GiftAndSupportController {
    
    private final GiftAndSupportService giftAndSupportService;
    
    @GetMapping("{weddingId}/gs")
    @ResponseStatus(HttpStatus.OK)
    public List<GiftAndSupportDto> getAllGiftAndSupportForWedding(@PathVariable(value = "weddingId") Long id) {
        return giftAndSupportService.getAllGiftAndSupportForWedding(id);
    }

    @PostMapping("gs/cash")
    @ResponseStatus(HttpStatus.CREATED)
    public GiftAndSupportDto send(@Valid @RequestBody PaymentRequest paymentRequest) {
        return giftAndSupportService.sendMoney(paymentRequest);        
    }
    
    @PutMapping("gs")
    @ResponseStatus(HttpStatus.OK)
    public GiftAndSupportDto update(@Valid @RequestBody GiftAndSupportDto giftAndSupportDto) {
        return giftAndSupportService.update(giftAndSupportDto);
    }

    @DeleteMapping("gs/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        giftAndSupportService.remove(id);
    }
}
