package tic_tac_toe;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Tester implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HashMap<State, Integer> hm;
	
	public static void start() {
		Tester test = new Tester();
		test.hm = new HashMap<State, Integer>();
		test.hm.put(State.emptyState(), 2);
		//test.hm.put(1, 2);
		
		System.out.println("First Get 1: " + test.hm.get(State.emptyState()));
		//System.out.println("First Get 1: " + test.hm.get(1));
		
        try {
			FileOutputStream fos =
		        new FileOutputStream("test");
		    ObjectOutputStream oos = new ObjectOutputStream(fos);
		    oos.writeObject(test);
		    oos.close();
		    fos.close();
        } catch(IOException ioe) {
        	ioe.printStackTrace();
        }
        
		try {
			FileInputStream fis =
				new FileInputStream("test");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Model m = new Model();
			test = (Tester) ois.readObject();		
			ois.close();
			fis.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		//System.out.println("Get 1: " + test.hm.get(1));
		System.out.println("Get 1: " + test.hm.get(State.emptyState()));
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException
    {      
		int size = aInputStream.readInt();
		//State s = (State) aInputStream.readObject();
		Integer[] in = new Integer[9];
		for (int i = 0; i < 9; i++) {
			in[i] = aInputStream.readInt();
		}
		State s = new State(in);
		this.hm = new HashMap<State, Integer>();
		this.hm.put(s, 2);
		//this.hm.put(1, 2);

    }
 
    private void writeObject(ObjectOutputStream aOutputStream) throws IOException
    {
    	aOutputStream.writeInt(this.hm.size());
    	for (State s: this.hm.keySet()) {
    		Integer[] i = s.get();
    		for (int j = 0; j < 9; j++) {
    			aOutputStream.writeInt(i[j]);
    		}
    		aOutputStream.writeInt(this.hm.get(s));
    	}
    }
	
}
