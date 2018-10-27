import Exceptions.UnknownGroupe;

public interface InterfaceGestionnairePersonne {
    public PersonneReference creerPersonne(String nom, String prenom,GroupeReference groupeReference) throws UnknownGroupe;
}
