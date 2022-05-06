import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class CodFiscale 
{
	private final String MeseNascita[]={"A","B","C","D","E","H","L","M","P","R","S","T"}; //
	private final int CifreInPosDispari[]= {1, 0, 5, 7, 9, 13, 15, 17, 19, 21}; //array per il calcolo del codice di controllo
	private final int LettereInPosDispari[]={ 1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4, 18, 20, 11, 3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23 }; //
	private final char TabCarattereControllo[]={'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };//
	
	public String eliminaAccenti(String s1)
	{
		String s2="";
		Character c;
		for(int i=0;i<s1.length();i++)
		{
			c=s1.charAt(i);
			if(Character.isLetter(c))
			{
				if(c=='\u00e0')
					s2+='a';
				else if(c=='\u00e8' || c=='\u00e9') 
					s2+='e';
				else if(c=='\u00ec')
					s2+='i';
				else if(c=='\u00f2')
					s2+='o';
				else if(c=='\u00f9')
					s2+='u';
				else
					s2+=c;
			}
			
		}
		
		return s2;
	}
	
	public String eliminaVocali(String s)
	{
		String s_tmp=eliminaAccenti(s);
		//s_tmp=s_tmp.toUpperCase();
		String s2=""; //dichiaro una nuova stringa
		Character c; //uso Character poiche' il codice fiscale e' composto sia da lettere che da numeri
		
		for (int i = 0; i < s_tmp.length(); ++i) 
		{
            c = s_tmp.charAt(i);
            
            if (Character.isLetter(c) && c != 'A' && c != 'E' && c != 'I' && c != 'O' && c != 'U' && c != ' ') // se il carattere non e' una vocale allora lo aggingo alla nuova stringa
                s2 += c; 
            
        }
        
		return s2;
		
	}
	
	public String estraiCognome(String s)
	{
        String s_tmp = eliminaAccenti(s);
        String s1 = eliminaVocali(s);
        String s2 = "";
        Character c;
        
        if (s1.length() >= 3) //se il numero di consonanti e' almeno 3
        {
            
        	for (int i = 0; i < 3; i++) 
            {
                c = s1.charAt(i);
                if (Character.isLetter(c)) //aggiungo alla nuova stringa le prime tre consonanti
                    s2 = String.valueOf(s2) + c;
                
            }
        }
        else if (s1.length() < 3) 
        {
            s_tmp = s_tmp.toUpperCase();
            s2 +=s1;
            
            for (int i = 0; i < s_tmp.length(); i++)
            {
                c = s_tmp.charAt(i);
                if (Character.isLetter(c) && (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U')) { //se le consonanti sono meno di 3, aggiungo la prima vocale del cognome
                    s2 = String.valueOf(s2) + c;
                }
                if (s2.length() == 3) {
                    break;
                }
            }
        }
        
        if (s2.length() < 3) //se il Cognome non ha abbastanza vocali
        {
            while (s2.length() < 3) {
                s2 = String.valueOf(s2) + 'X'; //completo la stringa con delle 'X'
            }
        }
        
        return s2;
    }
	
	public String estraiNome(String s) 
	{
        String tmp = eliminaAccenti(s);
        String s2 = "";
        String s1 = eliminaVocali(tmp);
        if (s1.length() >= 4) {
            Character c = s1.charAt(0);
            if (Character.isLetter(c)) {
                s2 = String.valueOf(s2) + c;
            }
            c = s1.charAt(2);
            if (Character.isLetter(c)) {
                s2 = String.valueOf(s2) + c;
            }
            c = s1.charAt(3);
            if (Character.isLetter(c)) {
                s2 = String.valueOf(s2) + c;
            }
        }
        else if (s1.length() == 3) {
            for (int i = 0; i < s1.length(); ++i) {
                final Character c2 = s1.charAt(i);
                if (Character.isLetter(c2)) {
                    s2 = String.valueOf(s2) + c2;
                }
            }
        }
        else if (s1.length() < 3) 
        {
            s2 = String.valueOf(s2) + s1;
            tmp = tmp.toUpperCase();
            for (int i = 0; i < tmp.length(); ++i) 
            {
                Character c2 = tmp.charAt(i);
                if (Character.isLetter(c2) && (c2 == 'A' || c2 == 'E' || c2 == 'I' || c2 == 'O' || c2 == 'U')) {
                    s2 = String.valueOf(s2) + c2;
                }
                if (s2.length() == 3) {
                    break;
                }
            }
        }
        
        if (s2.length() < 3) 
        {
            while (s2.length() < 3) 
            {
                s2 = String.valueOf(s2) + 'X';
            }
        }
        return s2;
    }
	
	public String estraiDataDiNascita(String anno,String mese, String giorno, String sesso) {
        String s = "";
        s = anno.substring(2);
        int tmp = Integer.parseInt(mese);
        s = String.valueOf(s) + MeseNascita[tmp - 1];
        
        if (sesso.equals("F")) //se il sesso e' femminile
        {
            tmp = Integer.parseInt(giorno);
            tmp += 40;
            giorno = String.valueOf(tmp).toString(); //sommo 40 al giorno
            s = String.valueOf(s) + giorno;
        }
        else 
            s = String.valueOf(s) + giorno;
        
        return s;
    }
	
	public String estraiComuneDiNascita(String s) throws XMLStreamException 
	{
		XMLInputFactory xmlif=null;
		XMLStreamReader xmlr=null;
		String codComune="";
		boolean found=false;
		
		final String COMUNI="comuni.xml";
		try
		{
			xmlif = XMLInputFactory.newInstance();
			xmlr = xmlif.createXMLStreamReader(COMUNI,new FileInputStream(COMUNI));
		}catch(Exception e)
		{
			System.out.println("Errore nell'inizializzazione del reader: ");
			System.out.println(e.getMessage());
		}
		
		while (xmlr.hasNext()) // continua a leggere finché ha eventi a disposizione
		{ 
			switch (xmlr.getEventType()) 
			{ 
			case XMLStreamConstants.START_DOCUMENT: // inizio del documento: stampa che inizia il documento
				break;
				case XMLStreamConstants.START_ELEMENT: // inizio di un elemento: stampa il nome del tag e i suoi attributi
					break;
				case XMLStreamConstants.END_ELEMENT: // fine di un elemento: stampa il nome del tag chiuso
					//System.out.println("END-Tag " + xmlr.getLocalName()); 
					break;
				case XMLStreamConstants.COMMENT: 
					break; // commento: ne stampa il contenuto
				case XMLStreamConstants.CHARACTERS: // content all’interno di un elemento: stampa il testo
					if (xmlr.getText().trim().length() > 0) // controlla se il testo non contiene solo spazi
					{
						if(found)
							codComune=xmlr.getText();
						if(xmlr.getText().equals(s))
							found=true;
					}
							
							
						//System.out.println("-> " + xmlr.getText());
					break;
			}
			if(!codComune.equals(""))
				break;
			else
				xmlr.next();
		}
		
		return codComune;
        
    }
	
	public String estraiCarattereDiControllo(String s) 
	{
        String s2 = "";
        int x = 0;
        Character c;
        
        for (int i = 0; i < s.length(); i++) 
        {
            int cont = 0;
            c = s.charAt(i);
            if (Character.isLetter(c) && i % 2 == 1) //se la lettera si trova in una posizione pari
            {
                for (char j = 'A'; j <= 'Z' && c != j; j++) 
                {
                    ++cont;  
                }
                x += cont;
            }
            else if (Character.isLetter(c) && i % 2 == 0)  //se la lettera si trova in posizione dispari
            {
                for (char j = 'A'; j <= 'Z' && c != j; j++) {
                    ++cont;
                }
                x += LettereInPosDispari[cont];
            }
            else if (Character.isDigit(c) && i % 2 == 1) //se un numero si trova in pos pari
            {
                x += Integer.parseInt(String.valueOf(c));
            }
            else if (Character.isDigit(c) && i % 2 == 0) //se un numero si trova in pos dispari
            {
                x += CifreInPosDispari[Integer.parseInt(String.valueOf(c))];
            }
        }
        x %= 26; //in base al resto della  divisione per 26
        s2 = String.valueOf(s2) + TabCarattereControllo[x]; //estraggo il carattere di controllo dalla tabella
        return s2;
    }
	
	public boolean cerca_duplicati(String s) throws XMLStreamException
	{
		XMLInputFactory xmlif=null;
		XMLStreamReader xmlr=null;
		final String CODICI="codiciFiscali.xml";
		
		try
		{
			xmlif = XMLInputFactory.newInstance();
			xmlr = xmlif.createXMLStreamReader(CODICI,new FileInputStream(CODICI));
		}catch(Exception e)
		{
			System.out.println("Errore nell'inizializzazione del reader: ");
			System.out.println(e.getMessage());
		}
		
		while (xmlr.hasNext()) // continua a leggere finché ha eventi a disposizione
		{ 
			switch (xmlr.getEventType()) 
			{ 
			case XMLStreamConstants.START_DOCUMENT: // inizio del documento: stampa che inizia il documento
				break;
				case XMLStreamConstants.START_ELEMENT: // inizio di un elemento: stampa il nome del tag e i suoi attributi
					break;
				case XMLStreamConstants.END_ELEMENT: // fine di un elemento: stampa il nome del tag chiuso
					//System.out.println("END-Tag " + xmlr.getLocalName()); 
					break;
				case XMLStreamConstants.COMMENT: 
					break; // commento: ne stampa il contenuto
				case XMLStreamConstants.CHARACTERS: // content all’interno di un elemento: stampa il testo
					if (xmlr.getText().trim().length() > 0) // controlla se il testo non contiene solo spazi
					{
						if(xmlr.getText().equals(s))
							return true;
					}
					break;
			}
			xmlr.next();
		}
		
		
		return false;
	}
	
	public boolean controlla_correttezza(String s)
	{
		if(s.length()!=16) //se la lunghezzza del codice e' diversa da 16 posso concludere che il codice e' invalido
			return false;
		
		String mese=s.substring(8, 9);
		int giorno=Integer.parseInt(s.substring(9,11));
		boolean found=false;
		
		for(int i=0;i<12;i++)
		{
			if(mese.equals(MeseNascita[i]))
			{
				found=true;
				break;
			}
		}
		
		if(!found)
			return false;
			
		if(mese.equals(MeseNascita[1]))  //caso mese di 28 giorni
		{
			if(giorno<40)
			{
				if(giorno<1 || giorno >28)
					return false;
			}
			else
			{
				if(giorno<41 || giorno>68)
					return false;
			}
		}
		else if(mese.equals(MeseNascita[0]) || mese.equals(MeseNascita[2]) || mese.equals(MeseNascita[4]) || mese.equals(MeseNascita[6]) || mese.equals(MeseNascita[7]) || mese.equals(MeseNascita[9]) || mese.equals(MeseNascita[11])) //caso mese di 31 giorni
		{
			if(giorno<40)
			{
				if(giorno<1 || giorno >31)
					return false;
			}
			else
			{
				if(giorno<41 || giorno>71)
					return false;
			}
		}
		else //caso mese di 30 giorni
		{
			if(giorno<40)
			{
				if(giorno<1 || giorno >30)
					return false;
			}
			else
			{
				if(giorno<41 || giorno>70)
					return false;
			}
		}
		
		if(!estraiCarattereDiControllo(s.substring(0, 15)).equals(s.substring(15)))  //controlla correttezza carattere di controllo
			return false;
		
		//verifica caratteri e cifre nelle posizioni corrette
		Character c;
		String cognome_nome=String.copyValueOf(s.substring(0, 6).toCharArray());
		
		for(int i=0;i<cognome_nome.length();i++) //controllo primi 6 caratteri del codice fiscale
		{
			c=cognome_nome.charAt(i);
			if(Character.isDigit(c))
				return false;
		}
		
		c=mese.charAt(0);
		if(Character.isDigit(c)) //controllo carattere nella nona posizione
			return false;
		
		if(Character.isDigit(s.charAt(11)) || Character.isDigit(s.charAt(15))) //controllo caratteri nella 12-esima e 15-esima posizione
			return false;
		
		for(int i=0;i<s.substring(6, 8).length();i++)
		{
			c=s.substring(6, 8).charAt(i);
			if(Character.isLetter(c))
				return false;
		}
		
		for(int i=0;i<s.substring(9, 11).length();i++)
		{
			c=s.substring(9, 11).charAt(i);
			if(Character.isLetter(c))
				return false;
		}
		
		for(int i=0;i<s.substring(12, 15).length();i++) //controllo caratteri dalla 13-esima posizione fino alla 15-esima
		{
			c=s.substring(12, 15).charAt(i);
			if(Character.isLetter(c))
				return false;
		}
		
		
			
		return true;
	}
	
	public void insCodiciSpaiati(ArrayList<String> cod,ArrayList<String> spaiati) throws XMLStreamException
	{
		XMLInputFactory xmlif=null;
		XMLStreamReader xmlr=null;
		boolean found=false;
		final String CODICI="codiciFiscali.xml";
		
		try
		{
			xmlif = XMLInputFactory.newInstance();
			xmlr = xmlif.createXMLStreamReader(CODICI,new FileInputStream(CODICI));
		}catch(Exception e)
		{
			System.out.println("Errore nell'inizializzazione del reader: ");
			System.out.println(e.getMessage());
		}
		
		while (xmlr.hasNext()) // continua a leggere finché ha eventi a disposizione
		{ 
			switch (xmlr.getEventType()) 
			{ 
			case XMLStreamConstants.START_DOCUMENT: // inizio del documento: stampa che inizia il documento
				break;
				case XMLStreamConstants.START_ELEMENT: // inizio di un elemento: stampa il nome del tag e i suoi attributi
					/*System.out.println("Tag " + xmlr.getLocalName());
					for (int i = 0; i < xmlr.getAttributeCount(); i++)
						System.out.printf(" => attributo %s->%s%n", xmlr.getAttributeLocalName(i), xmlr.getAttributeValue(i));*/
					break;
				case XMLStreamConstants.END_ELEMENT: // fine di un elemento: stampa il nome del tag chiuso
					//System.out.println("END-Tag " + xmlr.getLocalName()); 
					break;
				case XMLStreamConstants.COMMENT: 
					break; // commento: ne stampa il contenuto
				case XMLStreamConstants.CHARACTERS: // content all’interno di un elemento: stampa il testo
					if (xmlr.getText().trim().length() > 0) // controlla se il testo non contiene solo spazi
					{
						for(int i=0;i<cod.size();i++)
						{
							if(xmlr.getText().equals(cod.get(i)))
							{
								found=true;
								break;
							}
							else
								found=false;
						}
						
						if(!found)
						{
							if(controlla_correttezza(xmlr.getText()))
								spaiati.add(xmlr.getText());
							
						}
							
							
					}
					break;
			}
			xmlr.next();
			}
		}
		
		public void insCodiciInvalidi(ArrayList<String> invalidi) throws XMLStreamException
		{
			XMLInputFactory xmlif=null;
			XMLStreamReader xmlr=null;
			final String CODICI="codiciFiscali.xml";
			
			try
			{
				xmlif = XMLInputFactory.newInstance();
				xmlr = xmlif.createXMLStreamReader(CODICI,new FileInputStream(CODICI));
			}catch(Exception e)
			{
				System.out.println("Errore nell'inizializzazione del reader: ");
				System.out.println(e.getMessage());
			}
			
			while (xmlr.hasNext()) // continua a leggere finché ha eventi a disposizione
			{ 
				switch (xmlr.getEventType()) 
				{ 
				case XMLStreamConstants.START_DOCUMENT: // inizio del documento: stampa che inizia il documento
					break;
					case XMLStreamConstants.START_ELEMENT: // inizio di un elemento: stampa il nome del tag e i suoi attributi
						/*System.out.println("Tag " + xmlr.getLocalName());
						for (int i = 0; i < xmlr.getAttributeCount(); i++)
							System.out.printf(" => attributo %s->%s%n", xmlr.getAttributeLocalName(i), xmlr.getAttributeValue(i));*/
						break;
					case XMLStreamConstants.END_ELEMENT: // fine di un elemento: stampa il nome del tag chiuso
						//System.out.println("END-Tag " + xmlr.getLocalName()); 
						break;
					case XMLStreamConstants.COMMENT: 
						break; // commento: ne stampa il contenuto
					case XMLStreamConstants.CHARACTERS: // content all’interno di un elemento: stampa il testo
						if (xmlr.getText().trim().length() > 0) // controlla se il testo non contiene solo spazi
						{
							if(!controlla_correttezza(xmlr.getText()))
								invalidi.add(xmlr.getText());
								
								
						}
						break;
				}
				xmlr.next();
			}
		
		}
		
		public void scriviCodici(ArrayList<String[]> dati,ArrayList<String>codici, ArrayList<String> invalidi, ArrayList<String> spaiati)
		{
			XMLOutputFactory xmlof = null;
			XMLStreamWriter xmlw = null;
			
			final String CODPERSONE="codiciPersone.xml";
			
			try 
			{
				xmlof = XMLOutputFactory.newInstance();
				xmlw = xmlof.createXMLStreamWriter(new FileOutputStream(CODPERSONE), "utf-8");
				xmlw.writeStartDocument("utf-8", "1.0");
			} catch (Exception e) 
			{
				System.out.println("Errore nell'inizializzazione del writer:");
				System.out.println(e.getMessage());
			}
			
			try 
			{
				xmlw.writeStartElement("output"); // scrittura del tag radice <programmaArnaldo>
				xmlw.writeStartElement("persone");
				xmlw.writeAttribute("numero", Integer.toString(dati.size()));
				
				for (int i = 0; i < dati.size(); i++) 
				{
					xmlw.writeStartElement("persona"); // scrittura del tag autore...
					xmlw.writeAttribute("id", Integer.toString(i)); // ...con attributo id...
					xmlw.writeStartElement("nome");
					xmlw.writeCharacters(dati.get(i)[0]); // ...e content dato
					xmlw.writeEndElement();
					xmlw.writeStartElement("cognome");
					xmlw.writeCharacters(dati.get(i)[1]);
					xmlw.writeEndElement();
					xmlw.writeStartElement("sesso");
					xmlw.writeCharacters(dati.get(i)[2]);
					xmlw.writeEndElement();
					xmlw.writeStartElement("comune_nascita");
					xmlw.writeCharacters(dati.get(i)[3]);
					xmlw.writeEndElement();
					xmlw.writeStartElement("data_nascita");
					xmlw.writeCharacters(dati.get(i)[4]);
					xmlw.writeEndElement();
					xmlw.writeStartElement("codice_fiscale");
					xmlw.writeCharacters(codici.get(i));
					xmlw.writeEndElement();
					xmlw.writeEndElement(); // chiusura di </persona>
				}
				
				xmlw.writeEndElement();
				
				xmlw.writeStartElement("codici");
				xmlw.writeStartElement("invalidi");
				xmlw.writeAttribute("numero", Integer.toString(invalidi.size()));
				
				for(int i=0;i<invalidi.size();i++)
				{
					xmlw.writeStartElement("codice");
					xmlw.writeCharacters(invalidi.get(i));
					xmlw.writeEndElement();
				}
				xmlw.writeEndElement();
				
				xmlw.writeStartElement("spaiati");
				xmlw.writeAttribute("numero", Integer.toString(spaiati.size()));
				
				for(int i=0;i<invalidi.size();i++)
				{
					xmlw.writeStartElement("codice");
					xmlw.writeCharacters(spaiati.get(i));
					xmlw.writeEndElement();
				}
				xmlw.writeEndElement();
				
				xmlw.writeEndElement(); // chiusura di </codici>
				xmlw.writeEndDocument(); // scrittura della fine del documento
				xmlw.flush(); // svuota il buffer e procede alla scrittura
				xmlw.close(); // chiusura del documento e delle risorse impiegate
			
			} catch (Exception e) 
			{ // se c’è un errore viene eseguita questa parte
				System.out.println("Errore nella scrittura");
			}
		}
		

}
