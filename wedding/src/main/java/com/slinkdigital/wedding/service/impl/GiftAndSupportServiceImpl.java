package com.slinkdigital.wedding.service.impl;

import com.slinkdigital.wedding.domain.GiftAndSupport;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.GiftAndSupportDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.GSMapper;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import com.slinkdigital.wedding.repository.GiftAndSupportRepository;
import com.slinkdigital.wedding.repository.WeddingRepository;
import com.slinkdigital.wedding.service.GiftAndSupportService;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author TEGA
 */
@Service
@AllArgsConstructor
@Slf4j
public class GiftAndSupportServiceImpl implements GiftAndSupportService{
    
    private final GiftAndSupportRepository giftAndSupportRepository;
    private final WeddingRepository weddingRepository;
    private final WeddingMapper weddingMapper;
    private final GSMapper gsm;

    @Override
    public List<GiftAndSupportDto> getAllGiftAndSupportForWedding(Long id) {
        try{
           Wedding wedding = weddingRepository.findById(id).orElseThrow(() -> new WeddingException("No Such Wedding")); 
           List<GiftAndSupport> giftAndSupports = giftAndSupportRepository.findByWedding(wedding);
           List<GiftAndSupportDto> giftAndSupportDtos = new ArrayList<>();
           giftAndSupports.forEach(gs->{
               giftAndSupportDtos.add(gsm.mapGiftAndSupportToDto(gs));
           });
           return giftAndSupportDtos;
        }catch(RuntimeException ex){
            throw new WeddingException(ex.getMessage());
        }
    }

    @Override
    public List<GiftAndSupportDto> add(GiftAndSupportDto giftAndSupportDto) {
        try{
           GiftAndSupport giftAndSupport = gsm.mapDtoToGiftAndSupport(giftAndSupportDto);
           giftAndSupport = giftAndSupportRepository.save(giftAndSupport);
           List<GiftAndSupport> giftAndSupports = giftAndSupportRepository.findByWedding(giftAndSupport.getWedding());
           List<GiftAndSupportDto> giftAndSupportDtos = new ArrayList<>();
           giftAndSupports.forEach(gs->{
               giftAndSupportDtos.add(gsm.mapGiftAndSupportToDto(gs));
           });
           return giftAndSupportDtos;
        }catch(RuntimeException ex){
            throw new WeddingException(ex.getMessage());
        }
    }

    @Override
    public List<GiftAndSupportDto> remove(Long id) {
        try{
            
           GiftAndSupport giftAndSupport = giftAndSupportRepository.findById(id).orElseThrow(()-> new WeddingException("No such Gift Or Support With That Id"));
           giftAndSupportRepository.delete(giftAndSupport);
           List<GiftAndSupport> giftAndSupports = giftAndSupportRepository.findByWedding(giftAndSupport.getWedding());
           List<GiftAndSupportDto> giftAndSupportDtos = new ArrayList<>();
           giftAndSupports.forEach(gs->{
               giftAndSupportDtos.add(gsm.mapGiftAndSupportToDto(gs));
           });
           return giftAndSupportDtos;
        }catch(RuntimeException ex){
            throw new WeddingException(ex.getMessage());
        }
    }

    @Override
    public List<GiftAndSupportDto> update(GiftAndSupportDto giftAndSupportDto) {
        try{
           GiftAndSupport giftAndSupport = giftAndSupportRepository.findById(giftAndSupportDto.getId()).orElseThrow(()-> new WeddingException("No such Gift Or Support With That Id"));
           giftAndSupport.setItemLink(giftAndSupportDto.getItemLink());
           giftAndSupportRepository.saveAndFlush(giftAndSupport);
           List<GiftAndSupport> giftAndSupports = giftAndSupportRepository.findByWedding(weddingMapper.mapWeddingDtoToWedding(giftAndSupportDto.getWedding()));
           List<GiftAndSupportDto> giftAndSupportDtos = new ArrayList<>();
           giftAndSupports.forEach(gs->{
               giftAndSupportDtos.add(gsm.mapGiftAndSupportToDto(gs));
           });
           return giftAndSupportDtos;
        }catch(RuntimeException ex){
            throw new WeddingException(ex.getMessage());
        }
    }        
}
