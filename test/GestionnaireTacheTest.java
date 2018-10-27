import Exceptions.*;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GestionnaireTacheTest {

    Logger log = Logger.getLogger(GestionnaireTacheTest.class);

    Application bank = new Application();

    // Créer une tâche

    @Test
    public void creerTache_TacheBienAjoute() throws UnknownPersonne, UnknownGroupe, PreviousDate, UnknownPersonnInGroup {
        GroupeReference grpRef = bank.creerGroupe("Tango", "danse");
        PersonneReference persref = bank.creerPersonne("musk", "elon", grpRef);
        TacheReference tchRef = bank.creerTache(persref, grpRef, "description Tache", new Date(10000, 10, 1), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
        Assert.assertEquals(true, bank.taches.containsKey(tchRef));
    }

    @Test(expected = UnknownGroupe.class)
    public void creerTache_GroupeInconnu() throws UnknownPersonne, UnknownGroupe, PreviousDate, UnknownPersonnInGroup {
        GroupeReference grpRef1 = null;
        PersonneReference persref1 = bank.creerPersonne("musk1", "elon1", grpRef1);
        TacheReference tchRef1 = bank.creerTache(persref1, grpRef1, "description Tache", new Date(10000, 10, 1), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
        Assert.assertEquals(true, bank.taches.containsKey(tchRef1));
    }

    @Test(expected = UnknownPersonne.class)
    public void creerTache_PersonneInconnu() throws UnknownPersonne, UnknownGroupe, PreviousDate, UnknownPersonnInGroup {
        GroupeReference grpRef2 = bank.creerGroupe("Tango2", "danse2");
        PersonneReference persref2 = null;
        TacheReference tchRef2 = bank.creerTache(persref2, grpRef2, "description Tache", new Date(10000, 10, 1), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
    }

    @Test(expected = PreviousDate.class)
    public void creerTache_DateAnterieur() throws UnknownPersonne, UnknownGroupe, PreviousDate, UnknownPersonnInGroup {
        GroupeReference grpRef3 = bank.creerGroupe("Tango3", "danse3");
        PersonneReference persref3 = bank.creerPersonne("musk3", "elon3", grpRef3);
        TacheReference tchRef3 = bank.creerTache(persref3, grpRef3, "description Tache", new Date(1, 10, 1), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
    }

    @Test(expected = UnknownPersonnInGroup.class)
    public void creerTache_PersonnePasDansGroup() throws UnknownPersonne, UnknownGroupe, PreviousDate, UnknownPersonnInGroup {
        GroupeReference grpRef4 = bank.creerGroupe("Tango4", "danse4");
        GroupeReference grpRef5 = bank.creerGroupe("Tango5", "danse5");
        PersonneReference persref4 = bank.creerPersonne("musk4", "elon4", grpRef4);
        TacheReference tchRef4 = bank.creerTache(persref4, grpRef5, "description Tache", new Date(10000, 10, 1), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
    }

    @Test
    public void creerTache_TacheLierAPersonne() throws UnknownPersonne, UnknownGroupe, PreviousDate, UnknownPersonnInGroup {
        GroupeReference grpRef6 = bank.creerGroupe("Tango6", "danse6");
        PersonneReference persref6 = bank.creerPersonne("musk6", "elon6", grpRef6);
        TacheReference tchRef6 = bank.creerTache(persref6, grpRef6, "description Tache", new Date(10000, 10, 1), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
        Assert.assertEquals(true, bank.personne_taches.get(persref6).contains(tchRef6));
    }

    // Annuler une tâche

    @Test
    public void annulerTache_TacheBienAnnule() throws UnknownPersonne, UnknownGroupe, PreviousDate, UnknownPersonnInGroup, UnknownTache {
        GroupeReference grpRef7 = bank.creerGroupe("Tango7", "danse7");
        PersonneReference persref7 = bank.creerPersonne("musk7", "elon7", grpRef7);
        TacheReference tchRef7 = bank.creerTache(persref7, grpRef7, "description Tache", new Date(10000, 10, 1), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
        bank.annulerTache(persref7, tchRef7);
        Assert.assertEquals(false, bank.personne_taches.get(persref7).contains(tchRef7));
    }

    @Test(expected = UnknownTache.class)
    public void annulerTache_TacheBInconnue() throws UnknownPersonne, UnknownGroupe, UnknownTache {
        GroupeReference grpRef8 = bank.creerGroupe("Tango8", "danse8");
        PersonneReference persref8 = bank.creerPersonne("musk8", "elon8", grpRef8);
        bank.annulerTache(persref8, new TacheReference());
    }

    @Test(expected = UnknownGroupe.class)
    public void annulerTache_GroupeInconnu() throws UnknownPersonne, UnknownGroupe, PreviousDate, UnknownPersonnInGroup, UnknownTache {
        GroupeReference grpRef9 = null;
        PersonneReference persref9 = bank.creerPersonne("musk9", "elon9", grpRef9);
        TacheReference tchRef9 = bank.creerTache(persref9, grpRef9, "description Tache", new Date(10000, 10, 1), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
        bank.annulerTache(persref9, tchRef9);
    }

    @Test(expected = UnknownPersonne.class)
    public void annulerTache_PersonneInconnu() throws UnknownPersonne, UnknownGroupe, PreviousDate, UnknownPersonnInGroup, UnknownTache {
        GroupeReference grpRef10 = bank.creerGroupe("Tango6", "danse6");
        PersonneReference persref10 = null;
        TacheReference tchRef10 = bank.creerTache(persref10, grpRef10, "description Tache", new Date(10000, 10, 1), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
        bank.annulerTache(persref10, tchRef10);
    }

    // Replanifier tache

    @Test
    public void replanifierTache_TacheBienReplanifie() throws UnknownPersonne, UnknownGroupe, PreviousDate, UnknownPersonnInGroup, UnknownTache, UnknownTacheInPersons {
        GroupeReference grpRef10 = bank.creerGroupe("Tango10", "danse10");
        PersonneReference persref10 = bank.creerPersonne("musk10", "elon10", grpRef10);
        TacheReference tchRef10 = bank.creerTache(persref10, grpRef10, "description Tache", new Date(10000, 10, 1), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
        bank.replanifierTache(persref10, tchRef10, new Date(20000, 10, 1));
        Assert.assertEquals(new Date(20000, 10, 1), bank.taches.get(tchRef10).getDate());
    }

    // Consulter taches

    @Test
    public void consulterTaches_ListTacheComplete() throws UnknownPersonne, UnknownGroupe, UnknownPersonnInGroup, PreviousDate {
        GroupeReference grpRef11 = bank.creerGroupe("Tango11", "danse11");
        PersonneReference persref11 = bank.creerPersonne("musk11", "elon12", grpRef11);
        TacheReference tchRef11 = bank.creerTache(persref11, grpRef11, "description Tache", new Date(10000, 10, 1), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
        TacheReference tchRef12 = bank.creerTache(persref11, grpRef11, "description Tache", new Date(10000, 10, 1), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
        TacheReference tchRef13 = bank.creerTache(persref11, grpRef11, "description Tache", new Date(10000, 10, 1), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
        TacheReference tchRef14 = bank.creerTache(persref11, grpRef11, "description Tache", new Date(10000, 10, 1), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
        ArrayList<TacheReference> arrtchref = new ArrayList<>();
        arrtchref.add(tchRef11);
        arrtchref.add(tchRef12);
        arrtchref.add(tchRef13);
        arrtchref.add(tchRef14);
        Assert.assertEquals(arrtchref, bank.consulterTaches(persref11));
    }

    @Test
    public void consulterTaches_ListTacheVide() throws UnknownPersonne, UnknownGroupe, UnknownPersonnInGroup, PreviousDate {
        GroupeReference grpRef12 = bank.creerGroupe("Tango12", "danse12");
        PersonneReference persref12 = bank.creerPersonne("musk12", "elon12", grpRef12);
        ArrayList<TacheReference> arrtchref = new ArrayList<>();
        Assert.assertEquals(arrtchref, bank.consulterTaches(persref12));
    }

    // Consulter taches suivant status

    @Test
    public void consulterTachesFiltreStatus_ListTacheCompleteOpen() throws UnknownPersonne, UnknownGroupe, UnknownPersonnInGroup, PreviousDate {
        GroupeReference grpRef13 = bank.creerGroupe("Tango13", "danse13");
        PersonneReference persref13 = bank.creerPersonne("musk13", "elon13", grpRef13);
        TacheReference tchRef15 = bank.creerTache(persref13, grpRef13, "description Tache", new Date(10000, 10, 1), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
        bank.creerTache(persref13, grpRef13, "description Tache", new Date(10000, 10, 1), Status.CANCELED, Resolution.STANDBY, Mode.PARTAGE);
        bank.creerTache(persref13, grpRef13, "description Tache", new Date(10000, 10, 1), Status.CLOSED, Resolution.STANDBY, Mode.PARTAGE);
        TacheReference tchRef18 = bank.creerTache(persref13, grpRef13, "description Tache", new Date(10000, 10, 1), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
        ArrayList<TacheReference> arrtchref = new ArrayList<>();
        arrtchref.add(tchRef15);
        arrtchref.add(tchRef18);
        Assert.assertEquals(arrtchref, bank.consulterTachesFiltreStatus(persref13, Status.OPEN));
    }

    @Test
    public void consulterTachesFiltreStatus_ListTacheCompleteClosed() throws UnknownPersonne, UnknownGroupe, UnknownPersonnInGroup, PreviousDate {
        GroupeReference grpRef14 = bank.creerGroupe("Tango14", "danse14");
        PersonneReference persref14 = bank.creerPersonne("musk14", "elon14", grpRef14);
        bank.creerTache(persref14, grpRef14, "description Tache", new Date(10000, 10, 1), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
        bank.creerTache(persref14, grpRef14, "description Tache", new Date(10000, 10, 1), Status.CANCELED, Resolution.STANDBY, Mode.PARTAGE);
        TacheReference tchRef19 = bank.creerTache(persref14, grpRef14, "description Tache", new Date(10000, 10, 1), Status.CLOSED, Resolution.STANDBY, Mode.PARTAGE);
        bank.creerTache(persref14, grpRef14, "description Tache", new Date(10000, 10, 1), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
        ArrayList<TacheReference> arrtchref = new ArrayList<>();
        arrtchref.add(tchRef19);
        Assert.assertEquals(arrtchref, bank.consulterTachesFiltreStatus(persref14, Status.CLOSED));
    }

    @Test
    public void consulterTachesFiltreStatus_ListTacheCompleteCanceled() throws UnknownPersonne, UnknownGroupe, UnknownPersonnInGroup, PreviousDate {
        GroupeReference grpRef15 = bank.creerGroupe("Tango15", "danse15");
        PersonneReference persref15 = bank.creerPersonne("musk15", "elon15", grpRef15);
        bank.creerTache(persref15, grpRef15, "description Tache", new Date(10000, 10, 1), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
        TacheReference tchRef20 = bank.creerTache(persref15, grpRef15, "description Tache", new Date(10000, 10, 1), Status.CANCELED, Resolution.STANDBY, Mode.PARTAGE);
        bank.creerTache(persref15, grpRef15, "description Tache", new Date(10000, 10, 1), Status.CLOSED, Resolution.STANDBY, Mode.PARTAGE);
        bank.creerTache(persref15, grpRef15, "description Tache", new Date(10000, 10, 1), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
        ArrayList<TacheReference> arrtchref = new ArrayList<>();
        arrtchref.add(tchRef20);
        Assert.assertEquals(arrtchref, bank.consulterTachesFiltreStatus(persref15, Status.CANCELED));
    }

    // Attribution d'une tache

    @Test
    public void attributionTache_TacheBienAttribue() throws UnknownGroupe, UnknownPersonne, UnknownPersonnInGroup, PreviousDate, TaskAlreadyExist, Authorizationdenied, UnknownTache {
        GroupeReference grpRef16 = bank.creerGroupe("Tango16", "danse16");
        PersonneReference persref16 = bank.creerPersonne("musk16", "elon16", grpRef16);
        PersonneReference persref17 = bank.creerPersonne("musk17", "elon17", grpRef16);
        TacheReference tchRef21 = bank.creerTache(persref16, grpRef16, "description Tache", new Date(10000, 10, 1), Status.CANCELED, Resolution.STANDBY, Mode.PARTAGE);
        bank.attributionTache(persref17, tchRef21);
        ArrayList<TacheReference> arrtchref = new ArrayList<>();
        arrtchref.add(tchRef21);
        Assert.assertEquals(arrtchref, bank.personne_taches.get(persref17));
    }

    @Test(expected = TaskAlreadyExist.class)
    public void attributionTache_TacheExistDeja() throws UnknownGroupe, UnknownPersonne, UnknownPersonnInGroup, PreviousDate, TaskAlreadyExist, Authorizationdenied, UnknownTache {
        GroupeReference grpRef18 = bank.creerGroupe("Tango18", "danse18");
        PersonneReference persref18 = bank.creerPersonne("musk18", "elon18", grpRef18);
        TacheReference tchRef22 = bank.creerTache(persref18, grpRef18, "description Tache", new Date(10000, 10, 1), Status.CANCELED, Resolution.STANDBY, Mode.PARTAGE);
        bank.attributionTache(persref18, tchRef22);
        ArrayList<TacheReference> arrtchref = new ArrayList<>();
        arrtchref.add(tchRef22);
    }

    @Test(expected = Authorizationdenied.class)
    public void attributionTache_TacheModePrive() throws UnknownGroupe, UnknownPersonne, UnknownPersonnInGroup, PreviousDate, TaskAlreadyExist, Authorizationdenied, UnknownTache {
        GroupeReference grpRef19 = bank.creerGroupe("Tango19", "danse19");
        PersonneReference persref19 = bank.creerPersonne("musk19", "elon19", grpRef19);
        PersonneReference persref20 = bank.creerPersonne("musk20", "elon20", grpRef19);
        TacheReference tchRef23 = bank.creerTache(persref19, grpRef19, "description Tache", new Date(10000, 10, 1), Status.CANCELED, Resolution.STANDBY, Mode.PRIVE);
        bank.attributionTache(persref20, tchRef23);
        ArrayList<TacheReference> arrtchref = new ArrayList<>();
        arrtchref.add(tchRef23);
    }

    // Consulter taches suivant Date


    @Test
    public void consulterTachesFiltreDateExecution_ListTacheComplete() throws UnknownGroupe, UnknownPersonne, UnknownPersonnInGroup, PreviousDate {
        GroupeReference grpRef19 = bank.creerGroupe("Tango19", "danse19");
        PersonneReference persref21 = bank.creerPersonne("musk21", "elon21", grpRef19);
        TacheReference tchRef24 = bank.creerTache(persref21, grpRef19, "description Tache", new Date(10000, 10, 1), Status.CANCELED, Resolution.STANDBY, Mode.PARTAGE);
        TacheReference tchRef25 = bank.creerTache(persref21, grpRef19, "description Tache", new Date(10000, 10, 1), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
        bank.creerTache(persref21, grpRef19, "description Tache", new Date(1000, 10, 1), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
        ArrayList<TacheReference> arrtchref = new ArrayList<>();
        arrtchref.add(tchRef24);
        arrtchref.add(tchRef25);
        Assert.assertEquals(arrtchref, bank.consulterTachesFiltreDateExecution(persref21, new Date(10000, 10, 1)));
    }

    // Rappel tache un jour avant

    @Test
    public void rappelTacheProche_RappelTacheEffectue() throws UnknownPersonne, PreviousDate, UnknownPersonnInGroup, UnknownGroupe {
        GroupeReference grpRef20 = bank.creerGroupe("Tango19", "danse19");
        PersonneReference persref22 = bank.creerPersonne("musk21", "elon21", grpRef20);
        Date currentDatePlusUN = new Date();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(currentDatePlusUN);
        cal1.add(Calendar.DAY_OF_YEAR, 1);
        currentDatePlusUN = cal1.getTime();


        bank.creerTache(persref22, grpRef20, "description Tache", new Date(10000, 10, 1), Status.CANCELED, Resolution.STANDBY, Mode.PARTAGE);
        TacheReference tchRef26 = bank.creerTache(persref22, grpRef20, "description Tache", new Date(), Status.CLOSED, Resolution.STANDBY, Mode.PARTAGE);
        TacheReference tchRef27 = bank.creerTache(persref22, grpRef20, "description Tache", new Date(), Status.CANCELED, Resolution.STANDBY, Mode.PARTAGE);
        TacheReference tchRef28 = bank.creerTache(persref22, grpRef20, "description Tache", new Date(), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
        TacheReference tchRef29 = bank.creerTache(persref22, grpRef20, "description Tache", new Date(), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
        bank.taches.get(tchRef26).setDate(currentDatePlusUN);
        bank.taches.get(tchRef27).setDate(currentDatePlusUN);
        bank.taches.get(tchRef28).setDate(currentDatePlusUN);
        bank.taches.get(tchRef29).setDate(currentDatePlusUN);

        ArrayList<TacheReference> arrtchref = new ArrayList<>();
        arrtchref.add(tchRef28);
        arrtchref.add(tchRef29);

        Assert.assertEquals(arrtchref, bank.rappelTacheProche());
    }


    @Test
    public void rappelTachePasseNonTermine() throws UnknownPersonne, PreviousDate, UnknownPersonnInGroup, UnknownGroupe {
        GroupeReference grpRef21 = bank.creerGroupe("Tango19", "danse19");
        PersonneReference persref23 = bank.creerPersonne("musk21", "elon21", grpRef21);
        Date currentDateMoinsUN = new Date();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(currentDateMoinsUN);
        cal1.add(Calendar.DAY_OF_YEAR, -1);
        currentDateMoinsUN = cal1.getTime();


        bank.creerTache(persref23, grpRef21, "description Tache", new Date(10000, 10, 1), Status.CANCELED, Resolution.STANDBY, Mode.PARTAGE);
        TacheReference tchRef30 = bank.creerTache(persref23, grpRef21, "description Tache", new Date(), Status.CLOSED, Resolution.STANDBY, Mode.PARTAGE);
        TacheReference tchRef31 = bank.creerTache(persref23, grpRef21, "description Tache", new Date(), Status.CANCELED, Resolution.STANDBY, Mode.PARTAGE);
        TacheReference tchRef32 = bank.creerTache(persref23, grpRef21, "description Tache", new Date(), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
        TacheReference tchRef33 = bank.creerTache(persref23, grpRef21, "description Tache", new Date(), Status.OPEN, Resolution.STANDBY, Mode.PARTAGE);
        bank.taches.get(tchRef30).setDate(currentDateMoinsUN);
        bank.taches.get(tchRef31).setDate(currentDateMoinsUN);
        bank.taches.get(tchRef32).setDate(currentDateMoinsUN);
        bank.taches.get(tchRef33).setDate(currentDateMoinsUN);

        ArrayList<TacheReference> arrtchref = new ArrayList<>();
        arrtchref.add(tchRef32);
        arrtchref.add(tchRef33);

        Assert.assertEquals(arrtchref, bank.rappelTachePasseNonTermine());

    }
}
