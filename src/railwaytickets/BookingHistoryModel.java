package railwaytickets;

import java.time.LocalDate;

public class BookingHistoryModel {

    private Integer pnrNo, trainNo, noOfSeats;
    private LocalDate dateofJourney;

    public BookingHistoryModel(Integer pnrNo, Integer trainNo, Integer noOfSeats, LocalDate dateofJourney) {
        this.pnrNo = pnrNo;
        this.trainNo = trainNo;
        this.noOfSeats = noOfSeats;
        this.dateofJourney = dateofJourney;
    }

    public Integer getPnrNo() {
        return pnrNo;
    }

    public void setPnrNo(Integer pnrNo) {
        this.pnrNo = pnrNo;
    }

    public Integer getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(Integer trainNo) {
        this.trainNo = trainNo;
    }

    public Integer getNoOfSeats() {
        return noOfSeats;
    }

    public void setNoOfSeats(Integer noOfSeats) {
        this.noOfSeats = noOfSeats;
    }

    public LocalDate getDateofJourney() {
        return dateofJourney;
    }

    public void setDateofJourney(LocalDate dateofJourney) {
        this.dateofJourney = dateofJourney;
    }

    
}
