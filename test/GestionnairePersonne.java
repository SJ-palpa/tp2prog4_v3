import Exceptions.UnknownGroupe;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class GestionnairePersonne {
    Logger log = Logger.getLogger(GestionnaireTacheTest.class);

    Application bank = new Application();


    //creer personne

    @Test
    public void creerPersonne_RefExist() throws UnknownGroupe {
        GroupeReference grpRef = bank.creerGroupe("salsa","danse");
        PersonneReference personneReference = bank.creerPersonne("jean","dujardin",grpRef);
        Assert.assertEquals(true, bank.personnes.containsKey(personneReference));
    }

    @Test
    public void creerPersonne_InfoPrenom() throws UnknownGroupe{
        GroupeReference grpRef = bank.creerGroupe("salsa1","danse1");
        PersonneReference personneReference = bank.creerPersonne("dujardin1","jean1",grpRef);
        Assert.assertEquals("jean1", bank.personnes.get(personneReference).getPrenom());
    }

    @Test
    public void creerPersonne_InfoNom() throws UnknownGroupe{
        GroupeReference grpRef = bank.creerGroupe("salsa2","danse2");
        PersonneReference personneReference = bank.creerPersonne("dujardin2","jean2",grpRef);
        Assert.assertEquals("dujardin2", bank.personnes.get(personneReference).getNom());
    }

    @Test(expected  =  UnknownGroupe.class)
    public void creerPersonne_GroupeInconnue() throws UnknownGroupe{
        GroupeReference grpRef = null;
        PersonneReference personneReference = bank.creerPersonne("dujardin2","jean2",grpRef);
    }

}
