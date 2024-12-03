package com.bits.hr.service.dto;

import java.time.LocalDate;
import java.util.List;

public class RoomRequisitionBookingDTO {

    private LocalDate currentDate;

    private List<RoomRequisitionSummaryDTO> roomRequisitionSummaryDTOList;

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public List<RoomRequisitionSummaryDTO> getRoomRequisitionSummaryDTOList() {
        return roomRequisitionSummaryDTOList;
    }

    public void setRoomRequisitionSummaryDTOList(List<RoomRequisitionSummaryDTO> roomRequisitionSummaryDTOList) {
        this.roomRequisitionSummaryDTOList = roomRequisitionSummaryDTOList;
    }
}
