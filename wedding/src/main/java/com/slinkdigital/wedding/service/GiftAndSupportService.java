package com.slinkdigital.wedding.service;

import com.slinkdigital.wedding.domain.GiftAndSupport;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.GiftAndSupportDto;
import com.slinkdigital.wedding.dto.PaymentRequest;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.GSMapper;
import com.slinkdigital.wedding.repository.GiftAndSupportRepository;
import com.slinkdigital.wedding.repository.WeddingRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@AllArgsConstructor
public class GiftAndSupportService {

    private final GiftAndSupportRepository giftAndSupportRepository;
    private final WeddingRepository weddingRepository;
    private final GSMapper gsm;
    private final PaymentService paymentService;

    public List<GiftAndSupportDto> getAllGiftAndSupportForWedding(Long id) {
        Wedding wedding = weddingRepository.findById(id).orElseThrow(() -> new WeddingException("No Such Wedding"));
        List<GiftAndSupport> giftAndSupports = giftAndSupportRepository.findByWedding(wedding);
        List<GiftAndSupportDto> giftAndSupportDtos = new ArrayList<>();
        giftAndSupports.forEach(gs -> giftAndSupportDtos.add(gsm.mapGiftAndSupportToDto(gs)));
        return giftAndSupportDtos;
    }

    public GiftAndSupportDto sendMoney(PaymentRequest paymentRequest) {
        GiftAndSupport giftAndSupport = gsm.mapDtoToGiftAndSupport(paymentRequest.getGiftAndSupportDto());
        paymentService.chargeCard(paymentRequest);
        giftAndSupport = giftAndSupportRepository.save(giftAndSupport);
        return gsm.mapGiftAndSupportToDto(giftAndSupport);
    }

    public void remove(Long id) {
        GiftAndSupport giftAndSupport = giftAndSupportRepository.findById(id).orElseThrow(() -> new WeddingException("No such Gift Or Support With That Id"));
        giftAndSupportRepository.delete(giftAndSupport);
    }

    public GiftAndSupportDto update(GiftAndSupportDto giftAndSupportDto) {
        GiftAndSupport giftAndSupport = giftAndSupportRepository.findById(giftAndSupportDto.getId()).orElseThrow(() -> new WeddingException("No such Gift Or Support With That Id"));
        giftAndSupport = giftAndSupportRepository.saveAndFlush(giftAndSupport);
        return gsm.mapGiftAndSupportToDto(giftAndSupport);
    }
}
