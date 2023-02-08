package com.slinkdigital.wedding.service.impl;

import com.slinkdigital.wedding.domain.Reaction;
import com.slinkdigital.wedding.domain.Wedding;
import com.slinkdigital.wedding.dto.ReactionDto;
import com.slinkdigital.wedding.exception.WeddingException;
import com.slinkdigital.wedding.mapper.ReactionMapper;
import com.slinkdigital.wedding.mapper.WeddingMapper;
import com.slinkdigital.wedding.repository.ReactionRepository;
import com.slinkdigital.wedding.repository.WeddingRepository;
import com.slinkdigital.wedding.service.ReactionService;
import java.time.Instant;
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
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final WeddingRepository weddingRepository;
    private final WeddingMapper weddingMapper;
    private final ReactionMapper reactionMapper;

    @Override
    public List<ReactionDto> getReactionsForWedding(Long id) {
        try {
            Wedding wedding = weddingRepository.findById(id).orElseThrow(() -> new WeddingException("No Such Wedding"));
            List<Reaction> reactions = reactionRepository.findByWedding(wedding);
            List<ReactionDto> reactionDtos = new ArrayList<>();
            reactions.forEach(reaction -> {
                reactionDtos.add(reactionMapper.mapReactionToDto(reaction));
            });
            return reactionDtos;
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    @Override
    public List<ReactionDto> addReaction(ReactionDto reactionDto) {
        try {
            Reaction reaction = Reaction.builder()
                    .userId(reactionDto.getUserId())
                    .type(reactionDto.getType())
                    .createdAt(Instant.now())
                    .wedding(weddingMapper.mapWeddingDtoToWedding(reactionDto.getWedding()))
                    .build();
            reactionRepository.save(reaction);
            List<Reaction> reactions = reactionRepository.findByWedding(reaction.getWedding());
            List<ReactionDto> reactionDtos = new ArrayList<>();
            reactions.forEach(r -> {
                reactionDtos.add(reactionMapper.mapReactionToDto(r));
            });
            return reactionDtos;
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }

    @Override
    public List<ReactionDto> updateReaction(ReactionDto reactionDto) {
        try {
            Reaction reaction = reactionRepository.findById(reactionDto.getId()).orElseThrow(()-> new WeddingException("No such reaction"));
            reaction.setType(reactionDto.getType());
            reactionRepository.saveAndFlush(reaction);
            List<Reaction> reactions = reactionRepository.findByWedding(reaction.getWedding());
            List<ReactionDto> reactionDtos = new ArrayList<>();
            reactions.forEach(r -> {
                reactionDtos.add(reactionMapper.mapReactionToDto(r));
            });
            return reactionDtos;
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }
    
    @Override
    public List<ReactionDto> removeReaction(Long id) {
        try {
            Reaction reaction = reactionRepository.findById(id).orElseThrow(()-> new WeddingException("No such reaction"));
            reactionRepository.delete(reaction);
            List<Reaction> reactions = reactionRepository.findByWedding(reaction.getWedding());
            List<ReactionDto> reactionDtos = new ArrayList<>();
            reactions.forEach(r -> {
                reactionDtos.add(reactionMapper.mapReactionToDto(r));
            });
            return reactionDtos;
        } catch (WeddingException ex) {
            throw new WeddingException(ex.getMessage());
        }
    }
}
