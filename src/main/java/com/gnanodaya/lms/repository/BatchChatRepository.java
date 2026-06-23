package com.gnanodaya.lms.repository;

import com.gnanodaya.lms.entity.BatchChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BatchChatRepository extends JpaRepository<BatchChat, Long> {
    List<BatchChat> findByBatchIdAndIsDeletedFalseOrderBySentAtAsc(Long batchId);
    List<BatchChat> findBySenderIdOrderBySentAtDesc(Long senderId);
}