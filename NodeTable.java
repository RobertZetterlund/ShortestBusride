import java.util.*;
import java.text.Collator;
/*
* Mapping between nod number and node name and vice versa.
* @author Bror Bjerner, rewritten by E Holmström 2011, added reallocate
*/

public class NodeTable <NO extends NodeObject>{ 
	/** This structure maps between a name and a NodeObject */
	private TreeMap<String, NO>  fromName; 
	/** This structure maps between an node number and a NodeObject */
	private NO[]  fromNodeNo; 
	private int   capacity = 10;
	private int   size = 0;     
	private int   modCount = 0;

	public NodeTable() {
		this(10);
	}
	
	@SuppressWarnings("unchecked")
		
	public NodeTable(int initialNoOfNodes ) {
		capacity = initialNoOfNodes;
		fromNodeNo = (NO[]) new NodeObject[initialNoOfNodes];
		// to sort in "Swedish""
		Locale locale = new Locale("sv", "se");
		Collator collator = Collator.getInstance(locale);
		fromName   = new TreeMap<String, NO>(collator);
		Arrays.fill(fromNodeNo, null);
	}

	@SuppressWarnings("unchecked")
	/**
	Adds a node 
	*/
	public void add(NO no) {
		if ( !(fromName.get(no.name) == null) ) { // already added
			throw new RuntimeException("Duplicate nodenames illegal !!");
		} else {
			// need to reallocate?
			if ( size==capacity ) {
				capacity = 2 * capacity;
			    NO[] newfromNodeNo = (NO[]) new NodeObject[capacity];
				Arrays.fill(newfromNodeNo, null);
			    System.arraycopy(fromNodeNo, 0, newfromNodeNo, 0, size);
			    fromNodeNo = newfromNodeNo;
			}
			// now there is surely room for another element
			no.setNodeNo(size);
			fromNodeNo[size] = no;
			fromName.put( no.name, no );
			size++;
		}
	}
	public NO find( String name ) {
		return  fromName.get(name);
	}
	
	public NO findLeading( String name ) {
		/*
		 OBS experimentell metod
		 klarar inmatning typ "bevä" som ger Beväringsgatan
		 man måste mata in tillräckligt många tecken för att det skall vara 
		 ett unikt namn (och trycka return)
		 detta är dyrbart och inte optimalt så använd den inte i onödan 
		 tex inte när du bygger upp grafen
		 behövs egentligen inte för labben, find ovan räcker
		 @TODO felokänslig typ "bevaring" ger Beväringsgatan
		*/
		// make sure that name is lower case
		//name = name.toLowerCase();
		// try to lookup the name 
		NO tmpNode = fromName.get(name);
		if ( tmpNode != null ) { // direct hit
			return tmpNode; 
		} else {
			// here is the challenge
			// we didn't find the name, try to find a substring
			// this is expensive...
			// we want to create an array of the available stations
			// make a string of the map
			String tmp = fromName.toString();
			// take away {} in the beginning and the end
			tmp = tmp.substring(1, tmp.length()-1);
			// split the string
			String[] tmpArr = tmp.split(", ");
			// now we have an array with the strings twice 
			// like this "station=station"
			// make it an array with only the station i.e. "station"
			for (int i=0; i< tmpArr.length; i++) {
				tmpArr[i] = tmpArr[i].split("=")[1];
			}
			// make sure data is sorted since a TreeMap can't sort rigth!!!!
			// TODO fix this
			Arrays.sort(tmpArr);
			/*
			Now we do a binary search for the station. We don't expect to find 
			it but we get the position where it should be. 
			So we can search in the neighbourhood .
			@return index of the search key, if it is contained in the list; otherwise, 
			(-(insertion point) - 1). (So this is what we expect to get.)
			The insertion point is defined as the point 
			at which the key would be inserted into the list: the index of the 
			first element greater than the key, or list.size(), if all elements 
			in the list are less than the specified key. Note that this guarantees 
			that the return value will be >= 0 if and only if the key is found. 
			*/
			//name = name.toLowerCase();
			int pos = Arrays.binarySearch(tmpArr, name);
			pos = Math.abs(pos+1);
			// ===========================================================
			/* Debug start
				// print tmpArr with its position in the array
				int i = 0;
				System.out.println("tmpArr= ");
				for (String p : tmpArr ) {
					System.out.println( "" + i++ + " *" + p + "*");
				}
				System.out.println("****" + pos);    // debug
				if ( pos<tmpArr.length ) {
					System.out.println(tmpArr[pos]);     // debug
				}
				System.out.println("name= " + name);  // debug
			*/ //Debug end
			// ===========================================================
			int l = name.length();
			if ( pos>=tmpArr.length // outside table
				 // long input strings are not ok i.e name="beväqewfqwefqwef"
			     || l > tmpArr[pos].length() ) { 
				return null;
			}
			/* 
			Om vi nu antar att name="ull" och att tabellen ser ut så här
			...
			116 *UlleviNorra*
			117 *UlleviSödra*
			...
			132 *ÖstraSjukhuset*
			....
			så kommer pos att vara = 116, första större än "ull"
			om tmp[pos] == "ull" och tmp[pos+1] != "ull" så har vi en träff
			men om tmp[pos] == "ull" och tmp[pos+1] == "ull" så kan vi inte skilja på 116/117.
			om det inte är den sista (dvs pos==tmpArr.length-1) för då har vi ändå en träff.
			Sista alternativet är om vi söker efter "SahlgrenskaHuvuden"
			och har tabellen
			95 *SahlgrenskaHuvudentre*
			96 *Saltholmen*
			då kollar vi så längden på det vi söker inte är större än innehållet i 
			nästa position innan vi jämför nästa position (l>tmpArr[pos+1].length)
			*/
			if ( tmpArr[pos].substring(0, l).equals(name) // found?
						&& ( pos==tmpArr.length-1 // sista?
							 || l>tmpArr[pos+1].length()  // nästa station för "lång"?
							 ||  !(tmpArr[pos+1].substring(0, l).equals(name))) // nästa lika?
				) {
				return find(tmpArr[pos]);
			} else {
				return null;
			}
		}
		
	} 
	
	public NO find( int no ) {
		return fromNodeNo[no];
	} 

	public int noOfNodes() {
		return size;
	}

	public String toString() {
		return fromName.toString();
	}

}
