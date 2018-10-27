import Exceptions.*;

import java.util.Date;
import java.util.List;

public interface InterfaceGestionnaireTache {

    public TacheReference creerTache(PersonneReference personneReference, GroupeReference groupeReference,String description, Date date, Status status, Resolution resolution, Mode mode) throws UnknownPersonne, UnknownGroupe, UnknownPersonnInGroup, PreviousDate;

    public void annulerTache(PersonneReference personneReference, TacheReference tacheReference) throws UnknownPersonne, UnknownTache;

    public void replanifierTache(PersonneReference personneReference, TacheReference tacheReference, Date date) throws UnknownPersonne, UnknownTache, UnknownTacheInPersons;

    public List<TacheReference> consulterTaches(PersonneReference personneReference) throws UnknownPersonne;

    public List<TacheReference> consulterTachesFiltreStatus(PersonneReference personneReference, Status status) throws UnknownPersonne;

    public List<TacheReference> consulterTachesFiltreDateExecution(PersonneReference personneReference, Date date) throws UnknownPersonne;

    public void attributionTache(PersonneReference personneReference, TacheReference tacheReference) throws UnknownPersonne, UnknownTache,Authorizationdenied,TaskAlreadyExist;

    public List<TacheReference> rappelTacheProche();

    public List<TacheReference> rappelTachePasseNonTermine();
}
