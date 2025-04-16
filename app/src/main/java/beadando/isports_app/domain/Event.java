package beadando.isports_app.domain;

import com.google.firebase.Timestamp;

import java.util.List;

public class Event {
   private String id;
   private String title;
   private String location;
   private Timestamp date;
   private Boolean fee;
   private Integer participants;
   private String description;
   private String createdBy;
   private List<String> participantsList;
   private Timestamp createdAt;

   public Event() {}

   public Event(
           String title,
           String location,
           Timestamp date,
           Boolean fee,
           Integer participants,
           String description,
           String createdBy,
           List<String> participantsList
   ) {
      this.title = title;
      this.location = location;
      this.date = date;
      this.fee = fee;
      this.participants = participants;
      this.description = description;
      this.createdBy = createdBy;
      this.participantsList = participantsList;
      this.createdAt = Timestamp.now();
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public Timestamp getCreatedAt() {
      return createdAt;
   }

   public void setCreatedAt(Timestamp createdAt) {
      this.createdAt = createdAt;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getLocation() {
      return location;
   }

   public void setLocation(String location) {
      this.location = location;
   }

   public Timestamp getDate() {
      return date;
   }

   public void setDate(Timestamp date) {
      this.date = date;
   }

   public Boolean getFee() {
      return fee;
   }

   public void setFee(Boolean fee) {
      this.fee = fee;
   }

   public Integer getParticipants() {
      return participants;
   }

   public void setParticipants(Integer participants) {
      this.participants = participants;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getCreatedBy() {
      return createdBy;
   }

   public void setCreatedBy(String createdBy) {
      this.createdBy = createdBy;
   }

   public List<String> getParticipantsList() {
      return participantsList;
   }

   public void setParticipantsList(List<String> participantsList) {
      this.participantsList = participantsList;
   }
}
