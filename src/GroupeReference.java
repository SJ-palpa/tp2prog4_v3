import java.util.Objects;

public class GroupeReference {
    private int reference;
    private static int ref = 0;

    public GroupeReference() {
        this.reference = ref++;
    }


    public int getReference() {
        return reference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupeReference that = (GroupeReference) o;
        return Objects.equals(reference, that.reference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reference);
    }
}
