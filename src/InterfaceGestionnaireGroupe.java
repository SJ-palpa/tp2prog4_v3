import Exceptions.UnknownGroupe;
import Exceptions.UnknownPersonne;

public interface InterfaceGestionnaireGroupe {

    public GroupeReference creerGroupe(String nom,String description);

    public void ajouterPersonneAuGroupe(GroupeReference groupeReference,PersonneReference personneReference) throws UnknownGroupe, UnknownPersonne;

    public void supprimerPersonneDuGroupe(GroupeReference groupeReference, PersonneReference personneReference) throws UnknownGroupe, UnknownPersonne;

}
