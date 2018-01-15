
package railwaytickets;


public class PassengerModel {
    
    private String passengerName;
    private String passengerSex;

    public PassengerModel(String passengerName, String passengerSex) {
        this.passengerName = passengerName;
        this.passengerSex = passengerSex;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerSex() {
        return passengerSex;
    }

    public void setPassengerSex(String passengerSex) {
        this.passengerSex = passengerSex;
    }
    
    
    
}
