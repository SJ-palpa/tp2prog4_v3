import Exceptions.PreviousDate;

import java.util.Date;
import java.util.Objects;

public class Tache {
    private PersonneReference refpers = null;
    private String description = null;
    private Date date = null;
    private Status status = Status.OPEN;
    private Resolution resolution = Resolution.STANDBY;
    private Mode mode = Mode.PARTAGE;

    public Tache(PersonneReference refpers, String description, Date date, Status status, Resolution resolution, Mode mode) throws PreviousDate {
        if(date.compareTo(new Date()) < 0)
        {
            throw new PreviousDate("Vous ne pouvez pas créer une tâche avec une date antérieur à aujourd'hui");
        }
        this.refpers = refpers;
        this.description = description;
        this.date = date;
        this.status = status;
        this.resolution = resolution;
        this.mode = mode;
    }

    public PersonneReference getRefpers() { return refpers; }
    public Mode getMode() { return mode; }
    public Date getDate() {
        return date;
    }
    public Status getStatus() {
        return status;
    }
    public boolean annulerTache() { status = Status.CANCELED; return true; }
    public boolean replanifierTache(Date date) { this.date = date;return true; }

    public void setDate(Date date) { this.date = date; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tache tache = (Tache) o;
        return Objects.equals(description, tache.description) &&
                Objects.equals(date, tache.date) &&
                status == tache.status &&
                resolution == tache.resolution &&
                mode == tache.mode;
    }

    @Override
    public String toString() {
        return "            Tache{" +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", status=" + status +
                ", resolution=" + resolution +
                ", mode=" + mode +
                '}';
    }

    @Override
    public int hashCode() {

        return Objects.hash(description, date, status, resolution, mode);
    }
}
