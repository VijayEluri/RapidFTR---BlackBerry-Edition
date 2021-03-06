package com.rapidftr.datastore;

import java.util.Enumeration;
import java.util.Vector;

import com.rapidftr.model.Child;
import com.rapidftr.utilities.ChildSorter;
import com.rapidftr.utilities.ImageHelper;

public class Children {

	private final Vector vector;
    private ChildSorter childSorter;

	public Children(Vector vector) {
		this(vector, new ChildSorter());
	}

    public Children(Vector vector, ChildSorter sorter) {
        this.vector = vector;
        this.childSorter = sorter;
    }

	public Children(Child[] array) {
		vector = new Vector();
		for (int i = 0; i < array.length; i++) {
			vector.addElement(array[i]);
		}
	}

	public int count() {
		return vector.size();
	}

	public void forEachChild(ChildAction action) {
		Enumeration elements = vector.elements();
		while (elements.hasMoreElements()) {
			Child child = (Child) elements.nextElement();
			action.execute(child);
		}
	}

	public Child[] toArray() {
		Child[] array = new Child[count()];
		vector.copyInto(array);
		return array;
	}


	public Children sortBy(final Field field, final boolean isAscending) {
		Child[] children = toArray();
		childSorter.sort(children, isAscending, field);
		return new Children(children);
	}

	public Children sortBy(StringField stringField) {
		return sortBy(stringField,true);
   }
	public Object[] getChildrenAndImages() {
		int size = pageSize() <= count() ? pageSize() : count();
		Object[] childrenAndImages = new Object[size];
		for (int i = 0; i < size; i++) {
			childrenAndImages[i] = getImagePairAt(i);
		}
		return childrenAndImages;
	}

	public Object[] getImagePairAt(int i) {
		Object[] childImagePair = new Object[2];
		Child child = (Child) vector.elementAt(i);
		childImagePair[0] = child;
		childImagePair[1] = new ImageHelper().getThumbnail(child.getImageLocation());
		return childImagePair;
	}

	private int pageSize(){
		return 10;
	}

    public Children getChildrenToUpload() {
        Vector result = new Vector();
        Enumeration elements = vector.elements();
	    while(elements.hasMoreElements()){
            Child child = (Child)elements.nextElement();
            if (child.isNewChild() || child.isUpdated()) {
                result.addElement(child);
            }
        }
        return new Children(result);
    }
}
