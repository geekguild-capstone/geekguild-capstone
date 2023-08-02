package com.geekguild.services;

import com.geekguild.models.Reaction;
import com.geekguild.repositories.ReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReactionService {

    private final ReactionRepository reactionRepository;

    @Autowired
    public ReactionService(ReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    public Reaction saveReaction(Reaction reaction) {
        // You may want to add additional logic here before saving the reaction.
        // For simplicity, we will just call the repository to save the reaction.
        return reactionRepository.save(reaction);
    }
}
