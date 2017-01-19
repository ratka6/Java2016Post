package dataModel;

/**
 * Created by krzysiek on 19.01.2017.
 */
public enum Statuses {
    AWAITS("Oczekuje na odbiór"),
    DELIVERED("Dostarczona"),
    ATCOURIER("U kuriera"),
    CANCELLED("Anulowana"),
    UNKNOWN("Nieznany");

    private final String status;

    public String getStatus() {
        return status;
    }

    Statuses(String nazwa) {
        status = nazwa;
    }

    public static Statuses getStatusByString(String s) {
        if (s.equals(Statuses.ATCOURIER.getStatus())) {
            return Statuses.ATCOURIER;
        }
        else if (s.equals(Statuses.AWAITS.getStatus())) {
            return Statuses.ATCOURIER;
        }
        else if (s.equals(Statuses.CANCELLED.getStatus())) {
            return Statuses.CANCELLED;
        }
        else if (s.equals(Statuses.DELIVERED.getStatus())) {
            return Statuses.DELIVERED;
        }
        else {
            return Statuses.UNKNOWN;
        }
    }

}
