import Exceptions.UnknownGroupe;
import Exceptions.UnknownPersonne;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class GestionnaireGroupeTest {

    Logger log = Logger.getLogger(GestionnaireTacheTest.class);

    Application bank = new Application();


    @Test
    public void creerGroupe_RefExist(){
        GroupeReference grRef = bank.creerGroupe("Tango","danse");
        Assert.assertEquals(true, bank.groupes.containsKey(grRef));
    }

    @Test
    public void creerGroupe_InfoNom(){
        GroupeReference grRef1 = bank.creerGroupe("Tango1","danse1");
        Assert.assertEquals("Tango1", bank.groupes.get(grRef1).getNom());
    }

    @Test
    public void creerGroupe_InfoDescription(){
        GroupeReference grRef2 = bank.creerGroupe("Tango2","danse2");
        Assert.assertEquals("danse2", bank.groupes.get(grRef2).getDescription());
    }



    //ajouterPersonne Au Groupe

    @Test
    public void ajouterPersonneAuGroupe_PersonneBienAjoute() throws UnknownGroupe, UnknownPersonne {
        GroupeReference grpRef3 = bank.creerGroupe("Tango3","danse3");
        PersonneReference persref3 =bank.creerPersonne("musk3","elon3",grpRef3);
        bank.ajouterPersonneAuGroupe(grpRef3,persref3);
        Assert.assertEquals(true, bank.groupe_personnes.get(grpRef3).contains(persref3));
    }

    @Test(expected  =  UnknownGroupe.class)
    public void ajouterPersonneAuGroupe_GroupeInconnu() throws UnknownGroupe, UnknownPersonne{
        GroupeReference grpRef4 = null;
        PersonneReference persref4 =bank.creerPersonne("musk4","elon3",grpRef4);
        bank.ajouterPersonneAuGroupe(grpRef4,persref4);
        Assert.assertEquals(true, bank.groupe_personnes.get(grpRef4).contains(bank.personnes.get(persref4)));
    }

    @Test(expected  =  UnknownPersonne.class)
    public void ajouterPersonneAuGroupe_PersonneInconnu() throws UnknownGroupe, UnknownPersonne{
        GroupeReference grpRef5 = bank.creerGroupe("Tango5","danse5");
        PersonneReference persref5 = null;
        bank.ajouterPersonneAuGroupe(grpRef5,persref5);
        Assert.assertEquals(true, bank.groupe_personnes.get(grpRef5).contains(bank.personnes.get(persref5)));
    }


    // Supprimer personne du groupe

    @Test
    public void supprimerPersonneDuGroupe_PersonneBienSupprime() throws UnknownGroupe, UnknownPersonne{
        GroupeReference grpRef6 = bank.creerGroupe("Tango6","danse6");
        PersonneReference persref6 =bank.creerPersonne("musk6","elon6",grpRef6);
        bank.supprimerPersonneDuGroupe(grpRef6,persref6);
        Assert.assertEquals(false, bank.groupe_personnes.get(grpRef6).contains(persref6));
    }

    @Test(expected  =  UnknownGroupe.class)
    public void supprimerPersonneDuGroupe_GroupeInconnu() throws UnknownGroupe, UnknownPersonne{
        GroupeReference grpRef7 = null;
        PersonneReference persref7 =bank.creerPersonne("musk7","elon7",grpRef7);
        bank.supprimerPersonneDuGroupe(grpRef7,persref7);
    }


    @Test(expected  =  UnknownPersonne.class)
    public void supprimerPersonneDuGroupe_PersonneInconnu() throws UnknownGroupe, UnknownPersonne{
        GroupeReference grpRef8 = bank.creerGroupe("Tango8","danse8");
        PersonneReference persref8 =null;
        bank.supprimerPersonneDuGroupe(grpRef8,persref8);
    }

}