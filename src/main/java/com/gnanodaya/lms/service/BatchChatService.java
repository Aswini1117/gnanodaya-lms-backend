package com.gnanodaya.lms.service;

import com.gnanodaya.lms.entity.BatchChat;
import com.gnanodaya.lms.repository.BatchChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchChatService {

    private final BatchChatRepository batchChatRepository;

    public BatchChat sendMessage(BatchChat chat) {
        chat.setIsDeleted(false);
        if (chat.getMessageType() == null) chat.setMessageType("TEXT");
        return batchChatRepository.save(chat);
    }

    public List<BatchChat> getBatchMessages(Long batchId) {
        return batchChatRepository.findByBatchIdAndIsDeletedFalseOrderBySentAtAsc(batchId);
    }

    public void deleteMessage(Long messageId, Long requesterId) {
        BatchChat chat = batchChatRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        if (!chat.getSender().getId().equals(requesterId)) {
            throw new RuntimeException("You can only delete your own messages");
        }
        chat.setIsDeleted(true);
        batchChatRepository.save(chat);
    }
}