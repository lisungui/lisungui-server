package com.bizzy.skillbridge.rest.dto;

public class MarkAsReadRequest {
    
    private String userId;
        private String contactId;

        // Getters and setters
        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getContactId() {
            return contactId;
        }

        public void setContactId(String contactId) {
            this.contactId = contactId;
        }
}
