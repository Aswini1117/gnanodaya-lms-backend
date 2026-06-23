package com.gnanodaya.lms.controller;

import com.gnanodaya.lms.dto.response.ApiResponse;
import com.gnanodaya.lms.entity.BatchChat;
import com.gnanodaya.lms.service.BatchChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batch-chat")
@RequiredArgsConstructor
public class BatchChatController {

    private final BatchChatService batchChatService;

    // Send a message — Student or Instructor
    @PostMapping
    public ResponseEntity<ApiResponse<BatchChat>> sendMessage(@RequestBody BatchChat chat) {
        return ResponseEntity.ok(ApiResponse.success("Message sent", batchChatService.sendMessage(chat)));
    }

    // Get all messages for a batch
    @GetMapping("/{batchId}")
    public ResponseEntity<ApiResponse<List<BatchChat>>> getMessages(@PathVariable Long batchId) {
        return ResponseEntity.ok(ApiResponse.success("Messages", batchChatService.getBatchMessages(batchId)));
    }

    // Delete own message
    @DeleteMapping("/{messageId}/user/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(
            @PathVariable Long messageId, @PathVariable Long userId) {
        batchChatService.deleteMessage(messageId, userId);
        return ResponseEntity.ok(ApiResponse.success("Message deleted", null));
    }
}