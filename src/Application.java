import Exceptions.*;
import org.apache.log4j.Logger;

import java.util.*;

public class Application implements InterfaceGestionnairePersonne,InterfaceGestionnaireTache,InterfaceGestionnaireGroupe{

    final static Logger log = Logger.getLogger(Application.class);

    Map<PersonneReference, Personne> personnes = new HashMap<>();
    Map<TacheReference, Tache> taches = new HashMap<>();
    Map<GroupeReference,Groupe> groupes = new HashMap<>();

    Map<GroupeReference, ArrayList<PersonneReference>> groupe_personnes = new HashMap<>();
    Map<PersonneReference, ArrayList<TacheReference>> personne_taches = new HashMap<>();


    @Override
    public GroupeReference creerGroupe(String nom, String description) {

        Groupe gr = new Groupe(nom,description);
        GroupeReference grRef = new GroupeReference();
        groupes.put(grRef,gr);
        groupe_personnes.put(grRef,new ArrayList<>());
        log.info("La groupe "+grRef+" à bien été crée");
        return grRef;
    }

    @Override
    public PersonneReference creerPersonne(String nom, String prenom, GroupeReference groupeReference) throws UnknownGroupe {

        Personne pers = new Personne(nom,prenom);
        PersonneReference persRef = new PersonneReference();
        personnes.put(persRef,pers);

        if(groupe_personnes.containsKey(groupeReference))
        {
            ArrayList<PersonneReference> arrayPersonneGroupe = groupe_personnes.get(groupeReference);
            arrayPersonneGroupe.add(persRef);
            groupe_personnes.put(groupeReference, arrayPersonneGroupe);
            personne_taches.put(persRef,new ArrayList<>());
            log.info("La personne "+persRef+" à bien été crée");
            return persRef;
        }
        else
        {
            throw new UnknownGroupe("La référence du groupe n'existe pas !");
        }
    }

    @Override
    public void ajouterPersonneAuGroupe(GroupeReference groupeReference, PersonneReference personneReference) throws UnknownGroupe, UnknownPersonne {


        if(!groupe_personnes.containsKey(groupeReference))
        {
            throw new UnknownGroupe("La référence du groupe n'existe pas !");
        }
        else if(!personnes.containsKey(personneReference))
        {
            throw new UnknownPersonne("La référence de la personne n'existe pas !");
        }
        else
        {
            ArrayList<PersonneReference> arrayPersonneGroupe = groupe_personnes.get(groupeReference);
            arrayPersonneGroupe.add(personneReference);
            log.info("La personne "+personneReference+" à bien été ajouté au groupe" +groupeReference);
        }
    }

    @Override
    public void supprimerPersonneDuGroupe(GroupeReference groupeReference, PersonneReference personneReference) throws UnknownGroupe, UnknownPersonne {
        if(!groupe_personnes.containsKey(groupeReference))
        {
            throw new UnknownGroupe("La référence du groupe n'existe pas !");
        }
        else if(!personnes.containsKey(personneReference))
        {
            throw new UnknownPersonne("La référence de la personne n'existe pas !");
        }
        else
        {
            ArrayList<PersonneReference> arrPers = groupe_personnes.get(groupeReference);
            for(int i = 0; arrPers.size()>i ; i++)
            {
                if(arrPers.get(i) == personneReference)
                {
                    arrPers.remove(personneReference);
                    log.info(personneReference + ", à bien supprimé du groupe.");
                }
            }
        }
    }

    @Override
    public TacheReference creerTache(PersonneReference personneReference, GroupeReference groupeReference, String description, Date date, Status status, Resolution resolution, Mode mode) throws UnknownPersonne, UnknownGroupe, PreviousDate,UnknownPersonnInGroup {
        if(!groupe_personnes.containsKey(groupeReference))
        {
            throw new UnknownGroupe("La référence du groupe n'existe pas !");
        }
        else if(!personnes.containsKey(personneReference))
        {
            throw new UnknownPersonne("La référence de la personne n'existe pas !");
        }
        ArrayList<PersonneReference> arrPers = groupe_personnes.get(groupeReference);
        if(arrPers.contains(personneReference))
        {
            Tache tache = new Tache(personneReference,description,date,status,resolution,mode);
            TacheReference tchRef = new TacheReference();
            taches.put(tchRef,tache);

            ArrayList<TacheReference> arrTchRef = personne_taches.get(personneReference);
            if(arrTchRef == null)
            {
                arrTchRef = new ArrayList<>();
                personne_taches.put(personneReference,arrTchRef);
            }
            arrTchRef.add(tchRef);

            log.info(personneReference + ", à bien crée la tâhce :"+tchRef);
            return tchRef;
        }
        else
        {
            throw new UnknownPersonnInGroup(personneReference +", cette personne n'est pas présente dans le groupe");
        }
    }

    @Override
    public void annulerTache(PersonneReference personneReference, TacheReference tacheReference) throws UnknownPersonne, UnknownTache {
        if(!personnes.containsKey(personneReference))
        {
            throw new UnknownPersonne("La référence de la personne n'existe pas !");
        }
        else if(!taches.containsKey(tacheReference))
        {
            throw new UnknownTache("La référence de la personne n'existe pas !");
        }
        ArrayList<TacheReference> arrTchRef = personne_taches.get(personneReference);
        taches.remove(tacheReference);
        arrTchRef.remove(tacheReference);
        log.info("La tache "+tacheReference+" de" +personneReference +" à bien été supprimé");
    }

    @Override
    public void replanifierTache(PersonneReference personneReference, TacheReference tacheReference, Date date) throws UnknownPersonne, UnknownTache ,UnknownTacheInPersons{
        if(!personnes.containsKey(personneReference))
        {
            throw new UnknownPersonne("La référence de la personne n'existe pas !");
        }
        else if(!taches.containsKey(tacheReference))
        {
            throw new UnknownTache("La référence de la personne n'existe pas !");
        }
        ArrayList<TacheReference> arrTchRef = personne_taches.get(personneReference);
        if(arrTchRef.contains(tacheReference) && taches.get(tacheReference).getRefpers().equals(personneReference))
        {
            taches.get(tacheReference).setDate(date);
            log.info("La tache "+tacheReference+" de" +personneReference +" à bien été replanifié");
        }
        else
        {
            throw new UnknownTacheInPersons("La référence de la tâche n'appartien pas à la personne");
        }

    }

    @Override
    public List<TacheReference> consulterTaches(PersonneReference personneReference) throws UnknownPersonne {
        if(!personnes.containsKey(personneReference))
        {
            throw new UnknownPersonne("La référence de la personne n'existe pas !");
        }
        return personne_taches.get(personneReference);
    }

    @Override
    public List<TacheReference> consulterTachesFiltreStatus(PersonneReference personneReference, Status status) throws UnknownPersonne {
        if(!personnes.containsKey(personneReference))
        {
            throw new UnknownPersonne("La référence de la personne n'existe pas !");
        }
        ArrayList<TacheReference> tchRef = personne_taches.get(personneReference);
        ArrayList<TacheReference> tchRefReturn = new ArrayList<>();
        for(int i = 0; tchRef.size()>i ; i++)
        {
            if (taches.get(tchRef.get(i)).getStatus().equals(status))
            {
                tchRefReturn.add(tchRef.get(i));
            }
        }
        return tchRefReturn;
    }

    @Override
    public List<TacheReference> consulterTachesFiltreDateExecution(PersonneReference personneReference, Date date) throws UnknownPersonne {
        if(!personnes.containsKey(personneReference))
        {
            throw new UnknownPersonne("La référence de la personne n'existe pas !");
        }
        ArrayList<TacheReference> tchRef = personne_taches.get(personneReference);
        ArrayList<TacheReference> tchRefReturn = new ArrayList<>();
        for(int i = 0; tchRef.size()>i ; i++)
        {
            if (taches.get(tchRef.get(i)).getDate().equals(date))
            {
                tchRefReturn.add(tchRef.get(i));
            }
        }
        return tchRefReturn;
    }

    @Override
    public void attributionTache(PersonneReference personneReference, TacheReference tacheReference) throws UnknownPersonne, UnknownTache , Authorizationdenied,TaskAlreadyExist{
        if(!personnes.containsKey(personneReference))
        {
            throw new UnknownPersonne("La référence de la personne n'existe pas !");
        }
        else if(!taches.containsKey(tacheReference))
        {
            throw new UnknownTache("La référence de la personne n'existe pas !");
        }
        else if(personne_taches.get(personneReference).contains(tacheReference))
        {
            throw new TaskAlreadyExist("La personne possède déjà cette tache");
        }
        Tache tch = taches.get(tacheReference);
        if(tch.getMode() == Mode.PARTAGE)
        {
            personne_taches.get(personneReference).add(tacheReference);
            log.info("La tache "+tacheReference+" à bien été attribué à " +personneReference );
        }
        else
        {
            throw new Authorizationdenied("Le créateur de la tache n'a pas configurer cette tache en mode partage");
        }
    }

    @Override
    public List<TacheReference> rappelTacheProche() {
        ArrayList<TacheReference> toutesLesTaches = new ArrayList<>(taches.keySet());
        ArrayList<TacheReference> tacheRefReturn = new ArrayList<>();
        for (int i = 0; taches.size()>i ; i++)
        {
            Date currentDate = new Date();
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(currentDate);
            cal1.add(Calendar.DAY_OF_YEAR,1);


            Date date = taches.get(toutesLesTaches.get(i)).getDate();
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date);
            boolean dayPlusOne = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
            if (dayPlusOne && taches.get(toutesLesTaches.get(i)).getStatus() == Status.OPEN)
            {
                tacheRefReturn.add(toutesLesTaches.get(i));
            }
        }
        return tacheRefReturn;
    }

    @Override
    public List<TacheReference> rappelTachePasseNonTermine() {
        ArrayList<TacheReference> toutesLesTaches = new ArrayList<>(taches.keySet());
        ArrayList<TacheReference> tacheRefReturn = new ArrayList<>();
        for (int i = 0; taches.size()>i ; i++) {
           if (taches.get(toutesLesTaches.get(i)).getDate().compareTo(new Date()) < 0 && taches.get(toutesLesTaches.get(i)).getStatus() == Status.OPEN)
           {
               tacheRefReturn.add(toutesLesTaches.get(i));
           }
        }
        return tacheRefReturn;
    }
}
