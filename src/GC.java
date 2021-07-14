import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JFileChooser;

public class GC {
	Map<Integer, Object> heap;
	Map<Integer, ArrayList<Integer>> pointers;
	List<Pointer> ptns;
	List<Integer> roots;
	List<Object> marked = new ArrayList<Object>();
	List<Object> newHeap;

	public static void main(String[] args) {
		GC gc = new GC();
		System.out.println(args[0]);System.out.println(args[1]);System.out.println(args[2]);System.out.println(args[3]);
		File root = new File(args[0]);
		File heap = new File(args[1]);
		File pointers = new File(args[2]);
		File out = new File(args[3]);
		gc.readRoots(root);
		gc.readHeap(heap);
		gc.readPointers(pointers);
		gc.splitPointersArr();
		gc.rootMark();
		gc.pointersMark();
		gc.compactHeap();
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
			heap = new LinkedHashMap<Integer, Object>();
			File f = file;
			Scanner reader = new Scanner(f);

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
			for (int i = 0; i < newHeap.size(); i++) {
				String line = newHeap.get(i).getID() + "," + newHeap.get(i).getFrom() + "," + newHeap.get(i).getTo()
						+ '\n';
				sb.append(line);
			}

			writer.write(sb.toString());

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
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

	public void rootMark() {
		for (int i = 0; i < roots.size(); i++) {
			heap.get(roots.get(i)).setMark(1);
			Object obj = heap.get(roots.get(i));
			marked.add(obj);
		}
	}

	public void pointersMark() {
		for (int i = 0; i < marked.size(); i++) {
			List<Integer> ptns = new ArrayList<>();
			if (pointers.containsKey(marked.get(i).getID())) {
				ptns = pointers.get(marked.get(i).getID());
				for (int j = 0; j < ptns.size(); j++) {
					heap.get(ptns.get(j)).setMark(1);
					Object obj = heap.get(ptns.get(j));
					marked.add(obj);
				}
			}
		}
	}

	public void compactHeap() {
		newHeap = new ArrayList<>();
		int counter = 0;
		int indexNewHeap = 0;
		for (Map.Entry<Integer, Object> entry : heap.entrySet()) {
			if (entry.getValue().getMark() == 1) {
				newHeap.add(entry.getValue());
				newHeap.get(indexNewHeap).setFrom(counter);
				counter += newHeap.get(indexNewHeap).getSize();
				newHeap.get(indexNewHeap).setTo(counter);
				indexNewHeap++;
				counter++;
			}
		}
	}

}
