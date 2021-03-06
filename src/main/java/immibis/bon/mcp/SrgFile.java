package immibis.bon.mcp;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SrgFile {

	@Getter private final Map<String, String> classes = new HashMap<>(); // name -> name
	@Getter private final Map<String, String> fields = new HashMap<>(); // owner/name -> name
	@Getter private final Map<String, String> methods = new HashMap<>(); // owner/namedesc -> name
	
	public static String getLastComponent(String s) {
		String[] parts = s.split("/");
		return parts[parts.length - 1];
	}

	public SrgFile(File f, boolean reverse) throws IOException {
		try(Scanner in = new Scanner(new BufferedReader(new FileReader(f)))) {
			while(in.hasNextLine()) {
				if(in.hasNext("CL:")) {
					in.next();
					String obf = in.next();
					String deobf = in.next();
					if(reverse)
						classes.put(deobf, obf);
					else
						classes.put(obf, deobf);
				} else if(in.hasNext("FD:")) {
					in.next();
					String obf = in.next();
					String deobf = in.next();
					if(reverse)
						fields.put(deobf, getLastComponent(obf));
					else
						fields.put(obf, getLastComponent(deobf));
				} else if(in.hasNext("MD:")) {
					in.next();
					String obf = in.next();
					String obfdesc = in.next();
					String deobf = in.next();
					String deobfdesc = in.next();
					if(reverse)
						methods.put(deobf + deobfdesc, getLastComponent(obf));
					else
						methods.put(obf + obfdesc, getLastComponent(deobf));
				} else {
					in.nextLine();
				}
			}
		}
	}
}
