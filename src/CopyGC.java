import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.JFileChooser;

public class CopyGC {
	Map<Integer, Object> heap;
	Map<Integer, ArrayList<Integer>> pointers;
	List<Pointer> ptns;
	List<Integer> roots;
	List<Object> copy;
	int counter = 0;

	public static void main(String[] args) {
		CopyGC gc = new CopyGC();
		System.out.println(args[0]);System.out.println(args[1]);System.out.println(args[2]);System.out.println(args[3]);
		File root = new File(args[0]);
		File heap = new File(args[1]);
		File pointers = new File(args[2]);
		File out = new File(args[3]);
			gc.readRoots(root);
			gc.readHeap(heap);
			gc.readPointers(pointers);
			gc.splitPointersArr();
			gc.rootCopy();
			gc.pointersCopy();
			gc.writeResult(out);
		
	}

	private File selectFile(String str) {
		File f = null;
		JFileChooser j = new JFileChooser("c:");
		int returnVal = j.showOpenDialog(null);
		j.setDialogTitle("Choose " + str + " file");
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			f = j.getSelectedFile();
			System.out.println("Open File: " + f.getName() + ".");
			return f;
		} else {
			System.out.println("Open file command cancelled by user.");
		}
		return null;
	}

	private void readRoots(File file) {
		try {
			File f = file;
			Scanner reader = new Scanner(f);
			roots = new ArrayList<Integer>();
			while (reader.hasNextLine()) {
				int data = Integer.parseInt(reader.nextLine());
				roots.add(data);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File Is Not Found!");
			e.printStackTrace();
		}
	}

	private void readHeap(File file) {
		try {
			File f = file;
			Scanner reader = new Scanner(f);
			heap = new HashMap<Integer, Object>();
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				line = line.replaceFirst("﻿", "");
				String[] temp = line.split(",");
				Object obj = new Object(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]),
						Integer.parseInt(temp[2]));
				heap.put(Integer.parseInt(temp[0]), obj);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File Is Not Found!");
			e.printStackTrace();
		}

	}

	private void readPointers(File file) {
		try {
			File f = file;
			Scanner reader = new Scanner(f);
			ptns = new ArrayList<Pointer>();
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				line = line.replaceFirst("﻿", "");
				String[] temp = line.split(",");
				Pointer ptn = new Pointer(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
				ptns.add(ptn);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File Is Not Found!");
			e.printStackTrace();
		}

	}

	private void writeResult(File f) {
		try (PrintWriter writer = new PrintWriter(f)) {

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < copy.size(); i++) {
				String line = copy.get(i).getID() + "," + copy.get(i).getFrom() + "," + copy.get(i).getTo() + '\n';
				sb.append(line);
			}

			writer.write(sb.toString());

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public void rootCopy() {
		copy = new ArrayList<Object>();
		for (int i = 0; i < roots.size(); i++) {
			Object obj = heap.get(roots.get(i));
			obj.setFrom(counter);
			counter += obj.getSize();
			obj.setTo(counter++);
			copy.add(obj);
		}
	}

	public void pointersCopy() {
		for (int i = 0; i < copy.size(); i++) {
			List<Integer> ptns = new ArrayList<>();
			if (pointers.containsKey(copy.get(i).getID())) {
				ptns = pointers.get(copy.get(i).getID());
				for (int j = 0; j < ptns.size(); j++) {
					Object obj = heap.get(ptns.get(j));
					obj.setFrom(counter);
					counter += obj.getSize();
					obj.setTo(counter++);
					copy.add(obj);
				}
			}
		}
	}

	private void splitPointersArr() {
		pointers = new HashMap<Integer, ArrayList<Integer>>();
		for (int i = 0; i < ptns.size(); i++) {
			if (!pointers.containsKey(ptns.get(i).getFrom())) {
				List list = new ArrayList<Integer>();
				list.add(ptns.get(i).getTo());
				pointers.put(ptns.get(i).getFrom(), (ArrayList<Integer>) list);
			} else {
				pointers.get(ptns.get(i).getFrom()).add(ptns.get(i).getTo());
			}
		}
	}

}
