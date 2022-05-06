import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) throws XMLStreamException {
		// TODO Auto-generated method stub
		XMLInputFactory xmlif=null;
		XMLStreamReader xmlr=null;
		String codice_fiscale="";
		CodFiscale codfiscale=new CodFiscale();
		ArrayList<String[]> dati_persona=new ArrayList<String[]>(); 
		String ElementiXML[]=new String[5];
		int cont=0; //indice per dati_persona
		ArrayList<String> codici_fiscali=new ArrayList<String>(); 
		ArrayList<String> codici_invalidi=new ArrayList<String>();
		ArrayList<String> codici_spaiati=new ArrayList<String>();
		final String INPUTPERSONE="inputPersone.xml";
		
		try
		{
			xmlif = XMLInputFactory.newInstance();
			xmlr = xmlif.createXMLStreamReader(INPUTPERSONE,new FileInputStream(INPUTPERSONE));
		}catch(Exception e)
		{
			System.out.println("Errore nell'inizializzazione del reader: ");
			System.out.println(e.getMessage());
		}
		
		while (xmlr.hasNext()) 
		{ // continua a leggere finché ha eventi a disposizione
			switch (xmlr.getEventType()) 
			{ // switch sul tipo di evento
				case XMLStreamConstants.START_DOCUMENT: // inizio del documento: stampa che inizia il documento
					//System.out.println("Start Read Doc " + INPUTPERSONE); 
					break;
				case XMLStreamConstants.START_ELEMENT: // inizio di un elemento: stampa il nome del tag e i suoi attributi
					//System.out.println("Tag " + xmlr.getLocalName());
					//for (int i = 0; i < xmlr.getAttributeCount(); i++)
						//System.out.printf(" => attributo %s->%s%n", xmlr.getAttributeLocalName(i), xmlr.getAttributeValue(i));
					break;
				case XMLStreamConstants.END_ELEMENT: // fine di un elemento: stampa il nome del tag chiuso
					//System.out.println("END-Tag " + xmlr.getLocalName()); 
					break;
				case XMLStreamConstants.COMMENT:
					//System.out.println("// commento " + xmlr.getText()); 
					break; // commento: ne stampa il contenuto
				case XMLStreamConstants.CHARACTERS: // content all’interno di un elemento: stampa il testo
					if (xmlr.getText().trim().length() > 0) // controlla se il testo non contiene solo spazi
					{
						ElementiXML[cont]=String.copyValueOf(xmlr.getText().toCharArray()); //memorizzo i dati di ciascuna persona in un arraylist di array
						cont++;
					}
						
						
					break;
				}
			
			if(cont==5) // una volta che ho letto tutti i dati di una singola persona
			{
				String tmp[]=new String[5]; //li memorizzo in un array temporaneo
				tmp=ElementiXML.clone(); //salvo i dati di una persona in un array temporaneo
				dati_persona.add(tmp); // aggiungo l'array all'arraylist
				codice_fiscale=codfiscale.estraiCognome(ElementiXML[1])+codfiscale.estraiNome(ElementiXML[0])+codfiscale.estraiDataDiNascita(ElementiXML[4].substring(0, 4), ElementiXML[4].substring(5, 7), ElementiXML[4].substring(8, 10), ElementiXML[2])+codfiscale.estraiComuneDiNascita(ElementiXML[3]);//estrai la prima parte del codice fiscale senza il carattere di controllo
				codice_fiscale+=codfiscale.estraiCarattereDiControllo(codice_fiscale); //aggiunge il carattere di controllo al codice fiscale
				
				if(codfiscale.cerca_duplicati(codice_fiscale)) //controlla se il codice generato e' presente nel file 
					codici_fiscali.add(codice_fiscale);  //se e' presente aggiungo il codice
				else
					codici_fiscali.add("ASSENTE");  //altrimenti aggiungo all'arraylist la stringa "ASSENTE"
				
				
				cont=0;
				
			}
			
			xmlr.next();
			}
		
		codfiscale.insCodiciSpaiati(codici_fiscali, codici_spaiati); 
		codfiscale.insCodiciInvalidi(codici_invalidi);
		
		codfiscale.scriviCodici(dati_persona, codici_fiscali, codici_invalidi, codici_spaiati); //scrive il file xml contentente i codici fiscali
		
		
		
	}
	
	

}
